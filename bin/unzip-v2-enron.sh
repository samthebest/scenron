#!/bin/bash

# TODO Given that the logic of this script is a little complex, this would be better written in Scala

echo "INFO: Freeing up space on volume"
rm -r /enron/edrm-enron-v1
rm -r /enron/edrm-enron-v2/edrm-enron-v2*_pst*zip
rm /enron/edrm-enron-v2/*.csv /enron/edrm-enron-v2/*.bat /enron/edrm-enron-v2/*.z0* /enron/edrm-enron-v2/*.txt /enron/edrm-enron-v2/*.bz2
rm /enron/edrm-enron-v2/alldoc-sdoc.zip /enron/edrm-enron-v2/dedupe.zip /enron/edrm-enron-v2/flah

test -d /enron/flat
already_unzipped=$?

# Hack to ensure delayed stderr messages come through in correct order
sleep 2

if [ "$already_unzipped" = 0 ]; then
	echo "INFO: Skipping unzipping, /enron/flat already exists"
	exit 0
fi

set -e

mkdir /enron/flat

echo "INFO: Unzipping"
for file in /enron/edrm-enron-v2/*
do
    filename=`basename $file .zip`
    mkdir /enron/flat/${filename}
    echo "INFO: Unzipping $file (silencing output, as too verbose)"
    corrupt=false
    unzip -d /enron/flat/${filename}/ $file 2>/dev/null >/dev/null || corrupt=true
    if [ "${corrupt}" = "true" ]; then
    	echo "WARNING: Zip file corrupt: $file"
    	rm -r /enron/flat/${filename}
    	echo $file >> /enron/flat/corrupt_zip_files
    else
	    echo "INFO: Removing all but the text files"
	    rm -r /enron/flat/${filename}/native* || true
	    rm -r /enron/flat/${filename}/*.xml || true
	    echo "INFO: Removing all attachements"
	    find /enron/flat/${filename}/text* | grep "\..*\..*\..*\.txt" | xargs rm
	    echo $file >> /enron/flat/files_done
	fi
done
