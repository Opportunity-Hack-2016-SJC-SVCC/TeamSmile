# Download the twilio-python library from http://twilio.com/docs/libraries

import web
import json
from twilio.rest import TwilioRestClient

urls = (
    '/sms', 'interpret_request'
)

app = web.application(urls, globals(),port=8081)
class interpret_request:
    def POST(self):
      data = json.loads(web.data())
      number  = data['number']
      message = data['message']


account_sid = "MG2bffad310a42593343e6dc64ba226cc1"
auth_token = "0d25d41f3ed6d99e83552e630a483934"
client = TwilioRestClient(account_sid, auth_token)

message2 = client.messages.create(to=number, from_="+1870498575",
                                 body=message)




