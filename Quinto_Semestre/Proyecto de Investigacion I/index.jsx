import { useState } from "react";

// ── DESIGN TOKENS ────────────────────────────────────────────────────────────
const C = {
  bg: "#080D1A", card: "#0F1623", border: "#1C2333",
  accent: "#10B981", accentSoft: "#052E1C",
  blue: "#3B82F6", blueSoft: "#0F2444",
  purple: "#8B5CF6", purpleSoft: "#1E0A3C",
  orange: "#F59E0B", orangeSoft: "#2D1500",
  red: "#EF4444", redSoft: "#2D0000",
  yellow: "#FBBF24", yellowSoft: "#2D1A00",
  pink: "#EC4899", pinkSoft: "#2D0020",
  teal: "#06B6D4", tealSoft: "#042830",
  text: "#F1F5F9", muted: "#94A3B8", subtle: "#1E293B",
};

const colorOf = (k) => ({ accent: C.accent, blue: C.blue, purple: C.purple, orange: C.orange, red: C.red, yellow: C.yellow, pink: C.pink, teal: C.teal }[k] ?? C.accent);
const softOf  = (k) => ({ accent: C.accentSoft, blue: C.blueSoft, purple: C.purpleSoft, orange: C.orangeSoft, red: C.redSoft, yellow: C.yellowSoft, pink: C.pinkSoft, teal: C.tealSoft }[k] ?? C.accentSoft);

// ── PRIMITIVES ────────────────────────────────────────────────────────────────
const Badge = ({ children, color = "accent", xs }) => (
  <span style={{
    background: softOf(color), color: colorOf(color),
    padding: xs ? "1px 7px" : "2px 10px", borderRadius: 99,
    fontSize: xs ? 9 : 11, fontWeight: 700, letterSpacing: 0.8,
    textTransform: "uppercase", display: "inline-block",
  }}>{children}</span>
);

const Card = ({ children, style = {}, accent }) => (
  <div style={{
    background: C.card, border: `1px solid ${C.border}`,
    borderRadius: 14, padding: "18px 20px",
    ...(accent ? { borderLeft: `3px solid ${colorOf(accent)}` } : {}),
    ...style,
  }}>{children}</div>
);

const SecHead = ({ icon, title, sub }) => (
  <div style={{ marginBottom: 18 }}>
    <div style={{ display: "flex", alignItems: "center", gap: 9, marginBottom: 3 }}>
      <span style={{ fontSize: 20 }}>{icon}</span>
      <h2 style={{ margin: 0, fontSize: 17, fontWeight: 800, color: C.text, fontFamily: "Georgia,serif" }}>{title}</h2>
    </div>
    {sub && <p style={{ margin: "0 0 0 29px", fontSize: 12, color: C.muted }}>{sub}</p>}
  </div>
);

const Row = ({ label, value, vc = C.text, vc2 }) => (
  <div style={{ display: "flex", justifyContent: "space-between", fontSize: 12, marginBottom: 5 }}>
    <span style={{ color: C.muted }}>{label}</span>
    <span style={{ color: vc2 ?? vc, fontWeight: 600 }}>{value}</span>
  </div>
);

const Arrow = () => <span style={{ color: C.muted, fontSize: 13 }}>→</span>;

const Chip = ({ children, color = "accent" }) => (
  <span style={{
    background: softOf(color), color: colorOf(color), fontSize: 10,
    borderRadius: 6, padding: "2px 8px", display: "inline-block", margin: "2px",
  }}>{children}</span>
);

// ── PHONE MOCK ────────────────────────────────────────────────────────────────
const Phone = ({ children, label }) => (
  <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
    <div style={{
      width: 195, background: "#050810", borderRadius: 28, padding: "10px 5px",
      border: "2px solid #1a1f2e", boxShadow: "0 20px 50px rgba(0,0,0,.7)",
    }}>
      <div style={{ width: 44, height: 6, background: "#12172a", borderRadius: 99, margin: "0 auto 7px" }} />
      <div style={{ background: C.bg, borderRadius: 18, minHeight: 350, overflow: "hidden", display: "flex", flexDirection: "column" }}>
        {children}
      </div>
      <div style={{ width: 32, height: 3, background: "#1a1f2e", borderRadius: 99, margin: "7px auto 0" }} />
    </div>
    {label && <p style={{ margin: "7px 0 0", fontSize: 10, color: C.muted, textAlign: "center", maxWidth: 150 }}>{label}</p>}
  </div>
);
const PH = ({ title, dot = C.accent }) => (
  <div style={{ background: C.card, padding: "7px 11px", display: "flex", alignItems: "center", gap: 7, borderBottom: `1px solid ${C.border}` }}>
    <div style={{ width: 5, height: 5, borderRadius: "50%", background: dot }} />
    <span style={{ fontSize: 10, fontWeight: 700, color: C.text }}>{title}</span>
  </div>
);

// ── DATA ──────────────────────────────────────────────────────────────────────
const MODULES = [
  { icon: "🎙️", name: "Registro por Voz", color: "accent", tag: "core",
    desc: "El usuario habla naturalmente. Whisper transcribe. Claude clasifica. Todo en < 1 segundo.",
    items: ["\"Gasté 50 mil en restaurante\" → Alimentación / $50.000","\"Me cobró el Rappi 38\" → Delivery / $38.000","\"Pagué Netflix\" → Suscripciones / monto pendiente","Confirmación visual antes de guardar","Corrección por voz: \"No, fue 45, no 25\""],
    tech: ["Whisper API","Claude API","Web Speech API"] },
  { icon: "🤖", name: "Clasificación IA + Aprendizaje", color: "purple", tag: "ia",
    desc: "La IA categoriza cada gasto y aprende de tus correcciones con el tiempo.",
    items: ["8 categorías + subcategorías automáticas","Aprende: 'Rappi' siempre → Delivery para este usuario","Nivel de confianza 0–100 por cada clasificación","Si confianza < 70% → activa modo de clarificación","Después de 30 días uso → precisión personalizada 97%+"],
    tech: ["Claude API","Embeddings","Fine-tuning por usuario"] },
  { icon: "💰", name: "Gestión de Presupuesto", color: "blue", tag: "presupuesto",
    desc: "El usuario define su ingreso y la app distribuye automáticamente con reglas financieras.",
    items: ["Ingreso del salario mensual o quincenal","Distribución automática: Regla 50/30/20 o 50/20/30","Límites por categoría personalizables","Barra de progreso en tiempo real por categoría","Alerta al 80% del límite de cualquier categoría"],
    tech: ["PostgreSQL","Node.js","Firebase FCM"] },
  { icon: "📊", name: "Análisis Proactivo IA", color: "orange", tag: "análisis",
    desc: "La IA no espera que preguntes — te habla primero con reportes narrativos automáticos.",
    items: ["Reporte semanal lunes 8AM con narrativa en lenguaje natural","Reporte quincenal: comparativa vs periodo anterior","Detección de anomalías: gastos 3x mayores a lo habitual","Top 3 gastos necesarios vs innecesarios del mes","Proyección: 'Con este ritmo te quedas sin presupuesto en 8 días'"],
    tech: ["Claude API","CRON jobs","Recharts"] },
  { icon: "💡", name: "Sugerencias Económicas IA", color: "teal", tag: "tips ia",
    desc: "Tips personalizados y contextuales basados en tus hábitos reales.",
    items: ["Regla 50/30/20 adaptada a tu COP real","'Gastas $80K/mes en delivery. Cocinando 3x/semana ahorras $40K'","'Tienes 4 suscripciones ($180K/mes). ¿Cuáles usas menos?'","Metas de ahorro con proyección en semanas","Tips al momento de registrar un gasto inusualmente alto"],
    tech: ["Claude API","Historial 90 días","Reglas configurables"] },
  { icon: "🔔", name: "Alertas Proactivas", color: "red", tag: "alertas",
    desc: "La app te contacta primero — no espera a que tú la abras.",
    items: ["Lunes 8AM: resumen semanal automático","Días 15 y 30: reporte quincenal comparativo","Alerta si gastos pendientes llevan +24h sin verificar","Alerta de gasto > $200K en una sola transacción","'Llevas 3 días sin registrar gastos. ¿Todo bien?'"],
    tech: ["Firebase FCM","CRON jobs","Push notifications"] },
];

