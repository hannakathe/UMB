class Nota:
    def __init__(self, tipo_evaluacion, ponderacion, nota_obtenida):
        self.tipo_evaluacion = tipo_evaluacion
        self.ponderacion = ponderacion
        self.nota_obtenida = nota_obtenida

class Estudiante:
    def __init__(self, nombre, codigo, materia):
        self.nombre = nombre
        self.codigo = codigo
        self.materia = materia
        self.notas = []

    def agregar_nota(self, nota):
        self.notas.append(nota)

    def calcular_promedio(self):
        total_puntos = 0
        total_ponderacion = 0
        for nota in self.notas:
            total_puntos += nota.nota_obtenida * nota.ponderacion
            total_ponderacion += nota.ponderacion
        if total_ponderacion == 0:
            return 0
        return total_puntos / total_ponderacion

def mostrar_menu_principal():
    print("------ Bienvenido al sistema de gestión de notas ------")
    print("")
    print("1. Agregar estudiante")
    print("2. Agregar nota a estudiante")
    print("3. Calcular promedio de estudiante")
    print("4. Mostrar notas del estudiante")
    print("5. Salir")
    print("")
    print("----------------------------------------------------------")
    opcion = input("Seleccione una opción: ")
    return opcion

def agregar_estudiante():
    nombre = input("Ingrese el nombre del estudiante: ")
    codigo = input("Ingrese el código del estudiante: ")
    materia = input("Ingrese la materia: ")
    estudiantes.append(Estudiante(nombre, codigo, materia))
    print("Estudiante agregado con éxito")

def agregar_nota_estudiante():
    codigo = input("Ingrese el código del estudiante: ")
    estudiante_encontrado = None
    for estudiante in estudiantes:
        if estudiante.codigo == codigo:
            estudiante_encontrado = estudiante
            break
    if estudiante_encontrado:
        porcentaje_total = 0
        while porcentaje_total < 100:
            tipo_evaluacion_opcion = mostrar_submenu_tipo_evaluacion()
            tipo_evaluacion = ""
            if tipo_evaluacion_opcion == "1":
                tipo_evaluacion = "Talleres o laboratorios"
            elif tipo_evaluacion_opcion == "2":
                tipo_evaluacion = "Tareas"
            elif tipo_evaluacion_opcion == "3":
                tipo_evaluacion = "Exámenes o quizes"
            elif tipo_evaluacion_opcion == "4":
                tipo_evaluacion = "Parciales"
            else:
                print("Opción no válida")
                return

            cantidad_notas = int(input("Ingrese cuántas notas desea ingresar: "))
            ponderacion = float(input("Ingrese el porcentaje de ponderación (0-100): "))
            if 0 <= ponderacion <= 100:
                for _ in range(cantidad_notas):
                    nota_obtenida = float(input("Ingrese la nota obtenida (0-5.0): "))
                    if 0 <= nota_obtenida <= 5.0:
                        nota = Nota(tipo_evaluacion, ponderacion / 100, nota_obtenida)
                        estudiante_encontrado.agregar_nota(nota)
                        porcentaje_total += ponderacion
                    else:
                        print("Error: La nota obtenida debe estar entre 0 y 5.0")
                        return
            else:
                print("Error: El porcentaje de ponderación debe estar entre 0 y 100")
                return

    
def mostrar_submenu_tipo_evaluacion():
    print("-------Seleccione el tipo de evaluación-------")
    print("")
    print("1. Talleres o laboratorios")
    print("2. Tareas")
    print("3. Exámenes o quizes")
    print("4. Parciales")
    print("")
    print("-----------------------------------------------")
    opcion = input("Ingrese el número correspondiente a la opción: ")
    return opcion

def mostrar_informacion_notas():
    codigo = input("Ingrese el código del estudiante: ")
    estudiante_encontrado = None
    for estudiante in estudiantes:
        if estudiante.codigo == codigo:
            estudiante_encontrado = estudiante
            break
    if estudiante_encontrado:
        print("---------Información del estudiante---------")
        print("")
        print(f"Nombre: {estudiante_encontrado.nombre}")
        print(f"Código: {estudiante_encontrado.codigo}")
        print(f"Materia: {estudiante_encontrado.materia}")
        print("")
        print("-------------------- Notas --------------------")
        for nota in estudiante_encontrado.notas:
            print("")
            print(f"- Tipo de evaluación: {nota.tipo_evaluacion}")
            print(f"  Ponderación: {nota.ponderacion * 100}%")
            print(f"  Nota obtenida: {nota.nota_obtenida}")
            print("")
            print("-----------------------------------------------")
        promedio_final = estudiante_encontrado.calcular_promedio()
        print("")
        print(f"Promedio final: {promedio_final:.2f}")
        print("")
        print("-----------------------------------------------")

        if promedio_final < 3.0:
            print("El estudiante ha perdido la materia.")
        else:
            print("El estudiante ha aprobado la materia.")

estudiantes = []

def calcular_promedio_estudiante():
    codigo = input("Ingrese el código del estudiante: ")
    estudiante_encontrado = None
    for estudiante in estudiantes:
        if estudiante.codigo == codigo:
            estudiante_encontrado = estudiante
            break
    if estudiante_encontrado:
        promedio = estudiante_encontrado.calcular_promedio()
        print(f"El promedio del estudiante {estudiante_encontrado.nombre} es: {promedio:.2f}")
    else:
        print("Error: Estudiante no encontrado")

while True:
    opcion = mostrar_menu_principal()

    if opcion == "1":
        agregar_estudiante()
    elif opcion == "2":
        agregar_nota_estudiante()
    elif opcion == "3":
        calcular_promedio_estudiante()
    elif opcion == "4":
        mostrar_informacion_notas()
    elif opcion == "5":
        print("Saliendo del sistema... ")
        break