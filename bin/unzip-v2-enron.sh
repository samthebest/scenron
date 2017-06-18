#!/bin/bash

# TODO Given that the logic of this script is a little complex, this would be better written in Scala

echo "INFO: Freeing up space on volume"
rm -r /enron/edrm-enron-v1
rm -r /enron/edrm-enron-v2/edrm-enron-v2*_pst*zip
rm /enron/edrm-enron-v2/*.csv /enron/edrm-enron-v2/*.bat /enron/edrm-enron-v2/*.z0* /enron/edrm-enron-v2/*.txt /enron/edrm-enron-v2/*.bz2
rm /enron/edrm-enron-v2/alldoc-sdoc.zip /enron/edrm-enron-v2/dedupe.zip /enron/edrm-enron-v2/flah

set -e

test -d /enron/flat
already_unzipped=$?

if [ "$already_unzipped" = 0 ]; then
	echo "INFO: Skipping unzipping, /enron/flat already exists"
	exit 0
fi

mkdir /enron/flat

for file in /enron/edrm-enron-v2/*
do
    echo "INFO: Unzipping $file"
    filename=`basename $file`
    mkdir /enron/flat/${filename}
    unzip -d /enron/flat/${filename}/ $file
    echo "INFO: Removing all but the text files"
    rm -r /enron/flat/${filename}/native*
    rm -r /enron/flat/${filename}/*.xml
    echo "INFO: Removing all attachements"
    find /enron/flat/${filename}/text* | grep "\..*\..*\..*\.txt" | xargs rm
    echo $file >> /enron/flat/files_done
done
