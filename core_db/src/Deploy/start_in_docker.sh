#!/usr/bin/env bash

sudo /etc/init.d/nginx start;
uwsgi --socket /tmp/uwsgi.sock --module smile.wsgi --chmod-socket=777