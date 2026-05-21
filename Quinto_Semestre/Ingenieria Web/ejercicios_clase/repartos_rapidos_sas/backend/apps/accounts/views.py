"""
Vistas de autenticación y gestión de usuarios.

  LoginView           → POST /api/auth/login/   — obtiene access + refresh tokens
  LogoutView          → POST /api/auth/logout/  — invalida el refresh token (blacklist)
  MeView              → GET/PATCH /api/auth/me/ — perfil del usuario autenticado
  UserListCreateView  → GET/POST /api/auth/users/ — solo admin puede crear usuarios
"""

from rest_framework import generics, permissions, status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework_simplejwt.views import TokenObtainPairView
from rest_framework_simplejwt.tokens import RefreshToken

from .models import User
from .serializers import CustomTokenObtainPairSerializer, UserSerializer, UserCreateSerializer


class LoginView(TokenObtainPairView):
    """Retorna access + refresh token junto con los datos básicos del usuario."""
    serializer_class  = CustomTokenObtainPairSerializer
    permission_classes = (permissions.AllowAny,)


class LogoutView(APIView):
    """
    Invalida el refresh token añadiéndolo a la lista negra de SimpleJWT.
    Si el token ya expiró o es inválido, responde 200 igualmente
    (el frontend siempre debe limpiar su localStorage).
    """

    def post(self, request):
        try:
            refresh_token = request.data['refresh']
            token = RefreshToken(refresh_token)
            token.blacklist()
        except Exception:
            # No interrumpir el logout aunque el token sea inválido
            pass
        return Response({'detail': 'Sesión cerrada correctamente.'}, status=status.HTTP_200_OK)


class MeView(generics.RetrieveUpdateAPIView):
    """Devuelve o actualiza el perfil del usuario que realiza la petición."""
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user


class UserListCreateView(generics.ListCreateAPIView):
    """
    GET  → lista todos los usuarios (solo autenticados).
    POST → crea un usuario nuevo (solo administradores).
    """
    queryset = User.objects.all().order_by('first_name')

    def get_serializer_class(self):
        # Al crear, usamos el serializer que acepta y hashea la contraseña
        if self.request.method == 'POST':
            return UserCreateSerializer
        return UserSerializer

    def get_permissions(self):
        # Solo el admin puede crear usuarios; cualquier autenticado puede listarlos
        if self.request.method == 'POST':
            return [permissions.IsAuthenticated(), _IsAdmin()]
        return [permissions.IsAuthenticated()]


class _IsAdmin(permissions.BasePermission):
    """Permiso auxiliar: solo pasa si el usuario tiene rol 'admin'."""
    def has_permission(self, request, view):
        return request.user.is_authenticated and request.user.role == 'admin'
