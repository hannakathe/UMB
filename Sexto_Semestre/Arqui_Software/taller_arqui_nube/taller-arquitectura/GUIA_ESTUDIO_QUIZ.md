# 📚 Guía de estudio — Quiz de sustentación (50 V/F)

Tapa la columna de respuesta y practica. 🟢 = Verdadero · 🔴 = Falso.

## Métodos de evaluación (SAAM, ATAM)
1. **SAAM fue creado por el SEI y su foco principal es la modificabilidad.** — 🟢 V. Nació en los 90 para medir cuánto cuesta cambiar el sistema.
2. **SAAM es el método ideal para analizar rendimiento y seguridad al mismo tiempo.** — 🔴 F. Esa es su limitación; para varios atributos se usa ATAM.
3. **En SAAM, si un escenario de cambio afecta a casi todos los componentes, la arquitectura es rígida.** — 🟢 V. Impacto disperso = baja modificabilidad.
4. **ATAM dictamina si una arquitectura es buena o mala en términos absolutos.** — 🔴 F. Dice si es adecuada para los objetivos de negocio según los sacrificios aceptados.
5. **Un punto de trade-off es una decisión que mejora un atributo y empeora otro.** — 🟢 V. Ej.: más logging → más auditabilidad, menos rendimiento.
6. **Un punto de sensibilidad es una decisión que afecta fuertemente a un único atributo.** — 🟢 V. Ej.: usar balanceador mejora la disponibilidad.
7. **Un riesgo en ATAM es una decisión que podría impedir cumplir los requisitos.** — 🟢 V. Ej.: llamada síncrona sin timeout en el SUMP.
8. **El árbol de utilidad prioriza los escenarios con el par (Importancia, Dificultad).** — 🟢 V. Los (A,A) se analizan primero.
9. **Un no-riesgo es una decisión que se validó como adecuada.** — 🟢 V. Ej.: una BD por servicio en EventFlow.
10. **Analizar "picos de tráfico en ventas especiales" corresponde principalmente al atributo de usabilidad.** — 🔴 F. Es escalabilidad.
11. **Evaluar el cambio de Kafka a RabbitMQ y contar los servicios afectados es un análisis de sensibilidad tipo SAAM.** — 🟢 V. Solo cambian los servicios acoplados al bus.
12. **En el cambio Kafka→RabbitMQ, los servicios de Usuarios y Eventos deben reescribirse.** — 🔴 F. "Ni se enteran"; solo Pagos (publica) y Pedidos/Entradas (suscriben).
13. **Para un Project Manager, el hallazgo más valioso del ATAM suele ser un riesgo identificado.** — 🟢 V. Es accionable en alcance, costo y cronograma.

## Heurísticas de Nielsen
14. **Las heurísticas de Nielsen son 10 y fueron publicadas en 1994.** — 🟢 V.
15. **"Agradar visualmente al usuario" es una de las 10 heurísticas originales.** — 🔴 F. Es un juicio estético; la cercana es "diseño estético y minimalista".
16. **"Facilitar la navegación del usuario" es una heurística de Nielsen.** — 🔴 F. Es un objetivo; la cercana es "reconocer antes que recordar".
17. **Clic en "Comprar" sin spinner ni mensaje viola la visibilidad del estado del sistema.** — 🟢 V. El usuario no sabe qué pasa.
18. **Botón verde "Comprar" en una pantalla y azul "Adquirir" en otra viola consistencia y estándares.** — 🟢 V. Misma acción, distinta apariencia y palabra.
19. **Que el campo cantidad acepte "dos" y luego dé error viola prevención de errores.** — 🟢 V. Debió usarse un input numérico con min/max.
20. **"Ejecutar procedimiento de autenticación" viola la relación entre el sistema y el mundo real.** — 🟢 V. Es jerga; debe decir "Iniciar sesión".
21. **Exigir el código exacto ING-SOFT-402-A en vez del nombre viola "reconocer antes que recordar".** — 🟢 V. Obliga a memorizar.
22. **Mostrar "NullPointerException… línea 402" al usuario es un buen mensaje de error porque es preciso.** — 🔴 F. Viola "ayudar a reconocer, diagnosticar y recuperarse de errores".
23. **Borrar todo el formulario porque faltó la fecha de vencimiento respeta la prevención de errores.** — 🔴 F. Debe validar en línea y conservar los datos.
24. **Los atajos de teclado para expertos corresponden a la heurística de flexibilidad y eficiencia de uso.** — 🟢 V. Igual que la carga masiva por CSV.