const RULES = [
  { name: "Regla 50/30/20", color: "accent", sub: "La más popular para ingresos medios",
    items: [{ pct: "50%", label: "Necesidades", desc: "Arriendo, comida, transporte, servicios", c: C.accent },
            { pct: "30%", label: "Deseos", desc: "Salidas, ropa, entretenimiento", c: C.blue },
            { pct: "20%", label: "Ahorro", desc: "Fondo de emergencia, inversión", c: C.purple }] },
  { name: "Regla 50/20/30", color: "blue", sub: "Ahorro agresivo prioritario",
    items: [{ pct: "50%", label: "Necesidades", desc: "Gastos fijos imprescindibles", c: C.accent },
            { pct: "20%", label: "Deseos", desc: "Gastos variables y placer", c: C.blue },
            { pct: "30%", label: "Ahorro", desc: "Inversión prioritaria", c: C.purple }] },
  { name: "Método Sobres", color: "orange", sub: "Control total por categoría",
    items: [{ pct: "Sobre 1", label: "Comida", desc: "Monto fijo semanal", c: C.orange },
            { pct: "Sobre 2", label: "Transporte", desc: "Monto fijo semanal", c: C.blue },
            { pct: "Sobre 3", label: "Ocio", desc: "Lo que sobre después", c: C.purple }] },
];

const SCREENS = [
  { icon: "🏠", name: "Dashboard", color: "accent", desc: "Resumen del mes, presupuesto restante, últimos gastos, alerta activa. Dos micrófonos: 🎙️ Normal y ⚡ Rápido. Widget de pendientes si los hay.", badge: "Pantalla principal" },
  { icon: "🎙️", name: "Registro por Voz", color: "purple", desc: "Botón central grande. Transcripción en vivo. Card de confirmación con 3 botones: [✓ Sí] [✏️ Editar] [⏳ Ahora no].", badge: "Core feature" },
  { icon: "📋", name: "Mis Gastos", color: "blue", desc: "Lista cronológica con filtros por categoría, mes y monto. Gráfica de torta. Etiquetas de estado.", badge: "Historial" },
  { icon: "🤖", name: "Análisis IA", color: "orange", desc: "Chat conversacional con la IA. Pregunta: '¿En qué gasté más?' y responde con tus datos reales.", badge: "IA Chat" },
  { icon: "💰", name: "Presupuesto", color: "teal", desc: "Barras de progreso por categoría. Editar límites. Resumen 50/30/20 del mes actual.", badge: "Control" },
  { icon: "📊", name: "Reportes", color: "blue", desc: "Semanas y quincenas anteriores. Comparativas visuales. Exportar PDF.", badge: "Historial" },
  { icon: "💡", name: "Tips IA", color: "accent", desc: "Feed de sugerencias personalizadas según hábitos del último mes. Se actualiza semanalmente.", badge: "Coaching" },
  { icon: "⏳", name: "Bandeja Pendientes", color: "yellow", desc: "Gastos ⏳ y ⚡ sin confirmar. Acciones: [✓] [✏️] [🗑️]. Los pendientes NO suman al presupuesto hasta confirmarse.", badge: "Nuevo" },
];

const FLOW_STEPS = [
  { id: 1, icon: "🎙️", label: "Habla", color: "accent",
    title: "El usuario habla el gasto",
    desc: "Presiona el micrófono (🎙️ Normal o ⚡ Rápido) y dice el gasto en lenguaje natural, sin formato especial.",
    details: ["Acepta cualquier frase natural en español","Detecta montos en palabras: 'cincuenta mil', '50k', '$50'","Detecta fechas implícitas: 'ayer', 'el viernes'","El modo ⚡ Rápido no muestra confirmación — va directo a pendiente"] },
  { id: 2, icon: "⚙️", label: "IA procesa", color: "blue",
    title: "Whisper + Claude procesan en < 1 segundo",
    desc: "Whisper transcribe el audio a texto. Claude extrae todos los campos del gasto y calcula el nivel de confianza.",
    details: ["Monto → convierte '50k' a 50000 COP","Categoría → una de 8 categorías principales","Subcategoría → descriptor más específico","Fecha → ISO, por defecto timestamp actual","Confianza → 0-100, determina el siguiente paso"] },
  { id: 3, icon: "🤖", label: "IA confirma", color: "purple",
    title: "Card de confirmación con 3 botones",
    desc: "La IA presenta lo que entendió en un card visual claro. El usuario tiene 3 opciones de respuesta.",
    details: ["[✓ Sí, correcto] → guarda confirmado inmediatamente","[✏️ Editar] → abre formulario de edición","[⏳ Ahora no] → guarda como pendiente sin bloquear al usuario","El tono del mensaje cambia según la confianza (≥90% / 70-89% / 50-69% / <50%)"] },
  { id: 4, icon: "❓", label: "Bifurcación", color: "yellow",
    title: "¿El usuario tiene tiempo ahora?",
    desc: "Aquí el flujo se divide en dos caminos según la disponibilidad del usuario en ese momento.",
    details: ["SÍ tiene tiempo → presiona ✓ o ✏️ → flujo de confirmación normal","NO tiene tiempo → presiona ⏳ → gasto guardado como pendiente","⚡ Modo Rápido → nunca pasa por este paso → siempre pendiente","El gasto pendiente conserva: transcripción original, confianza IA, timestamp exacto"] },
  { id: 5, icon: "⏳", label: "Guardar pendiente", color: "yellow",
    title: "Gasto en estado pendiente",
    desc: "El gasto se guarda con los datos que detectó la IA pero en estado 'pendiente de verificación'. No suma al presupuesto.",
    details: ["Estado: ⏳ PENDIENTE o ⚡ MODO RÁPIDO","NO se suma al presupuesto ni a las estadísticas aún","Notificación inmediata: 'Guardado como pendiente 👀'","Recordatorio 2h después, a las 9PM y al día siguiente si no se verifica"] },
  { id: 6, icon: "✏️", label: "Verificar", color: "orange",
    title: "El usuario verifica desde la Bandeja",
    desc: "Cuando tiene tiempo, abre la Bandeja de Pendientes y revisa cada gasto uno por uno.",
    details: ["Ve el monto detectado, categoría sugerida y confianza IA","Puede confirmar con un toque o abrir el formulario de edición","La edición guarda la corrección para el aprendizaje de la IA","También puede eliminar el gasto si fue un error"] },
  { id: 7, icon: "✅", label: "Guardado final", color: "accent",
    title: "Gasto confirmado — suma al presupuesto",
    desc: "El gasto pasa de pendiente a confirmado. Ahora sí aparece en el historial y afecta el presupuesto.",
    details: ["El dashboard se actualiza en tiempo real","Si supera el 70% del presupuesto de esa categoría → alerta","La IA puede agregar un insight contextual post-guardado","El gasto desaparece de la Bandeja de Pendientes"] },
];

const EDGE_CASES = [
  { input: '"Gasté 50k en restaurante"', conf: 94, cat: "🍔 Alimentación", sub: "Comida fuera de casa", monto: "$50.000", color: "accent",
    response: "Muestra card directo. 'Entendí esto. ¿Es correcto?'" },
  { input: '"Gasté 50k en la tienda"', conf: 52, cat: "❓ Ambiguo", sub: "¿Mercado o ropa?", monto: "$50.000", color: "orange",
    response: "Chips de clarificación: [🛒 Mercado] [👗 Ropa] [💊 Droguería] [🏠 Hogar]" },
  { input: '"Gasté como unos 40 o 50 mil"', conf: 40, cat: "❓ Sin categoría", sub: "Monto ambiguo", monto: "¿$40K o $50K?", color: "orange",
    response: "Pregunta el monto exacto con opciones: [$40K] [$45K] [$50K] [Otro]" },
  { input: '"Fui al médico"', conf: 30, cat: "💊 Salud", sub: "Consulta médica", monto: "¿Cuánto?", color: "red",
    response: "Categoría OK, pide monto: campo numérico con teclado" },
  { input: '"Audio con ruido / poco claro"', conf: 18, cat: "❓", sub: "No procesable", monto: "—", color: "red",
    response: "No clasifica. '¿Puedes repetirlo o escribirlo?' [🎙️ Reintentar] [✏️ Escribir]" },
  { input: '"Pagué el arriendo"', conf: 72, cat: "🏠 Hogar", sub: "Arriendo/vivienda", monto: "¿Cuánto?", color: "blue",
    response: "Detecta gasto recurrente. '¿Quieres marcarlo como recurrente mensual?'" },
];

