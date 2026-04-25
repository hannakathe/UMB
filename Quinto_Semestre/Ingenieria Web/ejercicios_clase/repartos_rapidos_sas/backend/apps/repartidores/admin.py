from django.contrib import admin
from .models import Repartidor


@admin.register(Repartidor)
class RepartidorAdmin(admin.ModelAdmin):
    list_display = ('name', 'phone', 'vehicle', 'plate', 'rating', 'is_active')
    list_filter = ('vehicle', 'is_active')
    search_fields = ('name', 'phone', 'plate')
