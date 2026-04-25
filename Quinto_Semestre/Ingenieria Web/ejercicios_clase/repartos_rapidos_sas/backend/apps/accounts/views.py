from rest_framework import generics, permissions, status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework_simplejwt.views import TokenObtainPairView
from rest_framework_simplejwt.tokens import RefreshToken

from .models import User
from .serializers import CustomTokenObtainPairSerializer, UserSerializer, UserCreateSerializer


class LoginView(TokenObtainPairView):
    serializer_class = CustomTokenObtainPairSerializer
    permission_classes = (permissions.AllowAny,)


class LogoutView(APIView):
    def post(self, request):
        try:
            refresh_token = request.data['refresh']
            token = RefreshToken(refresh_token)
            token.blacklist()
        except Exception:
            pass
        return Response({'detail': 'Sesión cerrada correctamente.'}, status=status.HTTP_200_OK)


class MeView(generics.RetrieveUpdateAPIView):
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user


class UserListCreateView(generics.ListCreateAPIView):
    queryset = User.objects.all().order_by('first_name')

    def get_serializer_class(self):
        if self.request.method == 'POST':
            return UserCreateSerializer
        return UserSerializer

    def get_permissions(self):
        # Only admins can create users
        if self.request.method == 'POST':
            return [permissions.IsAuthenticated(), _IsAdmin()]
        return [permissions.IsAuthenticated()]


class _IsAdmin(permissions.BasePermission):
    def has_permission(self, request, view):
        return request.user.is_authenticated and request.user.role == 'admin'
