# Repartos Rápidos S.A.S — Sistema de Gestión de Envíos

Sistema fullstack para la gestión, asignación y rastreo público de envíos de una empresa de mensajería. Construido a partir de los wireframes del documento *"Repartos Rápidos · Wireframes & Arquitectura MVP"*.

---

## Tabla de contenidos

1. [Descripción general](#1-descripción-general)
2. [Arquitectura del sistema](#2-arquitectura-del-sistema)
3. [Stack tecnológico](#3-stack-tecnológico)
4. [Requisitos previos](#4-requisitos-previos)
5. [Instalación y configuración](#5-instalación-y-configuración)
6. [Variables de entorno](#6-variables-de-entorno)
7. [Estructura del proyecto](#7-estructura-del-proyecto)
8. [Modelo de datos](#8-modelo-de-datos)
9. [API REST — Referencia completa](#9-api-rest--referencia-completa)
10. [Rutas del frontend](#10-rutas-del-frontend)
11. [Funcionalidades implementadas](#11-funcionalidades-implementadas)
12. [Decisiones técnicas](#12-decisiones-técnicas)
13. [Flujo de desarrollo](#13-flujo-de-desarrollo)

---

## 1. Descripción general

Repartos Rápidos es una plataforma de gestión logística de última milla que permite a los operadores:

- **Crear envíos** con datos de remitente, destinatario, paquete y servicio.
- **Asignar repartidores** disponibles a cada envío.
- **Monitorear el dashboard** con KPIs en tiempo real (envíos del día, tasa de entrega, incidencias).
- **Cambiar el estado** de los envíos y registrar un historial completo.
- **Exportar CSV** con el listado filtrado de envíos.

Y a los **clientes finales** (sin login):

- **Rastrear su paquete** ingresando el número de guía (`RPD-YYYY-XXXXXX`).
- Ver el **timeline de estados** y la información del repartidor asignado.

---

## 2. Arquitectura del sistema

```
┌──────────────────────────────┐          HTTP / JSON
│        Navegador             │  ◄──────────────────────►  ┌──────────────────────────────┐
│                              │                             │                              │
│   React 18  (Vite)           │    Authorization: Bearer    │   Django 5  +  DRF           │
│   React Router v6            │         JWT Token           │   djangorestframework-        │
│   Context API (AuthContext)  │                             │   simplejwt                  │
│   Axios + interceptores      │                             │   django-filter              │
│   Puerto 5173                │                             │   Puerto 8000                │
└──────────────────────────────┘                             └─────────────┬────────────────┘
                                                                           │
                                                                           │  ORM / mysqlclient
                                                                           ▼
                                                             ┌──────────────────────────────┐
                                                             │   MySQL 8                    │
                                                             │   DB: repartos_rapidos       │
                                                             │   charset: utf8mb4           │
                                                             └──────────────────────────────┘
```

### Separación de responsabilidades

| Capa | Tecnología | Responsabilidad |
|------|-----------|-----------------|
| **Presentación** | React + CSS | Renderizado UI, navegación, estado local |
| **Lógica de negocio** | Django + DRF | Validaciones, reglas, generación de guía, historial |
| **Acceso a datos** | Django ORM | Queries, relaciones, migraciones |
| **Persistencia** | MySQL 8 | Almacenamiento relacional |

---

## 3. Stack tecnológico

### Backend

| Paquete | Versión | Uso |
|---------|---------|-----|
| Django | 5.1.4 | Framework principal |
| djangorestframework | 3.15.2 | API REST |
| djangorestframework-simplejwt | 5.3.1 | Autenticación JWT |
| django-cors-headers | 4.6.0 | CORS para el frontend |
| django-filter | 24.3 | Filtros en los listados |
| mysqlclient | 2.2.4 | Conector MySQL |
| python-decouple | 3.8 | Variables de entorno (.env) |

### Frontend

| Paquete | Versión | Uso |
|---------|---------|-----|
| React | 18.3.1 | UI |
| React Router DOM | 6.28.0 | Navegación SPA |
| Axios | 1.7.9 | Cliente HTTP |
| Vite | 6.0.5 | Bundler y dev server |

---

## 4. Requisitos previos

| Herramienta | Versión mínima | Verificar con |
|-------------|---------------|---------------|
| Python | 3.10 | `python --version` |
| pip | cualquiera | `pip --version` |
| Node.js | 18 | `node --version` |
| npm | 9 | `npm --version` |
| MySQL Server | 8.0 | `mysql --version` |

---

## 5. Instalación y configuración

### Paso 1 — Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd repartos_rapidos_sas
```

### Paso 2 — Base de datos MySQL

Conéctate a tu servidor MySQL y ejecuta:

```sql
CREATE DATABASE repartos_rapidos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'rr_user'@'localhost' IDENTIFIED BY 'rr_pass_2026';
GRANT ALL PRIVILEGES ON repartos_rapidos.* TO 'rr_user'@'localhost';
FLUSH PRIVILEGES;
```

### Paso 3 — Backend (Django)

```bash
cd backend

# 1. Crear y activar entorno virtual
python -m venv venv

# Windows:
venv\Scripts\activate
# macOS / Linux:
source venv/bin/activate

# 2. Instalar dependencias
pip install -r requirements.txt

# 3. Configurar variables de entorno
copy .env.example .env     # Windows
cp .env.example .env       # macOS / Linux
# → Editar .env con las credenciales de tu base de datos (ver sección 6)

# 4. Crear migraciones de las apps propias
python manage.py makemigrations accounts repartidores envios

# 5. Aplicar todas las migraciones
python manage.py migrate

# 6. Cargar datos de prueba (usuarios, repartidores, envíos de ejemplo)
python manage.py seed

# 7. Iniciar servidor de desarrollo
python manage.py runserver
```

El backend queda disponible en **http://localhost:8000**

> **Alternativa a mysqlclient en Windows:**  
> Si `pip install mysqlclient` falla, instala `PyMySQL` y añade al inicio de `manage.py`:
> ```python
> import pymysql
> pymysql.install_as_MySQLdb()
> ```

### Paso 4 — Frontend (React)

Abre **otra terminal** (el backend debe seguir corriendo):

```bash
cd frontend
npm install
npm run dev
```

La aplicación queda disponible en **http://localhost:5173**

### Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin1234` | Administrador |
| `operador` | `operador1234` | Operador |

---

## 6. Variables de entorno

Copia `backend/.env.example` a `backend/.env` y ajusta cada valor:

```env
# Clave secreta de Django (cámbiala en producción)
SECRET_KEY=cambia-esto-por-una-clave-secreta-larga

# Modo debug (False en producción)
DEBUG=True

# Conexión MySQL
DB_NAME=repartos_rapidos
DB_USER=rr_user
DB_PASSWORD=rr_pass_2026
DB_HOST=localhost
DB_PORT=3306

# Hosts permitidos (separados por coma)
ALLOWED_HOSTS=localhost,127.0.0.1

# Orígenes permitidos para CORS
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

> El archivo `.env` está en `.gitignore` y **nunca debe subirse al repositorio**.

---

## 7. Estructura del proyecto

```
repartos_rapidos_sas/
│
├── README.md
├── .gitignore
│
├── backend/
│   ├── manage.py
│   ├── requirements.txt
│   ├── .env.example               ← plantilla de variables (sin secretos)
│   │
│   ├── config/                    ← configuración del proyecto Django
│   │   ├── settings.py            ← DB, JWT, CORS, apps instaladas
│   │   ├── urls.py                ← rutas raíz de la API
│   │   └── wsgi.py
│   │
│   └── apps/
│       ├── accounts/              ← usuarios y autenticación
│       │   ├── models.py          ← User extendido con campo 'role'
│       │   ├── serializers.py     ← TokenObtainPair personalizado
│       │   ├── views.py           ← Login, Logout, Me, ListUsuarios
│       │   ├── urls.py
│       │   ├── admin.py
│       │   └── management/
│       │       └── commands/
│       │           └── seed.py    ← python manage.py seed
│       │
│       ├── repartidores/          ← gestión de repartidores
│       │   ├── models.py          ← Repartidor (nombre, placa, vehículo, rating)
│       │   ├── serializers.py
│       │   ├── views.py           ← ModelViewSet con filtros y búsqueda
│       │   ├── urls.py
│       │   └── admin.py
│       │
│       ├── envios/                ← núcleo del negocio
│       │   ├── models.py          ← Envio + EstadoHistorial
│       │   ├── serializers.py     ← List / Detail / Tracking (público)
│       │   ├── views.py           ← CRUD + cambiar-estado + rastreo público
│       │   ├── urls.py
│       │   └── admin.py
│       │
│       └── dashboard/             ← métricas y reportes
│           ├── views.py           ← StatsView + ReportesView
│           └── urls.py
│
└── frontend/
    ├── index.html
    ├── package.json
    ├── vite.config.js             ← proxy /api → localhost:8000
    │
    └── src/
        ├── main.jsx
        ├── App.jsx                ← router raíz con todas las rutas
        ├── index.css              ← sistema de diseño (CSS variables, utilidades)
        │
        ├── api/                   ← capa de acceso a la API (toda la lógica HTTP aquí)
        │   ├── client.js          ← axios + interceptor JWT automático
        │   ├── auth.js
        │   ├── envios.js
        │   ├── repartidores.js
        │   └── dashboard.js
        │
        ├── context/
        │   └── AuthContext.jsx    ← estado global de autenticación
        │
        ├── components/            ← componentes reutilizables
        │   ├── AdminLayout.jsx    ← Sidebar + Topbar + <Outlet>
        │   ├── Sidebar.jsx
        │   ├── Topbar.jsx
        │   ├── ProtectedRoute.jsx ← redirige a /login si no hay sesión
        │   ├── KPICard.jsx
        │   ├── StatusBadge.jsx
        │   └── StatusTimeline.jsx
        │
        └── pages/
            ├── TrackingPage.jsx        ←  /           (pública)
            ├── TrackingResultPage.jsx  ←  /rastrear/:guia (pública)
            ├── LoginPage.jsx           ←  /login
            └── admin/
                ├── DashboardPage.jsx
                ├── EnviosPage.jsx
                ├── NewEnvioPage.jsx
                ├── EnvioDetailPage.jsx
                ├── RepartidoresPage.jsx
                └── ReportesPage.jsx
```

---

## 8. Modelo de datos

```
┌────────────────────────────────────────────────────────────────────────┐
│  accounts_user                                                         │
│  ─────────────────────────────────────────────────────────────────     │
│  id           INT PK AUTO                                              │
│  username      VARCHAR(150) UNIQUE                                     │
│  first_name    VARCHAR(150)                                            │
│  last_name     VARCHAR(150)                                            │
│  email         VARCHAR(254)                                            │
│  role          ENUM('admin','operator')  DEFAULT 'operator'           │
│  phone         VARCHAR(20)                                             │
│  password      VARCHAR(128)  (hash bcrypt)                             │
│  is_active     BOOLEAN DEFAULT TRUE                                    │
└────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────┐
│  repartidores                                                          │
│  ─────────────────────────────────────────────────────────────────     │
│  id           INT PK AUTO                                              │
│  name          VARCHAR(150)                                            │
│  phone         VARCHAR(20)                                             │
│  vehicle       ENUM('moto','bicicleta','carro')                        │
│  plate         VARCHAR(10)                                             │
│  rating        DECIMAL(3,1)  CHECK 0..5                                │
│  is_active     BOOLEAN DEFAULT TRUE                                    │
│  created_at    DATETIME                                                │
└────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────┐
│  envios                                                                │
│  ─────────────────────────────────────────────────────────────────     │
│  id               INT PK AUTO                                          │
│  tracking_number   VARCHAR(20) UNIQUE  (RPD-YYYY-XXXXXX)               │
│  is_draft          BOOLEAN DEFAULT FALSE                               │
│  sender_name       VARCHAR(150)                                        │
│  sender_phone      VARCHAR(20)                                         │
│  sender_address    VARCHAR(300)                                        │
│  sender_city       VARCHAR(100)                                        │
│  recipient_name    VARCHAR(150)                                        │
│  recipient_phone   VARCHAR(20)                                         │
│  recipient_address VARCHAR(300)                                        │
│  recipient_city    VARCHAR(100)                                        │
│  description       TEXT                                                │
│  weight            DECIMAL(8,2)   kg                                   │
│  height            DECIMAL(8,2)   cm                                   │
│  width             DECIMAL(8,2)   cm                                   │
│  depth             DECIMAL(8,2)   cm                                   │
│  insured_value     DECIMAL(12,2)  COP                                  │
│  service_type      ENUM('standard','prioritario','express')            │
│  status            ENUM('en_bodega','en_ruta','en_entrega',            │
│                         'recibido','incidencia')                       │
│  repartidor_id     INT FK → repartidores.id  (nullable)               │
│  operator_id       INT FK → accounts_user.id (nullable)               │
│  created_at        DATETIME                                            │
│  updated_at        DATETIME                                            │
└────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────┐
│  envios_historial                                                      │
│  ─────────────────────────────────────────────────────────────────     │
│  id        INT PK AUTO                                                 │
│  envio_id   INT FK → envios.id  CASCADE DELETE                         │
│  status     ENUM (mismos valores que envios.status)                    │
│  notes      TEXT                                                       │
│  timestamp  DATETIME AUTO                                              │
└────────────────────────────────────────────────────────────────────────┘
```

**Relaciones:**
- `envios.repartidor_id` → `repartidores.id` — ManyToOne (nullable)
- `envios.operator_id` → `accounts_user.id` — ManyToOne (nullable)
- `envios_historial.envio_id` → `envios.id` — OneToMany, CASCADE DELETE

---

## 9. API REST — Referencia completa

La URL base es `http://localhost:8000/api`. Todos los endpoints privados requieren el header:

```
Authorization: Bearer <access_token>
```

---

### 9.1 Autenticación — `/api/auth/`

#### `POST /api/auth/login/`
Obtiene el par de tokens JWT.

```json
// Request:
{ "username": "admin", "password": "admin1234" }

// Response 200:
{
  "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "full_name": "María González",
    "email": "admin@repartosrapidos.co",
    "role": "admin"
  }
}

// Response 401:
{ "detail": "No active account found with the given credentials" }
```

#### `POST /api/auth/refresh/`
Renueva el access token (no requiere `Authorization` header).

```json
// Request:
{ "refresh": "eyJ..." }

// Response 200:
{ "access": "eyJ..." }
```

#### `POST /api/auth/logout/`  🔒
Invalida el refresh token.

```json
// Request:
{ "refresh": "eyJ..." }

// Response 200:
{ "detail": "Sesión cerrada correctamente." }
```

#### `GET /api/auth/me/`  🔒
Devuelve el perfil del usuario autenticado.

```json
// Response 200:
{
  "id": 1, "username": "admin",
  "first_name": "María", "last_name": "González",
  "email": "admin@repartosrapidos.co",
  "role": "admin", "phone": ""
}
```

---

### 9.2 Envíos — `/api/envios/`

#### `GET /api/envios/`  🔒
Lista paginada de envíos. Soporta filtros por query string.

| Parámetro | Tipo | Ejemplo |
|-----------|------|---------|
| `status` | string | `?status=en_ruta` |
| `service_type` | string | `?service_type=express` |
| `is_draft` | bool | `?is_draft=false` |
| `repartidor` | int | `?repartidor=2` |
| `search` | string | `?search=RPD-2026` |
| `page` | int | `?page=2` |

```json
// Response 200:
{
  "count": 47,
  "next": "http://localhost:8000/api/envios/?page=3",
  "previous": "http://localhost:8000/api/envios/?page=1",
  "results": [
    {
      "id": 1,
      "tracking_number": "RPD-2026-045782",
      "is_draft": false,
      "sender_name": "Juan P.",
      "sender_city": "Bogotá",
      "recipient_name": "Ana L.",
      "recipient_city": "Medellín",
      "repartidor": 1,
      "repartidor_name": "Carlos M.",
      "status": "en_entrega",
      "status_display": "En entrega",
      "service_type": "express",
      "service_type_display": "Express",
      "created_at": "2026-04-18T09:00:00Z"
    }
  ]
}
```

#### `POST /api/envios/`  🔒
Crea un envío nuevo (definitivo o borrador).

```json
// Request:
{
  "sender_name": "Juan Pérez",
  "sender_phone": "+57 311 555 0143",
  "sender_address": "Cra 13 #93-45",
  "sender_city": "Bogotá",
  "recipient_name": "Ana López",
  "recipient_phone": "3005510099",
  "recipient_address": "Cl 80 #11-22",
  "recipient_city": "Medellín",
  "description": "Caja con accesorios electrónicos",
  "weight": 2.4,
  "height": 20,
  "width": 30,
  "depth": 15,
  "insured_value": 500000,
  "service_type": "express",
  "repartidor": 1,
  "is_draft": false
}

// Response 201:
{
  "id": 6,
  "tracking_number": "RPD-2026-047821",
  "status": "en_bodega",
  "status_display": "En bodega",
  "historial": [
    {
      "id": 1,
      "status": "en_bodega",
      "status_display": "En bodega",
      "notes": "Envío creado",
      "timestamp": "2026-04-25T10:00:00Z"
    }
  ],
  ...
}
```

#### `GET /api/envios/{id}/`  🔒
Detalle completo con historial y datos del repartidor.

#### `PATCH /api/envios/{id}/`  🔒
Actualiza campos parciales (ej: reasignar repartidor).

```json
// Request:
{ "repartidor": 3 }

// Response 200: objeto Envio completo actualizado
```

#### `DELETE /api/envios/{id}/`  🔒
Elimina un envío. `Response 204`.

#### `POST /api/envios/{id}/cambiar-estado/`  🔒
Cambia el estado y registra automáticamente en el historial.

```json
// Request:
{ "status": "en_ruta", "notes": "Salió de bodega Bogotá" }

// Response 200: objeto Envio completo con historial actualizado
```

Estados válidos: `en_bodega` → `en_ruta` → `en_entrega` → `recibido` | `incidencia`

#### `GET /api/envios/borradores/`  🔒
Lista solo los envíos con `is_draft=true`.

#### `GET /api/envios/rastrear/{tracking_number}/`  🌐 público
No requiere autenticación. Devuelve información de seguimiento sin datos sensibles del operador.

```json
// GET /api/envios/rastrear/RPD-2026-045782/
// Response 200:
{
  "tracking_number": "RPD-2026-045782",
  "sender_name": "Juan P.",
  "sender_city": "Bogotá",
  "recipient_name": "Ana L.",
  "recipient_city": "Medellín",
  "status": "en_entrega",
  "status_display": "En entrega",
  "service_type": "express",
  "repartidor_name": "Carlos M.",
  "repartidor_vehicle": "moto",
  "repartidor_plate": "ABC123",
  "repartidor_rating": "4.8",
  "historial": [
    { "status": "en_bodega",  "status_display": "En bodega",   "notes": "Recibido en bodega",  "timestamp": "2026-04-18T10:00:00Z" },
    { "status": "en_ruta",    "status_display": "En ruta",     "notes": "En camino",            "timestamp": "2026-04-18T11:30:00Z" },
    { "status": "en_entrega", "status_display": "En entrega",  "notes": "Última milla",         "timestamp": "2026-04-18T13:45:00Z" }
  ],
  "created_at": "2026-04-18T09:00:00Z"
}

// Response 404:
{ "detail": "No found." }
```

---

### 9.3 Repartidores — `/api/repartidores/`

#### `GET /api/repartidores/`  🔒
Lista con filtros opcionales: `?is_active=true`, `?vehicle=moto`, `?search=carlos`.

```json
// Response 200:
[
  {
    "id": 1,
    "name": "Carlos M.",
    "phone": "311 000 0001",
    "vehicle": "moto",
    "plate": "ABC123",
    "rating": "4.8",
    "is_active": true
  }
]
```

#### `POST /api/repartidores/`  🔒
```json
// Request:
{
  "name": "Diana R.",
  "phone": "312 000 0002",
  "vehicle": "moto",
  "plate": "DEF456",
  "rating": 4.5,
  "is_active": true
}
// Response 201: objeto Repartidor completo con envios_activos
```

#### `GET /api/repartidores/{id}/`  🔒  
Incluye el campo calculado `envios_activos` (cantidad de envíos en curso).

#### `PATCH /api/repartidores/{id}/`  🔒  
Actualiza campos parciales. Usar `{ "is_active": false }` para desactivar.

#### `DELETE /api/repartidores/{id}/`  🔒  
`Response 204`.

---

### 9.4 Dashboard — `/api/dashboard/`

#### `GET /api/dashboard/stats/`  🔒
KPIs del día actual comparados con el día anterior.

```json
// Response 200:
{
  "envios_hoy": {
    "total": 47,
    "delta_pct": 13.0
  },
  "en_ruta": 23,
  "tasa_entrega": 89,
  "incidencias": 3
}
```

| Campo | Descripción |
|-------|-------------|
| `envios_hoy.total` | Envíos creados hoy (no borradores) |
| `envios_hoy.delta_pct` | Variación % respecto a ayer |
| `en_ruta` | Envíos con estado `en_bodega`, `en_ruta` o `en_entrega` |
| `tasa_entrega` | `recibidos / total_no_borradores * 100` |
| `incidencias` | Envíos con estado `incidencia` |

#### `GET /api/dashboard/reportes/`  🔒
Datos agregados para gráficas.

```json
// Response 200:
{
  "por_estado": [
    { "status": "en_bodega",  "total": 8 },
    { "status": "en_entrega", "total": 5 },
    { "status": "recibido",   "total": 31 }
  ],
  "por_servicio": [
    { "service_type": "express",     "total": 15 },
    { "service_type": "prioritario", "total": 12 },
    { "service_type": "standard",    "total": 20 }
  ],
  "top_repartidores": [
    {
      "repartidor__name": "Carlos M.",
      "total": 18,
      "entregados": 15
    }
  ]
}
```

---

## 10. Rutas del frontend

| Ruta | Componente | Auth | Descripción |
|------|-----------|------|-------------|
| `/` | `TrackingPage` | ❌ | Buscador de paquetes (página principal pública) |
| `/rastrear/:trackingNumber` | `TrackingResultPage` | ❌ | Resultado del rastreo con timeline |
| `/login` | `LoginPage` | ❌ | Formulario de acceso |
| `/admin/dashboard` | `DashboardPage` | ✅ | KPIs + tabla de envíos activos |
| `/admin/envios` | `EnviosPage` | ✅ | Listado completo con filtros y paginación |
| `/admin/envios/nuevo` | `NewEnvioPage` | ✅ | Formulario de 5 secciones (wireframe pág. 1) |
| `/admin/envios/:id` | `EnvioDetailPage` | ✅ | Detalle + cambio de estado + historial |
| `/admin/repartidores` | `RepartidoresPage` | ✅ | CRUD con modal integrado |
| `/admin/reportes` | `ReportesPage` | ✅ | Gráficas de barras por estado y servicio |

Las rutas `/admin/*` están protegidas por `<ProtectedRoute>`. Si no hay sesión activa, redirigen automáticamente a `/login`.

---

## 11. Funcionalidades implementadas

### Página pública de rastreo
- Formulario de búsqueda por número de guía (`RPD-YYYY-XXXXXX`)
- Timeline visual de estados (En bodega → En ruta → En entrega → Recibido)
- Indicador del estado actual con timestamp
- Información del repartidor (nombre, vehículo, placa, calificación)
- Manejo de guía no encontrada (error 404)

### Autenticación
- Login con usuario y contraseña
- Tokens JWT almacenados en `localStorage`
- Renovación automática del access token (interceptor en Axios)
- Logout con invalidación del refresh token
- Protección de rutas privadas con redirección

### Dashboard de administración
- 4 KPI cards: envíos del día, paquetes en ruta, tasa de entrega e incidencias
- Variación porcentual vs. el día anterior con indicador de color
- Tabla filtrable por estado y búsqueda libre
- Cambio rápido de estado desde la tabla sin salir de la página
- Exportar el listado filtrado como archivo CSV
- Paginación (20 registros por página)

### Gestión de envíos
- Creación con 5 secciones según wireframe: remitente, destinatario, paquete, repartidor y servicio
- Opción de guardar como borrador o crear directamente
- Filtros combinables: estado, tipo de servicio, borradores/activos, búsqueda
- Detalle de envío con historial cronológico de estados
- Cambio de estado con nota opcional (registrado en historial)
- Reasignación de repartidor desde el detalle
- Eliminación con confirmación

### Gestión de repartidores
- Listado con búsqueda por nombre o placa
- Modal de creación y edición (sin salir de la página)
- Activar / desactivar repartidor
- Contador de envíos activos por repartidor
- Eliminación con confirmación

### Reportes
- Barras de porcentaje por estado de envío
- Barras de porcentaje por tipo de servicio
- Ranking top-10 repartidores con tasa de entrega individual

---

## 12. Decisiones técnicas

| Decisión | Justificación |
|----------|--------------|
| **JWT + refresh automático** | El interceptor de Axios renueva el token de forma transparente al usuario cuando expira, sin forzar un nuevo login. |
| **Tracking number autogenerado** | El campo `tracking_number` usa `default=_generate_tracking` directamente en el modelo. La lógica de generación está en la capa de datos, no en la vista ni en el frontend. |
| **`set_status()` en el modelo** | Encapsula en un solo método la actualización del estado + la creación del registro en `EstadoHistorial`. Evita duplicar esa lógica en vistas o serializers. |
| **Serializer separado para rastreo público** | `TrackingSerializer` expone solo los campos seguros (sin datos del operador ni información interna). El endpoint usa `AllowAny`. |
| **Proxy en Vite** | `/api` → `localhost:8000` durante desarrollo. El frontend nunca hace llamadas directas a `localhost:8000`, lo que facilita cambiar el host en producción con un solo cambio de variable. |
| **CSS puro sin framework** | CSS variables (`--rr-red`, `--sidebar-w`, etc.) dan consistencia sin añadir dependencias. Facilita la lectura para evaluadores del proyecto. |
| **Comando `seed`** | `python manage.py seed` es idempotente y reproducible. Cualquier miembro del equipo puede poblar su base de datos local con un solo comando. |
| **Apps modulares en Django** | Cada dominio de negocio (`accounts`, `envios`, `repartidores`, `dashboard`) vive en su propia app con modelos, serializers, vistas y URLs independientes. |

---

## 13. Flujo de desarrollo

### Comandos útiles

```bash
# Backend
python manage.py runserver          # Iniciar servidor
python manage.py makemigrations     # Crear migraciones tras cambiar modelos
python manage.py migrate            # Aplicar migraciones pendientes
python manage.py seed               # Recargar datos de prueba
python manage.py createsuperuser    # Crear superusuario manualmente
python manage.py shell              # Shell interactivo de Django

# Frontend
npm run dev      # Servidor de desarrollo con HMR
npm run build    # Build de producción → frontend/dist/
npm run preview  # Previsualizar el build de producción
```

### Panel de administración Django

Django incluye un panel admin en **http://localhost:8000/admin** con acceso completo a todos los modelos. Ingresar con el usuario `admin` / `admin1234`.

### Probar la API manualmente

Puedes usar curl, Postman o cualquier cliente HTTP:

```bash
# 1. Obtener token
curl -X POST http://localhost:8000/api/auth/login/ \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin1234"}'

# 2. Usar el token en peticiones privadas
curl http://localhost:8000/api/dashboard/stats/ \
  -H "Authorization: Bearer <access_token>"

# 3. Rastreo público (sin token)
curl http://localhost:8000/api/envios/rastrear/RPD-2026-045782/
```

---

*Proyecto académico — Ingeniería Web, 5.º semestre, Universidad Manuela Beltrán.*
