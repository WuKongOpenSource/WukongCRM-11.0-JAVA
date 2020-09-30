#!/bin/sh
export appName="$2"
export appJar=$appName-0.0.1-SNAPSHOT.jar
BASE_PATH=$(cd `dirname $0`;pwd)

case "$1" in
start)
        ## 启动admin
        # shellcheck disable=SC2164
        cd $BASE_PATH
        cd package/$appName
        echo "--------$appName 开始启动--------------"
        if [ "$appName" == "gateway" ]; then
          echo "nohup java -jar -Dspring.profiles.active=test $appJar >> output.log 2>&1 &"
          nohup java -jar -Dspring.profiles.active=test \
                $appJar  >> output.log 2>&1 &
        else
          echo "nohup java -jar -Dspring.profiles.include=core,test $appJar >> output.log 2>&1 &"
          nohup java -Xms128m -Xmx512m -jar -Dspring.profiles.include=core,test \
                $appJar >> output.log 2>&1 &
        fi
        sleep 20s
        echo "--------$appName 启动成功--------------"
        ;;
stop)
        cd $BASE_PATH
        P_ID=`ps -ef | grep -w $appJar | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "===$appName process not exists or stop success"
        else
            kill -9 $P_ID
            echo "$appName killed PID = $P_ID success"
        fi
        ;;
copy)
      cd $BASE_PATH
      rm -rf package/$appName/*
      cp -rf $appName/target/* package/$appName
      sleep 1s
esac
exit 0
