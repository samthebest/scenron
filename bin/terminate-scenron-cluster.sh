#/bin/bash

source ./bin/utils.sh

aws emr terminate-clusters --cluster-ids `scenron_cluster_id`
