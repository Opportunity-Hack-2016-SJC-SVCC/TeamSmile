FROM ubuntu:14.04

RUN apt-get update && \
	apt-get install -y libxml2-dev libxslt1-dev python3-dev python3-setuptools python3.4 build-essential python3-pip nginx libpq-dev mysql-client libmysqlclient-dev && \
	apt-get --assume-yes install -f libjpeg-dev zlib1g-dev && \
	ln -s /usr/lib/x86_64-linux-gnu/libjpeg.so /usr/lib && \
	ln -s /usr/lib/x86_64-linux-gnu/libfreetype.so /usr/lib && \
	ln -s /usr/lib/x86_64-linux-gnu/libz.so /usr/lib

RUN apt-get install -y libsqlite3-dev libproj-dev libgeos-dev libspatialite-dev spatialite-bin

COPY requirements_production.txt /opt/app/requirements_production.txt
COPY requirements.txt /opt/app/requirements.txt

RUN pip3 install -r /opt/app/requirements_production.txt

COPY . /opt/app

VOLUME ["/opt/app/db"]

WORKDIR /opt/app

RUN openssl rand -base64 32 > secret_key.txt;

RUN rm /etc/nginx/sites-enabled/default;
RUN ln -s /opt/app/Deploy/nginx.conf /etc/nginx/sites-enabled/;

RUN python3 /opt/app/manage.py collectstatic --settings=smile.production_settings --noinput

EXPOSE 80

CMD python3 /opt/app/manage.py migrate --settings=smile.production_settings --noinput && sh /opt/app/Deploy/start_in_docker.sh