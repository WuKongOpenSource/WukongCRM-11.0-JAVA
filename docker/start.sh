#!/bin/bash
chmod 777 nacos.sh;
chmod 777 wkcrm.sh;
chmod 777 mysql.sh;
chmod -R 777 data/elasticsearch/
docker network create --driver=bridge wkcrm_network
docker-compose up -d
echo " ------------ 系统正在启动，启动过程大约需要10分钟，请10分钟后访问系统。  ------------ ";
