'use strict';
// logica de la aplicación


/** Renderiza los iconos Lucide en el DOM (tras contenido dinámico) */
const refreshIcons = () => typeof lucide !== 'undefined' && lucide.createIcons();

/** Icono Lucide como string HTML (para plantillas dinámicas) */
const icon = (name) => `<i data-lucide="${name}"></i>`;

/** COP moneda */
const formatCOP = (n) =>
  new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

/** Date format */
const formatFecha = (iso) => {
  const d = new Date(iso);
  return d.toLocaleDateString('es-CO', { day: '2-digit', month: '2-digit', year: 'numeric' })
    + ' ' + d.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
};

/** ID para simular DB nuevos elementos */
const nextId = (arr) => (arr.length === 0 ? 1 : Math.max(...arr.map(x => x.id)) + 1);

// indicador de tipo de movimiento pup up informativo
function toast(msg, type = 'success') {
  const icons = { success: 'check-circle', error: 'x-circle', info: 'info' };
  const container = document.getElementById('toastContainer');

  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.innerHTML = `<i data-lucide="${icons[type]}" class="toast-icon"></i><span class="toast-msg">${msg}</span>`;
  container.appendChild(el);
  refreshIcons();

  setTimeout(() => {
    el.style.animation = 'toastOut .3s ease forwards';
    setTimeout(() => el.remove(), 300);
  }, 3200);
}

// componente modal genérico
const Modal = {
  open(title, bodyHTML, footerHTML) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalBody').innerHTML   = bodyHTML;
    document.getElementById('modalFooter').innerHTML = footerHTML;
    document.getElementById('modalOverlay').classList.add('open');
  },
  close() {
    document.getElementById('modalOverlay').classList.remove('open');
  },
};

document.getElementById('modalClose').addEventListener('click',   Modal.close);
document.getElementById('modalOverlay').addEventListener('click', (e) => {
  if (e.target.id === 'modalOverlay') Modal.close();
});

// nav y gestión de secciones
const sectionMeta = {
  dashboard:    { title: 'Dashboard',          sub: 'Resumen general del sistema' },
  inventario:   { title: 'Inventario',         sub: 'Gestión de productos' },
  movimientos:  { title: 'Entradas / Salidas', sub: 'Registro de movimientos' },
  usuarios:     { title: 'Usuarios',           sub: 'Control de acceso' },
};

function showSection(name) {
  // ocultar todas las secciones
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

  // activar sección y nav correspondiente
  const sec = document.getElementById('sec-' + name);
  if (sec) sec.classList.add('active');

  const nav = document.querySelector(`.nav-item[data-section="${name}"]`);
  if (nav) nav.classList.add('active');

  // update topbar info según seccion
  const meta = sectionMeta[name] || { title: name, sub: '' };
  document.getElementById('topbarTitle').textContent = meta.title;
  document.getElementById('topbarSub').textContent   = meta.sub;

  // sidebar para moviles: cerrar al seleccionar
  document.getElementById('sidebar').classList.remove('open');

  // charge content 
  if (name === 'dashboard')   loadDashboard();
  if (name === 'inventario')  loadInventario();
  if (name === 'movimientos') loadMovimientos();
  if (name === 'usuarios')    loadUsuarios();
}

// control de navegacion
document.querySelectorAll('.nav-item[data-section]').forEach(item => {
  item.addEventListener('click', () => showSection(item.dataset.section));
});

// toggle sidebar en moviles
document.getElementById('menuToggle').addEventListener('click', () => {
  document.getElementById('sidebar').classList.toggle('open');
});

// datos de prueba iniciales (solo si no hay datos en localStorage)
(function init() {
  const session = DB.getSession();
  if (!session) return; // auth.js ya redirige

  // Mostrar info del usuario en sidebar
  document.getElementById('userName').textContent  = session.nombre;
  document.getElementById('userAvatar').textContent = session.nombre.charAt(0).toUpperCase();
  document.getElementById('topbarRole').textContent = session.rol === 'admin' ? 'Administrador' : 'Empleado';

  const rolLabel = session.rol === 'admin' ? 'Administrador' : 'Empleado';
  document.getElementById('userRole').textContent = rolLabel;

  // Ocultar seccion Usuarios si no es admin
  if (session.rol !== 'admin') {
    document.getElementById('navUsuarios').style.display   = 'none';
    document.getElementById('adminNavLabel').style.display = 'none';
  }

  showSection('dashboard');
  refreshIcons();
})();

