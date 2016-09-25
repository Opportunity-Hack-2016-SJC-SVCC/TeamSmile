#!/bin/bash

apt install python
apt install python-pip
pip install wit
pip install web.py
cp ./*.service /lib/systemd/system/
systemctl daemon-reload
service wit restart
