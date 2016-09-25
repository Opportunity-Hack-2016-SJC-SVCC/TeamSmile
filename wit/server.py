import web
import sys
from wit import Wit
import requests

urls = (
    '/food/(.*)/(.*)/(.*)', 'interpret_request'
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
  r = requests.post("http://139.59.210.181:8888/api/ask/", data={'time': time, 'number': number, 'locations': [location], 'protocol': protocol, 'subservices': subservice, 'service': service})

class interpret_request:
    def GET(self, protocol, number, message):
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
      else:
        sendRequest(protocol,number,'hello', [], '', [])
        
      print "intent: " + str(intent)
      print "meal: " + str(mealkind)
      print "hygiene: " + str(hygienekind)
      print "time: " + str(time)
      print "location: " + str(location)
      

if __name__ == "__main__":
    app.run()