// dashboard page
function loadDashboard() {
  const productos    = DB.getProductos();
  const movimientos  = DB.getMovimientos();
  const hoy          = new Date().toDateString();

  const totalStock   = productos.reduce((s, p) => s + p.stock, 0);
  const bajoStock    = productos.filter(p => p.stock <= p.stockMin);
  const movHoy       = movimientos.filter(m => new Date(m.fecha).toDateString() === hoy);

  document.getElementById('statProductos').textContent = productos.length;
  document.getElementById('statStock').textContent     = totalStock;
  document.getElementById('statBajoStock').textContent = bajoStock.length;
  document.getElementById('statMovHoy').textContent    = movHoy.length;

  // ultimos movimientos
  const recientes = [...movimientos].reverse().slice(0, 5);
  const tbodyMov  = document.getElementById('tbodyUltMov');

  if (recientes.length === 0) {
    tbodyMov.innerHTML = '<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--muted)">Sin movimientos</td></tr>';
  } else {
    tbodyMov.innerHTML = recientes.map(m => `
      <tr>
        <td>${m.productoNombre}</td>
        <td>${badgeTipo(m.tipo)}</td>
        <td><strong>${m.cantidad}</strong></td>
        <td style="font-size:12px;color:var(--muted)">${formatFecha(m.fecha)}</td>
      </tr>
    `).join('');
  }

  // stock bajo alerta
  const tbodyAlertas = document.getElementById('tbodyAlertas');
  if (bajoStock.length === 0) {
    tbodyAlertas.innerHTML = '<tr><td colspan="3" style="text-align:center;padding:24px;color:var(--muted)">Sin alertas de stock</td></tr>';
  } else {
    tbodyAlertas.innerHTML = bajoStock.map(p => {
      const pct   = Math.min(100, Math.round((p.stock / p.stockMin) * 100));
      const clase = p.stock === 0 ? 'danger' : p.stock < p.stockMin ? 'low' : 'good';
      return `
        <tr>
          <td>${p.nombre}</td>
          <td>
            <div class="stock-bar-wrap">
              <div class="stock-bar"><div class="stock-fill ${clase}" style="width:${pct}%"></div></div>
              <strong>${p.stock}</strong>
            </div>
          </td>
          <td>${p.stock === 0
            ? '<span class="badge badge-danger">Sin stock</span>'
            : '<span class="badge badge-warning">Bajo stock</span>'
          }</td>
        </tr>
      `;
    }).join('');
  }
  refreshIcons();
}

// inventario page
function loadInventario() {
  const session    = DB.getSession();
  const isAdmin    = session && session.rol === 'admin';
  const productos  = DB.getProductos();

  // filtrar categorias 
  const cats = [...new Set(productos.map(p => p.categoria))];
  const sel  = document.getElementById('filterCategoria');
  sel.innerHTML = '<option value="">Todas las categorías</option>'
    + cats.map(c => `<option value="${c}">${c}</option>`).join('');

  renderTablaInventario(productos, isAdmin);

  // btn producto nuevo solo para admin
  const btnNuevo = document.getElementById('btnNuevoProducto');
  btnNuevo.style.display = isAdmin ? '' : 'none';
  btnNuevo.onclick = () => modalProducto(null);

  // filtros
  document.getElementById('searchProducto').oninput  = aplicarFiltrosInv;
  document.getElementById('filterCategoria').onchange = aplicarFiltrosInv;
}

function aplicarFiltrosInv() {
  const q   = document.getElementById('searchProducto').value.toLowerCase();
  const cat = document.getElementById('filterCategoria').value;
  const session = DB.getSession();
  const isAdmin = session && session.rol === 'admin';

  const filtrados = DB.getProductos().filter(p =>
    (p.nombre.toLowerCase().includes(q) || p.categoria.toLowerCase().includes(q)) &&
    (!cat || p.categoria === cat)
  );
  renderTablaInventario(filtrados, isAdmin);
}

