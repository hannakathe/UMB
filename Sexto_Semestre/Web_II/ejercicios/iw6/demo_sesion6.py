"""
DEMO SESION 6 - Ingenieria Web II (UMB)
Comparacion de una consulta vulnerable a SQL Injection frente a una
consulta parametrizada segura, mas un ejemplo de escape de HTML para
prevenir XSS reflejado.

Ejecutar:  python demo_sesion6.py
"""

import sqlite3
import html

# Base de datos en memoria solo para la demostracion
conexion = sqlite3.connect(":memory:")
conexion.execute("CREATE TABLE usuarios (usuario TEXT, clave TEXT)")
conexion.execute("INSERT INTO usuarios VALUES ('admin', 'secreta123')")
conexion.commit()


def login_vulnerable(usuario, clave):
    """NUNCA HACER ESTO: la concatenacion directa permite SQL Injection."""
    consulta = (
        f"SELECT * FROM usuarios "
        f"WHERE usuario = '{usuario}' AND clave = '{clave}'"
    )
    return conexion.execute(consulta).fetchall()


def login_seguro(usuario, clave):
    """Consulta parametrizada: el driver escapa los valores automaticamente."""
    consulta = "SELECT * FROM usuarios WHERE usuario = ? AND clave = ?"
    return conexion.execute(consulta, (usuario, clave)).fetchall()


def escapar_para_html(texto_usuario):
    """Previene XSS: escapa caracteres especiales antes de renderizar en el navegador."""
    return html.escape(texto_usuario)


if __name__ == "__main__":
    usuario_atacante = "admin' --"

    print("Login vulnerable con payload malicioso:",
          login_vulnerable(usuario_atacante, "cualquier_cosa"))
    print("Login seguro con el mismo payload:      ",
          login_seguro(usuario_atacante, "cualquier_cosa"))

    comentario_malicioso = "<script>alert('XSS')</script>"
    print("Comentario escapado de forma segura:    ",
          escapar_para_html(comentario_malicioso))
