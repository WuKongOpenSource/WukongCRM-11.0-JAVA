#!/bin/bash
# shellcheck disable=SC2046
BASE_PATH=$(cd `dirname $0`;pwd)
echo "当前系统版本：";sudo cat /etc/redhat-release;
echo -e "\033[46;37;5m -------------- 开始安装docker所需环境 -------------- \033[0m";
# 安装docker环境
echo " ------------ 开始安装docker服务 ------------ ";
yum update -y;
yum install -y yum-utils device-mapper-persistent-data lvm2;
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo;
yum install -y docker-ce;
systemctl start docker;
systemctl enable docker;
docker version;
sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose;
sudo chmod +x /usr/local/bin/docker-compose;
docker-compose version;
echo " ------------ docker服务安装完毕 ------------ ";