function renderTablaInventario(lista, isAdmin) {
  const tbody = document.getElementById('tbodyInventario');

  if (lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="8" style="text-align:center;padding:32px">
      <div class="empty-state"><div class="empty-icon"><i data-lucide="package"></i></div><p>No se encontraron productos</p></div>
    </td></tr>`;
    return;
  }

  tbody.innerHTML = lista.map((p, i) => {
    const estado  = p.stock === 0
      ? '<span class="badge badge-danger">Sin stock</span>'
      : p.stock <= p.stockMin
        ? '<span class="badge badge-warning">Bajo stock</span>'
        : '<span class="badge badge-success">OK</span>';

    const acciones = isAdmin ? `
      <div class="table-actions">
        <button class="btn btn-warning btn-sm" onclick="modalProducto(${p.id})">${icon('pencil')} Editar</button>
        <button class="btn btn-danger btn-sm"  onclick="confirmarEliminarProducto(${p.id})">${icon('trash-2')}</button>
      </div>` : '<span style="color:var(--muted);font-size:12px">—</span>';

    return `
      <tr>
        <td style="color:var(--text-muted)">${i + 1}</td>
        <td><strong>${p.nombre}</strong><br><small style="color:var(--text-muted)">${p.descripcion || ''}</small></td>
        <td><span class="badge badge-info">${p.categoria}</span></td>
        <td><strong>${p.stock}</strong></td>
        <td>${p.stockMin}</td>
        <td>${formatCOP(p.precio)}</td>
        <td>${estado}</td>
        <td>${acciones}</td>
      </tr>
    `;
  }).join('');
  refreshIcons();
}

// modal para crear/editar producto
function modalProducto(id) {
  const productos = DB.getProductos();
  const p         = id ? productos.find(x => x.id === id) : null;
  const titulo    = p ? 'Editar Producto' : 'Nuevo Producto';

  const body = `
    <form id="formProducto" novalidate>
      <div class="form-group">
        <label>Nombre del Producto *</label>
        <input type="text" id="pNombre" value="${p ? p.nombre : ''}" placeholder="Ej: Laptop HP ProBook">
        <span class="error-msg" id="errPNombre"></span>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Categoría *</label>
          <input type="text" id="pCategoria" value="${p ? p.categoria : ''}" placeholder="Ej: Tecnología">
          <span class="error-msg" id="errPCategoria"></span>
        </div>
        <div class="form-group">
          <label>Precio (COP) *</label>
          <input type="number" id="pPrecio" value="${p ? p.precio : ''}" placeholder="0" min="0">
          <span class="error-msg" id="errPPrecio"></span>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Stock ${p ? 'Actual' : 'Inicial'} *</label>
          <input type="number" id="pStock" value="${p ? p.stock : ''}" placeholder="0" min="0">
          <span class="error-msg" id="errPStock"></span>
        </div>
        <div class="form-group">
          <label>Stock Mínimo *</label>
          <input type="number" id="pStockMin" value="${p ? p.stockMin : ''}" placeholder="5" min="0">
          <span class="error-msg" id="errPStockMin"></span>
        </div>
      </div>
      <div class="form-group">
        <label>Descripción</label>
        <input type="text" id="pDesc" value="${p ? p.descripcion : ''}" placeholder="Descripción breve">
      </div>
    </form>
  `;

  const footer = `
    <button class="btn btn-ghost" onclick="Modal.close()">Cancelar</button>
    <button class="btn btn-primary" onclick="guardarProducto(${id || 'null'})">
      ${icon('save')} ${p ? 'Guardar Cambios' : 'Crear Producto'}
    </button>
  `;

  Modal.open(titulo, body, footer);
  refreshIcons();
}

function guardarProducto(id) {
  const nombre   = document.getElementById('pNombre').value.trim();
  const cat      = document.getElementById('pCategoria').value.trim();
  const precio   = parseFloat(document.getElementById('pPrecio').value);
  const stock    = parseInt(document.getElementById('pStock').value);
  const stockMin = parseInt(document.getElementById('pStockMin').value);
  const desc     = document.getElementById('pDesc').value.trim();

  // Validaciones
  let valid = true;

  const v = (field, errId, msg, cond) => {
    const el = document.getElementById(field);
    el.classList.toggle('is-error', !cond);
    document.getElementById(errId).textContent = cond ? '' : msg;
    if (!cond) valid = false;
  };

  v('pNombre',   'errPNombre',   'El nombre es requerido.',          nombre.length > 0);
  v('pCategoria','errPCategoria','La categoría es requerida.',       cat.length > 0);
  v('pPrecio',   'errPPrecio',   'Ingresa un precio válido (≥ 0).', !isNaN(precio) && precio >= 0);
  v('pStock',    'errPStock',    'Ingresa un stock válido (≥ 0).',  !isNaN(stock)  && stock >= 0);
  v('pStockMin', 'errPStockMin', 'Ingresa un mínimo válido (≥ 0).', !isNaN(stockMin) && stockMin >= 0);

  if (!valid) return;

  const productos = DB.getProductos();

  if (id) {
    const idx = productos.findIndex(p => p.id === id);
    if (idx !== -1) {
      productos[idx] = { ...productos[idx], nombre, categoria: cat, descripcion: desc, precio, stock, stockMin };
    }
    toast('Producto actualizado correctamente.');
  } else {
    productos.push({
      id: nextId(productos),
      nombre, categoria: cat, descripcion: desc,
      precio, stock, stockMin,
      fechaCreacion: new Date().toISOString(),
    });
    toast('Producto creado exitosamente.');
  }

  DB.setProductos(productos);
  Modal.close();
  loadInventario();
}

function confirmarEliminarProducto(id) {
  const p = DB.getProductos().find(x => x.id === id);
  if (!p) return;

  const body = `<p>¿Estás seguro de eliminar el producto <strong>"${p.nombre}"</strong>?</p>
    <p style="margin-top:8px;font-size:12px;color:var(--text-muted)">Esta acción no se puede deshacer.</p>`;

  const footer = `
    <button class="btn btn-ghost" onclick="Modal.close()">Cancelar</button>
    <button class="btn btn-danger" onclick="eliminarProducto(${id})">${icon('trash-2')} Eliminar</button>
  `;

  Modal.open('Eliminar Producto', body, footer);
  refreshIcons();
}

function eliminarProducto(id) {
  const productos = DB.getProductos().filter(p => p.id !== id);
  DB.setProductos(productos);
  Modal.close();
  loadInventario();
  toast('Producto eliminado.', 'info');
}

// movimientos page
function loadMovimientos() {
  const session  = DB.getSession();
  const productos = DB.getProductos();

  // seleccionar producto en el formulario de registro
  const selProd = document.getElementById('movProducto');
  selProd.innerHTML = '<option value="">— Selecciona un producto —</option>'
    + productos.map(p => `<option value="${p.id}">${p.nombre} (Stock: ${p.stock})</option>`).join('');

  // info de stock actual al seleccionar producto
  selProd.onchange = () => {
    const pid = parseInt(selProd.value);
    const p   = productos.find(x => x.id === pid);
    const info = document.getElementById('movStockInfo');
    if (p) {
      document.getElementById('movStockInfoTxt').textContent =
        `Stock actual: ${p.stock} unidades | Stock mínimo: ${p.stockMin}`;
      info.style.display = 'flex';
    } else {
      info.style.display = 'none';
    }
  };

  renderTablaMovimientos(DB.getMovimientos());

  // formulario registrar movimiento
  document.getElementById('formMovimiento').onsubmit = (e) => {
    e.preventDefault();
    registrarMovimiento(session);
  };

  // filtros
  document.getElementById('searchMov').oninput    = aplicarFiltrosMov;
  document.getElementById('filterTipoMov').onchange = aplicarFiltrosMov;
}

function aplicarFiltrosMov() {
  const q    = document.getElementById('searchMov').value.toLowerCase();
  const tipo = document.getElementById('filterTipoMov').value;

  const filtrados = DB.getMovimientos().filter(m =>
    (m.productoNombre.toLowerCase().includes(q) || m.motivo.toLowerCase().includes(q)) &&
    (!tipo || m.tipo === tipo)
  );
  renderTablaMovimientos(filtrados);
}

function renderTablaMovimientos(lista) {
  const tbody = document.getElementById('tbodyMovimientos');
  const ordenados = [...lista].reverse();

  if (ordenados.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;padding:32px">
      <div class="empty-state"><div class="empty-icon"><i data-lucide="arrow-left-right"></i></div><p>No hay movimientos</p></div>
    </td></tr>`;
    return;
  }

  tbody.innerHTML = ordenados.map((m, i) => `
    <tr>
      <td style="color:var(--text-muted)">${i + 1}</td>
      <td><strong>${m.productoNombre}</strong></td>
      <td>${badgeTipo(m.tipo)}</td>
      <td><strong>${m.cantidad}</strong></td>
      <td>${m.motivo}</td>
      <td style="font-size:12px">${m.usuario}</td>
      <td style="font-size:12px;color:var(--muted)">${formatFecha(m.fecha)}</td>
    </tr>
  `).join('');
  refreshIcons();
}

