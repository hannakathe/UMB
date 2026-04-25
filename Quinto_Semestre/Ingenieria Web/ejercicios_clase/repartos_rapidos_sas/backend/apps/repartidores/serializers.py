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
        extra_kwargs = {
            'plate': {
                'error_messages': {
                    'unique': 'Ya existe un repartidor con esta placa.'
                }
            },
            'phone': {
                'error_messages': {
                    'unique': 'Ya existe un repartidor con este teléfono.'
                }
            },
        }

    def get_envios_activos(self, obj):
        return obj.envios.filter(
            status__in=['en_bodega', 'en_ruta', 'en_entrega']
        ).count()

    def validate_plate(self, value):
        value = value.upper().strip()
        # En edición, excluir el registro actual de la validación
        instance = self.instance
        qs = Repartidor.objects.filter(plate=value)
        if instance:
            qs = qs.exclude(pk=instance.pk)
        if qs.exists():
            raise serializers.ValidationError('Ya existe un repartidor con esta placa.')
        return value

    def validate_phone(self, value):
        value = value.strip()
        instance = self.instance
        qs = Repartidor.objects.filter(phone=value)
        if instance:
            qs = qs.exclude(pk=instance.pk)
        if qs.exists():
            raise serializers.ValidationError('Ya existe un repartidor con este teléfono.')
        return value


class RepartidorListSerializer(serializers.ModelSerializer):
    """Serializer ligero para listas y selects."""
    class Meta:
        model = Repartidor
        fields = ('id', 'name', 'phone', 'vehicle', 'plate', 'rating', 'is_active')
