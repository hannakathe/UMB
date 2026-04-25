"""
Serializers de autenticación y usuarios.

  CustomTokenObtainPairSerializer → extiende el JWT de SimpleJWT para
      incluir datos del usuario directamente en la respuesta de login,
      evitando una segunda petición a /me/ al iniciar sesión.

  UserSerializer        → lectura/actualización del perfil.
  UserCreateSerializer  → creación de usuario con hash de contraseña.
"""

from rest_framework import serializers
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer
from .models import User


class CustomTokenObtainPairSerializer(TokenObtainPairSerializer):
    """
    Añade nombre completo y rol al payload del token JWT
    y devuelve los datos del usuario en la respuesta de login.
    """

    @classmethod
    def get_token(cls, user):
        """Embebe `full_name` y `role` dentro del token para acceso rápido en el frontend."""
        token = super().get_token(user)
        token['full_name'] = user.get_full_name()
        token['role']      = user.role
        return token

    def validate(self, attrs):
        """Agrega la clave `user` al response para que el frontend no necesite llamar a /me/."""
        data = super().validate(attrs)
        data['user'] = {
            'id':        self.user.id,
            'username':  self.user.username,
            'full_name': self.user.get_full_name(),
            'email':     self.user.email,
            'role':      self.user.role,
        }
        return data


class UserSerializer(serializers.ModelSerializer):
    """Serializer estándar para leer y editar el perfil de un usuario."""

    class Meta:
        model  = User
        fields = ('id', 'username', 'first_name', 'last_name', 'email', 'role', 'phone')
        read_only_fields = ('id',)


class UserCreateSerializer(serializers.ModelSerializer):
    """Serializer de creación: acepta `password` en texto plano y lo hashea antes de guardar."""

    password = serializers.CharField(write_only=True, min_length=8)

    class Meta:
        model  = User
        fields = ('username', 'first_name', 'last_name', 'email', 'role', 'phone', 'password')

    def create(self, validated_data):
        """Extrae y hashea la contraseña antes de crear el usuario."""
        password = validated_data.pop('password')
        user = User(**validated_data)
        user.set_password(password)
        user.save()
        return user