function registrarMovimiento(session) {
  const productoId = parseInt(document.getElementById('movProducto').value);
  const tipo       = document.getElementById('movTipo').value;
  const cantidad   = parseInt(document.getElementById('movCantidad').value);
  const motivo     = document.getElementById('movMotivo').value.trim();

  // validaciones
  let valid = true;
  const vMov = (field, errId, msg, cond) => {
    const el = document.getElementById(field);
    el.classList.toggle('is-error', !cond);
    document.getElementById(errId).textContent = cond ? '' : msg;
    if (!cond) valid = false;
  };

  vMov('movProducto', 'errMovProducto', 'Selecciona un producto.',           !!productoId);
  vMov('movTipo',     'errMovTipo',     'Selecciona el tipo de movimiento.', !!tipo);
  vMov('movCantidad', 'errMovCantidad', 'Ingresa una cantidad mayor a 0.',   !isNaN(cantidad) && cantidad > 0);
  vMov('movMotivo',   'errMovMotivo',   'El motivo es requerido.',           motivo.length > 0);

  if (!valid) return;

  const productos = DB.getProductos();
  const idx       = productos.findIndex(p => p.id === productoId);
  if (idx === -1) { toast('Producto no encontrado.', 'error'); return; }

  // verificar stock suficiente para salidas
  if ((tipo === 'salida') && productos[idx].stock < cantidad) {
    document.getElementById('movCantidad').classList.add('is-error');
    document.getElementById('errMovCantidad').textContent =
      `Stock insuficiente. Disponible: ${productos[idx].stock} unidades.`;
    return;
  }

  // actualizar stock 
  if (tipo === 'entrada' || tipo === 'devolucion') {
    productos[idx].stock += cantidad;
  } else if (tipo === 'salida') {
    productos[idx].stock -= cantidad;
  }

  DB.setProductos(productos);

  // registrar movimiento
  const movimientos = DB.getMovimientos();
  movimientos.push({
    id: nextId(movimientos),
    productoId,
    productoNombre: productos[idx].nombre,
    tipo,
    cantidad,
    motivo,
    usuario: session.nombre,
    fecha: new Date().toISOString(),
  });
  DB.setMovimientos(movimientos);

  // reset formulario y actualizar tabla
  document.getElementById('formMovimiento').reset();
  document.getElementById('movStockInfo').style.display = 'none';
  ['movProducto','movTipo','movCantidad','movMotivo'].forEach(f => {
    document.getElementById(f).classList.remove('is-error');
    const errEl = document.getElementById('err' + f.charAt(0).toUpperCase() + f.slice(1));
    if (errEl) errEl.textContent = '';
  });

  renderTablaMovimientos(DB.getMovimientos());

  const tipoLabel = { entrada: 'Entrada', salida: 'Salida', devolucion: 'Devolución' }[tipo];
  toast(`${tipoLabel} registrada: ${cantidad} unidad(es) de "${productos[idx].nombre}".`);
}

