
class Persona1:
    #!se crea una lista de los atributos con una capacidad inicial de 100 elementos iniciando con None estas listas almacenaran toda la inormacion
    nombres = [None] * 100
    apellidos = [None] * 100
    carreras = [None] * 100
    #! se crea una lista para almacenar documentos y numero de semestre, se inicializa en 0
    documentos = [0] * 100
    semestres = [0] * 100
    #!contadorEstudiantes  lleva la cuenta de cuantos estudiantes estan inscritos se inicializa en 0
    contadorEstudiantes = 0 

    #!se usa para declarar static method.Los métodos estáticos pertenecen a la clase en lugar de a una instancia específica de la clase.
    @staticmethod 
    def inscribir(nombre, apellido, documento, semestre, carrera):
        Persona1.nombres[Persona1.contadorEstudiantes] = nombre
        Persona1.apellidos[Persona1.contadorEstudiantes] = apellido
        Persona1.documentos[Persona1.contadorEstudiantes] = documento
        Persona1.semestres[Persona1.contadorEstudiantes] = semestre
        Persona1.carreras[Persona1.contadorEstudiantes] = carrera
        Persona1.contadorEstudiantes += 1
        print("Estudiante inscrito correctamente.")
        #!el metodo anterior recibe los argumentos anteriores y los almacena de la lista correspondiente, y se aumenta el contador de estudiantes
        
    @staticmethod
    def consultar(documento):
        encontrado = False
        for i in range(Persona1.contadorEstudiantes):
            if Persona1.documentos[i] == documento:
                print("Nombre:", Persona1.nombres[i])
                print("Apellido:", Persona1.apellidos[i])
                print("Documento:", Persona1.documentos[i])
                print("Semestre:", Persona1.semestres[i])
                print("Carrera:", Persona1.carreras[i])
                encontrado = True
                break
        if not encontrado:
            print("No se encontró ningún estudiante con ese documento.")
        #!busca un estudiante por su número de documento y muestra su información si se encuentra.
        
    @staticmethod
    def listar():
        for i in range(Persona1.contadorEstudiantes):
            print("Nombre:", Persona1.nombres[i])
            print("Apellido:", Persona1.apellidos[i])
            print("Documento:", Persona1.documentos[i])
            print("Semestre:", Persona1.semestres[i])
            print("Carrera:", Persona1.carreras[i])
        #! muestra la información de todos los estudiantes inscritos


#! menu del sistema      
        
if __name__ == "__main__": #!este nos premite controlar que partes del codigo se ejecutan segun si se ejecuta el modulo directamente o si se importa en otro lugar
    nombres = [None] * 100
    apellidos = [None] * 100
    documentos = [0] * 100
    semestres = [0] * 100
    carreras = [None] * 100
    contadorEstudiantes = 0

    while True:
        print("\nMenu:")
        print("""
    1. Inscribir estudiante
    2. Consultar estudiante por documento
    3. Listar todos los estudiantes
    4. Salir""")
        opcion = int(input("Seleccione una opcion: "))

        if opcion == 1:
            print("\nInscripción de Estudiante:")
            nombre = input("Nombre: ")
            apellido = input("Apellido: ")
            documento = int(input("Documento: "))
            semestre = int(input("Semestre: "))
            carrera = input("Carrera: ")
            Persona1.inscribir(nombre, apellido, documento, semestre, carrera)
        elif opcion == 2:
            print("\nConsulta de Estudiante por Documento:")
            docConsulta = int(input("Ingrese el documento a consultar: "))
            print("")
            Persona1.consultar(docConsulta)
        elif opcion == 3:
            print("\nEstos son los estudiantes inscritos:")
            print("")
            Persona1.listar()
        elif opcion == 4:
            print("Saliendo del programa...")
            break
        else:
            print("Opción inválida. Intente nuevamente.")
