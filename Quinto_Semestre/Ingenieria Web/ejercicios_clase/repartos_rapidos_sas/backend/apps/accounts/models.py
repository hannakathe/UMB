from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    class Role(models.TextChoices):
        ADMIN = 'admin', 'Administrador'
        OPERATOR = 'operator', 'Operador'

    role = models.CharField(max_length=20, choices=Role.choices, default=Role.OPERATOR)
    phone = models.CharField(max_length=20, blank=True)

    class Meta:
        db_table = 'accounts_user'
        verbose_name = 'Usuario'
        verbose_name_plural = 'Usuarios'

    def __str__(self):
        return f'{self.get_full_name() or self.username} ({self.get_role_display()})'

    @property
    def is_admin_role(self):
        return self.role == self.Role.ADMIN
