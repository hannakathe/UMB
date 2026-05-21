import random
from django.db import models
from django.utils import timezone

from apps.repartidores.models import Repartidor


def _generate_tracking():
    year = timezone.now().year
    number = random.randint(100000, 999999)
    return f'RPD-{year}-{number:06d}'


class Envio(models.Model):
    class Status(models.TextChoices):
        EN_BODEGA = 'en_bodega', 'En bodega'
        EN_RUTA = 'en_ruta', 'En ruta'
        EN_ENTREGA = 'en_entrega', 'En entrega'
        RECIBIDO = 'recibido', 'Recibido'
        INCIDENCIA = 'incidencia', 'Incidencia'

    class ServiceType(models.TextChoices):
        STANDARD = 'standard', 'Standard'
        PRIORITARIO = 'prioritario', 'Prioritario'
        EXPRESS = 'express', 'Express'

    # Identificación
    tracking_number = models.CharField(
        max_length=20, unique=True, default=_generate_tracking,
        verbose_name='Número de guía',
    )
    is_draft = models.BooleanField(default=False, verbose_name='Borrador')

    # Remitente
    sender_name = models.CharField(max_length=150, verbose_name='Nombre remitente')
    sender_phone = models.CharField(max_length=20, verbose_name='Teléfono remitente')
    sender_address = models.CharField(max_length=300, verbose_name='Dirección remitente')
    sender_city = models.CharField(max_length=100, verbose_name='Ciudad remitente', blank=True)

    # Destinatario
    recipient_name = models.CharField(max_length=150, verbose_name='Nombre destinatario')
    recipient_phone = models.CharField(max_length=20, verbose_name='Teléfono destinatario')
    recipient_address = models.CharField(max_length=300, verbose_name='Dirección destinatario')
    recipient_city = models.CharField(max_length=100, verbose_name='Ciudad destinatario', blank=True)

    # Paquete
    description = models.TextField(verbose_name='Descripción', blank=True)
    weight = models.DecimalField(max_digits=8, decimal_places=2, verbose_name='Peso (kg)')
    height = models.DecimalField(max_digits=8, decimal_places=2, verbose_name='Alto (cm)')
    width = models.DecimalField(max_digits=8, decimal_places=2, verbose_name='Ancho (cm)')
    depth = models.DecimalField(max_digits=8, decimal_places=2, verbose_name='Profundo (cm)')
    insured_value = models.DecimalField(
        max_digits=12, decimal_places=2, default=0,
        verbose_name='Valor asegurado (COP)',
    )

    # Servicio y logística
    service_type = models.CharField(
        max_length=20, choices=ServiceType.choices, default=ServiceType.STANDARD,
    )
    status = models.CharField(
        max_length=20, choices=Status.choices, default=Status.EN_BODEGA,
    )
    repartidor = models.ForeignKey(
        Repartidor, null=True, blank=True,
        on_delete=models.SET_NULL, related_name='envios',
    )
    operator = models.ForeignKey(
        'accounts.User', null=True, blank=True,
        on_delete=models.SET_NULL, related_name='envios_creados',
    )

    # Auditoría
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'envios'
        verbose_name = 'Envío'
        verbose_name_plural = 'Envíos'
        ordering = ('-created_at',)

    def __str__(self):
        return f'{self.tracking_number} — {self.sender_name} → {self.recipient_name}'

    def set_status(self, new_status, notes=''):
        """Cambia estado y registra en el historial."""
        self.status = new_status
        self.save(update_fields=['status', 'updated_at'])
        EstadoHistorial.objects.create(envio=self, status=new_status, notes=notes)


class EstadoHistorial(models.Model):
    envio = models.ForeignKey(Envio, on_delete=models.CASCADE, related_name='historial')
    status = models.CharField(max_length=20, choices=Envio.Status.choices)
    notes = models.TextField(blank=True)
    timestamp = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'envios_historial'
        verbose_name = 'Historial de estado'
        verbose_name_plural = 'Historial de estados'
        ordering = ('timestamp',)

    def __str__(self):
        return f'{self.envio.tracking_number} → {self.status} @ {self.timestamp}'
