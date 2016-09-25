from django.contrib.gis.db import models


class FoodSource(models.Model):
    name = models.TextField()
    description = models.TextField(blank=True, null=True)

    address = models.TextField(blank=True, null=True)
    city = models.TextField(blank=True, null=True)
    zip = models.IntegerField(blank=True, null=True)
    main_address = models.TextField(blank=True, null=True)
    main_city = models.TextField(blank=True, null=True)
    main_state = models.TextField(blank=True, null=True)
    main_zip = models.IntegerField(blank=True, null=True)
    main_phone = models.TextField(blank=True, null=True)
    main_email = models.EmailField(blank=True, null=True)
    geo_position = models.PointField(blank=True, null=True)

    @property
    def phone(self):
        return self.main_phone

    @property
    def working_hours(self):
        return [{"start_time": 123, "end_time": 321, "day": "Monday"}]

    @property
    def latitude(self):
        if self.geo_position is not None:
            return self.geo_position.x
        else:
            return None

    @property
    def longitude(self):
        if self.geo_position is not None:
            return self.geo_position.y
        else:
            return None
