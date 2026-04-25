from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator


class Repartidor(models.Model):
    class Vehicle(models.TextChoices):
        MOTO = 'moto', 'Moto'
        BICICLETA = 'bicicleta', 'Bicicleta'
        CARRO = 'carro', 'Carro'

    name = models.CharField(max_length=150, verbose_name='Nombre completo')
    phone = models.CharField(max_length=20, verbose_name='Teléfono')
    vehicle = models.CharField(max_length=20, choices=Vehicle.choices, default=Vehicle.MOTO)
    plate = models.CharField(max_length=10, verbose_name='Placa')
    rating = models.DecimalField(
        max_digits=3, decimal_places=1,
        default=5.0,
        validators=[MinValueValidator(0), MaxValueValidator(5)],
        verbose_name='Calificación',
    )
    is_active = models.BooleanField(default=True, verbose_name='Activo')
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'repartidores'
        verbose_name = 'Repartidor'
        verbose_name_plural = 'Repartidores'
        ordering = ('name',)

    def __str__(self):
        return f'{self.name} ({self.plate})'
