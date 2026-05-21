"""
Comando para poblar la base de datos con datos de prueba.
Uso: python manage.py seed
"""
from django.core.management.base import BaseCommand
from django.utils import timezone

from apps.accounts.models import User
from apps.repartidores.models import Repartidor
from apps.envios.models import Envio, EstadoHistorial


class Command(BaseCommand):
    help = 'Crea datos de prueba para Repartos Rápidos'

    def handle(self, *args, **options):
        self.stdout.write('🌱 Creando datos de prueba...')

        # ── Usuarios ──────────────────────────────────────────────────────────
        admin, _ = User.objects.get_or_create(
            username='admin',
            defaults={
                'first_name': 'María', 'last_name': 'González',
                'email': 'admin@repartosrapidos.co',
                'role': 'admin', 'is_staff': True, 'is_superuser': True,
            },
        )
        admin.set_password('admin1234')
        admin.save()
        self.stdout.write(f'  ✓ Superusuario: admin / admin1234')

        operator, _ = User.objects.get_or_create(
            username='operador',
            defaults={
                'first_name': 'Carlos', 'last_name': 'Ramírez',
                'email': 'operador@repartosrapidos.co',
                'role': 'operator',
            },
        )
        operator.set_password('operador1234')
        operator.save()
        self.stdout.write(f'  ✓ Operador:     operador / operador1234')

        # ── Repartidores ──────────────────────────────────────────────────────
        reps_data = [
            {'name': 'Carlos M.',  'phone': '311 000 0001', 'vehicle': 'moto',      'plate': 'ABC123', 'rating': 4.8},
            {'name': 'Diana R.',   'phone': '312 000 0002', 'vehicle': 'moto',      'plate': 'DEF456', 'rating': 4.5},
            {'name': 'Luis F.',    'phone': '313 000 0003', 'vehicle': 'bicicleta', 'plate': 'GHI789', 'rating': 4.9},
            {'name': 'Empresa X.', 'phone': '314 000 0004', 'vehicle': 'carro',     'plate': 'JKL012', 'rating': 4.2},
            {'name': 'Pedro G.',   'phone': '315 000 0005', 'vehicle': 'moto',      'plate': 'MNO345', 'rating': 3.9},
        ]
        reps = []
        for d in reps_data:
            r, created = Repartidor.objects.get_or_create(plate=d['plate'], defaults=d)
            reps.append(r)
            if created:
                self.stdout.write(f'  ✓ Repartidor: {r.name}')

        # ── Envíos ────────────────────────────────────────────────────────────
        envios_data = [
            {
                'sender_name': 'Juan P.', 'sender_phone': '310 555 0101',
                'sender_address': 'Cra 13 #93-45', 'sender_city': 'Bogotá',
                'recipient_name': 'Ana L.', 'recipient_phone': '300 555 0202',
                'recipient_address': 'Cl 80 #11-22', 'recipient_city': 'Medellín',
                'description': 'Caja con accesorios electrónicos',
                'weight': 2.4, 'height': 20, 'width': 30, 'depth': 15,
                'insured_value': 500000, 'service_type': 'express',
                'status': 'en_entrega', 'repartidor': reps[0], 'operator': admin,
            },
            {
                'sender_name': 'Lucía F.', 'sender_phone': '311 555 0303',
                'sender_address': 'Av. El Dorado #68-95', 'sender_city': 'Bogotá',
                'recipient_name': 'Diana K.', 'recipient_phone': '320 555 0404',
                'recipient_address': 'Carrera 45 #18-30', 'recipient_city': 'Cali',
                'description': 'Documentos legales', 'weight': 0.3,
                'height': 5, 'width': 22, 'depth': 30,
                'insured_value': 0, 'service_type': 'prioritario',
                'status': 'en_bodega', 'repartidor': reps[1], 'operator': admin,
            },
            {
                'sender_name': 'Empresa X.', 'sender_phone': '314 555 0505',
                'sender_address': 'Zona Industrial Chia', 'sender_city': 'Bogotá',
                'recipient_name': 'Luis F.', 'recipient_phone': '313 555 0606',
                'recipient_address': 'Calle 50 #30-15', 'recipient_city': 'Bogotá',
                'description': 'Piezas de repuesto', 'weight': 8.5,
                'height': 40, 'width': 50, 'depth': 30,
                'insured_value': 1200000, 'service_type': 'standard',
                'status': 'recibido', 'repartidor': reps[2], 'operator': operator,
            },
            {
                'sender_name': 'Pedro G.', 'sender_phone': '315 555 0707',
                'sender_address': 'Cra 7 #45-10', 'sender_city': 'Bogotá',
                'recipient_name': 'Carlos M.', 'recipient_phone': '311 555 0808',
                'recipient_address': 'Av. Santander #20-50', 'recipient_city': 'Manizales',
                'description': 'Ropa y calzado', 'weight': 3.1,
                'height': 35, 'width': 45, 'depth': 25,
                'insured_value': 250000, 'service_type': 'standard',
                'status': 'incidencia', 'repartidor': reps[3], 'operator': operator,
            },
            {
                'sender_name': 'María L.', 'sender_phone': '316 555 0909',
                'sender_address': 'Cl 26 #85-20', 'sender_city': 'Bogotá',
                'recipient_name': 'Sofía R.', 'recipient_phone': '321 555 1010',
                'recipient_address': 'Cra 27 #55-10', 'recipient_city': 'Cali',
                'description': 'Cosméticos', 'weight': 1.2,
                'height': 18, 'width': 20, 'depth': 12,
                'insured_value': 350000, 'service_type': 'prioritario',
                'status': 'en_ruta', 'repartidor': reps[4], 'operator': admin,
            },
        ]

        for d in envios_data:
            # Evita duplicar si ya existe un envío con mismo remitente/destinatario/status
            exists = Envio.objects.filter(
                sender_name=d['sender_name'], recipient_name=d['recipient_name']
            ).exists()
            if exists:
                continue

            envio = Envio.objects.create(**d)
            EstadoHistorial.objects.create(envio=envio, status='en_bodega', notes='Recibido en bodega')
            if d['status'] in ['en_ruta', 'en_entrega', 'recibido', 'incidencia']:
                EstadoHistorial.objects.create(envio=envio, status='en_ruta', notes='En camino')
            if d['status'] in ['en_entrega', 'recibido']:
                EstadoHistorial.objects.create(envio=envio, status='en_entrega', notes='Última milla')
            if d['status'] == 'recibido':
                EstadoHistorial.objects.create(envio=envio, status='recibido', notes='Entregado con éxito')
            if d['status'] == 'incidencia':
                EstadoHistorial.objects.create(envio=envio, status='incidencia', notes='Paquete dañado en tránsito')

            self.stdout.write(f'  ✓ Envío: {envio.tracking_number} ({d["status"]})')

        self.stdout.write(self.style.SUCCESS('\n✅ Datos de prueba creados exitosamente.'))
