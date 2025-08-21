import { getPlanets, fmt } from "./api.js";

const grid = document.getElementById("grid");
const statusEl = document.getElementById("status");
const prevBtn = document.getElementById("prev-btn");
const nextBtn = document.getElementById("next-btn");
const pageIndicator = document.getElementById("page-indicator");
const form = document.getElementById("search-form");
const input = document.getElementById("search-input");
const clearBtn = document.getElementById("clear-search");

let state = {
  page: 1,
  name: "",
  lastResponse: null, // guardamos la última respuesta para saber si hay más páginas
};

function setStatus(msg) {
  statusEl.textContent = msg || "";
}

function cardTemplate(p) {
  // Campos típicos de la API: id, name, description, image, isDestroyed, residents, etc.
  const link = `./detalle.html?id=${encodeURIComponent(p.id)}`;
  return `
    <article class="card">
      <a href="${link}" title="Ver detalle de ${fmt(p.name)}">
        <img class="thumb" src="${fmt(p.image)}"
             alt="Planeta ${fmt(p.name)}"
             onerror="this.src='https://picsum.photos/640/360?blur=2&random=${p.id}'" />
        <div class="card-body">
          <h2 class="card-title">${fmt(p.name)}</h2>
          <div class="meta">
            ${p.isDestroyed ? `<span class="badge">Destruido</span>` : `<span class="badge">Activo</span>`}
            ${Array.isArray(p.residents) ? `<span class="badge">${p.residents.length} residentes</span>` : ""}
          </div>
        </div>
      </a>
    </article>
  `;
}

function renderList(items = []) {
  grid.innerHTML = items.map(cardTemplate).join("");
}

function updatePager() {
  const page = state.page;
  const totalPages = state.lastResponse?.meta?.last_page ?? null;
  pageIndicator.textContent = totalPages ? `Página ${page} de ${totalPages}` : `Página ${page}`;

  // habilitar/deshabilitar
  prevBtn.disabled = page <= 1;
  if (totalPages) {
    nextBtn.disabled = page >= totalPages;
  } else {
    // si la API no trae meta de páginas, deshabilita next si la página vino vacía
    const hasItems = (state.lastResponse?.items ?? state.lastResponse?.data ?? []).length > 0;
    nextBtn.disabled = !hasItems;
  }
}

async function load(page = 1) {
  try {
    setStatus("Cargando planetas…");
    grid.innerHTML = ""; // limpio mientras carga
    state.page = page;

    const data = await getPlanets(page, state.name);

    // Hay APIs que devuelven {items, meta} y otras {data, meta}. Cubrimos ambos.
    const items = Array.isArray(data.items) ? data.items : Array.isArray(data.data) ? data.data : data;

    state.lastResponse = data;
    renderList(items);
    setStatus(items.length ? "" : "No se encontraron planetas para este criterio.");
    updatePager();
  } catch (err) {
    console.error(err);
    setStatus("No se pudo cargar el listado. Reintenta más tarde.");
    grid.innerHTML = `
      <div class="card" style="padding:16px">
        <div class="card-body">
          <h2 class="card-title">Ups…</h2>
          <p>Ocurrió un error consultando la API.</p>
          <pre style="white-space:pre-wrap; color:#a1a1af">${err.message}</pre>
        </div>
      </div>
    `;
    prevBtn.disabled = true;
    nextBtn.disabled = true;
  }
}

// Eventos de paginación
prevBtn.addEventListener("click", () => load(state.page - 1));
nextBtn.addEventListener("click", () => load(state.page + 1));

// Búsqueda
form.addEventListener("submit", (e) => {
  e.preventDefault();
  state.name = input.value.trim();
  load(1);
});
clearBtn.addEventListener("click", () => {
  input.value = "";
  state.name = "";
  load(1);
});

// Primera carga
load(1);
