#!/bin/bash

set -e

num_nodes=2

source ./bin/utils.sh

aws emr create-cluster \
  --name "Scenron" \
  --release-label emr-5.6.0 \
  --applications Name=Spark \
  --region ${region} \
  --use-default-roles \
  --ec2-attributes KeyName=${key_pair} \
  --instance-type m3.xlarge \
  --instance-count ${num_nodes}

# TODO (should poll) Sleep just to be sure the default security groups are created
sleep 10

allow_inbound_ssh_this_ip || echo "INFO: Security group rule already exists, this is fine."

secs=600
SECONDS=0

while (( SECONDS < secs )); do
    status=`scenron_cluster_status_only`
    echo "INFO: Cluster status: $status"
    if [ "${status}" = "WAITING" ]; then
    	break
    fi
    sleep 5
done

if [ "${status}" != "WAITING" ]; then
	echo "ERROR: Cluster did not start"
	cluster_status
	exit 1
fi

attach_volume_to_master

# TODO (should poll) Sleep until volume attached 
sleep 10

mount_ebs

# TODO (should poll) Sleep until volume mounted
sleep 10

echo "INFO: Unzipping data this may take some time"
unzip_data
