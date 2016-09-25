from rest_framework import serializers
from .models import Interpreter


class InterpreterSerializer(serializers.Serializer):
    class Meta:
        model = "hoge"
        fields = ('id', 'text')


