import sys

class Curso:
    def __init__(self, nombre, duracion, precio_base, descripcion):
        self.nombre = nombre
        self.duracion = duracion
        self.precio_base = precio_base
        self.descripcion = descripcion
        self.estudiantes = []

    def inscribir_estudiante(self, estudiante):
        self.estudiantes.append(estudiante)

    def dar_info(self):
        return f"""
                Curso: {self.nombre}
                Duración: {self.duracion} meses
                Precio: COP {self.precio():.2f}
                Descripción: {self.descripcion}"""
    
    def precio(self):
        if self.duracion <= 0:
            return "Por favor, ingresa un número válido de meses."
        
        descuento = 0
        if self.duracion >= 12:
            descuento = 0.2  # 20% de descuento para 12 meses o más
        elif self.duracion >= 6:
            descuento = 0.1  # 10% de descuento para 6 meses o más

        precio_total = self.precio_base * self.duracion * (1 - descuento)
        return precio_total

class Python(Curso):
    def __init__(self, duracion, nivel, proyecto):
        descripcion = "Aprende Python, un lenguaje de programación popular conocido por su simplicidad y versatilidad."
        super().__init__("Python", duracion, 150000.0, descripcion)
        self.nivel = nivel
        self.proyecto = proyecto

    def dar_info(self):
        return super().dar_info() + f", Nivel: {self.nivel}, Proyecto: {self.proyecto}"

class Java(Curso):
    def __init__(self, duracion, nivel, proyecto):
        descripcion = "Domina Java, un lenguaje de programación utilizado en una amplia gama de aplicaciones, desde aplicaciones de escritorio hasta sistemas empresariales."
        super().__init__("Java", duracion, 150000.0, descripcion)
        self.nivel = nivel
        self.proyecto = proyecto

    def dar_info(self):
        return super().dar_info() + f", Nivel: {self.nivel}, Proyecto: {self.proyecto}"

class CSharp(Curso):
    def __init__(self, duracion, nivel, proyecto):
        descripcion = "Explora C#, un lenguaje de programación desarrollado por Microsoft, ideal para el desarrollo de aplicaciones de escritorio y juegos."
        super().__init__("C#", duracion, 120000.0, descripcion)
        self.nivel = nivel
        self.proyecto = proyecto

    def dar_info(self):
        return super().dar_info() + f", Nivel: {self.nivel}, Proyecto: {self.proyecto}"

class JS(Curso):
    def __init__(self, duracion, nivel, proyecto):
        descripcion = "Aprende JavaScript es un lenguaje de programación ampliamente utilizado en el desarrollo web para agregar interactividad y dinamismo a las páginas web."
        super().__init__("JavaScript", duracion, 120000.0, descripcion)
        self.nivel = nivel
        self.proyecto = proyecto

    def dar_info(self):
        return super().dar_info() + f", Nivel: {self.nivel}, Proyecto: {self.proyecto}"

class HTML_CSS(Curso):
    def __init__(self, duracion, nivel, proyecto):
        descripcion = "Aprende HTML (HyperText Markup Language) y CSS (Cascading Style Sheets) son dos de los lenguajes fundamentales en el desarrollo web. HTML proporciona la estructura básica de una página web, mientras que CSS se encarga de dar estilo y diseño a esa estructura."
        super().__init__("HTML y CSS", duracion, 100000.0, descripcion)
        self.nivel = nivel
        self.proyecto = proyecto

    def dar_info(self):
        return super().dar_info() + f", Nivel: {self.nivel}, Proyecto: {self.proyecto}"

class Estudiante:
    def __init__(self, nombre, email):
        self.nombre = nombre
        self.email = email


def elegir_curso():
    
    curso_valido = False
    
    while not curso_valido:
        print("Seleccione el tipo de curso:")
        print("""
            1. Python
            2. Java
            3. C#
            4. JavaScript 
            5. HTML y CSS
            6. Salir
        """)
        opcion = input("Ingrese el número correspondiente al curso: ")
        duracion = int(input("Ingrese la duración del curso (en meses): "))

        if opcion == "1":
            nivel = input("Ingrese el nivel del curso (básico, intermedio, avanzado): ")
            proyecto= input("Ingresa tu objetivo al finalizar este curso: ")
            curso = Python(duracion, nivel, proyecto)
        elif opcion == "2":
            nivel = input("Ingrese el nivel del curso (básico, intermedio, avanzado): ")
            proyecto= input("Ingresa tu objetivo al finalizar este curso: ")
            curso = Java(duracion, nivel, proyecto)
        elif opcion == "3":
            nivel = input("Ingrese el nivel del curso (básico, intermedio, avanzado): ")
            proyecto= input("Ingresa tu objetivo al finalizar este curso: ")
            curso = CSharp(duracion, nivel, proyecto)
        elif opcion == "4":
            nivel = input("Ingrese el nivel del curso (básico, intermedio, avanzado): ")
            proyecto= input("Ingresa tu objetivo al finalizar este curso: ")
            curso = JS(duracion, nivel, proyecto)
        elif opcion == "5":
            nivel = input("Ingrese el nivel del curso (básico, intermedio, avanzado): ")
            proyecto= input("Ingresa tu objetivo al finalizar este curso: ")
            curso = HTML_CSS(duracion, nivel, proyecto)
        elif opcion== "6":
            print("Saliendo del sistema...")
            sys.exit()
        else:
            print("Opción no válida. Por favor, seleccione una opción válida.")
            return elegir_curso()

        return curso

def inter_usu():
    curso_elegido = elegir_curso()
    print("Detalles del curso seleccionado:")
    print(curso_elegido.dar_info())
    print("Quieres continuar con la inscripcion: ")
    print(""" 
        1. Si
        2. No""")

    choice = input("Ingresa tu eleccion: ")

    if choice == "1": 
        nombre_estudiante = input("Ingrese su nombre: ")
        email_estudiante = input("Ingrese su correo electrónico: ")

        estudiante = Estudiante(nombre_estudiante, email_estudiante)
        curso_elegido.inscribir_estudiante(estudiante)

        print(f"Felicitaciones, {nombre_estudiante}! Te has inscrito en el curso de {curso_elegido.nombre}.")

    elif choice == "2": 
        return elegir_curso()
    
    else:
        print("Opción no válida. Por favor, seleccione una opción válida.")
        return inter_usu()

if __name__ == "__main__":
    inter_usu()

