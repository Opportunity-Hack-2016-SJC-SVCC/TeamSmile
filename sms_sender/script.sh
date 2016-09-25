#!/bin/bash
#run as root
pip install twilio
cp ./*.service /lib/systemd/system/
systemctl daemon-reload
service smssender restart
