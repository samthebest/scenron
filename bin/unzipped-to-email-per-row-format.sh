#!/bin/bash

set -ex

source ./bin/utils.sh

scp_jar

unzipped_to_email_per_row_format
