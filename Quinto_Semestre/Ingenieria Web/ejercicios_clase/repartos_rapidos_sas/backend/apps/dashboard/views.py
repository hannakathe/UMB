from django.utils import timezone
from django.db.models import Count, Q
from rest_framework.response import Response
from rest_framework.views import APIView

from apps.envios.models import Envio


class StatsView(APIView):
    def get(self, request):
        today = timezone.localdate()
        yesterday = today - timezone.timedelta(days=1)

        envios_today = Envio.objects.filter(created_at__date=today, is_draft=False)
        envios_yesterday = Envio.objects.filter(created_at__date=yesterday, is_draft=False)

        total_today = envios_today.count()
        total_yesterday = envios_yesterday.count()

        if total_yesterday > 0:
            delta_pct = round((total_today - total_yesterday) / total_yesterday * 100, 1)
        else:
            delta_pct = 0

        en_ruta = Envio.objects.filter(
            status__in=['en_bodega', 'en_ruta', 'en_entrega'],
            is_draft=False,
        ).count()

        total_no_draft = Envio.objects.filter(is_draft=False)
        total_cerrados = total_no_draft.filter(status='recibido').count()
        total_count = total_no_draft.count()
        tasa_entrega = round(total_cerrados / total_count * 100) if total_count else 0

        incidencias = Envio.objects.filter(status='incidencia', is_draft=False).count()

        return Response({
            'envios_hoy': {
                'total': total_today,
                'delta_pct': delta_pct,
            },
            'en_ruta': en_ruta,
            'tasa_entrega': tasa_entrega,
            'incidencias': incidencias,
        })


class ReportesView(APIView):
    def get(self, request):
        qs = Envio.objects.filter(is_draft=False)

        by_status = list(
            qs.values('status').annotate(total=Count('id')).order_by('status')
        )
        by_service = list(
            qs.values('service_type').annotate(total=Count('id')).order_by('service_type')
        )
        by_repartidor = list(
            qs.filter(repartidor__isnull=False)
            .values('repartidor__name')
            .annotate(total=Count('id'), entregados=Count('id', filter=Q(status='recibido')))
            .order_by('-total')[:10]
        )

        return Response({
            'por_estado': by_status,
            'por_servicio': by_service,
            'top_repartidores': by_repartidor,
        })
