from django.urls import path
from .views import StatsView, ReportesView

urlpatterns = [
    path('stats/', StatsView.as_view(), name='dashboard-stats'),
    path('reportes/', ReportesView.as_view(), name='dashboard-reportes'),
]
