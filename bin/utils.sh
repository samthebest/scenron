#/bin/bash

region=us-east-1
availability_zone=us-east-1b
enron_snapshot_id=snap-d203feb5
key_pair=scenron

function cluster_status {
	aws emr list-clusters | jq -r '.Clusters[] | [.Id, .Name, .Status.State] | @tsv'
}

function scenron_cluster_status {
	cluster_status | grep Scenron | grep -v TERMINATED
}

function check_only_one_cluster {
	num_scenron_clusters=`scenron_cluster_status | wc -l | awk {'print $1'}`

	if [ "${num_scenron_clusters}" != "1" ]; then
		echo "ERROR: We assume exactly a single cluster with non terminated state called Scenron exists"
		exit 1
	fi
}

function scenron_cluster_status_only {
	check_only_one_cluster && scenron_cluster_status | cut -f 3
}

function scenron_cluster_id {
	check_only_one_cluster && scenron_cluster_status | cut -f 1
}

function create_enron_volume {
	aws ec2 create-volume --size 220 --snapshot-id ${enron_snapshot_id} --region us-east-1 --availability-zone us-east-1b --volume-type gp2
}

function delete_enron_volume {
	# TODO
	echo "ERROR: Not implemented"
	exit 1
}

function enron_volume_id {
	# TODO we just grab the first (in case many have been created), we ought to give an error like in scenron_cluster_id
	aws ec2 describe-volumes | jq -r '.Volumes[] | [.VolumeId, .SnapshotId] | @tsv' | grep ${enron_snapshot_id} | cut -f1 | head -1
}

function master_instance_id {
	aws ec2 describe-instances | jq -cr '.Reservations[] | .Instances[] | [.InstanceId, .Tags, .State.Name]' | grep MASTER | grep running | jq -r '.[0]'
}

function attach_volume_to_master {
	aws ec2 attach-volume --volume-id `enron_volume_id` --instance-id `master_instance_id` --device /dev/sdf
}

function master_public_dns {
	# TODO Should learn to use jq better to make this line shorter
	aws ec2 describe-instances | jq -cr '.Reservations[] | .Instances[] | [.InstanceId, .Tags, .NetworkInterfaces]' | grep MASTER | jq -r '.[2]' | jq -r '.[] | .Association.PublicDnsName'
}

function master_security_group_id {
	aws ec2 describe-security-groups | jq -cr '.SecurityGroups[] | [.GroupName, .GroupId] | @tsv' | grep "ElasticMapReduce-master" | cut -f2
}

function allow_inbound_ssh_this_ip {
	ip=`curl ipecho.net/plain ; echo`
	aws ec2 authorize-security-group-ingress --group-name ElasticMapReduce-master --protocol tcp --port 22 --cidr ${ip}/24
}

function ssh_to_master {
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns`
}

function mount_ebs {
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns` "sudo mkdir /enron"
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns` "sudo mount /dev/xvdf /enron"
	echo "INFO: Mounted Enron EBS, contents:"
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns` "ls -lha /enron"
}

function unzip_data {
	scp -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem ./bin/unzip-v2-enron.sh hadoop@`master_public_dns`:/home/hadoop/
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns` "sudo /home/hadoop/unzip-v2-enron.sh"
}

function grab_sample {
	scp -r -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns`:/enron/flat/edrm-enron-v2_buy-r_xml sample_enron/
	scp -r -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns`:/enron/flat/edrm-enron-v2_dasovich-j_xml sample_enron/
}

function scp_jar {
	sbt assembly
	scp -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem target/scala-2.11/scenron-assembly-1.jar hadoop@`master_public_dns`:/home/hadoop/scenron.jar
}

function unzipped_to_email_per_row_format {
	ssh -o StrictHostKeyChecking=no -i ~/.ssh/${key_pair}.pem hadoop@`master_public_dns` \
	  "sudo spark-submit --class scenron.UnzippedToEmailPerRowDistinctApp --master yarn --deploy-mode client /home/hadoop/scenron.jar"
}
