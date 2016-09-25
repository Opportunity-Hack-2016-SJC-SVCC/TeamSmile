from rest_framework import serializers
from .models import Interpreter
import requests
import json


class InterpreterSerializer(serializers.Serializer):
    class Meta:
        model = "hoge"
        fields = ('id', 'text')

    def send_reply(self, client_type, client_id, message):
        # Using Telegram API?, send result to a client.
        print(message)
        if client_type.lower() == 'telegram':
            print('telegram', client_id, message)
            send_request = 'http://localhost:5000/telegram_center/post'  # TODO Hardcoded!
            requests.post(send_request, data=json.dumps({'userid': client_id, 'message': message}))
            # send_request = 'http://139.59.212.15:3045/api/uid/%s/%s' % (client_data[0], place_request_message)
        elif client_type.lower() == 'sms':
            print('sms', client_id, message)
            send_request = 'http://139.59.210.181:8081/sms'  # TODO Hardcoded!
            requests.post(send_request, data=json.dumps({'number': client_id, 'message': message}))
            # send_request = 'http://139.59.212.15:3045/api/uid/%s/%s' % (client_data[0], place_request_message)

    def create(self, validated_data):
        print(validated_data)
        # validated_data = {'protocol':'SMS', 'number':'num1', 'time':'time1', 'service':'sv1', 'subservice':'subsv1', 'locations':['sanjose']}
        if not validated_data:
            print('Error: POST-data is empty')
            return True
        client_type = validated_data.get('protocol')
        client_id = validated_data.get('number')
        time = validated_data.get('time')
        service = validated_data.get('service')
        subservice = validated_data.get('subservice')
        locations = validated_data.get('locations')
        print(locations)
        if not (client_type and client_id and time and service and subservice):
            print('Error: POST-data-value is empty')
            return True

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
                    db_request = 'http://139.59.212.15:3045/api/food_source/?latitude=%s&longitude=%s' % (lat, lng)
                    print(db_request)
                    db_result = requests.get(db_request)
                    res = db_result.content.decode("utf-8")
                    json_res = json.loads(res)
                    r = json_res['0']
                    self.send_reply(client_type, client_id, ("Hello! The nearest free food is in %s miles(?). "
                                                             "There is %s. Open a further %s hours." % (r['distance'], r['address'], r['how_long_would_be_opened_in_string'])))
                else:
                    print('OK-NG')
                    self.send_reply(client_type, client_id, "[%s] is ambiguous. Please send again with more clear address." % location)

        else:  # No location info! => Ask client again
            print('NG')
            self.send_reply(client_type, client_id, "Please send message with address.")

        return True  # No meaning



