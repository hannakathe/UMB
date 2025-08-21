import { getPlanetById, fmt } from "./api.js";

const statusEl = document.getElementById("status");
const container = document.getElementById("planet-detail");

function setStatus(msg) {
  statusEl.textContent = msg || "";
}

function fact(label, value) {
  return `<div class="fact"><b>${label}:</b> ${fmt(value)}</div>`;
}

function renderDetail(p) {
  container.innerHTML = `
    <header class="detail-header">
      <img src="${fmt(p.image)}" alt="Planeta ${fmt(p.name)}"
           onerror="this.src='https://picsum.photos/640/360?blur=2&random=${p.id}'" />
      <div>
        <h2>${fmt(p.name)}</h2>
        <div class="meta" style="margin:.5rem 0 1rem">
          ${p.isDestroyed ? `<span class="badge">Destruido</span>` : `<span class="badge">Activo</span>`}
          ${Array.isArray(p.residents) ? `<span class="badge">${p.residents.length} residentes</span>` : ""}
        </div>
        <p style="color:#c9c9d6">${fmt(p.description, "Sin descripción")}</p>
      </div>
    </header>

    <section class="facts">
      ${fact("Galaxia", p.galaxy)}
      ${fact("Universo", p.universe)}
      ${fact("Kaio", p.kaio)}
      ${fact("Población", p.population)}
      ${fact("Creador", p.creator)}
      ${fact("Apariciones", Array.isArray(p.appearances) ? p.appearances.length : p.appearances)}
      ${fact("Región", p.region)}
      ${fact("Tipo", p.type)}
    </section>
  `;
}

async function init() {
  try {
    setStatus("Cargando detalle…");

    const params = new URLSearchParams(location.search);
    const id = params.get("id");
    if (!id) {
      setStatus("No se proporcionó un ID de planeta.");
      return;
    }

    const data = await getPlanetById(id);
    // Algunas APIs devuelven {id, name, ...} directamente; otras {data:{...}}
    const planet = data?.data ?? data;

    renderDetail(planet);
    setStatus("");
    document.title = `Dragon Ball • ${fmt(planet.name)}`;
  } catch (err) {
    console.error(err);
    setStatus("No se pudo cargar el detalle del planeta.");
    container.innerHTML = `
      <div class="fact">
        <b>Error:</b> ${fmt(err.message)}
      </div>
    `;
  }
}

init();
