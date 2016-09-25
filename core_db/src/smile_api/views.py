from django.contrib.gis.geos import Point

from rest_framework import viewsets
from rest_framework.response import Response

from smile_api.models import FoodSource
from smile_api.serializers import FoodSourceSerializer


class FoodSourceVeiwSet(viewsets.ReadOnlyModelViewSet):
    queryset = FoodSource.objects.all()
    serializer_class = FoodSourceSerializer

    def list(self, request, *args, **kwargs):
        queryset = FoodSource.objects.all()

        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        data = serializer.data

        if request.GET.get('latitude') is not None and request.GET.get('longitude') is not None:
            user_position = Point(x=float(request.GET.get('latitude')), y=float(request.GET.get('longitude')))

            sorted_data = {}
            for address in data:
                address_position = Point(x=address['latitude'], y=address['longitude'])
                address['distance'] = user_position.distance(address_position)
                sorted_data[len(sorted_data)] = address

            for first_pk in sorted_data:
                for second_pk in sorted_data:
                    if sorted_data[first_pk]['distance'] < sorted_data[second_pk]['distance']:
                        buffer = sorted_data[second_pk]
                        sorted_data[second_pk] = sorted_data[first_pk]
                        sorted_data[first_pk] = buffer

            data = sorted_data

        response = Response(data)
        return response
