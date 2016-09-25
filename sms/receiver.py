from flask import Flask, request, redirect
import twilio.twiml
import urllib2

app = Flask(__name__)

@app.route("/", methods=['GET', 'POST'])
def receive_message():
  number = request.form['From']
  body = request.form['Body']
  body.replace(" ", "%20")
  urlforGet = "http://139.59.210.181:8080/food/" + "sms/" + number + "/" + body
  print urlforGet
  urllib2.urlopen(urlforGet).read()

  resp = twilio.twiml.Response()
  return str(resp)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
