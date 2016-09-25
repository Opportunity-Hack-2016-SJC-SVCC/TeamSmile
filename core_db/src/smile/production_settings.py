from smile.settings import *
import os

DEBUG = False

ALLOWED_HOSTS = [
    '*'
]

with open('/opt/app/secret_key.txt') as f:
    SECRET_KEY = f.read().strip()

STATIC_ROOT = os.path.join(BASE_DIR, 'collected_static')

LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'handlers': {
        'file': {
            'level': 'DEBUG',
            'class': 'logging.FileHandler',
            'filename': '/opt/app/django.log',
        },
    },
    'loggers': {
        'django.request': {
            'handlers': ['file'],
            'level': 'DEBUG',
            'propagate': True,
        },
    },
}