## Dominio, backlog y estilos arquitectónicos
25. **En EventFlow, el Dominio Core es el Login de usuarios.** — 🔴 F. Es la gestión de inventario de entradas/pedidos; Login es genérico.
26. **Un subdominio genérico conviene comprarlo o delegarlo (p. ej., OAuth/Auth0).** — 🟢 V. No da ventaja competitiva.
27. **En Scrum hay que detallar todas las historias de usuario antes de empezar.** — 🔴 F. Solo las del primer sprint.
28. **Separar la lógica de negocio de la presentación facilita la reutilización del código.** — 🟢 V. La misma OrderService sirve a web, app o API.
29. **En el monolito se puede escalar solo OrderService durante un pico.** — 🔴 F. Hay que escalar la aplicación entera.
30. **El NFR de 10.000 usuarios en 1 minuto afecta críticamente la escalabilidad del monolito.** — 🟢 V. Un servicio caliente degrada todo.
31. **La escalabilidad horizontal agrega más instancias en lugar de un servidor más grande.** — 🟢 V. El vertical tiene techo.
32. **"Los microservicios son tan fáciles de diseñar y desarrollar como un monolito."** — 🔴 F. Red, datos distribuidos, despliegue y observabilidad los hacen más complejos.
33. **Alta cohesión y bajo acoplamiento permiten que el equipo de Pagos despliegue sin avisar al de Usuarios.** — 🟢 V. Se conocen solo por contratos.
34. **En microservicios bien diseñados, cada servicio tiene su propia base de datos.** — 🟢 V. Compartir BD es acoplamiento.
35. **Cuando Pagos publica "PagoAprobado" y Pedidos reacciona, Pagos debe conocer a todos sus suscriptores.** — 🔴 F. Es invocación implícita: el publicador no sabe quién escucha.
36. **El API Gateway actúa como "portero": autentica y enruta las peticiones a los servicios.** — 🟢 V.

## DevOps, deuda técnica e IaC (archivos del taller)
37. **Los "malos olores" detectados por SonarCloud son deuda de código.** — 🟢 V. No rompen hoy, encarecen cada cambio.
38. **El `<style>` anidado y el JavaScript en `onclick` de index.html son malos olores intencionales.** — 🟢 V. Están para que SonarCloud los detecte.
39. **En ci.yml, el job `test` se ejecuta en paralelo con `build`.** — 🔴 F. `needs: build` lo hace esperar.
40. **El pipeline del taller se dispara con cada push a la rama main y también manualmente.** — 🟢 V. `on: push` y `workflow_dispatch`.
41. **Si `grep -q "Rock" index.html` no encuentra la palabra, el pipeline falla.** — 🟢 V. Devuelve código 1 y el paso queda en rojo.
42. **El principio DevOps que muestra GitHub Actions es la automatización para procesos repetibles y fiables.** — 🟢 V.
43. **`terraform plan` crea la VM, la red y el firewall en Google Cloud.** — 🔴 F. Solo muestra el plan; crea recursos `apply`, que no se ejecuta.
44. **`terraform init` descarga el proveedor hashicorp/google versión 5.10.0.** — 🟢 V. Es la versión fijada en main.tf.
45. **El main.tf del taller define 3 recursos: VPC, regla de firewall en el puerto 80 y una VM e2-micro.** — 🟢 V. Plan: 3 to add.

## Caso SUMP y cuestionario
46. **En el SUMP, la llamada REST síncrona Java→Node por la VPN es un punto de sensibilidad y un riesgo.** — 🟢 V. Sin timeout bloquea hilos y colapsa el sistema.
47. **El conflicto del SUMP es entre el Vicerrector (disponibilidad/rendimiento) y el Director Financiero (seguridad/consistencia).** — 🟢 V.
48. **Usar una cola de mensajería con patrón outbox mejora la disponibilidad pero sacrifica la consistencia inmediata.** — 🟢 V. La matrícula queda "pago en verificación" unos segundos.
49. **La arquitectura actual del SUMP es altamente modificable frente al cambio de pasarela en 30 días.** — 🔴 F. Afecta de 4 a 6 componentes (monolito, microservicio, Oracle, BD pagos…).
50. **Para el último asiento F-12, el bloqueo optimista con actualización condicional evita la sobreventa sin retener bloqueos largos.** — 🟢 V. Si actualiza 0 filas, el otro usuario ganó.

---
**Datos que conviene memorizar:** 99,99 % ≈ 52,6 min/año · p95 ≤ 200 ms (disponibilidad de asientos) · 20.000 estudiantes / 2 s (SUMP) · 45 s sin feedback · 5.000 llamadas diarias · 30 días para la nueva pasarela · 3 recursos en main.tf · jobs build → test.
