from django.contrib import admin
from .models import Envio, EstadoHistorial


class EstadoHistorialInline(admin.TabularInline):
    model = EstadoHistorial
    extra = 0
    readonly_fields = ('timestamp',)


@admin.register(Envio)
class EnvioAdmin(admin.ModelAdmin):
    list_display = (
        'tracking_number', 'sender_name', 'recipient_name',
        'repartidor', 'status', 'service_type', 'is_draft', 'created_at',
    )
    list_filter = ('status', 'service_type', 'is_draft', 'repartidor')
    search_fields = ('tracking_number', 'sender_name', 'recipient_name')
    readonly_fields = ('tracking_number', 'created_at', 'updated_at')
    inlines = (EstadoHistorialInline,)
