#!/bin/bash

apt install python
apt install python-pip
pip install wit
pip install web.py
sudo cp ./*.service /lib/systemd/system/
systemctl daemon-reload
sudo service wit restart
