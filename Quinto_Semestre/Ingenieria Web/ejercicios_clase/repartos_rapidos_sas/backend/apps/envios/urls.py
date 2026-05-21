from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import EnvioViewSet, TrackingPublicView

router = DefaultRouter()
router.register('', EnvioViewSet, basename='envio')

urlpatterns = [
    path('rastrear/<str:tracking_number>/', TrackingPublicView.as_view(), name='tracking-public'),
    path('', include(router.urls)),
]
