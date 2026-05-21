"""
Modelo de usuario extendido.

Extiende AbstractUser de Django añadiendo:
  - campo `role` para diferenciar Administrador y Operador
  - campo `phone` opcional
  - propiedad `is_admin_role` para simplificar las comprobaciones de permisos
"""

from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    """
    Usuario del sistema.

    Roles:
      - admin    → acceso completo: ve todos los envíos, reportes y repartidores
      - operator → acceso restringido: solo ve sus propios envíos
    """

    class Role(models.TextChoices):
        ADMIN    = 'admin',    'Administrador'
        OPERATOR = 'operator', 'Operador'

    role  = models.CharField(max_length=20, choices=Role.choices, default=Role.OPERATOR)
    phone = models.CharField(max_length=20, blank=True)

    class Meta:
        db_table        = 'accounts_user'
        verbose_name    = 'Usuario'
        verbose_name_plural = 'Usuarios'

    def __str__(self):
        return f'{self.get_full_name() or self.username} ({self.get_role_display()})'

    @property
    def is_admin_role(self):
        """Atajo para comprobar si el usuario es administrador en vistas y permisos."""
        return self.role == self.Role.ADMIN
