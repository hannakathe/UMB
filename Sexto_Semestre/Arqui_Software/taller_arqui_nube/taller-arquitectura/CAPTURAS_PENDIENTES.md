# 📸 Capturas pendientes — Taller de Arquitectura (EventFlow + SUMP)

Guarda cada captura **con el nombre exacto** en la carpeta `capturas/` (PNG). Cuando las tengas, ejecuta
`python informe/generar_informe.py` y cada recuadro gris "📸 PENDIENTE" del informe se reemplaza solo por tu imagen.

> Consejo: usa `Win + Shift + S` (Recortes de Windows), recorta la ventana del navegador sin barras personales
> y guarda directamente en `capturas/` con el nombre indicado.

---

## Paso 0 — Repositorio en GitHub ✅ HECHO
Repositorio público: **https://github.com/hannakathe/taller-arquitectura** (creado el 13/10/2026).
GitHub Actions corrió 2 veces y ambas en verde ✅: ejecución #1 (commit inicial) y #2 (copyright 2026 + documentos).
Ya se capturaron automáticamente `fig10`, `fig13`, `fig14` y `fig15` (ítems 4, 7, 8 y 9 abajo).

## ✅ Checklist en el orden del informe

### 1. `fig03_trello_tablero.png` — Trello: tablero completo
- **Herramienta:** https://trello.com (login con tu cuenta).
- **Pasos:**
  1. *Crear → Crear tablero* → nombre **EventFlow — Backlog**.
  2. Crea 4 listas: **Backlog**, **Sprint 1**, **En progreso**, **Hecho**.
  3. En la lista *Backlog* haz clic en **Añadir una tarjeta**, pega el **BLOQUE 1** completo de `backlog_trello.txt`
     (las 10 líneas HU-01 … HU-10) y pulsa Enter: **Trello detecta que pegaste varias líneas y te ofrece
     "Crear 10 tarjetas"** (una por línea). Acepta esa opción (no "Crear una sola tarjeta").
  4. Arrastra HU-01 … HU-05 a *Sprint 1*; luego HU-01 a *En progreso*.
  5. (Opcional) Etiquetas: verde "Dominio Core" en HU-01/02/03/05, gris "Genérico" en HU-09, roja "Seguridad" en HU-04.
- **Qué debe verse:** las 4 listas con las tarjetas repartidas y el nombre del tablero.

### 2. `fig04_trello_tarjeta.png` — Trello: tarjeta abierta con criterios
- **Pasos:** abre la tarjeta **HU-03**, pega en *Descripción* su bloque del **BLOQUE 2** (valor de negocio y estimación)
  y crea una **Checklist** "Criterios de aceptación" con cada criterio (también puedes pegarlos en la descripción).
  Repite con HU-01, 02, 04 y 05 (solo las del Sprint 1 llevan detalle).
- **Qué debe verse:** la tarjeta HU-03 abierta con descripción, checklist y etiqueta.

### 3. `fig09_drawio_evidencia.png` — diagrams.net (o Miro): evidencia de la herramienta
- **Herramienta:** https://app.diagrams.net (no requiere cuenta; elige "Dispositivo" para abrir archivos locales).
- **Pasos:** *Archivo → Abrir desde → Dispositivo* → `diagramas/05_microservicios.drawio` (o el 03).
  Deja visible la paleta de formas de la izquierda y el panel de formato de la derecha.
- **Qué debe verse:** el diagrama abierto dentro del editor de diagrams.net.
- **Opcional (recomendado si el docente insiste en "Miro"):** en Miro crea un tablero, usa *Upload → Image* para
  subir los PNG de `diagramas/` o recréalos; también puedes importar el .drawio con la integración "diagrams.net" de Miro.
- **Opcional:** si quieres que cada figura del informe sea una captura de la herramienta en lugar del PNG
  renderizado, abre cada `.drawio` (01 a 10) y expórtalo con *Archivo → Exportar como → PNG* sobrescribiendo
  `diagramas/NN_nombre.png`.

### ✅ HECHO — 4. `fig10_github_repo.png` — GitHub: página del repositorio
- **Pasos:** tras el Paso 0, abre `https://github.com/TU_USUARIO/taller-arquitectura`.
- **Qué debe verse:** la lista de archivos (`index.html`, `main.tf`, `.github/workflows`, `diagramas/`…),
  el último commit y el contador de commits.

