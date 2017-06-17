#/bin/bash

source ./bin/utils.sh

set -ex

aws emr terminate-clusters --cluster-ids `scenron_cluster_id`
