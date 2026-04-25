from rest_framework import viewsets, filters
from django_filters.rest_framework import DjangoFilterBackend

from .models import Repartidor
from .serializers import RepartidorSerializer, RepartidorListSerializer


class RepartidorViewSet(viewsets.ModelViewSet):
    queryset = Repartidor.objects.all()
    filter_backends = (DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter)
    filterset_fields = ('is_active', 'vehicle')
    search_fields = ('name', 'phone', 'plate')
    ordering_fields = ('name', 'rating', 'created_at')

    def get_serializer_class(self):
        if self.action == 'list':
            return RepartidorListSerializer
        return RepartidorSerializer
