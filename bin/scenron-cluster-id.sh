#!/bin/bash

set -e

function scenron_clusters {
	aws emr list-clusters | jq -r '.Clusters[] | [.Id, .Name, .Status.State] | @tsv' | grep Scenron | cut -f1,3 | grep WAITING | cut -f1
}

num_scenron_clusters=`scenron_clusters | wc -l | awk {'print $1'}`

if [ "${num_scenron_clusters}" == "1" ]; then
	scenron_clusters
else
	# TODO
	echo "ERROR: We assume exactly a single cluster with state WAITING called Scenron exists"
	exit 1
fi
