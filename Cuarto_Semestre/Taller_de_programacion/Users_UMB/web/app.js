const form = document.getElementById('clienteForm');
const tbody = document.querySelector('#tablaClientes tbody');
let clientes = [];

form.addEventListener('submit', e => {
  e.preventDefault();
  const nombre = document.getElementById('nombres').value;
  const correo = document.getElementById('correo').value;
  const direccion = document.getElementById('direccion').value;
  const celular = document.getElementById('celular').value;

  clientes.push({ nombre, correo, direccion, celular });
  renderClientes();
  form.reset();
});

function renderClientes() {
  tbody.innerHTML = '';
  clientes.forEach((c, i) => {
    const fila = `
      <tr>
        <td>${c.nombre}</td>
        <td>${c.correo}</td>
        <td>${c.celular}</td>
        <td><button onclick="eliminar(${i})">Eliminar</button></td>
      </tr>
    `;
    tbody.innerHTML += fila;
  });
}

function eliminar(index) {
  clientes.splice(index, 1);
  renderClientes();
}
