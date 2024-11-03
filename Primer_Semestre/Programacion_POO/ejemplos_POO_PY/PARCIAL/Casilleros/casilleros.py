
#! TENER EN CUENTA

#! TENER EN CUENTA YA QUE PYTHON NO CUENTA CON UN MODULO SWITCH
#! EN ESTE CASO SE DEBE USAR EL METODO MATCH-CASE PERO ESTE SOLO ESTA
#! DISPONIBLE EN VERSIONES DE PYTHON COMO LA 3.12.2 O SUPERIORES

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
                    codigo_estudiante = input("Ingrese el código del estudiante: ")
                    nombre_estudiante = input("Ingrese el nombre del estudiante: ")
                    numero_casillero = int(input("Ingrese el número del casillero a alquilar: "))
                    casillero = self.inventario.buscar_casillero('1', numero_casillero)
                    if casillero is not None:
                        if casillero.alquilar(codigo_estudiante, nombre_estudiante):
                            print("Alquiler realizado con éxito.")
                        else:
                            print("No se pudo realizar el alquiler.")
                    else:
                        print("No se encontró ningún casillero con ese número.")
                case '2':
                    numero_casillero = int(input("Ingrese el número del casillero a devolver: "))
                    casillero = self.inventario.buscar_casillero('1', numero_casillero)
                    if casillero is not None:
                        if casillero.devolver():
                            print("Devolución realizada con éxito.")
                        else:
                            print("No se pudo realizar la devolución.")
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
