from rest_framework import serializers
from django.db import models
import ast

# Create your models here.


class Interpreter(models.Model):

    class Meta:
        managed = False

    time = models.TextField(blank=True, null=True)
    number = models.TextField(blank=True, null=True)
    locations = models.TextField(blank=True, null=True)
    protocol = models.TextField(blank=True, null=True)
    subservices = models.TextField(blank=True, null=True)
    service = models.TextField(blank=True, null=True)
