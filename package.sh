#!/bin/sh
export appName="$1"
BASE_PATH=$(cd `dirname $0`;pwd)
cd $BASE_PATH
cd $appName
mvn clean -Dmaven.test.skip=true  package