#!/bin/bash

rootdir=$(pwd)
for runscript in $(find . -name gradlew); do
    dir=$(dirname $runscript)
    echo 'Building' $dir
    cd $dir
    ./gradlew build
    if [ $? -ne 0 ]; then
        exit 1
    fi
    cd $rootdir
done