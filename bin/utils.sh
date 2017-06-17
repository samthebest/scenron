#/bin/bash

region=us-east-1

function cluster_status {
	aws emr list-clusters | jq -r '.Clusters[] | [.Id, .Name, .Status.State] | @tsv'
}

function scenron_clusters {
	cluster_status | grep Scenron | cut -f1,3 | grep WAITING | cut -f1
}

function scenron_cluster_id {
	num_scenron_clusters=`scenron_clusters | wc -l | awk {'print $1'}`

	if [ "${num_scenron_clusters}" == "1" ]; then
		scenron_clusters
	else
		# TODO should handle all non-terminated states
		echo "ERROR: We assume exactly a single cluster with state WAITING called Scenron exists"
		exit 1
	fi
}

function create_enron_volume {
	aws ec2 create-volume --size 220 --snapshot-id snap-d203feb5 --region us-east-1 --availability-zone us-east-1a --volume-type gp2
}
