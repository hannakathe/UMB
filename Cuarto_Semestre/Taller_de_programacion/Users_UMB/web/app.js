const API = 'http://localhost:5000/api';

const tablaBody = document.querySelector('#tablaClientes tbody');
const form = document.getElementById('clienteForm');
const btnGuardar = document.getElementById('guardar');
const btnActualizar = document.getElementById('actualizar');
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
});

async function cargarPaises(){
  const res = await fetch(`${API}/paises`);
  const paises = await res.json();
  paisSelect.innerHTML = '<option value="">-- Seleccione País --</option>';
  paises.forEach(p => {
    const opt = document.createElement('option');
    opt.value = p.cod_pais;
    opt.textContent = p.nombre_pais;
    paisSelect.appendChild(opt);
  });
}

paisSelect.addEventListener('change', async (e) => {
  const cod = e.target.value;
  ciudadSelect.innerHTML = '<option value="">-- Seleccione Ciudad --</option>';
  if(!cod) return;
  const res = await fetch(`${API}/ciudades/${cod}`);
  const ciudades = await res.json();
  ciudades.forEach(ci => {
    const opt = document.createElement('option');
    opt.value = ci.cod_ciudad;
    opt.textContent = ci.nombre_ciudad;
    ciudadSelect.appendChild(opt);
  });

  // centrar mapa según país (usamos API de geocoding simple via nominatim)
  fetchCountryCoords(paisSelect.options[paisSelect.selectedIndex].text);
});

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  if(editando){
    await actualizarCliente();
    return;
  }
  const data = formToObject();
  const res = await fetch(`${API}/clientes`, {
    method: 'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(data)
  });
  if(res.ok){
    form.reset();
    await listarClientes();
  } else {
    alert('Error al crear cliente');
  }
});

async function listarClientes(){
  const res = await fetch(`${API}/clientes`);
  const clientes = await res.json();
  tablaBody.innerHTML = '';
  clientes.forEach(c => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${c.nroID}</td>
      <td>${c.nombres}</td>
      <td>${c.correo||''}</td>
      <td>${c.celular||''}</td>
      <td>${c.nombre_pais||''}</td>
      <td>${c.nombre_ciudad||''}</td>
      <td>
        <button onclick="editar('${c.nroID}')">Editar</button>
        <button onclick="borrar('${c.nroID}')">Eliminar</button>
      </td>
    `;
    tablaBody.appendChild(tr);
  });
}

function formToObject(){
  return {
    tipoID: document.getElementById('tipoID').value || null,
    nroID: document.getElementById('nroID').value,
    nombres: document.getElementById('nombres').value,
    correo: document.getElementById('correo').value,
    direccion: document.getElementById('direccion').value,
    celular: document.getElementById('celular').value,
    cod_ciudad: ciudadSelect.value ? parseInt(ciudadSelect.value): null,
    cod_pais: paisSelect.value ? parseInt(paisSelect.value): null
  };
}

async function editar(nroID){
  const res = await fetch(`${API}/clientes`);
  const clientes = await res.json();
  const c = clientes.find(x => x.nroID === nroID);
  if(!c) return alert('Cliente no encontrado');
  document.getElementById('tipoID').value = c.tipoID || 'CC';
  document.getElementById('nroID').value = c.nroID;
  document.getElementById('nombres').value = c.nombres;
  document.getElementById('correo').value = c.correo;
  document.getElementById('direccion').value = c.direccion;
  document.getElementById('celular').value = c.celular;
  // set pais y cargar ciudades
  if(c.cod_pais){
    paisSelect.value = c.cod_pais;
    const ev = new Event('change');
    paisSelect.dispatchEvent(ev);
    // esperar 200ms para que se llenen las ciudades
    setTimeout(()=>{
      ciudadSelect.value = c.cod_ciudad || '';
    }, 300);
  }
  editando = true;
  editarNroID = nroID;
  document.getElementById('nroID').disabled = true;
  btnGuardar.style.display = 'none';
  btnActualizar.style.display = 'inline-block';
}

async function actualizarCliente(){
  const data = formToObject();
  const res = await fetch(`${API}/clientes/${editarNroID}`, {
    method:'PUT',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(data)
  });
  if(res.ok){
    form.reset();
    editando = false;
    editarNroID = null;
    document.getElementById('nroID').disabled = false;
    btnGuardar.style.display = 'inline-block';
    btnActualizar.style.display = 'none';
    await listarClientes();
  } else alert('Error al actualizar');
}

btnCancelar.addEventListener('click', ()=>{
  form.reset();
  editando=false;
  editarNroID=null;
  document.getElementById('nroID').disabled=false;
  btnGuardar.style.display='inline-block';
  btnActualizar.style.display='none';
});

async function borrar(nroID){
  if(!confirm('Eliminar cliente ' + nroID + '?')) return;
  const res = await fetch(`${API}/clientes/${nroID}`, { method:'DELETE' });
  if(res.ok) await listarClientes();
  else alert('Error al eliminar');
}

/* ---------------- Mapa (Leaflet) ---------------- */
let map;
let marker;
function initMap(){
  map = L.map('map').setView([4.6, -74.07], 5); // centro aproximado Colombia
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution:'&copy; OpenStreetMap contributors'
  }).addTo(map);
}

async function fetchCountryCoords(countryName){
  // consulta a Nominatim para geocoding (limit simple)
  try{
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(countryName)}`;
    const r = await fetch(url);
    const data = await r.json();
    if(data && data.length){
      const lat = parseFloat(data[0].lat);
      const lon = parseFloat(data[0].lon);
      map.setView([lat, lon], 5);
      if(marker) marker.remove();
      marker = L.marker([lat, lon]).addTo(map).bindPopup(`<b>${countryName}</b>`).openPopup();
    }
  }catch(e){
    console.error('Geocoding error', e);
  }
}
