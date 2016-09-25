from rest_framework import serializers
from .models import Interpreter
import requests
import json
import time as time_lib


class InterpreterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Interpreter
        fields = ("time", "number", "locations", "protocol", "subservices", "service")

    def send_reply(self, client_type, client_id, message):
        # Using Telegram API?, send result to a client.
        if client_type.lower() == 'telegram':
            print('telegram', client_id, message)
            send_request = 'http://139.59.210.181:5001/telegram_center/post'  # TODO Hardcoded!
            requests.post(send_request, data=json.dumps({'userid': client_id, 'message': message}),
                          headers={'content-type': 'application/json'})
        elif client_type.lower() == 'sms':
            print('sms', client_id, message)
            send_request = 'http://139.59.210.181:8081/sms'  # TODO Hardcoded!
            requests.post(send_request, data=json.dumps({'number': client_id, 'message': message}),
                          headers={'content-type': 'application/json'})

    def create(self, validated_data):
        print("From wit.ai:", validated_data)
        if not validated_data:
            print('Error: POST-data is empty')
            return super(InterpreterSerializer, self).create(validated_data)
        client_type = validated_data.get('protocol')
        client_id = validated_data.get('number')
        time = validated_data.get('time')
        service = validated_data.get('service')
        subservices = validated_data.get('subservices')
        locations = validated_data.get('locations')
        print(locations)
        #  my json mode
        # d = json.loads(validated_data['json_text'])
        # client_type = d.get('protocol')
        # client_id = d.get('number')
        # time = d.get('time')
        # service = d.get('service')
        # subservices = d.get('subservices')
        # locations = d.get('locations')

        if not (client_type and client_id and service):
            print('Error: POST-data-value is empty')
            return super(InterpreterSerializer, self).create(validated_data)

        if locations:  # Send client about the near free food place
            print('OK')
            # for location in locations:
            # Google Geocode API example
            # http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false
            geo = requests.get('http://maps.google.com/maps/api/geocode/json?address=sanjose %s&sensor=false' % locations)
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
                # r = json_res['0']
                # print(r)
                # self.send_reply(client_type, client_id, "Hello! The nearest free food is in %s miles. There is . Open a further  hours. ()" % (round(r['distance'], 2)))
                # hlh = r['how_long_would_be_opened_in_string'];
                flag = 0
                for key, r in json_res.items():
                    hlh = r['how_long_would_be_opened_in_string'];
                    if hlh != None:
                        flag = 1
                        self.send_reply(client_type, client_id, "I just find a place near you that hopefully will meet your needs: It is  %s meters away at %s. It will be opened for the next %s hours. (The name of the place is %s and you can reach them at %s)" % (round(r['distance'], 2), r['address'], hlh, r['name'], r['phone']))
                        time_lib.sleep(1)
                if flag != 1:
                    self.send_reply(client_type, client_id, "Unfortunately I cannot find any nearby place for you.");
            elif service == "thanks":
                self.send_reply(client_type, client_id, "You are welcome! :) Feel free to ask me at any moment.")
            else:
                print('OK-NG')
                self.send_reply(client_type, client_id, "I'm sorry, I cannot understant where you are, could you please be more specific? (You said [%s])" % locations)

        else:  # No location info! => Ask client again
            print('NG')
            self.send_reply(client_type, client_id, "Hello!, my name is SMileS and I'm here to help you to find some food nearby.\n I'm not very intelligent but I'll try my best :) \n You can ask me for food. I just need to know your location.\n For example: \"I'm in Downtown San Jose and I wish to have some food.\" or \"Where can I get food near 3200 Zanker Road San Jose?\" ")

        return super(InterpreterSerializer, self).create(validated_data)  # No meaning



