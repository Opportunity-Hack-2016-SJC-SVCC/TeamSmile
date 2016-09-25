import requests
import telebot
import re
import json

"""
Setup the bot
"""
BOTTOKEN = "263599717:AAFa4x7qcjqCqynC07es041VbSCGfXy73xM"
bot = telebot.TeleBot(BOTTOKEN)

"""
Get updates sent to the bot
"""
def update_listener(messages):
  url_for_post = "http://139.59.210.181:8080/food"
  for m in messages:
    if m.content_type == "text":
      @bot.message_handler(commands=['start', 'help'])
      def send_welcome(message):
          bot.reply_to(message, "Howdy, how are you doing?")

      print(m)
      user_id= m.from_user.id
      msg = m.text.replace("\n", "%20")
      msg = msg.replace(" ", "%20")
      msg = re.sub("/", "", msg).encode('utf-8')
      data = json.dumps({'protocol': 'telegram', 'number': user_id, 'message': msg})
      req = requests.post(url_for_post, data)
      print(req)

update = bot.set_update_listener(update_listener)
bot.polling(none_stop=True)

