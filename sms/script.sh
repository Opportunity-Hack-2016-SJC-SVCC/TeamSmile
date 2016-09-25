#!/bin/bash
#run as root
pip install flask
pip install twilio
cp ./*.service /lib/systemd/system/
systemctl daemon-reload
service smsreceiver restart
