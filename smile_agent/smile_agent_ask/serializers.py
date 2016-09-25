from rest_framework import serializers
from .models import Interpreter
import requests
import json


class InterpreterSerializer(serializers.Serializer):
    class Meta:
        model = "hoge"
        fields = ('id', 'text')

    def create(self, validated_data):
        print(validated_data)
        # validated_data = {'hoge':'mm'}
        locations = validated_data['locations']
        print(locations)
        if locations:  # Send client about the near free food place
            print('OK')
            for location in locations:
                # Google Geocode API example
                # http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false
                geo = requests.get('http://maps.google.com/maps/api/geocode/json?address=%s&sensor=false' % location)
                json_geo = json.loads(geo.text)
                if json_geo['status'] != 'ZERO_RESULTS':
                    json_location = json_geo['results'][0]['geometry']['location']
                    lat = json_location['lat']
                    lng = json_location['lng']
                    print(lat, lng)
            # db_request = 'http://139.59.212.15:3045/api/food_source/'
            # db_result = requests.get(db_request)
            # Using Telegram API, send result to a client.
        else:  # No location info! => Ask client again
            print('NG')
            # Using Telegram API, send result to a client.

        ret = 'mokemoke'
        # ret = self.kwargs.get('location')
        return ret  # No meaning



