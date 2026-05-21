from rest_framework import viewsets, filters, permissions
from django_filters.rest_framework import DjangoFilterBackend

from .models import Repartidor
from .serializers import RepartidorSerializer, RepartidorListSerializer


class IsAdminRole(permissions.BasePermission):
    """Solo usuarios con rol 'admin' pueden escribir."""
    def has_permission(self, request, view):
        if request.method in permissions.SAFE_METHODS:
            return request.user.is_authenticated
        return request.user.is_authenticated and request.user.is_admin_role


class RepartidorViewSet(viewsets.ModelViewSet):
    queryset = Repartidor.objects.all()
    permission_classes = (IsAdminRole,)
    filter_backends = (DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter)
    filterset_fields = ('is_active', 'vehicle')
    search_fields = ('name', 'phone', 'plate')
    ordering_fields = ('name', 'rating', 'created_at')

    def get_serializer_class(self):
        if self.action == 'list':
            return RepartidorListSerializer
        return RepartidorSerializer