const STACK = [
  { layer: "📱 App Móvil", tech: "React Native + Expo", why: "iOS + Android en JS/TS. Expo elimina Android Studio/Xcode. Mismo lenguaje en toda la app, web y backend.", color: "blue" },
  { layer: "🌐 Web App", tech: "React + Vite", why: "Mismo ecosistema que la app. Comparte hooks, servicios y lógica de negocio con React Native sin reescribir.", color: "teal" },
  { layer: "♻️ Código compartido", tech: "Hooks + Servicios JS/TS", why: "La lógica de clasificación, formateo COP y llamadas a la API se reutiliza entre app y web.", color: "accent" },
  { layer: "⚙️ Backend / API", tech: "Node.js + Express", why: "API REST. Maneja lógica de reportes, CRON jobs de notificaciones y aprendizaje IA.", color: "purple" },
  { layer: "🗄️ Base de datos", tech: "PostgreSQL", why: "Ya tienes experiencia en SQL. Relacional y robusto para gastos, categorías, usuarios.", color: "accent" },
  { layer: "🤖 Clasificación IA", tech: "Claude API", why: "Clasificación de gastos + reportes narrativos. SDK oficial en JS/TS — integración directa.", color: "purple" },
  { layer: "🎙️ Transcripción voz", tech: "Whisper (OpenAI)", why: "$0.006/min o plan gratis. SDK JS oficial. Mejor integración que en Dart.", color: "orange" },
  { layer: "🔐 Autenticación", tech: "Firebase Auth", why: "Login con Google/email. SDK JS oficial. Gratuito hasta alto volumen.", color: "yellow" },
  { layer: "🔔 Notificaciones", tech: "Expo Notifications + FCM", why: "Expo Notifications simplifica el setup de push notifications en React Native. FCM como base.", color: "red" },
  { layer: "☁️ Hosting Backend", tech: "Railway", why: "Gratis para proyectos universitarios. Backend + BD en un mismo lugar.", color: "accent" },
  { layer: "☁️ Hosting Web", tech: "Vercel", why: "Deploy automático desde GitHub. HTTPS gratis. Optimizado para React/Next.js.", color: "blue" },
];

const DIFFERENTIATORS = [
  { icon: "🎙️", title: "Registro 100% por voz", color: "accent", desc: "La mayoría de apps de finanzas requieren tipear. VozFinanzas elimina esa fricción — es la razón principal por la que la gente abandona otras apps." },
  { icon: "🤖", title: "IA proactiva, no reactiva", color: "purple", desc: "No espera a que el usuario pregunte. Cada semana la app inicia: 'Hey, esta semana gastaste $180K. Aquí está el análisis.' Eso es radicalmente diferente." },
  { icon: "⏳", title: "Registro flexible — sin bloquear", color: "yellow", desc: "3 modos de registro: Normal, Ahora No y Rápido. Nunca pierdes un gasto aunque estés en la caja pagando. Los pendientes se verifican después." },
  { icon: "🇨🇴", title: "Hecha para Colombia", color: "blue", desc: "Maneja COP, quincenas (no solo meses), contexto local: Rappi, D1, Éxito, SITP. No es una app gringa traducida." },
  { icon: "💬", title: "IA conversacional sobre tus datos", color: "teal", desc: "Puedes preguntarle directamente a la app: '¿En qué gasté más este mes?' y responde con tus datos reales. Como un asesor financiero personal." },
  { icon: "🧠", title: "Aprende de tus correcciones", color: "orange", desc: "Cada vez que corriges una categoría, la IA aprende. Después de 30 días tu app es radicalmente más precisa que la del vecino." },
];

// ── MAIN TABS CONFIG ──────────────────────────────────────────────────────────
const TABS = [
  { id: 0, label: "🏠 Resumen",     short: "Resumen" },
  { id: 1, label: "🧩 Módulos",     short: "Módulos" },
  { id: 2, label: "💰 Reglas IA",   short: "Reglas" },
  { id: 3, label: "📱 Pantallas",   short: "Pantallas" },
  { id: 4, label: "🔄 Flujo",       short: "Flujo" },
  { id: 5, label: "⚡ Modos",       short: "Modos" },
  { id: 6, label: "⏳ Pendientes",  short: "Pendientes" },
  { id: 7, label: "⚠️ Edge Cases",  short: "Edge Cases" },
  { id: 8, label: "⚙️ Tech Stack",  short: "Stack" },
  { id: 9, label: "🚀 Diferenciadores", short: "Diferenc." },
];

