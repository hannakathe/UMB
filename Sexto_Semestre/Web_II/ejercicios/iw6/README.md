# Sesión 6 — Seguridad Web (Ingeniería Web II, UMB)

SQL Injection, XSS almacenado y CSRF.

## Archivos

| Archivo | Contenido |
|---|---|
| `demo_sesion6.py` | Demo de clase: consulta vulnerable vs. parametrizada + escape HTML. |
| `ejercicio1_xss.py` | **Ejercicio 1** — sanitización contra XSS almacenado. |
| `ejercicio2_csrf.py` | **Ejercicio 2** — token CSRF único por sesión. |
| `app.py` + `templates/` | **Trabajo proyecto** — servidor Flask que integra Ej. 1 y Ej. 2. |

## Requisitos

```bash
pip install -r requirements.txt
```

## Ejecutar

```bash
python demo_sesion6.py
python ejercicio1_xss.py
python ejercicio2_csrf.py
python app.py
```

Con el servidor arriba, abrir en el navegador:

- http://127.0.0.1:5000/ → `Hola, Ingeniería Web II`
- http://127.0.0.1:5000/comentarios → formulario con token CSRF y filtro anti-XSS
- http://127.0.0.1:5000/salud → JSON de estado

## Pruebas en el navegador

1. Publicar un comentario normal → se guarda intacto y se muestra escapado.
2. Publicar `<script>alert(1)</script>` → **rechazado** (Ejercicio 1).
3. Enviar el formulario sin el campo `csrf_token` (con DevTools) → **rechazado** (Ejercicio 2).
