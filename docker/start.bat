@echo off

rem -------------------------------------------------------------------------
rem
rem 使用说明：
rem
rem 1: 该命令直接启动docker镜像，请先安装docker相关工具
rem
rem 2: 运行后命令行会从服务器下载镜像，下载完成后启动大概需要15分钟左右，请稍等
rem
rem
rem -------------------------------------------------------------------------

docker run -itd --privileged=true -p 80:8443 registry.cn-hangzhou.aliyuncs.com/72crm/wkcrm_windows:11.0.0 /usr/sbin/init
echo "docker环境启动中，请稍等"

