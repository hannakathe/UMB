const API = 'http://localhost:5000/api';

const tablaBody = document.querySelector('#tablaClientes tbody');
const form = document.getElementById('clienteForm');
const btnGuardar = document.getElementById('guardar');
const btnActualizar = document.getElementById('actualizar');
const btnEliminar = document.getElementById('eliminar');
const btnCancelar = document.getElementById('cancelar');
const paisSelect = document.getElementById('paisSelect');
const ciudadSelect = document.getElementById('ciudadSelect');

let editando = false;
let editarNroID = null;

// Inicializar
document.addEventListener('DOMContentLoaded', async () => {
  await cargarPaises();
  await listarClientes();
  initMap();
  resetFormulario(); // asegura estado inicial
});

// ------------------ Carga de países y ciudades ------------------
async function cargarPaises() {
  try {
    const res = await fetch(`${API}/paises`);
    const paises = await res.json();
    paisSelect.innerHTML = '<option value="">-- Seleccione País --</option>';
    paises.forEach(p => {
      const opt = document.createElement('option');
      opt.value = p.cod_pais;
      opt.textContent = p.nombre_pais;
      paisSelect.appendChild(opt);
    });
  } catch (e) {
    console.error('Error cargando países:', e);
  }
}

paisSelect.addEventListener('change', async (e) => {
  const cod = e.target.value;
  ciudadSelect.innerHTML = '<option value="">-- Seleccione Ciudad --</option>';
  if (!cod) return;
  try {
    const res = await fetch(`${API}/ciudades/${cod}`);
    const ciudades = await res.json();
    ciudades.forEach(ci => {
      const opt = document.createElement('option');
      opt.value = ci.cod_ciudad;
      opt.textContent = ci.nombre_ciudad;
      ciudadSelect.appendChild(opt);
    });
  } catch (err) {
    console.error('Error cargando ciudades:', err);
  }
  // centrar mapa (no crítico para guardar)
  const nombrePais = paisSelect.options[paisSelect.selectedIndex]?.text;
  if (nombrePais) fetchCountryCoords(nombrePais);
});

// ------------------ BOTONES: Crear / Actualizar / Eliminar / Cancelar ------------------