### 5. `fig11_sonarcloud_dashboard.png` — SonarCloud (SonarQube Cloud): dashboard
- **Herramienta:** https://sonarcloud.io
- **Pasos:**
  1. **Log in → With GitHub** y autoriza.
  2. **+ → Analyze new project** → *Import an organization from GitHub* → elige tu cuenta personal → plan **Free**
     → instala la app de SonarCloud solo para el repo `taller-arquitectura`.
  3. Selecciona el repo → **Set up** → en "new code" elige *Previous version* → **Create project**.
  4. Deja el método **Automatic Analysis** (no necesitas tocar el pipeline). Espera 1–3 min a que termine.
  5. Abre la pestaña **Overview** (o *Main Branch*).
- **Qué debe verse:** Quality Gate, *Maintainability* con el número de **Code Smells** y la **Technical Debt**
  (minutos), *Reliability* y *Security*.
- Si el análisis automático no detecta HTML/CSS, en *Administration → Analysis Method* confirma que está en
  "Automatic" y vuelve a hacer un push (cualquier cambio menor).

### 6. `fig12_sonarcloud_code_smells.png` — SonarCloud: lista de Code Smells
- **Pasos:** pestaña **Issues** → filtro **Type: Code Smell** (o *Software quality: Maintainability*).
  Haz clic en uno de `index.html` para que se vea la línea marcada (ideal: el `<style>` anidado o el `onclick`).
- **Qué debe verse:** la lista de issues de `index.html` con su severidad y esfuerzo estimado.

### ✅ HECHO — 7. `fig13_github_actions_lista.png` — GitHub Actions: lista de ejecuciones
- **Pasos:** repo → pestaña **Actions** → workflow **Taller CI Pipeline**.
- **Qué debe verse:** al menos 2 ejecuciones en verde (commit inicial y "actualizar copyright a 2026").

### ✅ HECHO — 8. `fig14_github_actions_run.png` — GitHub Actions: detalle del run
- **Pasos:** clic en la ejecución "chore: actualizar copyright a 2026" (o la más reciente).
- **Qué debe verse:** el grafo **build → test** con ambos jobs en ✓ verde y la duración.

### ✅ HECHO (opcional mejorarla) — 9. `fig15_github_actions_logs.png` — GitHub Actions: logs del job test
- Se capturó la lista de pasos en verde. Si quieres que se vea el texto de los logs, inicia sesión en GitHub, expande los pasos y reemplaza el archivo.
- **Pasos:** en ese run, clic en el job **test** → expande los pasos "2. Simular pruebas (Linting)",
  "3. Simular pruebas de unidad" y "4. ¡Despliegue simulado!".
- **Qué debe verse:** los textos "Prueba de 'Linting' pasada.", "Prueba de 'Copyright' pasada." y
  "¡Pruebas exitosas! Listo para desplegar."

### 10. `fig16_cloudshell_terraform_init.png` — Google Cloud Shell: terraform init
- **Herramienta:** https://shell.cloud.google.com (o el ícono `>_` en https://console.cloud.google.com).
- **Pasos exactos (NO ejecutes `terraform apply`):**
  1. Obtén tu Project ID (si no tienes proyecto, créalo en la consola; no hace falta facturación para `plan`):
     ```bash
     gcloud config get-value project
     ```
  2. Trae el `main.tf` — opción A, clonar el repo (después del Paso 0):
     ```bash
     git clone https://github.com/TU_USUARIO/taller-arquitectura.git
     ```
     ```bash
     cd taller-arquitectura
     ```
     Opción B: menú ⋮ de Cloud Shell → **Upload** → sube `main.tf` y entra a la carpeta donde quedó.
  3. Reemplaza el placeholder por tu Project ID (o edítalo con *Open Editor*):
     ```bash
     sed -i "s/tu-proyecto-de-gcloud/$(gcloud config get-value project)/" main.tf
     ```
  4. Inicializa:
     ```bash
     terraform init
     ```
- **Qué debe verse:** la descarga de `hashicorp/google v5.10.0` y el mensaje verde
  **"Terraform has been successfully initialized!"**.

### 11. `fig17_cloudshell_terraform_plan.png` — Google Cloud Shell: terraform plan
- **Pasos:**
  ```bash
  terraform plan
  ```
  Si pide autorizar Cloud Shell, acepta. Si aparece un error de API deshabilitada, ejecuta
  `gcloud services enable compute.googleapis.com` y repite el plan. **No ejecutes `terraform apply`.**
