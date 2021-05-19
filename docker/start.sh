#!/bin/bash
set -e

chmod +x *.sh;
mkdir -p data/elasticsearch/data && chmod 777 data/elasticsearch/data -R;
docker-compose up -d
echo " ------------ 系统正在启动，启动过程大约需要10分钟，请10分钟后访问系统。  ------------ ";