// CREAR (btnGuardar debe ser type="button" en HTML)
btnGuardar.addEventListener('click', async (e) => {
  e.preventDefault();
  console.log('Guardar clic - estado editando:', editando);

  if (editando) {
    // Si está en modo edición, sugerimos usar Actualizar en lugar de Guardar.
    return alert('Actualmente estás en modo edición. Presiona Actualizar o Cancelar primero.');
  }

  const data = formToObject();
  if (!data.nroID || !data.nombres) {
    return alert('NroID y Nombre son obligatorios.');
  }

  try {
    const res = await fetch(`${API}/clientes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (res.ok) {
      console.log('Respuesta POST OK');
      alert('✅ Cliente creado correctamente');
      form.reset();
      await listarClientes();
      resetFormulario();
    } else {
      const text = await res.text();
      console.error('Error al crear cliente:', text);
      alert('❌ Error al crear cliente: ' + text);
    }
  } catch (err) {
    console.error('Fetch POST error:', err);
    alert('❌ Error de red al crear cliente');
  }
});

// ACTUALIZAR
btnActualizar.addEventListener('click', async (e) => {
  e.preventDefault();
  console.log('Actualizar clic - editarNroID:', editarNroID, 'editando:', editando);

  if (!editando || !editarNroID) return alert('Selecciona un cliente primero para actualizar.');

  const data = formToObject();
  try {
    const res = await fetch(`${API}/clientes/${encodeURIComponent(editarNroID)}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (res.ok) {
      alert('✅ Cliente actualizado correctamente');
      await listarClientes();
      resetFormulario();
    } else {
      const text = await res.text();
      console.error('Error PUT:', text);
      alert('❌ Error al actualizar: ' + text);
    }
  } catch (err) {
    console.error('Fetch PUT error:', err);
    alert('❌ Error de red al actualizar');
  }
});

// ELIMINAR
btnEliminar.addEventListener('click', async (e) => {
  e.preventDefault();
  if (!editando || !editarNroID) return alert('Selecciona un cliente primero.');
  if (!confirm(`¿Eliminar cliente ${editarNroID}?`)) return;
  try {
    const res = await fetch(`${API}/clientes/${encodeURIComponent(editarNroID)}`, { method: 'DELETE' });
    if (res.ok) {
      alert('🗑️ Cliente eliminado correctamente');
      await listarClientes();
      resetFormulario();
    } else {
      const text = await res.text();
      console.error('Error DELETE:', text);
      alert('❌ Error al eliminar: ' + text);
    }
  } catch (err) {
    console.error('Fetch DELETE error:', err);
    alert('❌ Error de red al eliminar');
  }
});

// CANCELAR
btnCancelar.addEventListener('click', (e) => {
  e.preventDefault();
  resetFormulario();
});

// ------------------ Listar clientes ------------------
async function listarClientes() {
  try {
    const res = await fetch(`${API}/clientes`);
    const clientes = await res.json();
    tablaBody.innerHTML = '';
    clientes.forEach(c => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${c.nroID}</td>
        <td>${c.nombres}</td>
        <td>${c.correo || ''}</td>
        <td>${c.celular || ''}</td>
        <td>${c.nombre_pais || ''}</td>
        <td>${c.nombre_ciudad || ''}</td>
      `;
      tr.addEventListener('click', () => llenarFormulario(c));
      tablaBody.appendChild(tr);
    });
  } catch (err) {
    console.error('Error listar clientes:', err);
  }
}

// ------------------ Llenar formulario (seleccionar fila) ------------------
function llenarFormulario(c) {
  document.getElementById('tipoID').value = c.tipoID || 'CC';
  document.getElementById('nroID').value = c.nroID;
  document.getElementById('nombres').value = c.nombres;
  document.getElementById('correo').value = c.correo || '';
  document.getElementById('direccion').value = c.direccion || '';
  document.getElementById('celular').value = c.celular || '';

  // cargar ciudades del país y luego asignar ciudad
  if (c.cod_pais) {
    paisSelect.value = c.cod_pais;
    // disparar cambio para cargar ciudades
    const ev = new Event('change');
    paisSelect.dispatchEvent(ev);
    setTimeout(() => {
      ciudadSelect.value = c.cod_ciudad || '';
    }, 300);
  }

  editando = true;
  editarNroID = c.nroID;
  document.getElementById('nroID').readOnly = true;
  alternarBotones(true);
}

// ------------------ Helpers: form -> objeto ------------------
function formToObject() {
  return {
    tipoID: document.getElementById('tipoID').value || null,
    nroID: document.getElementById('nroID').value,
    nombres: document.getElementById('nombres').value,
    correo: document.getElementById('correo').value,
    direccion: document.getElementById('direccion').value,
    celular: document.getElementById('celular').value,
    cod_ciudad: ciudadSelect.value ? parseInt(ciudadSelect.value) : null,
    cod_pais: paisSelect.value ? parseInt(paisSelect.value) : null
  };
}

// ------------------ Reset / alternar botones ------------------
function resetFormulario() {
  form.reset();
  editando = false;
  editarNroID = null;
  document.getElementById('nroID').readOnly = false;
  alternarBotones(false);
}

function alternarBotones(editMode) {
  btnGuardar.style.display = editMode ? 'none' : 'inline-block';
  btnActualizar.style.display = editMode ? 'inline-block' : 'none';
  btnEliminar.disabled = !editMode;
}

// ------------------ Mapa (Leaflet) ------------------
let map, marker;
function initMap() {
  map = L.map('map').setView([4.6, -74.07], 5);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map);
}
async function fetchCountryCoords(countryName) {
  try {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(countryName)}`;
    const r = await fetch(url);
    const data = await r.json();
    if (data && data.length) {
      const lat = parseFloat(data[0].lat);
      const lon = parseFloat(data[0].lon);
      map.setView([lat, lon], 5);
      if (marker) marker.remove();
      marker = L.marker([lat, lon]).addTo(map).bindPopup(`<b>${countryName}</b>`).openPopup();
    }
  } catch (e) {
    console.error('Geocoding error', e);
  }
}