- **Qué debe verse:** los 3 recursos con `+ create` (`google_compute_network.vpc_network`,
  `google_compute_firewall.default`, `google_compute_instance.web_server`) y al final
  **"Plan: 3 to add, 0 to change, 0 to destroy."** (si no cabe, toma la parte final con el resumen).

### 12. `fig18_swagger_hub.png` — Swagger Hub (opcional)
- **Herramienta:** https://app.swaggerhub.com (login con GitHub).
- **Pasos:** *Create New → Import and Document API* → sube `openapi.yaml` → nombre `eventflow-api`, visibilidad pública.
  (Alternativa sin cuenta: https://editor.swagger.io → *File → Import file*.)
- **Qué debe verse:** el panel con los endpoints `GET /eventos`, `GET /eventos/{eventoId}/disponibilidad`,
  `POST /pedidos` agrupados en *Eventos* y *Pedidos*.

### 13. `fig20_figma_wireframe_malo.png` — Figma: wireframe mal hecho
### 14. `fig22_figma_wireframe_corregido.png` — Figma: wireframe corregido
El taller pide Figma; los wireframes están hechos en HTML (`wireframes/*.html`) y renderizados a PNG. Dos formas de pasarlos:

- **Opción A — plugin html.to.design (rápida):**
  1. Abre https://figma.com → *New design file* → nómbralo **EventFlow — Wireframes**.
  2. *Actions / Resources (Shift+I) → Plugins* → busca **html.to.design** → *Run*.
  3. El plugin importa desde **URL** o desde **archivo HTML**: elige la pestaña de archivo/código y carga
     `wireframes/eventflow_malo.html` (o pega su contenido). Elige viewport *Desktop 1366*. Importa.
     Repite con `eventflow_corregido.html`. (La versión gratuita tiene un número limitado de importaciones al mes;
     con 2 alcanza.) Si el plugin solo acepta URL, publica el repo con GitHub Pages
     (*Settings → Pages → Branch main*) y usa `https://TU_USUARIO.github.io/taller-arquitectura/wireframes/eventflow_malo.html`.
  4. Renombra los frames **EventFlow — Malo** y **EventFlow — Corregido**.
- **Opción B — recrearlo a mano (≈ 20 min, demuestra más dominio):**
  1. Tecla **F** (Frame) → *Desktop 1440×1024* por pantalla (Eventos, Detalle y, en el corregido, "Reservando…").
  2. **R** para rectángulos grises (tarjetas, imágenes con relleno #D9D9D9), **T** para textos.
  3. Malo: botones verde "Comprar" en Eventos y azul redondeado "Adquirir" en Detalle; un campo con el texto "dos"
     y el mensaje "ERR_QTY_NAN"; agrega círculos amarillos con 1, 2, 3 como en el PNG.
  4. Corregido: crea **un componente** de botón "Comprar entrada" (Ctrl+Alt+K) y usa instancias en todas las pantallas
     (eso evidencia la consistencia), un campo "– 2 +", y la pantalla "Reservando tu entrada…" con un círculo de spinner,
     barra de progreso y botón "Cancelar reserva".
  5. (Opcional) *Prototype*: conecta "Comprar entrada" → pantalla "Reservando…" para mostrar el feedback.
- **Qué capturar:** el lienzo de Figma con el frame completo seleccionado y el panel de capas visible
  (malo → `fig20_…`, corregido → `fig22_…`).

---

## Lo que ya quedó hecho automáticamente (no necesitas capturarlo)
- Figuras 1, 2, 5, 6, 7, 8, 23, 24, 25 y 26: diagramas renderizados desde `diagramas/*.drawio`.
- Figuras 19 y 21: wireframes HTML renderizados a PNG (también en versión móvil en `wireframes/`).
- `capturas/terraform_local.txt` **no** se generó: Terraform no está instalado en este equipo; el `init`/`plan`
  se hace en Cloud Shell (pasos 10 y 11).

## 🔁 Reconstruir el informe cuando tengas las capturas
Desde la carpeta `taller-arquitectura/`:

```bash
python informe/generar_informe.py
```

Genera `informe/Informe_Taller_Arquitectura.docx` y `.pdf` (con Word), actualiza la tabla de contenido y lista
en consola las capturas que aún falten. Antes de entregar, llena en amarillo **[ASIGNATURA]**, **[DOCENTE]** y
(la URL del repo ya quedó puesta) (en `generar_informe.py` para que no se pierdan al regenerar).
