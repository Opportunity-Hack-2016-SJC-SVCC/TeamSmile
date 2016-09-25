from django.shortcuts import render

from rest_framework import viewsets
from rest_framework.response import Response

from .models import Interpreter
from .serializers import InterpreterSerializer
import requests
import json as json2

# Create your views here.


class InterpreterViewSet(viewsets.ModelViewSet):
    queryset = Interpreter.objects.all()
    serializer_class = InterpreterSerializer

    def func(self):
        # Google Geocode API example
        # http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false
        geo = requests.get('http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false')
        geojson = json2.loads(geo.text)
        location = geojson['results'][0]['geometry']['location']
        lat = location['lat']
        lng = location['lng']
        print(lat, lng)
        # db_result = requests.get('http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false')
        # ret = json['status']
        # ret = geojson['status']

        # Using Telegram API, send result to a client.
        if self.kwargs.get('location'):  # Send client about the near free food place
            print('OK')
        else:  # No location info! => Ask client again
            print('NG')

        # ret = 'mokemoke'
        ret = self.kwargs.get('location')
        return ret  # No meaning

    def retrieve(self, request, *args, **kwargs):
        return Response({'answer': self.func()})

    def list(self, request, *args, **kwargs):
        return Response({'answer': self.func()})

