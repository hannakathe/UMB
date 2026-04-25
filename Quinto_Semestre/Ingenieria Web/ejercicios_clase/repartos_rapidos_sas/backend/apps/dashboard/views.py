from django.utils import timezone
from django.db.models import Count, Q
from rest_framework.response import Response
from rest_framework.views import APIView

from apps.envios.models import Envio


def _base_qs(user):
    """Devuelve el queryset base filtrado según el rol del usuario."""
    qs = Envio.objects.filter(is_draft=False)
    if not user.is_admin_role:
        qs = qs.filter(operator=user)
    return qs


class StatsView(APIView):
    def get(self, request):
        today = timezone.localdate()
        yesterday = today - timezone.timedelta(days=1)

        base = _base_qs(request.user)

        total_today = base.filter(created_at__date=today).count()
        total_yesterday = base.filter(created_at__date=yesterday).count()

        delta_pct = 0
        if total_yesterday > 0:
            delta_pct = round((total_today - total_yesterday) / total_yesterday * 100, 1)

        en_ruta = base.filter(status__in=['en_bodega', 'en_ruta', 'en_entrega']).count()

        total_count = base.count()
        total_cerrados = base.filter(status='recibido').count()
        tasa_entrega = round(total_cerrados / total_count * 100) if total_count else 0

        incidencias = base.filter(status='incidencia').count()

        return Response({
            'envios_hoy': {'total': total_today, 'delta_pct': delta_pct},
            'en_ruta': en_ruta,
            'tasa_entrega': tasa_entrega,
            'incidencias': incidencias,
            'total_envios': total_count,
        })


class ReportesView(APIView):
    def get(self, request):
        # Reportes globales: solo admin
        if not request.user.is_admin_role:
            return Response(
                {'detail': 'No tienes permiso para ver los reportes globales.'},
                status=403,
            )

        qs = Envio.objects.filter(is_draft=False)

        by_status = list(qs.values('status').annotate(total=Count('id')).order_by('status'))
        by_service = list(qs.values('service_type').annotate(total=Count('id')).order_by('service_type'))
        by_repartidor = list(
            qs.filter(repartidor__isnull=False)
            .values('repartidor__name')
            .annotate(total=Count('id'), entregados=Count('id', filter=Q(status='recibido')))
            .order_by('-total')[:10]
        )
        by_operator = list(
            qs.filter(operator__isnull=False)
            .values('operator__first_name', 'operator__last_name')
            .annotate(total=Count('id'), entregados=Count('id', filter=Q(status='recibido')))
            .order_by('-total')
        )

        return Response({
            'por_estado': by_status,
            'por_servicio': by_service,
            'top_repartidores': by_repartidor,
            'por_operador': by_operator,
        })
