#!/bin/sh

cp /etc/mysql/conf.d/source/mysqld.cnf /etc/mysql/mysql.conf.d/

/entrypoint.sh mysqld