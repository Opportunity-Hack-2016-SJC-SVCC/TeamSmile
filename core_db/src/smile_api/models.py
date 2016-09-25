from django.contrib.gis.db import models
from django.contrib.gis.geos import GEOSGeometry
from django.contrib.gis.measure import Distance
from django.contrib.gis.geos import Point


class FoodSource(models.Model):
    name = models.TextField()
    address = models.TextField(blank=False, null=True)
    city = models.TextField(blank=False, null=True)
    zip = models.IntegerField(blank=False, null=True)
    main_address = models.TextField(blank=False, null=True)
    main_city = models.TextField(blank=False, null=True)
    main_state = models.TextField(blank=False, null=True)
    main_zip = models.IntegerField(blank=False, null=True)
    main_phone = models.TextField(blank=False, null=True)
    main_email = models.EmailField(blank=False, null=True)
    geo_position = models.PointField(blank=False, null=True)
