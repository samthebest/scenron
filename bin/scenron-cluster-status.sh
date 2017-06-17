#/bin/bash

aws emr list-clusters | jq -r '.Clusters[] | [.Id, .Name, .Status.State] | @tsv'
