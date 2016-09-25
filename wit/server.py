import web
import sys
from wit import Wit
import requests
import json

urls = (
    '/food', 'interpret_request'
)

actions = {
}

app = web.application(urls, globals())
client = Wit(access_token='C6MNPRA4ICV5ZSDKGBSBJEUE7GUD4YBJ', actions=actions)

def getEntityIfExists(entities, eName):    
  if eName in entities:
    return map(lambda x: x['value'], entities[eName])
  return []


#subservice and time are lists
def sendRequest(protocol,number, service, subservice, location, time):
  r = requests.post("http://139.59.210.181:8888/api/ask/", headers={'content-type': 'application/json'}, data=json.dumps({"time": "".join(map(lambda x: str(x),time)), "number": str(number), "locations": str(location), "protocol": str(protocol), "subservices": "".join(map(lambda x: str(x), subservice)), "service": str(service)}))
#  r = requests.post("http://139.59.210.181:8888/api/ask/", headers={'content-type': 'application/json'}, data=json.dumps({"time": map(lambda x: str(x),time), "number": str(number), "locations": str(location), "protocol": str(protocol), "subservices": map(lambda x: str(x), subservice), "service": str(service)}))

class interpret_request:
    def POST(self):
      data = json.loads(web.data())
      protocol = data['protocol']
      number = data['number']
      message = data['message']
      resp = client.message(message)
      print resp
      entities = resp['entities'] #should we consider confidence?
      intent = getEntityIfExists(entities, 'intent')
      mealkind = getEntityIfExists(entities, 'kindofmeal') 
      hygienekind = getEntityIfExists(entities, 'hygieneStuff')
      time = getEntityIfExists(entities, 'datetime')
      location = getEntityIfExists(entities, 'location')
      if len(location) == 0:
        location = ['']
      location = location[0]
        
      if 'foodRequest' in intent:
        sendRequest(protocol,number,'food', mealkind, location, time)
      elif 'hygiene' in intent:
        sendRequest(protocol,number,'hygiene', hygienekind, location, time)
      elif 'thanks' in intent:
        sendRequest(protocol,number,'thanks', [], '', [])
      else:
        sendRequest(protocol,number,'hello', [], '', [])
        
      print "intent: " + str(intent)
      print "meal: " + str(mealkind)
      print "hygiene: " + str(hygienekind)
      print "time: " + str(time)
      print "location: " + str(location)
      

if __name__ == "__main__":
    app.run()
