from rest_framework import viewsets

from smile_api.models import FoodSource
from smile_api.serializers import FoodSourceSerializer


class FoodSourceVeiwSet(viewsets.ReadOnlyModelViewSet):
    queryset = FoodSource.objects.all()
    serializer_class = FoodSourceSerializer
