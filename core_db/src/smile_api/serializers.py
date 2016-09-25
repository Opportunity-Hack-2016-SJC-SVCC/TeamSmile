import json

from rest_framework import serializers
from smile_api.models import FoodSource


class PositionToJsonField(serializers.Field):
    def to_representation(self, obj):
        return {"latitude": obj.x, "longitude": obj.y}


class FoodSourceSerializer(serializers.ModelSerializer):
    geo_position = PositionToJsonField()

    class Meta:
        model = FoodSource
        fields = ('name', 'address', 'geo_position')
