from rest_framework import viewsets, generics, filters, status
from rest_framework.decorators import action
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.response import Response
from django_filters.rest_framework import DjangoFilterBackend

from .models import Envio
from .serializers import (
    EnvioListSerializer, EnvioDetailSerializer,
    UpdateStatusSerializer, TrackingSerializer,
)


class EnvioViewSet(viewsets.ModelViewSet):
    filter_backends = (DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter)
    filterset_fields = ('status', 'service_type', 'is_draft', 'repartidor')
    search_fields = ('tracking_number', 'sender_name', 'recipient_name', 'repartidor__name')
    ordering_fields = ('created_at', 'updated_at', 'status')

    def get_queryset(self):
        qs = Envio.objects.select_related('repartidor', 'operator').prefetch_related('historial')
        # El operador solo ve sus propios envíos; el admin ve todos
        if not self.request.user.is_admin_role:
            qs = qs.filter(operator=self.request.user)
        return qs

    def get_serializer_class(self):
        if self.action == 'list':
            return EnvioListSerializer
        return EnvioDetailSerializer

    @action(detail=True, methods=['post'], url_path='cambiar-estado')
    def cambiar_estado(self, request, pk=None):
        envio = self.get_object()
        serializer = UpdateStatusSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        envio.set_status(
            serializer.validated_data['status'],
            serializer.validated_data.get('notes', ''),
        )
        return Response(EnvioDetailSerializer(envio, context={'request': request}).data)

    @action(detail=False, methods=['get'], url_path='borradores')
    def borradores(self, request):
        qs = self.get_queryset().filter(is_draft=True)
        serializer = EnvioListSerializer(qs, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'], url_path='mis-envios')
    def mis_envios(self, request):
        """Historial personal del operador autenticado (ignora rol admin)."""
        qs = Envio.objects.filter(
            operator=request.user, is_draft=False
        ).select_related('repartidor').order_by('-created_at')
        serializer = EnvioListSerializer(qs, many=True)
        return Response(serializer.data)


class TrackingPublicView(generics.RetrieveAPIView):
    """Endpoint público: no requiere autenticación."""
    permission_classes = (AllowAny,)
    serializer_class = TrackingSerializer
    lookup_field = 'tracking_number'
    queryset = Envio.objects.select_related('repartidor').prefetch_related('historial')
