import { useEffect, useState } from 'react'

const API_URL = 'http://localhost:3000/api/libros'
// URL inválida para forzar el estado de error durante las pruebas (evidencia 5).
const API_URL_ERROR = 'http://localhost:3000/api/no-existe'

function LibrosList() {
  const [libros, setLibros] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [forceError, setForceError] = useState(false)

  useEffect(() => {
    const url = forceError ? API_URL_ERROR : API_URL
    let cancelado = false

    setLoading(true)
    setError(null)
    setLibros(null)

    fetch(url)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`Error HTTP ${res.status}: ${res.statusText}`)
        }
        return res.json()
      })
      .then((data) => {
        if (!cancelado) setLibros(data.data)
      })
      .catch((err) => {
        if (!cancelado) setError(err.message || 'Error de conexión con la API')
      })
      .finally(() => {
        if (!cancelado) setLoading(false)
      })

    return () => {
      cancelado = true
    }
  }, [forceError])

  return (
    <section>
      <h1>Catálogo de libros</h1>
      <button type="button" onClick={() => setForceError((v) => !v)}>
        {forceError ? 'Usar endpoint correcto' : 'Forzar error (endpoint inválido)'}
      </button>

      {loading && <p>Cargando datos...</p>}

      {!loading && error && (
        <p style={{ color: 'red' }}>Error al obtener los libros: {error}</p>
      )}

      {!loading && !error && libros && (
        <ul>
          {libros.map((libro) => (
            <li key={libro.id}>
              {libro.titulo} — {libro.autor} ({libro.anio})
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}

export default LibrosList
