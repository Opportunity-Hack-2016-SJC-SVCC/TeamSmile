from flask import Flask, request, redirect
import twilio.twiml

app = Flask(__name__)

@app.route("/", methods=['GET', 'POST'])
def receive_message():
  print request
  resp = twilio.twiml.Response()
  return str(resp)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
