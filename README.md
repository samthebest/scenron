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

