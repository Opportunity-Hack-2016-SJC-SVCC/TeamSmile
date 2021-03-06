from rest_framework import serializers
from smile_api.models import FoodSource, WorkingHours


class WorkingHoursSerializer(serializers.ModelSerializer):

    class Meta:
        model = WorkingHours
        fields = ('day', 'start_time', 'end_time', 'start_time_string', 'end_time_string')


class FoodSourceSerializer(serializers.ModelSerializer):
    working_hours = WorkingHoursSerializer(many=True)

    class Meta:
        model = FoodSource
        read_only_fields = ('working_hours', 'is_opened_now')
        fields = (
            'id',
            'name',
            'description',
            'address',
            'latitude',
            'longitude',
            'working_hours',
            'phone',
            'is_opened_now',
            'how_long_would_be_opened_in_seconds',
            'how_long_would_be_opened_in_hours',
            'how_long_would_be_opened_in_string'
        )


class UpdateFoodSourceSerializer(serializers.ModelSerializer):
    name = serializers.CharField(required=False)

    class Meta(FoodSourceSerializer.Meta):
        pass
