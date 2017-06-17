#/bin/bash

id=`./bin/scenron-cluster-id.sh`

aws emr terminate-clusters --cluster-ids ${id}
