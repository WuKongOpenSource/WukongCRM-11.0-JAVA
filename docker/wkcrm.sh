#!/bin/sh
#chkconfig: 2345 80 05
#description: wkcrm
#author: hmb
server_names=('admin' 'authorization' 'bi' 'crm' 'gateway' 'job' 'oa' 'work')
case "$1" in
start)
    cd /usr/local/crm_pro;
    mvn clean -Dmaven.test.skip=true  package;
    mvn clean -Dmaven.test.skip=true  package;
    cd docker/workspace
    echo "启动sentinel"
    nohup java -Dserver.port=8079 -Dproject.name=sentinel-dashboard -jar sentinel/sentinel-dashboard.jar > sentinel/log.out 2>&1 &
    echo "启动seata"
    nohup sh seata/bin/seata-server.sh  > seata/log.out 2>&1 &
    for value in "${server_names[@]}"
    do
        unzip -o /usr/local/crm_pro/$value/target/wk_$value-0.0.1-SNAPSHOT.zip -d /usr/local/crm_pro/docker/workspace/package/$value
        sleep 2s;
    done
    for value in "${server_names[@]}"
    do
        # shellcheck disable=SC2164
        cd /usr/local/crm_pro/docker/workspace/package/$value
        sh 72crm.sh start
        sleep 2s;
    done
    echo "wkcrm startup"
    tail -f /dev/null
    ;;
stop)
    cd /usr/local/crm_pro/docker/workspace
    for value in "${server_names[@]}"
    do
        sh package/$value/72crm.sh stop;
        sleep 0.5s;
    done
    ;;
restart)
    cp -f /72crm.sh /usr/local/crm_pro
    cd /usr/local/crm_pro/docker/workspace
    for value in "${server_names[@]}"
    do
        sh package/$value/72crm.sh stop;
        sleep 0.5s;
    done
    cd /usr/local/crm_pro;
    mvn clean -Dmaven.test.skip=true  package;
    mvn clean -Dmaven.test.skip=true  package;
    cd docker/workspace
    echo "启动sentinel"
    nohup java -Dserver.port=8079 -Dproject.name=sentinel-dashboard -jar sentinel/sentinel-dashboard.jar > sentinel/log.out 2>&1 &
    echo "启动seata"
    nohup sh seata/bin/seata-server.sh  > seata/log.out 2>&1 &
    for value in "${server_names[@]}"
    do
        unzip -o /usr/local/crm_pro/$value/target/wk_$value-0.0.1-SNAPSHOT.zip -d /usr/local/crm_pro/docker/workspace/package/$value
        sleep 2s;
    done
    for value in "${server_names[@]}"
    do
        # shellcheck disable=SC2164
        cd /usr/local/crm_pro/docker/workspace/package/$value
        sh 72crm.sh start
        sleep 2s;
    done
    echo "wkcrm startup"
    tail -f /dev/null
    ;;
*)
    echo "start|stop|restart"
    ;;
esac
exit $?
