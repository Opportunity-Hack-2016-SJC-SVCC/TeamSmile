from flask import Flask, request, redirect
import twilio.twiml
import requests
import json

app = Flask(__name__)

@app.route("/", methods=['GET', 'POST'])
def receive_message():
  number = request.form['From']
  body = request.form['Body']
  urlforPost = "http://139.59.210.181:8080/food"
  data = json.dumps({'protocol': 'sms', 'number': number, 'message': body})
  requests.post(urlforPost, data)
  resp = twilio.twiml.Response()
  return str(resp)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
