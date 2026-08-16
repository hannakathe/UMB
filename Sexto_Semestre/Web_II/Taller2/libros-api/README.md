# libros-api

API REST de libros construida con Node.js + Express, con datos almacenados en memoria (sin base de datos).

## Requisitos

- Node.js 18 o superior

## Instalación y ejecución

```bash
cd libros-api
npm install
npm start
```

El servidor queda escuchando en `http://localhost:3000`.

## API Key

El endpoint `POST /api/libros` requiere el header `x-api-key`. La clave por defecto es:

```
clave-secreta-123
```

Puede sobreescribirse con la variable de entorno `API_KEY`.

## Endpoints

| Método | Ruta               | Descripción                          |
|--------|--------------------|---------------------------------------|
| GET    | /api/libros        | Lista paginada de libros              |
| GET    | /api/libros/:id    | Obtiene un libro por id               |
| POST   | /api/libros        | Crea un libro (requiere x-api-key)    |
| PUT    | /api/libros/:id    | Actualiza un libro                    |
| DELETE | /api/libros/:id    | Elimina un libro                      |

## Ejemplos con curl

### Listar libros (paginación por defecto: page=1, limit=5)

```bash
curl "http://localhost:3000/api/libros"
```

### Listar libros con paginación personalizada

```bash
curl "http://localhost:3000/api/libros?page=2&limit=3"
```

### Obtener un libro por id

```bash
curl "http://localhost:3000/api/libros/1"
```

### Crear un libro CON x-api-key (201 Created)

```bash
curl -X POST "http://localhost:3000/api/libros" \
  -H "Content-Type: application/json" \
  -H "x-api-key: clave-secreta-123" \
  -d '{"titulo":"Ensayo sobre la ceguera","autor":"José Saramago","anio":1995}'
```

### Crear un libro SIN x-api-key (401 No autorizado)

```bash
curl -X POST "http://localhost:3000/api/libros" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Ensayo sobre la ceguera","autor":"José Saramago","anio":1995}'
```

### Actualizar un libro

```bash
curl -X PUT "http://localhost:3000/api/libros/1" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Cien años de soledad (edición actualizada)","autor":"Gabriel García Márquez","anio":1967}'
```

### Eliminar un libro

```bash
curl -X DELETE "http://localhost:3000/api/libros/1"
```

### Ejemplo que dispara 404 (libro inexistente)

```bash
curl "http://localhost:3000/api/libros/9999"
```

### Ejemplo que dispara 422 (datos inválidos, falta "autor")

```bash
curl -X POST "http://localhost:3000/api/libros" \
  -H "Content-Type: application/json" \
  -H "x-api-key: clave-secreta-123" \
  -d '{"titulo":"Libro sin autor"}'
```

## Estructura del proyecto

```
libros-api/
├── server.js
├── package.json
├── middlewares/
│   ├── logger.js   → registra método, ruta y timestamp de cada solicitud
│   └── auth.js     → valida el header x-api-key en POST /api/libros
└── routes/
    └── libros.js   → rutas CRUD de libros (datos en memoria)
```
