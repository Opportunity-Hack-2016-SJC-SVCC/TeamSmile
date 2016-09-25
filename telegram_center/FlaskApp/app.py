#!flask/bin/python
from flask import Flask, jsonify, abort
from flask import request
import telebot

app = Flask(__name__)

BOTTOKEN = ""
bot = telebot.TeleBot(BOTTOKEN)

@app.route('/telegram_center/post', methods=['POST'])
def create_task():
  if not request.json or not 'userid' in request.json:
    abort(400)
  msg = {
    'userid': request.json['userid'],
    'message': request.json['message'],
  }
  print(msg)
  bot.send_message(int(request.json['userid']), request.json['message'])
  return jsonify({'msg': msg}), 201

if __name__ == '__main__':
  app.run(port=5001)
