import datetime

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

    def __str__(self):
        return self.name

    @property
    def phone(self):
        return self.main_phone

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

    @property
    def how_long_would_be_opened_in_seconds(self):
        result = None
        now = datetime.datetime.now()
        seconds_since_midnight = (now - now.replace(hour=0, minute=0, second=0, microsecond=0)).total_seconds()
        for working_hour in self.working_hours.all():
            if working_hour.start_time < seconds_since_midnight < working_hour.end_time:
                result = working_hour.end_time - seconds_since_midnight

        return result

    @property
    def how_long_would_be_opened_in_hours(self):
        if self.how_long_would_be_opened_in_seconds is not None:
            return int(round(self.how_long_would_be_opened_in_seconds / 3600))
        else:
            return None

    @property
    def how_long_would_be_opened_in_string(self):
        if self.how_long_would_be_opened_in_seconds is not None:
            return WorkingHours.seconds_to_string(self.how_long_would_be_opened_in_seconds, am_pm=False)
        else:
            return None

    @property
    def is_opened_now(self):
        now = datetime.datetime.now()
        seconds_since_midnight = (now - now.replace(hour=0, minute=0, second=0, microsecond=0)).total_seconds()
        opened = False
        for working_hour in self.working_hours.all():
            if working_hour.start_time < seconds_since_midnight < working_hour.end_time:
                opened = True
        return opened


class WorkingHours(models.Model):
    MONDAY = 'Monday'
    TUESDAY = 'Tuesday'
    WEDNESDAY = 'Wednesday'
    THURSDAY = 'Thursday'
    FRIDAY = 'Friday'
    SATURDAY = 'Saturday'
    SUNDAY = 'Sunday'

    DAY_OF_WEEK_CHOISES = (
        (MONDAY, 'Monday'),
        (TUESDAY, 'Tuesday'),
        (WEDNESDAY, 'Wednesday'),
        (THURSDAY, 'Thursday'),
        (FRIDAY, 'Friday'),
        (SATURDAY, 'Saturday'),
        (SUNDAY, 'Sunday'),
    )

    food_source = models.ForeignKey(FoodSource, related_name='working_hours')
    start_time = models.IntegerField()
    end_time = models.IntegerField()
    day = models.CharField(
        max_length=15,
        choices=DAY_OF_WEEK_CHOISES,
        default=MONDAY,
    )

    @staticmethod
    def seconds_to_string(seconds, am_pm=True):
        datetime_object = datetime.datetime(
                year=2000, month=1, day=1, hour=0, minute=0, second=0
            ) + datetime.timedelta(
                seconds=seconds
            )

        if am_pm:
            format = "%I"
        else:
            format = "%H"

        if datetime_object.minute != 0:
            format += ":%M"

        if am_pm:
            format += "%p"

        string = datetime_object.strftime(format)

        return string.lower()

    @property
    def start_time_string(self):
        return WorkingHours.seconds_to_string(self.start_time)

    @property
    def end_time_string(self):
        return WorkingHours.seconds_to_string(self.end_time)
