const express = require("express");
const logger = require("../middlewares/logger");
const auth = require("../middlewares/auth");

const router = express.Router();

let libros = [
  { id: 1, titulo: "Cien años de soledad", autor: "Gabriel García Márquez", anio: 1967 },
  { id: 2, titulo: "El Quijote", autor: "Miguel de Cervantes", anio: 1605 },
  { id: 3, titulo: "1984", autor: "George Orwell", anio: 1949 },
  { id: 4, titulo: "Rayuela", autor: "Julio Cortázar", anio: 1963 },
  { id: 5, titulo: "Fahrenheit 451", autor: "Ray Bradbury", anio: 1953 },
  { id: 6, titulo: "Crónica de una muerte anunciada", autor: "Gabriel García Márquez", anio: 1981 },
  { id: 7, titulo: "El principito", autor: "Antoine de Saint-Exupéry", anio: 1943 },
  { id: 8, titulo: "Ficciones", autor: "Jorge Luis Borges", anio: 1944 }
];
let nextId = libros.length + 1;

// GET /api/libros - lista paginada
router.get("/", logger, (req, res) => {
  const { page = 1, limit = 5 } = req.query;

  const pageNum = Number(page);
  const limitNum = Number(limit);

  if (
    !Number.isInteger(pageNum) ||
    !Number.isInteger(limitNum) ||
    pageNum < 1 ||
    limitNum < 1
  ) {
    return res.status(400).json({ error: "Parámetros de paginación inválidos" });
  }

  const total = libros.length;
  const totalPaginas = Math.ceil(total / limitNum) || 1;
  const start = (pageNum - 1) * limitNum;
  const data = libros.slice(start, start + limitNum);

  res.status(200).json({
    total,
    page: pageNum,
    limit: limitNum,
    totalPaginas,
    data
  });
});

// GET /api/libros/:id - obtener por id
router.get("/:id", logger, (req, res) => {
  const id = Number(req.params.id);
  const libro = libros.find((l) => l.id === id);

  if (!libro) {
    return res.status(404).json({ error: "Libro no encontrado" });
  }

  res.status(200).json(libro);
});

// POST /api/libros - crear libro (requiere autenticación)
router.post("/", auth, (req, res) => {
  const { titulo, autor, anio } = req.body || {};

  const detalles = [];
  if (!titulo) detalles.push("El campo 'titulo' es obligatorio");
  if (!autor) detalles.push("El campo 'autor' es obligatorio");

  if (detalles.length > 0) {
    return res.status(422).json({ error: "Datos inválidos", detalles });
  }

  const nuevoLibro = {
    id: nextId++,
    titulo,
    autor,
    anio: anio !== undefined ? anio : null
  };

  libros.push(nuevoLibro);
  res.status(201).json(nuevoLibro);
});

// PUT /api/libros/:id - actualizar libro
router.put("/:id", (req, res) => {
  const id = Number(req.params.id);
  const libro = libros.find((l) => l.id === id);

  if (!libro) {
    return res.status(404).json({ error: "Libro no encontrado" });
  }

  const { titulo, autor, anio } = req.body || {};

  const detalles = [];
  if (!titulo) detalles.push("El campo 'titulo' es obligatorio");
  if (!autor) detalles.push("El campo 'autor' es obligatorio");

  if (detalles.length > 0) {
    return res.status(422).json({ error: "Datos inválidos", detalles });
  }

  libro.titulo = titulo;
  libro.autor = autor;
  if (anio !== undefined) libro.anio = anio;

  res.status(200).json(libro);
});

// DELETE /api/libros/:id - eliminar libro
router.delete("/:id", (req, res) => {
  const id = Number(req.params.id);
  const index = libros.findIndex((l) => l.id === id);

  if (index === -1) {
    return res.status(404).json({ error: "Libro no encontrado" });
  }

  const [eliminado] = libros.splice(index, 1);
  res.status(200).json(eliminado);
});

module.exports = router;
