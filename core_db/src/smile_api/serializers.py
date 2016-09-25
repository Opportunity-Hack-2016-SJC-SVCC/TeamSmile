from rest_framework import serializers
from smile_api.models import FoodSource


class FoodSourceSerializer(serializers.ModelSerializer):

    class Meta:
        model = FoodSource
        fields = ('id', 'name', 'description', 'address', 'latitude', 'longitude', 'working_hours', 'phone')
