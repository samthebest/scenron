# Scenron

AWS + Spark based set of tools to analyse the Enron emails. The analytical code is written in Scala, hence "Scenron".

## Infrastructure

The AWS infrastructure code is semi-automated as bash scripts in `bin`.  Ideally this code would be rewritten using the AWS java API (with a nice Scala wrapper) or the AWS Python API.  The nice thing about bash though is portability and simplicity (since this is a rather simple project).

Please run `source ./bin/utils.sh` to bring some handy functions into your shell. If you read the `./bin/utils.sh` file the function names should be self documenting.

### Requirements

 - The `aws` cli must be installed and configured (with region us-east-1 & format json). 
 - You must have a key-pair called `scenron`. 
 - The `scenron` .pem file should exist in `~/.ssh/scenron.pem` with permissions 600.
 - Please ensure `jq` is installed: Mac `brew install jq`, Linux (debian based) `sudo apt-get install -y jq`.

### Create EBS Volume Script

`./bin/create-enron-volume.sh` will create an EBS with the enron data.

### Create Cluster Script

Requries `./bin/create-enron-volume.sh` to be run once first.

This will create an EMR cluster with:

 - spark
 - automatically add a rules so your IP can ssh into master
 - automatically attach and mount the EBS
 - automatically unpacks the enron data (if it isn't already unpacked) onto the EBS

# Code Notes

## Preparing The Data

Note we unzip the data using a bash script (which ought to be rewritten using Scala/Java). 
More notably we then read all the files, deduplicate and write out into a single directory where each row is a (json escaped) email (partitioned into 1000 files).

Putting the data into this format is extremely useful for further analysis as reading time is greatly decreased. 
This format is ideal for putting onto HDFS or s3.

## Parsing The Emails

The code that determines the email body, "To" and "Cc" lists parses the .txt version of the email file.
Furthermore the code doesn't even bother using a parser (like Scala Parser Combinators or Parboiled).
Instead we use some assumption loaded string manipulation hacks.

Nevertheless I believe this approach is OK for an initial spike for the following reasons:

### YAGNI & KISS

I don't know the scope of this "project" nor what direction it could evolve.
The code is written using TDD and could easily be refactored to use Parsers.

### Using XML Alternative?

We could argue for using the .xml files and joining the meta-data.
I would prefer to use parsers as believe the complexity of the application *as a whole* is lower using parsers.
The meta-data complexity is nicely captured within the parsing module and need not spill out onto the MapReduce/Spark level code, nor the data-preparation code.
If scalability was ever a concern we also cut down on a rather expensive join.

Finally, we still have to write some parsing code to extract only the email body from the file since the file includes the email chain.
Using the XML cannot avoid this.

### Using PST or EML Alternatives?

I did not have time to analyse the format of PST or EML, perhaps these formats lend themselves to parsing easier.

# Solution

Best efforts have been made to ensure a correct solution, nevertheless I believe the process is more important here especially when this kind of analysis is done in isolation.
Due to the unusual data formats, time constraints and data preparation required, I don't have vast confidence in the numbers.
Peer review would be essential to ensure correctness.
In fact for an initial spike like this it could make sense to require independent reproduction by another team/team-member as per the scientific method.
Finally, of course iterations with stakeholders and data producers would also be required to ensure correctness.

 - Average email word length (only email body, not chain): TODO
 - Total email files unzipped: 1,227,645
 - Number of deduplicated emails: TODO
 - Top recipients:
 
TODO
