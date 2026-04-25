from rest_framework import serializers
from .models import Repartidor


class RepartidorSerializer(serializers.ModelSerializer):
    envios_activos = serializers.SerializerMethodField()

    class Meta:
        model = Repartidor
        fields = (
            'id', 'name', 'phone', 'vehicle', 'plate',
            'rating', 'is_active', 'created_at', 'envios_activos',
        )
        read_only_fields = ('id', 'created_at', 'envios_activos')

    def get_envios_activos(self, obj):
        return obj.envios.filter(
            status__in=['en_bodega', 'en_ruta', 'en_entrega']
        ).count()


class RepartidorListSerializer(serializers.ModelSerializer):
    """Serializer ligero para listas y selects."""
    class Meta:
        model = Repartidor
        fields = ('id', 'name', 'phone', 'vehicle', 'plate', 'rating', 'is_active')
