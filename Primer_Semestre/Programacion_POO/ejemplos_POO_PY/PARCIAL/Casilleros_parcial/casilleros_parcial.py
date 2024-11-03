
#! TENER EN CUENTA

#! TENER EN CUENTA YA QUE PYTHON NO CUENTA CON UN MODULO SWITCH
#! EN ESTE CASO SE DEBE USAR EL METODO MATCH-CASE PERO ESTE SOLO ESTA
#! DISPONIBLE EN VERSIONES DE PYTHON COMO LA 3.12.2 O SUPERIORES


# tipos de prioridades :
# discapacidad
# Estudiante de medicina o ingenieria


"""PARCIAL:

ASIGNAR PRIORIZACION DE CASILLEROS, POR EJEMPLO POR EDADES, CURSOS ESPECIFICOS O PRIORIDADES PARTICULARES
GENERAR FUNCION CON ALERTAS EN LOS QUE INDIQUEN ALERTAS EN EL ALQUILER DE LOS CASILLEROS O CUALQUIER ALERTA CONSIDERADA NECESARIA"""

class Casillero:
    def __init__(self, numero, estado='disponible', codigo_estudiante=None, nombre_estudiante=None, piso=None):
        self.numero = numero
        self.estado = estado
        self.codigo_estudiante = codigo_estudiante
        self.nombre_estudiante = nombre_estudiante
        self.piso = piso

    def alquilar(self, codigo_estudiante, nombre_estudiante):
        if self.estado == 'disponible':
            self.estado = 'ocupado'
            self.codigo_estudiante = codigo_estudiante
            self.nombre_estudiante = nombre_estudiante
            print(f"Casillero {self.numero} alquilado a {self.nombre_estudiante}")
        else:
            print(f"Casillero {self.numero} ya está ocupado")

    def devolver(self):
        if self.estado == 'ocupado':
            self.estado = 'disponible'
            self.codigo_estudiante = None
            self.nombre_estudiante = None
            print(f"Casillero {self.numero} devuelto")
        else:
            print(f"Casillero {self.numero} ya está disponible")

    def __str__(self):
        ocupante = self.nombre_estudiante if self.estado == 'ocupado' else 'Ninguno'
        return f"Casillero {self.numero}   |   Piso {self.piso}   |   Estado: {self.estado}   |   Ocupante: {ocupante}"


class Inventario:
    def __init__(self):
        self.casilleros = []
        self.agregar_casilleros()

    def agregar_casilleros(self):
        total_casilleros = 150 * 7  # 150 casilleros por piso, 7 pisos
        for numero_casillero in range(1, total_casilleros + 1):
            piso = (numero_casillero - 1) // 150 + 1
            casillero = Casillero(numero_casillero, piso=piso)
            self.casilleros.append(casillero)

    def buscar_por_estado(self, estado):
        return [casillero for casillero in self.casilleros if casillero.estado == estado]

    def buscar_casillero(self, criterio, valor):
        criterio = criterio.lower()
        if criterio == '1':
            return next((casillero for casillero in self.casilleros if casillero.numero == int(valor)), None)
        elif criterio == '2':
            return next((casillero for casillero in self.casilleros if casillero.estado == 'ocupado' and casillero.nombre_estudiante == valor), None)
        elif criterio == '3':
            return self.buscar_por_estado(valor)
        elif criterio == '4':
            return [casillero for casillero in self.casilleros if casillero.piso == int(valor)]
        return None

    def mostrar_inventario(self):
        print("Inventario de casilleros:")
        for casillero in self.casilleros:
            print(casillero)


