#!/bin/bash

COMMAND="$1"

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]]; then
	echo "Usage: $0 start | stop | restart"
	exit 0
fi

APP_BASE_PATH=$(cd `dirname $0`; pwd)

function start()
{
    JAVA_OPTS=-Dspring.profiles.include=core,test
    if [[ "${project.artifactId}" == "wk_gateway" ]]; then
    	JAVA_OPTS=
    fi
    # -Xms分配堆最小内存，默认为物理内存的1/64；-Xmx分配最大内存，默认为物理内存的1/4 如果程序会崩溃请将此值调高
    nohup java -Xms128m -Xmx512m -jar ${JAVA_OPTS} ${project.artifactId}-${project.version}.jar >> output.log 2>&1 &
    echo "--------项目启动成功--------"
    echo "--------欢迎使用悟空CRM ^_^--------"
}

function stop()
{
    P_ID=`ps -ef | grep -w ${project.artifactId}-${project.version}.jar | grep -v "grep" | awk '{print $2}'`
    kill $P_ID
    echo "项目已关闭"
}

if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "stop" ]]; then
    stop
else
    stop
    start
fi
