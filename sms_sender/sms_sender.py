# Download the twilio-python library from http://twilio.com/docs/libraries

import web
import json
from twilio.rest import TwilioRestClient

urls = (
    '/sms', 'interpret_request'
)
account_sid = "ACf66468a5a451f273e063e5a7740e28a9"
auth_token = "0d25d41f3ed6d99e83552e630a483934"
client = TwilioRestClient(account_sid, auth_token)

app = web.application(urls, globals())
class interpret_request:
    def POST(self):
      data = json.loads(web.data())
      number  = data['number']
      message = data['message']
#      message2 = client.messages.create(to=number, from_=str("+1870498575"),
      message2 = client.messages.create(to=number, messaging_service_sid="MG2bffad310a42593343e6dc64ba226cc1",
                                 body=message)
      message3 = client.messages.create(to=number, messaging_service_sid="MG2bffad310a42593343e6dc64ba226cc1", body="Thanks for Contacting us. Feel free to ask us anyti				 me.")      
    
if __name__ == "__main__":
    app.run()
