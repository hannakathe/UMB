"""
Enrutador raíz del proyecto.

Cada app expone sus propias URLs bajo el prefijo /api/:
  /api/auth/         → accounts (login, logout, refresh, perfil)
  /api/envios/       → envios (CRUD + rastreo público + acciones)
  /api/repartidores/ → repartidores (CRUD con filtros)
  /api/dashboard/    → stats + reportes
  /admin/            → panel Django Admin
"""

from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/auth/',         include('apps.accounts.urls')),
    path('api/envios/',       include('apps.envios.urls')),
    path('api/repartidores/', include('apps.repartidores.urls')),
    path('api/dashboard/',    include('apps.dashboard.urls')),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
