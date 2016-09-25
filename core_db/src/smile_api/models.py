from django.db import models


class FoodSource(models.Model):
    name = models.TextField()
    address = models.TextField()
    city = models.TextField()
    zip = models.IntegerField()
    main_address = models.TextField()
    main_city = models.TextField()
    main_state = models.TextField()
    main_zip = models.IntegerField()
    main_phone = models.TextField()
    main_email = models.EmailField()
