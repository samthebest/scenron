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

### Prepare Data

In addition to unzipping the data we have a spark job that reformats the data into something much more Big Data friendly.  (See "Code Notes" for details).

After you have created a cluster run `./bin/unzipped-to-email-per-row-format.sh`.  This will:

 - Build the code that includes the data preparation
 - Write back to the EBS for future use

We don't include this step in the Create Cluster Script because its very slow and ought to be run in a screen session for robustness (to allow for network issues).

### Run Analysis

This will use the data from above.

Finally run `./bin/run.sh`

## To Build

Simply `sbt assembly`

## To Run Tests

Simply `sbt test`

# Code Notes

## Preparing The Data

Note we unzip the data using a bash script (which ought to be rewritten using Scala/Java). 
More notably we then read all the files, deduplicate and write out into a single directory where each row is a (json escaped) email (partitioned into 1000 files).

Putting the data into this format is extremely useful for further analysis as reading time is greatly decreased.
Spark will read continuous blocks from disk significantly faster than 100s of 1000s of small files.
The time to parse the escaping is completely negligble.

This format is ideal for putting onto HDFS or s3, 1000 partitions is a reasonable number for most clusters.

Started 17/06/19 07:20:33

## Calculating The Stats

Observe we use a Monoid so we only need to pass the data once to calculate all the statistics.
Note I opt for using `RDD` and Functional Programming as it's much easier to test than `Dataset`s, 
is more stable, often faster (if you know what your doing) and easier to maintain (less layers of indirection). 

## Parsing The Emails

The code that determines the email body, "To" and "Cc" lists parses the .txt version of the email file.
Furthermore the code doesn't even bother using a parser (like Scala Parser Combinators or Parboiled).
Instead we use some assumption loaded string manipulation hacks.

When I wrote the unit tests I based the tests off only a few sample emails.  This may cause inaccuracies.
To improve accuracy we ought to trawl through a wide range of emails to ensure we have covered the edge cases.
Ideally we could track down some documentation for the format.

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
