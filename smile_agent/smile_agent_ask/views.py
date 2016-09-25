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
        location = self.kwargs.get('location')
        if location:  # Send client about the near free food place
            print('OK')
            # Google Geocode API example
            # http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false
            geo = requests.get('http://maps.google.com/maps/api/geocode/json?address=%s&sensor=false' % location)
            json_geo = json2.loads(geo.text)
            json_location = json_geo['results'][0]['geometry']['location']
            lat = json_location['lat']
            lng = json_location['lng']

            db_request = 'http://139.59.212.15:3045/api/food_source/'
            db_result = requests.get(db_request)
            print(lat, lng)
            # Using Telegram API, send result to a client.
        else:  # No location info! => Ask client again
            print('NG')
            # Using Telegram API, send result to a client.

        # ret = 'mokemoke'
        ret = self.kwargs.get('location')
        return ret  # No meaning

    def retrieve(self, request, *args, **kwargs):
        return Response({'answer': self.func()})

    def list(self, request, *args, **kwargs):
        return Response({'answer': self.func()})