// ── COMPONENT: FLOW PHONE ─────────────────────────────────────────────────────
const FlowPhone = ({ stepId }) => {
  if (stepId === 1) return (
    <Phone label="El usuario habla">
      <PH title="VozFinanzas" dot={C.accent} />
      <div style={{ flex: 1, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", padding: 14, gap: 12 }}>
        <div style={{ width: 64, height: 64, borderRadius: "50%", background: C.accentSoft, border: `3px solid ${C.accent}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 26, boxShadow: `0 0 0 10px ${C.accent}15, 0 0 0 20px ${C.accent}08` }}>🎙️</div>
        <div style={{ background: C.card, border: `1px solid ${C.border}`, borderRadius: 10, padding: "9px 12px", width: "100%" }}>
          <p style={{ margin: "0 0 3px", fontSize: 9, color: C.accent, fontWeight: 700 }}>ESCUCHANDO...</p>
          <p style={{ margin: 0, fontSize: 12, color: C.text, fontStyle: "italic" }}>"Gasté 50 mil en restaurante"</p>
        </div>
        <div style={{ display: "flex", gap: 3 }}>{[5,9,13,8,11,7,14,9,6,12].map((h,i) => <div key={i} style={{ width: 3, height: h, background: C.accent, borderRadius: 2, opacity: 0.7 }} />)}</div>
      </div>
    </Phone>
  );
  if (stepId === 2) return (
    <Phone label="IA extrae datos">
      <PH title="VozFinanzas" dot={C.blue} />
      <div style={{ flex: 1, padding: 12, display: "flex", flexDirection: "column", gap: 7, justifyContent: "center" }}>
        <p style={{ margin: 0, fontSize: 9, color: C.muted, fontWeight: 700 }}>TRANSCRIPCIÓN</p>
        <div style={{ background: C.subtle, borderRadius: 7, padding: 8 }}><p style={{ margin: 0, fontSize: 11, color: C.text }}>"Gasté 50 mil en restaurante"</p></div>
        <p style={{ margin: 0, fontSize: 9, color: C.muted, fontWeight: 700 }}>CLAUDE EXTRAE...</p>
        {[["Monto","$50.000",C.accent],["Lugar","Restaurante",C.blue],["Categoría","🍔 Alimentación",C.orange],["Subcategoría","Comida fuera",C.purple],["Confianza","94%",C.accent]].map(([k,v,c]) => (
          <div key={k} style={{ display: "flex", justifyContent: "space-between", fontSize: 10 }}>
            <span style={{ color: C.muted }}>{k}</span><span style={{ color: c, fontWeight: 700 }}>{v}</span>
          </div>
        ))}
      </div>
    </Phone>
  );
  if (stepId === 3) return (
    <Phone label="3 botones de acción">
      <PH title="VozFinanzas" dot={C.purple} />
      <div style={{ flex: 1, padding: 11, display: "flex", flexDirection: "column", gap: 8 }}>
        <div style={{ background: `${C.blueSoft}`, border: `1px solid ${C.blue}33`, borderRadius: "4px 10px 10px 10px", padding: 8 }}>
          <p style={{ margin: "0 0 2px", fontSize: 9, color: C.blue, fontWeight: 700 }}>🤖 IA</p>
          <p style={{ margin: 0, fontSize: 11, color: C.text }}>Entendí esto. ¿Es correcto?</p>
        </div>
        <div style={{ background: C.card, border: `2px solid ${C.orange}`, borderRadius: 10, padding: 10 }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 5 }}>
            <div><p style={{ margin: 0, fontSize: 8, color: C.muted }}>GASTO</p><p style={{ margin: 0, fontSize: 17, fontWeight: 900 }}>$50.000</p></div>
            <span style={{ fontSize: 20 }}>🍔</span>
          </div>
          <Row label="Categoría" value="Alimentación" vc={C.orange} />
          <Row label="Subcategoría" value="Comida fuera" />
        </div>
        <button style={{ background: C.accentSoft, color: C.accent, border: `1px solid ${C.accent}`, borderRadius: 7, padding: "7px 0", fontSize: 11, fontWeight: 700, cursor: "pointer" }}>✓ Sí, correcto</button>
        <div style={{ display: "flex", gap: 5 }}>
          <button style={{ flex: 1, background: C.blueSoft, color: C.blue, border: `1px solid ${C.blue}44`, borderRadius: 7, padding: "7px 0", fontSize: 11, fontWeight: 700, cursor: "pointer" }}>✏️ Editar</button>
          <button style={{ flex: 1, background: C.yellowSoft, color: C.yellow, border: `1px solid ${C.yellow}`, borderRadius: 7, padding: "7px 0", fontSize: 11, fontWeight: 700, cursor: "pointer" }}>⏳ Ahora no</button>
        </div>
      </div>
    </Phone>
  );
  if (stepId === 5) return (
    <Phone label="Guardado como pendiente">
      <PH title="VozFinanzas" dot={C.yellow} />
      <div style={{ flex: 1, padding: 12, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", gap: 10 }}>
        <div style={{ width: 48, height: 48, borderRadius: "50%", background: C.yellowSoft, border: `2px solid ${C.yellow}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20 }}>⏳</div>
        <p style={{ margin: 0, fontWeight: 800, fontSize: 12, color: C.yellow }}>Guardado como pendiente</p>
        <div style={{ background: C.yellowSoft, border: `1px solid ${C.yellow}22`, borderRadius: 9, padding: 9, width: "100%" }}>
          <p style={{ margin: "0 0 3px", fontSize: 9, color: C.yellow, fontWeight: 700 }}>💡 VOZFINANZAS</p>
          <p style={{ margin: 0, fontSize: 10, color: C.text }}>Te recordaré verificarlo esta noche. ¡Que te vaya bien! 👋</p>
        </div>
        <p style={{ margin: 0, fontSize: 9, color: C.muted, textAlign: "center" }}>No suma al presupuesto hasta que confirmes</p>
      </div>
    </Phone>
  );
  if (stepId === 6) return (
    <Phone label="Bandeja de pendientes">
      <PH title="Pendientes" dot={C.yellow} />
      <div style={{ flex: 1, padding: 9 }}>
        <div style={{ background: C.yellowSoft, borderRadius: 7, padding: 7, marginBottom: 8, textAlign: "center" }}>
          <p style={{ margin: 0, fontSize: 9, color: C.yellow, fontWeight: 700 }}>4 gastos · $215.000 sin confirmar</p>
        </div>
        {[["$50.000","Alimentación","⚡ rápido",C.pink],["$18.000","Transporte","",C.yellow],["$120.000","Hogar · ⚠️ baja conf.","",C.orange],["$27.000","Alimentación · D1","",C.yellow]].map(([a,d,tag,c],i) => (
          <div key={i} style={{ background: C.bg, border: `1px solid ${c}33`, borderLeft: `2px solid ${c}`, borderRadius: 8, padding: "6px 8px", marginBottom: 5, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div><p style={{ margin: 0, fontSize: 11, fontWeight: 700, color: C.text }}>{a}</p><p style={{ margin: 0, fontSize: 9, color: C.muted }}>{d} {tag && <span style={{ color: c }}>{tag}</span>}</p></div>
            <div style={{ display: "flex", gap: 3 }}>
              <div style={{ background: C.accentSoft, color: C.accent, fontSize: 9, padding: "2px 5px", borderRadius: 4 }}>✓</div>
              <div style={{ background: C.subtle, color: C.muted, fontSize: 9, padding: "2px 5px", borderRadius: 4 }}>✏️</div>
            </div>
          </div>
        ))}
      </div>
    </Phone>
  );
  if (stepId === 7) return (
    <Phone label="Gasto confirmado">
      <PH title="VozFinanzas" dot={C.accent} />
      <div style={{ flex: 1, padding: 12, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", gap: 9 }}>
        <div style={{ width: 48, height: 48, borderRadius: "50%", background: C.accentSoft, border: `2px solid ${C.accent}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20 }}>✓</div>
        <p style={{ margin: 0, fontWeight: 800, fontSize: 12, color: C.accent }}>¡Gasto confirmado!</p>
        <div style={{ background: `${C.blueSoft}`, border: `1px solid ${C.blue}44`, borderRadius: 9, padding: 9, width: "100%" }}>
          <p style={{ margin: "0 0 3px", fontSize: 9, color: C.blue, fontWeight: 700 }}>💡 IA INSIGHT</p>
          <p style={{ margin: 0, fontSize: 10, color: C.text }}>Llevas <span style={{ color: C.orange, fontWeight: 700 }}>$187K</span> en alimentación. Ya usaste el <span style={{ color: C.red, fontWeight: 700 }}>74%</span> del presupuesto y van 18 días. 🔔</p>
        </div>
        <div style={{ width: "100%" }}>
          <div style={{ display: "flex", justifyContent: "space-between", fontSize: 9, color: C.muted, marginBottom: 3 }}><span>Alimentación</span><span>$187K / $250K</span></div>
          <div style={{ background: C.subtle, borderRadius: 99, height: 5 }}><div style={{ width: "74%", background: `linear-gradient(90deg,${C.orange},${C.red})`, borderRadius: 99, height: 5 }} /></div>
        </div>
      </div>
    </Phone>
  );
  return (
    <Phone label={`Paso ${stepId}`}>
      <PH title="VozFinanzas" />
      <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center" }}>
        <p style={{ color: C.muted, fontSize: 12 }}>Paso {stepId}</p>
      </div>
    </Phone>
  );
};

// ── APP ───────────────────────────────────────────────────────────────────────
export default function App() {
  const [tab, setTab] = useState(0);
  const [flowStep, setFlowStep] = useState(1);

  return (
    <div style={{ background: C.bg, minHeight: "100vh", fontFamily: "'Segoe UI',sans-serif", color: C.text, paddingBottom: 80 }}>

      {/* ── HEADER ── */}
      <div style={{ background: "linear-gradient(160deg,#080D1A 0%,#0D1830 40%,#100820 100%)", borderBottom: `1px solid ${C.border}`, padding: "28px 20px 22px", textAlign: "center" }}>
        <div style={{ display: "flex", justifyContent: "center", gap: 6, marginBottom: 10, flexWrap: "wrap" }}>
          <Badge color="accent">App Móvil + Web</Badge>
          <Badge color="purple">IA + Voz</Badge>
          <Badge color="blue">Colombia 🇨🇴</Badge>
          <Badge color="yellow">3 Semestres</Badge>
          <Badge color="orange">React Native + Expo</Badge>
        </div>
        <h1 style={{
          margin: "0 0 6px", fontSize: 34, fontWeight: 900, letterSpacing: -1, fontFamily: "Georgia,serif",
          background: "linear-gradient(100deg,#10B981 0%,#3B82F6 40%,#8B5CF6 70%,#EC4899 100%)",
          WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent",
        }}>💸 VozFinanzas</h1>
        <p style={{ margin: "0 auto", fontSize: 13, color: C.muted, maxWidth: 520 }}>
          Plataforma de gestión financiera personal con registro por voz, clasificación con IA, análisis proactivo y registro flexible sin bloquear al usuario.
        </p>
        <div style={{ display: "flex", justifyContent: "center", flexWrap: "wrap", gap: 16, marginTop: 14 }}>
          {[["🎙️","Voz natural"],["🤖","IA clasifica"],["⏳","Sin bloquear"],["📊","Análisis semanal"],["💡","Tips COP"]].map(([ic,lb]) => (
            <div key={lb} style={{ display: "flex", alignItems: "center", gap: 5, fontSize: 11, color: C.muted }}>
              <span>{ic}</span><span>{lb}</span>
            </div>
          ))}
        </div>
      </div>

      {/* ── TABS ── */}
      <div style={{ display: "flex", gap: 2, padding: "10px 10px 0", overflowX: "auto", background: C.card, borderBottom: `1px solid ${C.border}` }}>
        {TABS.map(t => (
          <button key={t.id} onClick={() => setTab(t.id)} style={{
            background: tab === t.id ? C.accent : "transparent",
            color: tab === t.id ? "#000" : C.muted,
            border: "none", borderRadius: "7px 7px 0 0",
            padding: "7px 13px", fontSize: 11, fontWeight: tab === t.id ? 700 : 400,
            cursor: "pointer", whiteSpace: "nowrap",
          }}>{t.label}</button>
        ))}
      </div>

      <div style={{ padding: "20px 14px", maxWidth: 980, marginInline: "auto" }}>

        {/* ══════════════════════════════════════════════════════════════════
            TAB 0 — RESUMEN
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 0 && (
          <div>
            <SecHead icon="🏠" title="Resumen del Proyecto" sub="Todo el proyecto en una sola vista" />
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 14, marginBottom: 14 }}>
              <Card style={{ gridColumn: "1/-1", background: "linear-gradient(135deg,#052E1C,#0D1830)", border: `1px solid ${C.accent}44` }}>
                <p style={{ margin: "0 0 6px", fontSize: 10, color: C.accent, fontWeight: 700, letterSpacing: 1 }}>PROPUESTA DE VALOR</p>
                <p style={{ margin: 0, fontSize: 15, color: C.text, lineHeight: 1.7, fontStyle: "italic" }}>
                  "VozFinanzas es el único asistente financiero que <span style={{ color: C.accent }}>escucha cuando gastas</span>, <span style={{ color: C.blue }}>piensa cuando no tienes tiempo</span>, te <span style={{ color: C.yellow }}>guarda el gasto sin bloquearte</span> y te <span style={{ color: C.purple }}>habla primero cuando vas por mal camino</span>."
                </p>
              </Card>
              {[
                { label: "Nombre", value: "VozFinanzas" },
                { label: "Tipo", value: "App Móvil + Web (Full Stack)" },
                { label: "Sector", value: "Finanzas personales" },
                { label: "País objetivo", value: "Colombia 🇨🇴" },
                { label: "Duración proyecto", value: "3 semestres UMB" },
                { label: "Desarrolladora", value: "Individual — Ing. Sistemas" },
                { label: "Presupuesto", value: "$0–$80 USD" },
                { label: "Pantallas", value: "8 vistas principales" },
              ].map(({ label, value }) => (
                <Card key={label}>
                  <p style={{ margin: "0 0 2px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>{label}</p>
                  <p style={{ margin: 0, fontSize: 14, fontWeight: 700, color: C.text }}>{value}</p>
                </Card>
              ))}
            </div>
            <Card style={{ marginBottom: 14 }}>
              <p style={{ margin: "0 0 12px", fontWeight: 700, fontSize: 14 }}>🗺️ Mapa completo del sistema</p>
              <div style={{ display: "grid", gridTemplateColumns: "repeat(3,1fr)", gap: 10 }}>
                {[
                  { title: "6 Módulos", items: ["🎙️ Registro por voz","🤖 Clasificación IA","💰 Presupuesto","📊 Análisis proactivo","💡 Tips económicos","🔔 Alertas"], color: "accent" },
                  { title: "8 Pantallas", items: ["🏠 Dashboard","🎙️ Registro voz","📋 Mis gastos","🤖 Análisis IA","💰 Presupuesto","📊 Reportes","💡 Tips","⏳ Pendientes"], color: "blue" },
                  { title: "3 Modos de Registro", items: ["🎙️ Normal: ~20seg, confirmado","⏳ Ahora no: ~5seg, pendiente","⚡ Rápido: ~2seg, pendiente","","Flujo 7 pasos con bifurcación","IA aprende de correcciones"], color: "yellow" },
                ].map(s => (
                  <div key={s.title} style={{ background: C.bg, borderRadius: 10, padding: 12 }}>
                    <p style={{ margin: "0 0 8px", fontSize: 12, fontWeight: 800, color: colorOf(s.color) }}>{s.title}</p>
                    {s.items.map(i => i ? <p key={i} style={{ margin: "0 0 3px", fontSize: 11, color: C.muted }}>• {i}</p> : <div key="spacer" style={{ height: 4 }} />)}
                  </div>
                ))}
              </div>
            </Card>
            <Card accent="purple">
              <p style={{ margin: "0 0 10px", fontWeight: 700, fontSize: 14 }}>🔄 Flujo principal resumido</p>
              <div style={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 5, fontSize: 11 }}>
                {["🎙️ Habla","→","⚙️ IA procesa","→","🤖 Confirma","→","❓ ¿Tiempo?","→","✅ Confirmar ahora","ó","⏳ Guardar pendiente","→","📥 Bandeja","→","✅ Verificar después"].map((s,i) => (
                  <span key={i} style={{ color: s === "→" || s === "ó" ? C.accent : s.includes("Confirmar ahora") ? C.accent : s.includes("pendiente") || s.includes("Bandeja") || s.includes("después") ? C.yellow : C.text, fontWeight: s !== "→" && s !== "ó" ? 600 : 400 }}>{s}</span>
                ))}
              </div>
            </Card>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 1 — MÓDULOS
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 1 && (
          <div>
            <SecHead icon="🧩" title="Módulos del Sistema" sub="6 bloques funcionales que componen VozFinanzas" />
            <div style={{ display: "grid", gap: 14 }}>
              {MODULES.map(m => (
                <Card key={m.name} accent={m.color}>
                  <div style={{ display: "flex", gap: 12, marginBottom: 12 }}>
                    <span style={{ fontSize: 24 }}>{m.icon}</span>
                    <div>
                      <div style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 3 }}>
                        <span style={{ fontWeight: 800, fontSize: 15 }}>{m.name}</span>
                        <Badge color={m.color}>{m.tag}</Badge>
                      </div>
                      <p style={{ margin: 0, fontSize: 12, color: C.muted }}>{m.desc}</p>
                    </div>
                  </div>
                  <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
                    <div>
                      <p style={{ margin: "0 0 6px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>Funcionalidades</p>
                      {m.items.map(it => <div key={it} style={{ display: "flex", gap: 5, marginBottom: 4, fontSize: 11, color: C.muted }}><span style={{ color: colorOf(m.color) }}>→</span><span>{it}</span></div>)}
                    </div>
                    <div>
                      <p style={{ margin: "0 0 6px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>Tecnologías</p>
                      {m.tech.map(t => <Chip key={t} color={m.color}>{t}</Chip>)}
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 2 — REGLAS IA
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 2 && (
          <div>
            <SecHead icon="💰" title="Reglas Financieras + Sugerencias IA" sub="La IA aplica la regla que mejor se adapte al salario del usuario" />
            <div style={{ display: "grid", gap: 14 }}>
              {RULES.map(r => (
                <Card key={r.name}>
                  <div style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 12 }}>
                    <span style={{ fontWeight: 800, fontSize: 15 }}>{r.name}</span>
                    <Badge color={r.color}>{r.sub}</Badge>
                  </div>
                  <div style={{ display: "grid", gridTemplateColumns: "repeat(3,1fr)", gap: 10 }}>
                    {r.items.map(it => (
                      <div key={it.label} style={{ background: C.bg, borderRadius: 10, padding: 12, textAlign: "center", border: `1px solid ${C.border}` }}>
                        <div style={{ fontSize: 20, fontWeight: 900, color: it.c, marginBottom: 4 }}>{it.pct}</div>
                        <div style={{ fontWeight: 700, fontSize: 12, marginBottom: 3 }}>{it.label}</div>
                        <div style={{ fontSize: 11, color: C.muted }}>{it.desc}</div>
                      </div>
                    ))}
                  </div>
                </Card>
              ))}
              <Card accent="accent">
                <p style={{ margin: "0 0 10px", fontWeight: 700, fontSize: 14 }}>💬 Ejemplos de sugerencias que genera la IA</p>
                {[
                  '"Llevas el 68% del mes y ya usaste el 90% de tu presupuesto de comida. Considera cocinar en casa esta semana."',
                  '"Tienes 4 suscripciones por $230K/mes. Netflix + Spotify + YouTube Premium + Disney+. ¿Cuál podrías pausar?"',
                  '"Aplicando la regla 50/30/20 deberías ahorrar $320K este mes. Llevas $0 guardado. ¿Qué tal automatizarlo?"',
                  '"Tus gastos en delivery aumentaron 40% vs el mes pasado. En 3 meses eso es $360K extras."',
                ].map((s,i) => <div key={i} style={{ background: C.bg, borderRadius: 9, padding: 10, marginBottom: 8, fontSize: 12, color: C.muted, borderLeft: `2px solid ${C.accent}` }}>{s}</div>)}
              </Card>
            </div>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 3 — PANTALLAS
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 3 && (
          <div>
            <SecHead icon="📱" title="Las 8 Pantallas de la App" sub="Cada vista y su propósito en la experiencia de usuario" />
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill,minmax(220px,1fr))", gap: 12, marginBottom: 14 }}>
              {SCREENS.map((s, i) => (
                <Card key={s.name} style={{ position: "relative", overflow: "hidden" }}>
                  <div style={{ position: "absolute", top: -8, right: -8, fontSize: 52, opacity: 0.05 }}>{s.icon}</div>
                  <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 8 }}>
                    <div style={{ width: 30, height: 30, borderRadius: 8, background: softOf(s.color), display: "flex", alignItems: "center", justifyContent: "center", fontSize: 15 }}>{s.icon}</div>
                    <div>
                      <p style={{ margin: 0, fontSize: 13, fontWeight: 800 }}>{s.name}</p>
                      <Badge color={s.color} xs>{s.badge}</Badge>
                    </div>
                  </div>
                  <p style={{ margin: "0 0 8px", fontSize: 11, color: C.muted, lineHeight: 1.6 }}>{s.desc}</p>
                  <p style={{ margin: 0, fontSize: 10, color: colorOf(s.color), fontWeight: 700 }}>Pantalla {i+1} / {SCREENS.length}</p>
                </Card>
              ))}
            </div>
            <Card accent="blue">
              <p style={{ margin: "0 0 8px", fontWeight: 700 }}>🔄 Flujo de navegación principal</p>
              <div style={{ display: "flex", flexWrap: "wrap", gap: 4, fontSize: 11 }}>
                {["Abre app","→","Dashboard","→","🎙️ / ⚡ Micrófono","→","Habla gasto","→","IA confirma","→","✓ Confirmar","ó","⏳ Ahora no","→","Bandeja pendientes","→","Verificar después","→","Dashboard actualizado"].map((s,i) => (
                  <span key={i} style={{ color: s === "→" || s === "ó" ? C.accent : C.muted }}>{s}</span>
                ))}
              </div>
            </Card>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 4 — FLUJO COMPLETO (7 pasos)
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 4 && (
          <div>
            <SecHead icon="🔄" title="Flujo Completo de Confirmación (7 pasos)" sub="Desde que el usuario habla hasta que el gasto queda confirmado o verificado" />
            {/* Selector de pasos */}
            <div style={{ display: "flex", gap: 5, marginBottom: 18, flexWrap: "wrap" }}>
              {FLOW_STEPS.map(s => (
                <button key={s.id} onClick={() => setFlowStep(s.id)} style={{
                  background: flowStep === s.id ? colorOf(s.color) : C.card,
                  color: flowStep === s.id ? (s.color === "yellow" ? "#000" : "#fff") : C.muted,
                  border: `1px solid ${flowStep === s.id ? colorOf(s.color) : C.border}`,
                  borderRadius: 99, padding: "5px 12px", fontSize: 11, fontWeight: 700, cursor: "pointer",
                }}>{s.icon} {s.id}. {s.label}</button>
              ))}
            </div>
            {/* Diagrama */}
            <Card style={{ marginBottom: 16, padding: "12px 16px" }}>
              <p style={{ margin: "0 0 10px", fontSize: 10, color: C.muted, fontWeight: 700 }}>DIAGRAMA DEL FLUJO</p>
              <div style={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 4, fontSize: 11, marginBottom: 10 }}>
                {FLOW_STEPS.slice(0,4).map((s,i) => <><span key={s.id} style={{ background: softOf(s.color), color: colorOf(s.color), borderRadius: 7, padding: "3px 9px", fontWeight: 700 }}>{s.icon} {s.label}</span>{i<3&&<Arrow key={`a${i}`}/>}</>)}
              </div>
              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, paddingTop: 10, borderTop: `1px solid ${C.border}` }}>
                <div>
                  <p style={{ margin: "0 0 6px", fontSize: 11, color: C.accent, fontWeight: 700 }}>✅ SÍ tiene tiempo</p>
                  <div style={{ display: "flex", gap: 4, flexWrap: "wrap", fontSize: 10 }}>
                    {["✏️ Editar/Confirmar","→","💾 Guardado","→","📊 Dashboard"].map((n,i) => <span key={i} style={{ color: n === "→" ? C.accent : C.muted }}>{n}</span>)}
                  </div>
                </div>
                <div>
                  <p style={{ margin: "0 0 6px", fontSize: 11, color: C.yellow, fontWeight: 700 }}>⏳ NO tiene tiempo</p>
                  <div style={{ display: "flex", gap: 4, flexWrap: "wrap", fontSize: 10 }}>
                    {["⏳ Pendiente","→","🔔 Notif","→","📥 Bandeja","→","✅ Verificar"].map((n,i) => <span key={i} style={{ color: n === "→" ? C.yellow : C.muted }}>{n}</span>)}
                  </div>
                </div>
              </div>
            </Card>
            {/* Detalle del paso seleccionado */}
            {FLOW_STEPS.filter(s => s.id === flowStep).map(s => (
              <div key={s.id} style={{ display: "grid", gridTemplateColumns: "1fr 200px", gap: 18, alignItems: "start" }}>
                <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                  <Card accent={s.color}>
                    <p style={{ margin: "0 0 3px", fontSize: 10, color: C.muted, fontWeight: 700 }}>PASO {s.id} DE {FLOW_STEPS.length}</p>
                    <p style={{ margin: "0 0 8px", fontSize: 16, fontWeight: 800 }}>{s.icon} {s.title}</p>
                    <p style={{ margin: "0 0 12px", fontSize: 13, color: C.muted, lineHeight: 1.7 }}>{s.desc}</p>
                    <div style={{ display: "grid", gap: 5 }}>
                      {s.details.map(d => <div key={d} style={{ display: "flex", gap: 6, fontSize: 12, color: C.muted }}><span style={{ color: colorOf(s.color), flexShrink: 0 }}>→</span><span>{d}</span></div>)}
                    </div>
                  </Card>
                  {s.id === 3 && (
                    <Card>
                      <p style={{ margin: "0 0 10px", fontWeight: 700, fontSize: 13 }}>Variantes del mensaje según confianza IA</p>
                      {[["≥ 90%","accent","Confianza ALTA","\"Entendí esto 👆. ¿Es correcto?\""],["70–89%","blue","Media","\"Creo que entendí esto. Verifica que esté bien.\""],["50–69%","orange","Baja","\"No estoy 100% segura. ¿Esto es lo que quisiste?\""],["< 50%","red","Muy baja","\"No entendí bien. ¿Puedes repetirlo o escribirlo?\""]].map(([conf,c,tag,msg]) => (
                        <div key={conf} style={{ background: C.bg, borderRadius: 9, padding: 10, marginBottom: 8 }}>
                          <div style={{ display: "flex", gap: 8, marginBottom: 4 }}><span style={{ color: colorOf(c), fontWeight: 700, fontSize: 11 }}>{conf}</span><Badge color={c} xs>{tag}</Badge></div>
                          <p style={{ margin: 0, fontSize: 12, color: C.muted, fontStyle: "italic" }}>{msg}</p>
                        </div>
                      ))}
                    </Card>
                  )}
                </div>
                <FlowPhone stepId={s.id} />
              </div>
            ))}
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 5 — MODOS DE REGISTRO
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 5 && (
          <div>
            <SecHead icon="⚡" title="Los 3 Modos de Registro" sub="Cada modo se adapta al tiempo disponible del usuario en ese momento" />
            <div style={{ display: "grid", gap: 14, marginBottom: 14 }}>
              {[
                { icon: "🎙️", name: "Modo Normal", color: "accent", badge: "por defecto", tiempo: "~20 seg", resultado: "Gasto confirmado", desc: "Registro completo con confirmación de IA. El usuario revisa y aprueba antes de guardar.", cuando: "Cuando estás tranquila y tienes unos segundos para revisar la clasificación de la IA.",
                  flujo: ["Presionas el micrófono grande 🎙️","Hablas el gasto","IA muestra card con monto y categoría","Tú presionas [✓ Sí] o [✏️ Editar]","Gasto guardado como CONFIRMADO","Suma al presupuesto inmediatamente"] },
                { icon: "⏳", name: "Modo Ahora No", color: "yellow", badge: "nuevo", tiempo: "~5 seg", resultado: "Gasto pendiente", desc: "La IA muestra el card de confirmación, pero el usuario cierra con 'Ahora no'. Se guarda como pendiente sin interrumpir lo que está haciendo.", cuando: "Cuando estás en la caja, en una reunión, en el bus o en cualquier situación que no te permite detenerte.",
                  flujo: ["Presionas el micrófono grande 🎙️","Hablas el gasto","IA muestra card con monto y categoría","Tú presionas [⏳ Ahora no]","Gasto guardado como PENDIENTE","Notificación recordatoria luego"] },
                { icon: "⚡", name: "Modo Rápido", color: "pink", badge: "nuevo", tiempo: "~2 seg", resultado: "Pendiente automático", desc: "Un segundo micrófono pequeño en el dashboard. Sin pantalla de confirmación. La IA clasifica y guarda automáticamente como pendiente. Máxima velocidad.", cuando: "Cuando solo tienes 2 segundos. En la caja, saliendo del restaurante, subiendo al bus.",
                  flujo: ["Presionas el micrófono pequeño ⚡","Hablas el gasto","La app confirma con un 'ding'","Gasto guardado como PENDIENTE ⚡","Etiquetado como 'Modo rápido'","No hay pantalla de confirmación"] },
              ].map(m => (
                <Card key={m.name} style={{ borderTop: `3px solid ${colorOf(m.color)}` }}>
                  <div style={{ display: "flex", gap: 12, alignItems: "flex-start", marginBottom: 14 }}>
                    <div style={{ width: 44, height: 44, borderRadius: 12, background: softOf(m.color), display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20, flexShrink: 0 }}>{m.icon}</div>
                    <div style={{ flex: 1 }}>
                      <div style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 4 }}>
                        <span style={{ fontWeight: 800, fontSize: 15 }}>{m.name}</span>
                        <Badge color={m.color}>{m.badge}</Badge>
                      </div>
                      <p style={{ margin: 0, fontSize: 12, color: C.muted }}>{m.desc}</p>
                    </div>
                    <div style={{ textAlign: "right", flexShrink: 0 }}>
                      <p style={{ margin: "0 0 2px", fontSize: 10, color: C.muted }}>Tiempo</p>
                      <p style={{ margin: "0 0 6px", fontSize: 13, fontWeight: 800, color: colorOf(m.color) }}>{m.tiempo}</p>
                      <Badge color={m.color} xs>{m.resultado}</Badge>
                    </div>
                  </div>
                  <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
                    <div style={{ background: C.bg, borderRadius: 10, padding: 12 }}>
                      <p style={{ margin: "0 0 6px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>Cuándo usarlo</p>
                      <p style={{ margin: 0, fontSize: 12, color: C.muted }}>{m.cuando}</p>
                    </div>
                    <div style={{ background: C.bg, borderRadius: 10, padding: 12 }}>
                      <p style={{ margin: "0 0 6px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>Pasos del flujo</p>
                      {m.flujo.map((f,i) => <p key={i} style={{ margin: "0 0 3px", fontSize: 11, color: C.muted }}><span style={{ color: colorOf(m.color) }}>{i+1}.</span> {f}</p>)}
                    </div>
                  </div>
                </Card>
              ))}
            </div>
            {/* Tabla comparativa */}
            <Card>
              <p style={{ margin: "0 0 12px", fontWeight: 700, fontSize: 14 }}>📊 Tabla comparativa de los 3 modos</p>
              <div style={{ overflowX: "auto" }}>
                <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 12 }}>
                  <thead>
                    <tr>{["","🎙️ Normal","⏳ Ahora no","⚡ Rápido"].map(h => <th key={h} style={{ padding: "8px 12px", textAlign: "left", color: C.muted, borderBottom: `1px solid ${C.border}`, fontSize: 11, fontWeight: 700 }}>{h}</th>)}</tr>
                  </thead>
                  <tbody>
                    {[
                      ["Pantalla de confirmación","✅ Sí","✅ Sí","❌ No"],
                      ["El usuario revisa","Inmediato","Después","Después"],
                      ["Velocidad","~20 seg","~5 seg","~2 seg"],
                      ["Estado resultante","✅ Confirmado","⏳ Pendiente","⏳ Pendiente"],
                      ["Suma al presupuesto","Inmediato","Al verificar","Al verificar"],
                      ["Etiqueta especial","—","—","⚡ Modo rápido"],
                      ["Ideal para","Con tiempo","Sin tiempo momentáneo","Máxima prisa"],
                    ].map(([lbl,...vals]) => (
                      <tr key={lbl}>
                        <td style={{ padding: "7px 12px", color: C.muted, borderBottom: `1px solid ${C.border}22`, fontWeight: 600 }}>{lbl}</td>
                        {vals.map((v,i) => <td key={i} style={{ padding: "7px 12px", borderBottom: `1px solid ${C.border}22`, color: v.includes("✅") ? C.accent : v.includes("⏳") ? C.yellow : v.includes("❌") ? C.red : C.text }}>{v}</td>)}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </Card>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 6 — BANDEJA DE PENDIENTES
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 6 && (
          <div>
            <SecHead icon="⏳" title="Bandeja de Pendientes" sub="Pantalla donde el usuario verifica los gastos guardados sin confirmar" />
            <div style={{ display: "grid", gridTemplateColumns: "1fr 200px", gap: 18, alignItems: "start" }}>
              <div style={{ display: "flex", flexDirection: "column", gap: 14 }}>
                <Card accent="yellow">
                  <p style={{ margin: "0 0 12px", fontWeight: 700, fontSize: 14 }}>📋 Qué muestra la bandeja</p>
                  {[
                    ["Lista de gastos pendientes","Ordenados por fecha, más reciente arriba. Etiquetados con ⏳ o ⚡."],
                    ["Datos detectados por la IA","Monto, categoría sugerida, transcripción original, hora del registro."],
                    ["Barra de confianza IA","Si es baja, se resalta en naranja para que el usuario preste más atención."],
                    ["Acciones rápidas","[✓ Confirmar] [✏️ Editar] [🗑️ Eliminar] por cada gasto."],
                    ["Resumen en la parte superior","'Tienes 4 gastos pendientes por $215.000 sin confirmar.'"],
                    ["Botón revisar todos","Modo carrusel para revisar uno por uno o confirmar todos si se confía en la IA."],
                  ].map(([k,v]) => (
                    <div key={k} style={{ background: C.bg, borderRadius: 8, padding: 10, marginBottom: 8 }}>
                      <p style={{ margin: "0 0 3px", fontSize: 12, fontWeight: 700, color: C.yellow }}>{k}</p>
                      <p style={{ margin: 0, fontSize: 12, color: C.muted }}>{v}</p>
                    </div>
                  ))}
                </Card>
                <Card>
                  <p style={{ margin: "0 0 10px", fontWeight: 700, fontSize: 14 }}>🔖 Estados de un gasto pendiente</p>
                  <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                    {[
                      ["⏳","Pendiente Normal","yellow","El usuario eligió 'Ahora no' en el card de confirmación."],
                      ["⚡","Modo Rápido","pink","Guardado sin confirmación. Siempre va como pendiente."],
                      ["⚠️","Baja confianza","orange","IA tenía <70% de confianza. Requiere atención especial."],
                      ["✅","Verificado","accent","El usuario confirmó. Sale de la bandeja y suma al presupuesto."],
                    ].map(([ico,lbl,c,desc]) => (
                      <div key={lbl} style={{ background: C.bg, borderRadius: 10, padding: 10 }}>
                        <div style={{ display: "flex", gap: 6, alignItems: "center", marginBottom: 4 }}><span>{ico}</span><span style={{ fontSize: 12, fontWeight: 700, color: colorOf(c) }}>{lbl}</span></div>
                        <p style={{ margin: 0, fontSize: 11, color: C.muted }}>{desc}</p>
                      </div>
                    ))}
                  </div>
                </Card>
                <Card>
                  <p style={{ margin: "0 0 10px", fontWeight: 700, fontSize: 14 }}>🔔 Notificaciones recordatorias</p>
                  {[
                    ["Inmediata","\"Gasto de $50K guardado como pendiente. Verifica cuando puedas 👀\""],
                    ["2 horas después","\"Tienes 1 gasto pendiente. ¿Tienes un momento?\""],
                    ["9PM (noche)","\"Antes de dormir: tienes 3 gastos pendientes de hoy sin verificar.\""],
                    ["Día siguiente","\"Buenos días. Quedaron 2 gastos de ayer sin confirmar.\""],
                  ].map(([t,m]) => (
                    <div key={t} style={{ background: C.bg, borderRadius: 8, padding: 10, marginBottom: 8 }}>
                      <p style={{ margin: "0 0 3px", fontSize: 11, color: C.yellow, fontWeight: 700 }}>{t}</p>
                      <p style={{ margin: 0, fontSize: 12, color: C.muted, fontStyle: "italic" }}>{m}</p>
                    </div>
                  ))}
                </Card>
                <Card accent="accent">
                  <p style={{ margin: "0 0 8px", fontWeight: 700 }}>📊 Widget en el Dashboard</p>
                  <p style={{ margin: "0 0 10px", fontSize: 12, color: C.muted }}>Cuando hay pendientes, el dashboard muestra este widget de alerta:</p>
                  <div style={{ background: C.yellowSoft, border: `1px solid ${C.yellow}`, borderRadius: 10, padding: 12, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div><p style={{ margin: "0 0 2px", fontSize: 12, fontWeight: 700, color: C.yellow }}>⏳ 4 gastos pendientes</p><p style={{ margin: 0, fontSize: 11, color: C.muted }}>$215.000 sin confirmar · Toca para revisar</p></div>
                    <span style={{ fontSize: 18, color: C.yellow }}>→</span>
                  </div>
                </Card>
              </div>
              <FlowPhone stepId={6} />
            </div>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 7 — EDGE CASES
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 7 && (
          <div>
            <SecHead icon="⚠️" title="Casos Extremos que maneja la IA" sub="Situaciones reales con frases ambiguas, incompletas o audio difícil" />
            <div style={{ display: "grid", gap: 12 }}>
              {EDGE_CASES.map((ec, i) => (
                <Card key={i} accent={ec.color}>
                  <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 12 }}>
                    <div>
                      <p style={{ margin: "0 0 5px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>El usuario dice</p>
                      <div style={{ background: C.bg, borderRadius: 8, padding: 10 }}>
                        <p style={{ margin: 0, fontSize: 12, color: C.text, fontStyle: "italic" }}>{ec.input}</p>
                      </div>
                    </div>
                    <div>
                      <p style={{ margin: "0 0 5px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>IA detecta</p>
                      <div style={{ background: C.bg, borderRadius: 8, padding: 10, fontSize: 11 }}>
                        <Row label="Monto" value={ec.monto} vc={C.accent} />
                        <Row label="Categoría" value={ec.cat} vc={C.orange} />
                        <Row label="Subcategoría" value={ec.sub} />
                        <div style={{ marginTop: 6 }}>
                          <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                            <div style={{ flex: 1, height: 4, background: C.subtle, borderRadius: 99 }}>
                              <div style={{ width: `${ec.conf}%`, height: 4, background: ec.conf > 80 ? C.accent : ec.conf > 55 ? C.orange : C.red, borderRadius: 99 }} />
                            </div>
                            <span style={{ fontSize: 10, color: C.muted, minWidth: 28 }}>{ec.conf}%</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div>
                      <p style={{ margin: "0 0 5px", fontSize: 10, color: C.muted, fontWeight: 700, textTransform: "uppercase" }}>IA responde</p>
                      <div style={{ background: C.blueSoft, border: `1px solid ${C.blue}33`, borderRadius: 8, padding: 10 }}>
                        <p style={{ margin: 0, fontSize: 12, color: C.text }}>{ec.response}</p>
                      </div>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 8 — TECH STACK
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 8 && (
          <div>
            <SecHead icon="⚙️" title="Stack Tecnológico" sub="Seleccionado para trabajar sola y ser 100% funcional en 3 semestres" />
            <div style={{ display: "grid", gap: 10, marginBottom: 14 }}>
              {STACK.map(s => (
                <Card key={s.layer} style={{ display: "flex", gap: 14, alignItems: "center" }}>
                  <div style={{ minWidth: 130, fontSize: 13, fontWeight: 700 }}>{s.layer}</div>
                  <div style={{ minWidth: 150 }}><span style={{ background: softOf(s.color), color: colorOf(s.color), borderRadius: 6, padding: "3px 10px", fontSize: 12, fontWeight: 700 }}>{s.tech}</span></div>
                  <div style={{ fontSize: 12, color: C.muted, flex: 1 }}>{s.why}</div>
                </Card>
              ))}
            </div>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 14 }}>
              <Card accent="orange">
                <p style={{ margin: "0 0 10px", fontWeight: 700 }}>🔑 APIs de pago / costo estimado</p>
                {[["Whisper (OpenAI)","$0.006/min · plan gratuito para volumen universitario"],["Claude API","Tier gratis durante desarrollo. ~$5–15 en producción beta"],["Google Maps","$200 crédito mensual gratis — suficiente para el proyecto"]].map(([n,d]) => (
                  <div key={n} style={{ marginBottom: 8 }}><span style={{ color: C.orange, fontWeight: 700, fontSize: 12 }}>{n}</span><p style={{ margin: "2px 0 0", fontSize: 11, color: C.muted }}>{d}</p></div>
                ))}
              </Card>
              <Card style={{ gridColumn: "span 2", borderTop: `3px solid ${C.yellow}` }}>
                <p style={{ margin: "0 0 12px", fontWeight: 700, fontSize: 14 }}>🤔 ¿Por qué React Native + Expo y no Flutter?</p>
                <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill,minmax(200px,1fr))", gap: 10 }}>
                  {[
                    { icon: "🟡", title: "Flutter Web es experimental", desc: "Flutter Web está marcado como beta. No es apto para producción web seria. React Native + Expo sí lo es.", color: "red" },
                    { icon: "🔵", title: "Un solo lenguaje: JS/TS", desc: "App, web y backend comparten el mismo lenguaje. Con Flutter usarías Dart solo en el móvil y JS en todo lo demás.", color: "blue" },
                    { icon: "🟣", title: "SDKs nativos en JS", desc: "Claude, Whisper, Firebase y Expo tienen SDKs oficiales en JS/TS. En Dart hay que hacer llamadas HTTP manuales.", color: "purple" },
                    { icon: "🟢", title: "Expo Go = sin Android Studio", desc: "Con Expo Go pruebas la app en tu celular físico escaneando un QR. Sin instalar Android Studio ni Xcode.", color: "accent" },
                    { icon: "🔴", title: "Dart es un nicho", desc: "Dart casi no se usa fuera de Flutter. Aprender JS/TS te sirve para todo el stack actual y proyectos futuros.", color: "orange" },
                    { icon: "♻️", title: "Código reutilizable", desc: "Los hooks y servicios de React Native se pueden importar directamente en la web React. Zero reescritura.", color: "teal" },
                  ].map(d => (
                    <div key={d.title} style={{ background: C.bg, borderRadius: 10, padding: 12, borderLeft: `2px solid ${colorOf(d.color)}` }}>
                      <p style={{ margin: "0 0 4px", fontSize: 12, fontWeight: 700, color: colorOf(d.color) }}>{d.icon} {d.title}</p>
                      <p style={{ margin: 0, fontSize: 11, color: C.muted, lineHeight: 1.6 }}>{d.desc}</p>
                    </div>
                  ))}
                </div>
              </Card>
              <Card accent="purple">
                <p style={{ margin: "0 0 10px", fontWeight: 700 }}>🤖 Prompt base para Claude API</p>
                <div style={{ background: "#000", borderRadius: 9, padding: 12, fontFamily: "monospace", fontSize: 10, color: "#a3e635", lineHeight: 1.8 }}>
                  <span style={{ color: "#64748b" }}>// System prompt</span><br />
                  <span style={{ color: "#f472b6" }}>Eres asistente de finanzas personales</span><br />
                  <span style={{ color: "#f472b6" }}>para usuarios colombianos.</span><br />
                  <span>Extrae del texto:</span><br />
                  <span>- monto (COP, "50k"→50000)</span><br />
                  <span>- categoria (8 opciones)</span><br />
                  <span>- subcategoria (string)</span><br />
                  <span>- fecha (ISO, default hoy)</span><br />
                  <span>- confianza (0-100)</span><br />
                  <span style={{ color: "#60a5fa" }}>Responde SOLO en JSON.</span>
                </div>
              </Card>
            </div>
          </div>
        )}

        {/* ══════════════════════════════════════════════════════════════════
            TAB 9 — DIFERENCIADORES
        ══════════════════════════════════════════════════════════════════ */}
        {tab === 9 && (
          <div>
            <SecHead icon="🚀" title="¿Qué hace única a VozFinanzas?" sub="Los diferenciadores vs Fintonic, Mint, Mobills y otras apps del mercado" />
            <div style={{ display: "grid", gap: 12, marginBottom: 14 }}>
              {DIFFERENTIATORS.map(d => (
                <Card key={d.title} accent={d.color}>
                  <div style={{ display: "flex", gap: 14 }}>
                    <span style={{ fontSize: 26 }}>{d.icon}</span>
                    <div>
                      <p style={{ margin: "0 0 4px", fontWeight: 800, fontSize: 14 }}>{d.title}</p>
                      <p style={{ margin: 0, fontSize: 13, color: C.muted, lineHeight: 1.7 }}>{d.desc}</p>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
            <Card style={{ background: "linear-gradient(135deg,#052E1C,#0D1830,#1E0A3C)", border: `1px solid ${C.accent}44` }}>
              <p style={{ margin: "0 0 8px", fontWeight: 900, fontSize: 15, color: C.accent }}>🎯 Propuesta de valor en una frase</p>
              <p style={{ margin: 0, fontSize: 14, color: C.text, lineHeight: 1.8, fontStyle: "italic" }}>
                "VozFinanzas es el único asistente financiero que <span style={{ color: C.accent }}>escucha cuando gastas</span>, <span style={{ color: C.yellow }}>te guarda el gasto sin bloquearte</span>, <span style={{ color: C.purple }}>piensa cuando tú no tienes tiempo</span>, y <span style={{ color: C.pink }}>te habla primero cuando vas por mal camino</span>."
              </p>
            </Card>
          </div>
        )}

      </div>
    </div>
  );
}