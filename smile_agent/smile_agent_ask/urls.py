# coding: utf-8

from rest_framework import routers
from .views import InterpreterViewSet

router = routers.DefaultRouter()
# router.register(r'ask/protocol/(?P<protocol>.*)/number/(?P<number>.*)/service/(?P<service>.*)'
#                 r'/subsurvice/(?P<subservice>.*)/location/(?P<location>.*)/time/(?P<time>.*)', InterpreterViewSet)
router.register(r'ask', InterpreterViewSet)
urlpatterns = router.urls
