#!/bin/bash

apt install python
apt install python-pip
pip install django
pip install djangorestframework

cp ./*.service /lib/systemd/system/
systemctl daemon-reload
service smile-agent restart
