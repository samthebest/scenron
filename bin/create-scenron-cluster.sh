#!/bin/bash

set -ex

num_nodes=2

aws emr create-cluster \
  --name "Scenron" \
  --release-label emr-5.6.0 \
  --applications Name=Spark \
  --region eu-west-1 \
  --use-default-roles \
  --ec2-attributes KeyName=scenron \
  --instance-type m3.xlarge \
  --instance-count ${num_nodes}

# Ideally this should block until the cluster is ready
