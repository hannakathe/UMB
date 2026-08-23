# Taller 3 — Sesión 3: Integración Backend-Frontend

Ingeniería Web II — Guía 2, Sesión 3 (Hanna).

Reutiliza el backend Express de [`libros-api`](../Taller2/libros-api) (Taller 2) y agrega un
frontend React (`frontend/`) que consume el endpoint `GET /api/libros` con manejo explícito
de los estados `loading`, `error` y `data`.

## 1. Ejecutar el backend

```bash
cd ../Taller2/libros-api
npm install
npm start
```

Queda escuchando en `http://localhost:3000`. Endpoint reutilizado: `GET /api/libros`.

## 2. Ejecutar el frontend

```bash
cd frontend
npm install
npm run dev
```

Queda disponible en `http://localhost:5173`.

## 3. Probar el endpoint

```bash
curl "http://localhost:3000/api/libros?limit=2"
```

## 4. Verificar el encabezado CORS

Con el backend corriendo, simula una petición desde el origin del frontend:

```bash
curl -i -H "Origin: http://localhost:5173" "http://localhost:3000/api/libros?limit=2"
```

La respuesta debe incluir:

```
Access-Control-Allow-Origin: http://localhost:5173
Vary: Origin
```

Configurado en [`libros-api/server.js`](../Taller2/libros-api/server.js) con el paquete
`cors`, restringido a `FRONTEND_ORIGIN` (por defecto `http://localhost:5173`, configurable
por variable de entorno). Con cualquier otro `Origin`, el header sigue devolviendo solo
`http://localhost:5173`, por lo que el navegador bloquea la respuesta en ese otro origen.

También puede verificarse desde el navegador: abrir `http://localhost:5173`, abrir
DevTools → pestaña Network → seleccionar la petición a `/api/libros` → ver Response Headers.

## 5. Comprobar loading / error / data

Con backend y frontend corriendo, abrir `http://localhost:5173`:

- **loading**: al cargar la página se muestra brevemente "Cargando datos...".
- **data**: luego se muestra el listado de libros obtenido del backend.
- **error**: hacer clic en el botón "Forzar error (endpoint inválido)" — el componente
  cambia a una URL inexistente (`/api/no-existe`) y muestra el mensaje de error HTTP 404.
  Clic nuevamente en "Usar endpoint correcto" para volver al estado normal.

Componente: [`frontend/src/LibrosList.jsx`](frontend/src/LibrosList.jsx).

## Preguntas orientadoras

**1. ¿Por qué es necesario configurar CORS para que un frontend en un puerto distinto pueda
consumir una API?**

Por la política del mismo origen (*same-origin policy*) que aplican los navegadores: una
página cargada desde un origen (protocolo + dominio + puerto) no puede leer la respuesta de
una petición `fetch`/XHR hecha a otro origen distinto, salvo que el servidor lo autorice
explícitamente mediante encabezados CORS (`Access-Control-Allow-Origin`, etc.). Como el
frontend (`http://localhost:5173`) y el backend (`http://localhost:3000`) tienen puertos
distintos, son orígenes distintos para el navegador, aunque compartan el mismo host.
Ejemplo colombiano: la app web de **Rappi** consume su API en un subdominio o puerto propio
(por ejemplo `api.rappi.com` desde `www.rappi.com`); sin CORS configurado en su API, el
navegador bloquearía las respuestas y el sitio no podría mostrar restaurantes ni pedidos.

**2. ¿Qué problema de experiencia de usuario evita manejar explícitamente los estados de
carga y error al consumir una API?**

Evita que la interfaz quede en blanco, congelada o muestre datos vacíos/incorrectos sin
explicación mientras la petición está en curso o si esta falla. Sin un estado de `loading`,
el usuario no sabe si la app está funcionando o si debe recargar la página. Sin un estado de
`error`, una falla de red o del servidor se percibe como que "la app no funciona" o queda
silenciosamente sin datos, sin indicarle al usuario qué pasó ni qué puede hacer (reintentar,
verificar conexión, etc.).

## Glosario

| Término (inglés) | Definición | Relación con React | Ejemplo |
|---|---|---|---|
| **effect** | Código que se ejecuta como reacción a un cambio de estado o al renderizado de un componente, para sincronizar el componente con un sistema externo (API, DOM, timers). | Se declara con el hook `useEffect`; en este proyecto se usa para disparar el `fetch` a `/api/libros` cuando el componente se monta o cuando cambia `forceError`. | `useEffect(() => { fetch(url) }, [forceError])` |
| **dependency array** | Arreglo de valores que se pasa como segundo argumento a `useEffect`, y que React compara entre renders para decidir si debe volver a ejecutar el efecto. | Controla cuándo se repite un `effect`; un arreglo vacío `[]` ejecuta el efecto una sola vez al montar el componente. | En `LibrosList.jsx`, `[forceError]` hace que el `fetch` se repita cada vez que cambia ese estado. |
| **cleanup** | Función opcional que retorna un `effect`, ejecutada por React antes de volver a correr el efecto o al desmontar el componente, para cancelar suscripciones, timers o peticiones pendientes. | Evita actualizar el estado de un componente que ya no está en pantalla o que inició una petición obsoleta. | `return () => { cancelado = true }` dentro del `useEffect` de `LibrosList.jsx`. |
| **side effect** | Cualquier interacción de un componente con algo fuera del propio render (llamadas a API, manipulación del DOM, timers, `localStorage`, etc.). | React separa el render (puro) de los side effects (impuros), que deben vivir en `useEffect` y no directamente en el cuerpo del componente. | La petición `fetch` a `/api/libros` es un side effect porque depende de un servidor externo. |
| **render** | Proceso mediante el cual React ejecuta el componente (función) para calcular qué debe mostrarse en la interfaz, a partir de su estado y props actuales. | Cada cambio de estado (`loading`, `error`, `libros`) provoca un nuevo render que actualiza la UI mostrada. | Cuando `setLoading(false)` se ejecuta, React vuelve a renderizar `LibrosList` mostrando los datos o el error en vez de "Cargando datos...". |
