# Scenron

AWS + Spark based set of tools to analyse the Enron emails. The analytical code is written in Scala, hence "Scenron".

## Infrastructure Setup & Running

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

`./bin/create-scenron-cluster.sh` will create an EMR cluster with:

 - spark
 - automatically add a rules so your IP can ssh into master
 - automatically attach and mount the EBS
 - automatically unpacks the enron data (if it isn't already unpacked) onto the EBS

### Prepare Data

In addition to unzipping the data we have a spark job that reformats the data into something much more Big Data friendly.  (See "Code Notes" for details).

After you have created a cluster run `./bin/unzipped-to-email-per-row-format.sh`.  This will:

 - Build the code that includes the data preparation
 - Write back to the EBS for future use

TODO Add this to `./bin/create-scenron-cluster.sh`

### Run Analysis

This will use the data, cluster (and jar) from above.

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

### Performance Stats

(Not currently Horizontally Scalable) To write each email as a single row (single threaded using UnzippedToEmailPerRowDistinctAlt code): 11 minutes

(Horizontally Scalable) To perform the distinct using Spark (single node local mode with 8G memory and using 6 cores): 2 minutes

### Bug In Spark

Unfortunately Spark cannot handle large numbers of small files (this has been a problem since time immemorial).  
The code looks much more succinct; UnzippedToEmailPerRowDistinct, so it's a shame that it never works.
So this project serves as a very nice reproduction of this bug https://issues.apache.org/jira/browse/SPARK-21137

To reproduce: In `./bin/unzipped-to-email-per-row-format.sh` change `unzipped_to_email_per_row_format` to `unzipped_to_email_per_row_format_deprecated` then run `./bin/unzipped-to-email-per-row-format.sh` (be sure you have read Infrastructure Setup & Running so you have run the prerequisite commands).

## Calculating The Stats

Observe we use a Semigroup so we only need a single read, shuffle & reduce to calculate all the statistics.
Note I opt for using `RDD` and Functional Programming as it's much easier to test than `Dataset`s, 
is more stable, often faster (if you know what your doing) and easier to maintain (less layers of indirection). 

### Performance Stats

Is Horizontally Scalable.

Using single executor with 8G memory and 8 cores: 2 minutes

So using many executors with more cores would bring this down to seconds.

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

Best efforts have been made to ensure a correct solution, nevertheless I believe transparency of the process is more important here especially when this kind of analysis is done in isolation without peer review.
Due to the unusual data formats, time constraints and data preparation required, I don't have 100% confidence in the numbers.
Peer review would be essential to ensure correctness.
In fact for an initial spike like this it could make sense to require independent reproduction by another team/team-member as per the scientific method.
Finally, of course iterations with stakeholders and data producers would also be required to ensure correctness.

The number of deduplicated emails is only fractionaly smaller than the number of files unzipped.  The number I was expecting was 500K.  Therefore the approach taken to deduplicate is not sufficient and needs more work.

The average email length seems reasonable.

 - Average email word length (only email body, not chain): 115.41
 - Total email files unzipped: 1,227,645
 - Number of deduplicated emails: 1,227,255
 - Top recipients (TSV):
 
```
Top 100 Recipients:
Steven J Kean	29025
Richard Shapiro	17743
Jeff Dasovich	15188
James D Steffes	14464
Vince J Kaminski	11325
pete.davis@enron.com	10968
Mark Taylor	10917
Tana Jones	10779
Sara Shackleton	8524
vkaminski@aol.com	7523
Sally Beck	7345
Alan Comnes	6872
Maureen McVicker	6169
Mark Palmer	6106
Susan J Mara	5944
Daren J Farmer	5897
skean@enron.com	5761
Karen Denne	5510
Paul Kaufman	5193
Tim Belden	5139
Joe Hartsoe	4535
All Enron Worldwide	4289
Carol	4259
Jeffrey T Hodge	4118
Bill Votaw	4081
Beverly Aden	4071
Brenda Barreda	4001
Angela Schwarz	3980
Gerald Nemec	3967
John J Lavorato	3954
Kay Mann	3919
Jeff Skilling	3674
Alan Aronowitz	3615
Susan Bailey	3602
Kate Symes	3571
Outlook Migration Team	3551
Beck	3524
Sandra McCubbin	3503
Sally </O=ENRON/OU=NA/CN=RECIPIENTS/CN=Sbeck>	3304
Carol St Clair	3285
Shirley Crenshaw	3268
Kenneth Lay	3208
Mark E Haedicke	3201
Louise Kitchen	3135
Elizabeth Linnell	3075
mark.guzman@enron.com	3016
Greg Whalley	2934
Brent Hendry	2900
EX	2853
Cliff Baxter	2829
Harry Kingerski	2814
Kitchen  Louise <Louise.Kitchen@ENRON.com>	2794
All Enron Worldwide@ENRON <IMCEANOTES-All+20Enron+20Worldwide+40ENRON@ENRON.com>	2765
ryan.slinger@enron.com	2756
bert.meyers@enron.com	2738
Linda Robertson	2709
monika.causholli@enron.com	2691
bill.williams.III@enron.com	2679
dporter3@enron.com	2671
jbryson@enron.com	2667
leaf.harasin@enron.com	2667
Geir.Solberg@enron.com	2667
David W Delainey	2624
Suzanne Adams	2569
Jeffery Fawcett	2518
David Forster	2426
Stinson Gibner	2425
All Enron Houston	2385
Elizabeth Sager	2367
Rick Buy	2351
Dan J Hyvl	2350
William S Bradford	2320
Mona L Petrochko	2320
Frank L Davis	2292
Eric.Linder@enron.com	2280
Craig.Dean@enron.com	2276
jdasovic@enron.com	2250
Charlie Stone <cstone1@txu.com>	2249
Steven Harris	2242
Leslie Hansen	2213
Gary Green <ggreen2@txu.com>	2211
Mary Hain	2205
Chris Germany	2193
Karen Lambert	2124
Mike McConnell	2110
daren.j.farmer@enron.com	2084
Benjamin Rogers	2080
Kevin Hyatt	2075
Stacy E Dickson	2037
Jeff.Dasovich@enron.com	2025
gary.a.hanks@enron.com	2018
Sarah Novosel	2015
Mary Cook	2010
earl.tisdale@enron.com	2006
Bryan Hull	1951
vkamins@enron.com	1936
Sheri Thomas	1934
Jeffrey A Shankman	1902
Richard B Sanders	1894
Mark Frevert	1799
```
