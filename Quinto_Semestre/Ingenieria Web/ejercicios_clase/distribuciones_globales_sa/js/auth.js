'use strict';
// auth y manejo de datos en localStorage simulando backend
const DB = {
  get: (key, def = null) => {
    const d = localStorage.getItem(key);
    return d ? JSON.parse(d) : def;
  },
  set: (key, val) => localStorage.setItem(key, JSON.stringify(val)),
  del: (key)       => localStorage.removeItem(key),

  getSession:   ()  => DB.get('dg_session'),
  setSession:   (s) => DB.set('dg_session', s),
  clearSession: ()  => DB.del('dg_session'),

  getUsers:    ()  => DB.get('dg_users', []),
  setUsers:    (u) => DB.set('dg_users', u),

  getProductos:    ()  => DB.get('dg_productos', []),
  setProductos:    (p) => DB.set('dg_productos', p),

  getMovimientos:  ()  => DB.get('dg_movimientos', []),
  setMovimientos:  (m) => DB.set('dg_movimientos', m),
};

// datos iniciales de prueba (solo se crean si no hay datos en localStorage)
function initData() {
  if (!localStorage.getItem('dg_users')) {
    DB.setUsers([
      {
        id: 1,
        nombre: 'Administrador',
        email: 'admin@distribuciones.com',
        password: 'admin123',
        rol: 'admin',
        activo: true,
        fechaCreacion: new Date().toISOString(),
      },
      {
        id: 2,
        nombre: 'Juan Pérez',
        email: 'empleado@distribuciones.com',
        password: 'emp123',
        rol: 'empleado',
        activo: true,
        fechaCreacion: new Date().toISOString(),
      },
    ]);
  }

  if (!localStorage.getItem('dg_productos')) {
    DB.setProductos([
      { id: 1, nombre: 'Laptop HP EliteBook',  categoria: 'Tecnología',  descripcion: 'Laptop empresarial i7',      precio: 2500000, stock: 15, stockMin: 5,  fechaCreacion: new Date().toISOString() },
      { id: 2, nombre: 'Monitor Samsung 24"',  categoria: 'Tecnología',  descripcion: 'Monitor Full HD 1080p',      precio:  800000, stock:  8, stockMin: 3,  fechaCreacion: new Date().toISOString() },
      { id: 3, nombre: 'Silla Ergonómica',     categoria: 'Mobiliario',  descripcion: 'Silla de oficina lumbar',    precio:  450000, stock: 20, stockMin: 5,  fechaCreacion: new Date().toISOString() },
      { id: 4, nombre: 'Papel A4 (Resma)',      categoria: 'Papelería',   descripcion: 'Resma 500 hojas bond 75g',  precio:   15000, stock:  3, stockMin: 10, fechaCreacion: new Date().toISOString() },
      { id: 5, nombre: 'Tóner Laser Negro',    categoria: 'Papelería',   descripcion: 'Tóner compatible HP/Canon',  precio:  120000, stock:  6, stockMin: 5,  fechaCreacion: new Date().toISOString() },
      { id: 6, nombre: 'Escritorio Ejecutivo', categoria: 'Mobiliario',  descripcion: 'Escritorio en L 160cm',      precio:  890000, stock:  4, stockMin: 2,  fechaCreacion: new Date().toISOString() },
    ]);
  }

  if (!localStorage.getItem('dg_movimientos')) {
    const hoy = new Date();
    const ayer = new Date(Date.now() - 86400000);

    DB.setMovimientos([
      { id: 1, productoId: 1, productoNombre: 'Laptop HP EliteBook',  tipo: 'entrada',    cantidad: 10, motivo: 'Compra inicial',     usuario: 'Administrador', fecha: ayer.toISOString() },
      { id: 2, productoId: 3, productoNombre: 'Silla Ergonómica',     tipo: 'salida',     cantidad:  2, motivo: 'Dotación oficina 1',  usuario: 'Juan Pérez',    fecha: ayer.toISOString() },
      { id: 3, productoId: 4, productoNombre: 'Papel A4 (Resma)',      tipo: 'entrada',    cantidad:  5, motivo: 'Reposición stock',    usuario: 'Administrador', fecha: hoy.toISOString() },
      { id: 4, productoId: 2, productoNombre: 'Monitor Samsung 24"',  tipo: 'devolucion', cantidad:  1, motivo: 'Equipo defectuoso',   usuario: 'Juan Pérez',    fecha: hoy.toISOString() },
    ]);
  }
}

// login page
(function loginPage() {
  const form = document.getElementById('loginForm');
  if (!form) return; // No estamos en la página de login

  initData();

  // Si ya hay sesión activa → redirigir al dashboard
  if (DB.getSession()) {
    window.location.href = 'dashboard.html';
    return;
  }

  lucide.createIcons();

  // mostrar/ocultar contraseña
  document.getElementById('togglePassword').addEventListener('click', function () {
    const input = document.getElementById('password');
    if (input.type === 'password') {
      input.type = 'text';
      this.innerHTML = '<i data-lucide="eye-off"></i>';
    } else {
      input.type = 'password';
      this.innerHTML = '<i data-lucide="eye"></i>';
    }
    lucide.createIcons();
  });

  // submit del formulario de login
  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (!validateLogin()) return;

    const email    = document.getElementById('email').value.trim().toLowerCase();
    const password = document.getElementById('password').value;

    const users = DB.getUsers();
    const user  = users.find(u => u.email.toLowerCase() === email && u.password === password);

    if (!user) {
      showLoginError('Correo o contraseña incorrectos. Intenta de nuevo.');
      return;
    }

    if (!user.activo) {
      showLoginError('Tu cuenta está desactivada. Contacta al administrador.');
      return;
    }

    // guardar sesion y redirigir al dashboard
    DB.setSession({ id: user.id, nombre: user.nombre, email: user.email, rol: user.rol });
    window.location.href = 'dashboard.html';
  });

  // validar formulario de login
  function validateLogin() {
    let valid = true;

    const email    = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    clearErrors();

    // validar email
    if (!email) {
      setError('email', 'El correo es requerido.');
      valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError('email', 'Ingresa un correo válido.');
      valid = false;
    }

    // validar contraseña
    if (!password) {
      setError('password', 'La contraseña es requerida.');
      valid = false;
    } else if (password.length < 3) {
      setError('password', 'La contraseña debe tener al menos 3 caracteres.');
      valid = false;
    }

    return valid;
  }

  function setError(field, msg) {
    document.getElementById(field).classList.add('is-error');
    document.getElementById(field + 'Error').textContent = msg;
  }

  function clearErrors() {
    ['email', 'password'].forEach(f => {
      document.getElementById(f).classList.remove('is-error');
      document.getElementById(f + 'Error').textContent = '';
    });
    document.getElementById('loginError').style.display = 'none';
  }

  function showLoginError(msg) {
    const el = document.getElementById('loginError');
    document.getElementById('loginErrorMsg').textContent = msg;
    el.style.display = 'flex';
  }
})();

// verificar sesion en dashboard y manejar logout
(function dashboardCheck() {
  // Solo aplica en dashboard.html
  if (!document.getElementById('btnLogout')) return;

  initData();

  const session = DB.getSession();
  if (!session) {
    window.location.href = 'index.html';
    return;
  }

  // Cerrar sesion
  document.getElementById('btnLogout').addEventListener('click', function () {
    DB.clearSession();
    window.location.href = 'index.html';
  });
})();
