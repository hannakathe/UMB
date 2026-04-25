from rest_framework import serializers
from apps.repartidores.serializers import RepartidorListSerializer
from .models import Envio, EstadoHistorial


class EstadoHistorialSerializer(serializers.ModelSerializer):
    status_display = serializers.CharField(source='get_status_display', read_only=True)

    class Meta:
        model = EstadoHistorial
        fields = ('id', 'status', 'status_display', 'notes', 'timestamp')


class EnvioListSerializer(serializers.ModelSerializer):
    """Serializer ligero para la tabla del dashboard."""
    repartidor_name = serializers.CharField(source='repartidor.name', read_only=True, default=None)
    status_display = serializers.CharField(source='get_status_display', read_only=True)
    service_type_display = serializers.CharField(source='get_service_type_display', read_only=True)

    class Meta:
        model = Envio
        fields = (
            'id', 'tracking_number', 'is_draft',
            'sender_name', 'sender_city',
            'recipient_name', 'recipient_city',
            'repartidor', 'repartidor_name',
            'status', 'status_display',
            'service_type', 'service_type_display',
            'created_at',
        )


class EnvioDetailSerializer(serializers.ModelSerializer):
    repartidor_detail = RepartidorListSerializer(source='repartidor', read_only=True)
    historial = EstadoHistorialSerializer(many=True, read_only=True)
    status_display = serializers.CharField(source='get_status_display', read_only=True)
    service_type_display = serializers.CharField(source='get_service_type_display', read_only=True)

    class Meta:
        model = Envio
        fields = '__all__'
        read_only_fields = ('id', 'tracking_number', 'created_at', 'updated_at', 'operator')

    def create(self, validated_data):
        request = self.context.get('request')
        if request and request.user.is_authenticated:
            validated_data['operator'] = request.user

        envio = super().create(validated_data)

        # Registrar estado inicial en historial
        EstadoHistorial.objects.create(
            envio=envio,
            status=envio.status,
            notes='Envío creado',
        )
        return envio


class UpdateStatusSerializer(serializers.Serializer):
    status = serializers.ChoiceField(choices=Envio.Status.choices)
    notes = serializers.CharField(required=False, allow_blank=True, default='')


class TrackingSerializer(serializers.ModelSerializer):
    """Serializer público (sin datos sensibles de operador)."""
    historial = EstadoHistorialSerializer(many=True, read_only=True)
    repartidor_name = serializers.CharField(source='repartidor.name', read_only=True, default=None)
    repartidor_vehicle = serializers.CharField(source='repartidor.vehicle', read_only=True, default=None)
    repartidor_plate = serializers.CharField(source='repartidor.plate', read_only=True, default=None)
    repartidor_rating = serializers.DecimalField(
        source='repartidor.rating', max_digits=3, decimal_places=1,
        read_only=True, default=None,
    )
    status_display = serializers.CharField(source='get_status_display', read_only=True)

    class Meta:
        model = Envio
        fields = (
            'tracking_number',
            'sender_name', 'sender_city',
            'recipient_name', 'recipient_city',
            'status', 'status_display',
            'service_type',
            'repartidor_name', 'repartidor_vehicle', 'repartidor_plate', 'repartidor_rating',
            'historial',
            'created_at',
        )