class Main:
    def __init__(self):
        self.inventario = Inventario()

    def mostrar_menu(self):
        while True:
            print("---------- Menú ----------")
            print("")
            print("1. Alquilar casillero")
            print("2. Devolver casillero")
            print("3. Buscar casillero")
            print("4. Mostrar inventario")
            print("5. Salir")
            print("--------------------------")
            print("")
            opcion = input("Seleccione una opción: ")
            match opcion:
                case '1':
                    print(''' Seleccione el tipo de estudiante: 
                    
                    1. Con discapacidad (Visual, auditiva, silla de ruedas, entre otras)
                    2. Estudiante de ingenieria
                    3. Estudiante de medicina 
                    3. Otros ''')
                    print("")
                    prioridad= input('Ingrese el numero correspondiente al tipo de estudiante: ')
                    
                    match prioridad:

                        case '1':
                            print('''
                            Por motivos de seguridad unicamente se asignaran casilleros del primer o segundo piso 
                            y de los niveles bajos.

                            Tener en cuenta: 

                            Primer piso: 1-50
                            Segundo piso: 151-200''')
                            print("")
                            codigo_estudiante = input("Ingrese el código del estudiante: ")
                            nombre_estudiante = input("Ingrese el nombre del estudiante: ")
                            numero_casillero = int(input("Ingrese el número del casillero a alquilar tener en cuenta los parametros anteriores: "))
                            casillero = self.inventario.buscar_casillero('1', numero_casillero)
                            if casillero is not None:
                                if casillero>=1 and casillero<=50:
                                    if casillero>= 151 and casillero<=200:
                                        if casillero.alquilar(codigo_estudiante, nombre_estudiante):
                                            print("Alquiler realizado con éxito.")
                                        else:
                                            print("No se pudo realizar el alquiler.")
                            else:
                                print("No se encontró ningún casillero con ese número.")

                        case '2' :
                            print('''
                            Por motivos de comodidad los estudiantes de ingenieria tienen acceso a los casilleros 
                            de los pisos 4 - 5, debido a la ubicacion de los laboratorios de ingenieria.
                            
                            Tener en cuenta: 
    
                            Cuarto piso: 600 - 750
                            Quinto piso: 751 - 900''')
                            print("")
                            codigo_estudiante = input("Ingrese el código del estudiante: ")
                            nombre_estudiante = input("Ingrese el nombre del estudiante: ")
                            numero_casillero = int(input("Ingrese el número del casillero a alquilar tener en cuenta los parametros anteriores: "))
                            casillero = self.inventario.buscar_casillero('1', numero_casillero)
                            if casillero is not None:
                                if casillero>=600 and casillero<=900:
                                    if casillero.alquilar(codigo_estudiante, nombre_estudiante):
                                        print("Alquiler realizado con éxito.")
                                    else:
                                            print("No se pudo realizar el alquiler.")
                            else:
                                print("No se encontró ningún casillero con ese número.")

                        case '3':
                            print('''
                            Por motivos de comodidad los estudiantes de medicina tienen acceso a los casilleros 
                            de los pisos 6 - 7, debido a la ubicacion de los laboratorios y consultorios de medicina.

                            Tener en cuenta: 

                            Sexto piso: 901 - 1050
                            Septimo piso: 1051 - 1200''')
                            print("")
                            codigo_estudiante = input("Ingrese el código del estudiante: ")
                            nombre_estudiante = input("Ingrese el nombre del estudiante: ")
                            numero_casillero = int(input("Ingrese el número del casillero a alquilar tener en cuenta los parametros anteriores: "))
                            casillero = self.inventario.buscar_casillero('1', numero_casillero)
                            if casillero is not None:
                                if casillero>=901 and casillero<=1200:
                                    if casillero.alquilar(codigo_estudiante, nombre_estudiante):
                                        print("Alquiler realizado con éxito.")
                                    else:
                                        print("No se pudo realizar el alquiler.")
                            else:
                                print("No se encontró ningún casillero con ese número.")

                        case '4':
                            print('''
                            Los estudiantes tienen accesos a todos los casilleros menos los siguientes: 

                            Tener en cuenta no se pueden pedir los siguientes casilleros:

                            Primer piso: 1-50
                            Segundo piso: 151-200
                            Cuarto piso: 600 - 750
                            Quinto piso: 751 -900
                            Sexto piso: 901 - 1050
                            Septimo piso: 1051 - 1200''')
                            print("")
                            codigo_estudiante = input("Ingrese el código del estudiante: ")
                            nombre_estudiante = input("Ingrese el nombre del estudiante: ")
                            numero_casillero = int(input("Ingrese el número del casillero a alquilar tener en cuenta los parametros anteriores: "))
                            casillero = self.inventario.buscar_casillero('1', numero_casillero)
                            if casillero is not None:
                                if casillero>=51 and casillero<=150:
                                    if casillero>= 200 and casillero<=559:
                                        if casillero.alquilar(codigo_estudiante, nombre_estudiante):
                                            print("Alquiler realizado con éxito.")
                                        else:
                                            print("No se pudo realizar el alquiler.")
                            else:
                                print("No se encontró ningún casillero con ese número.")
                    

                case '2':
                    numero_casillero = int(input("Ingrese el número del casillero a devolver: "))
                    meses_prestado=input("Ingrese la cantidad de meses que se le presto el casillero: ")
                    casillero = self.inventario.buscar_casillero('1', numero_casillero)
                    if meses_prestado<5:
                        if casillero is not None:
                            if casillero.devolver():
                                print("Devolución realizada con éxito.")
                            else:
                                print("No se pudo realizar la devolución.")
                        if meses_prestado>5:
                            if casillero is not None:
                                if casillero.devolver():
                                    print("Devolución realizada con éxito.")
                                    print("¡ Se le aplicara una multa por no devolver el casillero a tiempo !")
                    else:
                        print("No se encontró ningún casillero con ese número.")

                case '3':
                    print(" ----------- Opciones de búsqueda -----------")
                    print("")
                    print("1. Por número de casillero")
                    print("2. Por nombre de ocupante (si está ocupado)")
                    print("3. Por estado (ocupado o desocupado)")
                    print("4. Por piso")
                    print("---------------------------------------------")
                    print("")
                    opcion_busqueda = input("Seleccione una opción de búsqueda: ")
                    match opcion_busqueda:
                        case '3':
                            print(" --- Submenú para búsqueda por estado ---")
                            print("---------------------------------------------")
                            print("1. Casilleros ocupados")
                            print("2. Casilleros desocupados")
                            print("---------------------------------------------")
                            print("")
                            opcion_estado = input("Seleccione una opción de estado: ")
                            match opcion_estado:
                                case '1':
                                    casilleros_encontrados = self.inventario.buscar_por_estado('ocupado')
                                case '2':
                                    casilleros_encontrados = self.inventario.buscar_por_estado('disponible')
                                case _:
                                    print("Opción de estado no válida.")
                                    continue
                            if casilleros_encontrados:
                                print("\nCasilleros encontrados:")
                                for casillero in casilleros_encontrados:
                                    print(casillero)
                            else:
                                print("\nNo se encontraron casilleros con ese estado.")
                        case _:
                            valor_busqueda = input("Ingrese el valor a buscar: ")
                            casilleros_encontrados = self.inventario.buscar_casillero(opcion_busqueda, valor_busqueda)
                            if casilleros_encontrados is not None:
                                if isinstance(casilleros_encontrados, list):
                                    print("\nCasilleros encontrados:")
                                    for casillero in casilleros_encontrados:
                                        print(casillero)
                                else:
                                    print("\nCasillero encontrado:")
                                    print(casilleros_encontrados)
                            else:
                                print("\nNo se encontró ningún casillero con esos criterios de búsqueda.")
                case '4':
                    self.inventario.mostrar_inventario()
                case '5':
                    print("Saliendo del programa...")
                    break
                case _:
                    print("Opción no válida. Intente de nuevo.")


main = Main()
main.mostrar_menu()