// usuarios page (solo admin)
function loadUsuarios() {
  const session = DB.getSession();
  if (!session || session.rol !== 'admin') {
    document.getElementById('sec-usuarios').innerHTML =
      '<div class="alert alert-danger" style="margin-top:20px">Acceso restringido. Solo administradores.</div>';
    return;
  }

  renderTablaUsuarios(DB.getUsers());

  document.getElementById('btnNuevoUsuario').onclick = () => modalUsuario(null);
  document.getElementById('searchUsuario').oninput = () => {
    const q = document.getElementById('searchUsuario').value.toLowerCase();
    const filtrados = DB.getUsers().filter(u =>
      u.nombre.toLowerCase().includes(q) || u.email.toLowerCase().includes(q)
    );
    renderTablaUsuarios(filtrados);
  };
}

function renderTablaUsuarios(lista) {
  const session = DB.getSession();
  const tbody   = document.getElementById('tbodyUsuarios');

  if (lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;padding:32px">
      <div class="empty-state"><div class="empty-icon"><i data-lucide="users"></i></div><p>No se encontraron usuarios</p></div>
    </td></tr>`;
    return;
  }

  tbody.innerHTML = lista.map((u, i) => {
    const esYo    = u.id === session.id;
    const rolBadge = u.rol === 'admin'
      ? '<span class="badge badge-info">Administrador</span>'
      : '<span class="badge badge-gray">Empleado</span>';
    const estadoBadge = u.activo
      ? '<span class="badge badge-success">Activo</span>'
      : '<span class="badge badge-danger">Inactivo</span>';

    return `
      <tr>
        <td style="color:var(--muted)">${i + 1}</td>
        <td>
          <div style="display:flex;align-items:center;gap:8px">
            <div class="user-avatar" style="width:30px;height:30px;font-size:12px;">
              ${u.nombre.charAt(0).toUpperCase()}
            </div>
            <strong>${u.nombre}</strong> ${esYo ? '<span style="font-size:11px;color:var(--muted)">(tú)</span>' : ''}
          </div>
        </td>
        <td style="font-size:12px">${u.email}</td>
        <td>${rolBadge}</td>
        <td>${estadoBadge}</td>
        <td style="font-size:12px;color:var(--muted)">${new Date(u.fechaCreacion).toLocaleDateString('es-CO')}</td>
        <td>
          <div class="table-actions">
            <button class="btn btn-warning btn-sm" onclick="modalUsuario(${u.id})">${icon('pencil')} Editar</button>
            ${!esYo ? `<button class="btn btn-danger btn-sm" onclick="confirmarEliminarUsuario(${u.id})">${icon('trash-2')}</button>` : ''}
          </div>
        </td>
      </tr>
    `;
  }).join('');
  refreshIcons();
}

function modalUsuario(id) {
  const usuarios = DB.getUsers();
  const u        = id ? usuarios.find(x => x.id === id) : null;
  const titulo   = u ? 'Editar Usuario' : 'Nuevo Usuario';

  const body = `
    <form id="formUsuario" novalidate>
      <div class="form-group">
        <label>Nombre Completo *</label>
        <input type="text" id="uNombre" value="${u ? u.nombre : ''}" placeholder="Ej: María González">
        <span class="error-msg" id="errUNombre"></span>
      </div>
      <div class="form-group">
        <label>Correo Electrónico *</label>
        <input type="email" id="uEmail" value="${u ? u.email : ''}" placeholder="correo@empresa.com">
        <span class="error-msg" id="errUEmail"></span>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Contraseña ${u ? '(dejar vacío para no cambiar)' : '*'}</label>
          <input type="password" id="uPassword" placeholder="${u ? '••••••••' : 'Mínimo 4 caracteres'}">
          <span class="error-msg" id="errUPassword"></span>
        </div>
        <div class="form-group">
          <label>Rol *</label>
          <select id="uRol">
            <option value="empleado" ${u && u.rol === 'empleado' ? 'selected' : ''}>Empleado</option>
            <option value="admin"    ${u && u.rol === 'admin'    ? 'selected' : ''}>Administrador</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label>Estado</label>
        <select id="uActivo">
          <option value="1" ${!u || u.activo ? 'selected' : ''}>Activo</option>
          <option value="0" ${u && !u.activo ? 'selected' : ''}>Inactivo</option>
        </select>
      </div>
    </form>
  `;

  const footer = `
    <button class="btn btn-ghost" onclick="Modal.close()">Cancelar</button>
    <button class="btn btn-primary" onclick="guardarUsuario(${id || 'null'})">
      ${icon('save')} ${u ? 'Guardar Cambios' : 'Crear Usuario'}
    </button>
  `;

  Modal.open(titulo, body, footer);
  refreshIcons();
}

function guardarUsuario(id) {
  const nombre   = document.getElementById('uNombre').value.trim();
  const email    = document.getElementById('uEmail').value.trim().toLowerCase();
  const password = document.getElementById('uPassword').value;
  const rol      = document.getElementById('uRol').value;
  const activo   = document.getElementById('uActivo').value === '1';

  let valid = true;
  const vU = (field, errId, msg, cond) => {
    const el = document.getElementById(field);
    el.classList.toggle('is-error', !cond);
    document.getElementById(errId).textContent = cond ? '' : msg;
    if (!cond) valid = false;
  };

  vU('uNombre', 'errUNombre', 'El nombre es requerido.',    nombre.length > 0);
  vU('uEmail',  'errUEmail',  'Ingresa un correo válido.', /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email));

  if (!id) {
    vU('uPassword', 'errUPassword', 'La contraseña debe tener al menos 4 caracteres.', password.length >= 4);
  } else if (password.length > 0) {
    vU('uPassword', 'errUPassword', 'La contraseña debe tener al menos 4 caracteres.', password.length >= 4);
  }

  if (!valid) return;

  const usuarios = DB.getUsers();

  // Verificar email único
  const emailDuplicado = usuarios.some(u => u.email === email && u.id !== id);
  if (emailDuplicado) {
    document.getElementById('uEmail').classList.add('is-error');
    document.getElementById('errUEmail').textContent = 'Este correo ya está registrado.';
    return;
  }

  if (id) {
    const idx = usuarios.findIndex(u => u.id === id);
    if (idx !== -1) {
      usuarios[idx] = {
        ...usuarios[idx],
        nombre, email, rol, activo,
        ...(password ? { password } : {}),
      };
    }
    toast('Usuario actualizado correctamente.');
  } else {
    usuarios.push({
      id: nextId(usuarios),
      nombre, email, password, rol, activo,
      fechaCreacion: new Date().toISOString(),
    });
    toast('Usuario creado exitosamente.');
  }

  DB.setUsers(usuarios);
  Modal.close();
  loadUsuarios();
}

function confirmarEliminarUsuario(id) {
  const u = DB.getUsers().find(x => x.id === id);
  if (!u) return;

  const body = `<p>¿Estás seguro de eliminar al usuario <strong>"${u.nombre}"</strong>?</p>
    <p style="margin-top:8px;font-size:12px;color:var(--text-muted)">Esta acción no se puede deshacer.</p>`;

  const footer = `
    <button class="btn btn-ghost" onclick="Modal.close()">Cancelar</button>
    <button class="btn btn-danger" onclick="eliminarUsuario(${id})">${icon('trash-2')} Eliminar</button>
  `;

  Modal.open('Eliminar Usuario', body, footer);
  refreshIcons();
}

function eliminarUsuario(id) {
  DB.setUsers(DB.getUsers().filter(u => u.id !== id));
  Modal.close();
  loadUsuarios();
  toast('Usuario eliminado.', 'info');
}

// HELPERS
function badgeTipo(tipo) {
  const map = {
    entrada:    `<span class="badge badge-success">${icon('arrow-up')} Entrada</span>`,
    salida:     `<span class="badge badge-danger">${icon('arrow-down')} Salida</span>`,
    devolucion: `<span class="badge badge-warning">${icon('undo-2')} Devolución</span>`,
  };
  return map[tipo] || `<span class="badge badge-gray">${tipo}</span>`;
}
