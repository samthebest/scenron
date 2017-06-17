# Scenron

AWS + Spark based set of tools to analyse the Enron emails. The analytical code is written in Scala, hence "Scenron".

## Infrastructure

The AWS infrastructure code is semi-automated as bash scripts in `bin`.  Ideally this code would be rewritten using the AWS java API (with a nice Scala wrapper) or the AWS Python API.  The nice thing about bash though is portability and simplicity (since this is a rather simple project).

### Requirements

The `aws` cli must be installed and configured, you must have a key-pair called `scenron`. The `scenron` .pem file should exist in `~/.ssh/scenron.pem` with permissions 600.

Please ensure

```
export AWS_DEFAULT_OUTPUT="json"
```

Please ensure `jq` is installed, Mac `brew install jq`, Linux (debian based): `sudo apt-get install -y jq`.
