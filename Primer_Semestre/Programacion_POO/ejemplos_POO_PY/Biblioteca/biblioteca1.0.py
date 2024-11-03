import datetime


class Biblioteca:
    
    fecha_entrega=[]
    fecha_salida=[]
    cont_usuarios=0
    cont_libros=0
    
    
    
    @staticmethod
    def consultar(documento):
        encontrado = False
        for j in range(Biblioteca.cont_usuarios):
            if Usuarios.documento[j]==documento:
                print("Usuario consultado:")
                print("--------------------------------------------")
                print(f"Nombre: {Usuarios.nombre[j]}")
                print(f"Documento: {Usuarios.documento[j]}")
                print(f"Email: {Usuarios.email[j]}")
                print(f"Tipo de membresía: {Usuarios.tipo_de_membresia[j]}")
                print("--------------------------------------------")
                encontrado = True
                break

        if not encontrado:
            print("No se encontró ningún usuario correspondiente.")
       

                
    @staticmethod
    def consultar_libro(titulo,categoria,autor):
        encontrado = False
        for i in range(Biblioteca.cont_libros):
            if Libros.titulo[i]==titulo or Libros.categoria[i] == categoria or Libros.autor[i]==autor:
                print("Libro consultado:")
                print("--------------------------------------------")
                print(f"Nombre: {Libros.titulo[i]}")
                print(f"Categoria: {Libros.categoria[i]}")
                print(f"Autor: {Libros.autor[i]}")
                print(f"Codigo: {Libros.isbn[i]}")
                print("--------------------------------------------")
                encontrado = True
            
        if not encontrado:
            print("No se encontró ningún libro correspondiente.")
        
    
    @staticmethod
    def listar_lib():
        if Biblioteca.cont_libros == 0:
            print("No hay libros en la biblioteca.")
            return
        
        for i in range(Biblioteca.cont_libros):
            print("--------------------------------------------")
            print(f"Nombre: {Libros.titulo[i]}")
            print(f"Categoria: {Libros.categoria[i]}")
            print(f"Codigo: {Libros.isbn[i]}")
            print(f"Autor: {Libros.autor[i]}")
            print(f"Cantidad: {Libros.cantidad[i]}")
            print("--------------------------------------------")
    

        
    @staticmethod
    def agregar(titulo, isbn, autor, categoria, cantidad):
        Libros.titulo.append(titulo)
        Libros.isbn.append(isbn)
        Libros.autor.append(autor)
        Libros.categoria.append(categoria)
        Libros.cantidad.append(cantidad)
        Biblioteca.cont_libros+=1
        print("Libro agregado correctamente.")
        
        
        
    @staticmethod
    def eliminar(titulo):
        if titulo in Libros.titulo:
            indice_a_eliminar = Libros.titulo.indice_a_eliminar(titulo)
            del Libros.titulo[indice_a_eliminar]
            del Libros.isbn[indice_a_eliminar]
            del Libros.autor[indice_a_eliminar]
            del Libros.categoria[indice_a_eliminar]
            del Libros.cantidad[indice_a_eliminar]
            Biblioteca.cont_libros -= 1
            print(f"Libro '{titulo}' eliminado correctamente.")
        else:
            print("El libro no está en la lista")
    
        
        
    @staticmethod
    def eliminar_libro_pedido(titulo):
        if titulo in Usuarios.libros_pedidos:
            Usuarios.libros_pedidos.remove(titulo)
            print(f"Libro '{titulo}' eliminado de la lista de libros pedidos")
        else:
            print(f"El libro '{titulo}' no está en la lista de libros pedidos")

    
    
    @staticmethod
    def calcular_fecha_devolucion(tipo_de_membresia, fecha_ingreso):
        fecha_ingreso=datetime.strptime(fecha_ingreso, '%d/%m/%Y')
        if tipo_de_membresia == "Estandar":
            plazo = datetime.timedelta(days=7)
        elif tipo_de_membresia == "VIP":
            plazo = datetime.timedelta(days=15)
        else:
            raise ValueError("Tipo de membresía no válido")
        
        fecha_devolucion = fecha_ingreso + plazo
        return fecha_devolucion

class Libros:
    titulo=[]
    categoria=[]
    autor=[]
    isbn=[]
    cantidad=[]
           
class Usuarios:
    
    libros_pedidos=[]
    cont_lib_usu=0
    nombre=[]
    documento=[]
    email=[]
    tipo_de_membresia=[]
    pasword=[]
    

    
    @staticmethod 
    def inscribir(nombre, documento, email, tipo_de_membresia,pasword):
        Usuarios.nombre.append(nombre)
        Usuarios.documento.append(documento)
        Usuarios.email.append(email)
        Usuarios.tipo_de_membresia.append(tipo_de_membresia)
        Usuarios.pasword.append(pasword)
        Biblioteca.cont_usuarios+=1
        print("Usuario inscrito correctamente.")
    
  
    
    @staticmethod
    def listar_usu():
        if Biblioteca.cont_usuarios == 0:
            print("No hay usuarios inscritos.")
            return
        
        for j in range(len(Biblioteca.cont_usuarios)):
            print("--------------------------------------------")
            print(f"Nombre: {Usuarios.nombre[j]}")
            print(f"Documento: {Usuarios.documento[j]}")
            print(f"Email: {Usuarios.email[j]}")
            print(f"Tipo de membresia: {Usuarios.tipo_de_membresia[j]}")
            print("--------------------------------------------")

        
    
    @staticmethod
    def pedir_libro(titulo, tipo_de_membresia, fecha_ingreso):
        Biblioteca.fecha_entrega.append(fecha_ingreso)
        fecha_devolucion = Biblioteca.calcular_fecha_devolucion(tipo_de_membresia, fecha_ingreso)
        Biblioteca.fecha_devolucion.append(fecha_devolucion)
        print(f"Libro '{titulo}' prestado correctamente.")
        print(f"Fecha de devolución: {fecha_devolucion}")
    
    
    
    @staticmethod
    def verificar_devolucion_tardia(fecha_devolucion_real, fecha_devolucion_esperada):
        if fecha_devolucion_real > fecha_devolucion_esperada:
            print("¡Libro devuelto tarde! Se aplicará una amonestación.")
        else:
            print("Fecha de devolucion correcta")
            
    
    
    @staticmethod 
    def devolver_lib(titulo):
        if titulo in Usuarios.libros_pedidos:
            Biblioteca.eliminar_libro_pedido(titulo)
            Usuarios.verificar_devolucion_tardia()
        else:
            print("El libro no está en la lista de libros pedidos")
    
    
    
    @staticmethod
    def listar_lib_usu():
        if Usuarios.cont_lib_usu == 0:
            print("No hay libros en la biblioteca.")
            return
        
        for i in range(Biblioteca.cont_libros):
            print("--------------------------------------------")
            print(f"Nombre: {Libros.titulo[i]}")
            print(f"Categoria: {Libros.categoria[i]}")
            print(f"Codigo: {Libros.isbn[i]}")
            print(f"Autor: {Libros.autor[i]}")
            print("--------------------------------------------")
            
        


while True:
    
    """ El código utiliza un bucle while True para mostrar 
    un menú de opciones al usuario y gestionar las interacciones 
    con el sistema. """
    
    
    
    print("--------------------------------------------")
    print("\n Menu:")
    print("""
    1. Bibliotecario
    2. Usuario
    3. Inscribirse
    4. Salir del sistema
    """)
    print("--------------------------------------------")
    opcion = int(input("Ingresa una opcion: "))
    
    """ El usuario puede elegir entre ser un bibliotecario, 
    un usuario regular o salir del sistema. Dependiendo de 
    la opción elegida, se muestran submenús con diferentes 
    acciones disponibles para cada tipo de usuario."""
    

    
    if opcion == 1:
        print("--------------------------------------------")
        print("\n Menu:")
        print("""
        1. Consultar usuario por documento
        2. Consultar libro por titulo, categoria o autor
        3. Listar todos los usuarios
        4. Listar todos los libros
        5. Agregar libros a la biblioteca/sistema
        6. Almacenar (sacar o descontinuar) libros
        7. Salir del sistema 
        """)
        print("--------------------------------------------")
        opc_b = int(input("Ingresa una opcion: "))
        
        
        if opc_b == 1:
            print("Consultar Usuario: ")
            doc = input("Ingrese el documento: ")
            Biblioteca.consultar(doc)
        
        
        if opc_b == 2:
            print("--------------------------------------------")
            print("\n Escoja tipo de búsqueda del libro: ")
            print("""
            1. Consultar libro por titulo
            2. Consultar libro por categoria
            3. Consultar libro por autor
            """)
            print("--------------------------------------------")
            op_lib = int(input("Ingresa una opcion: "))
            
            
            if op_lib == 1:
                print("Consulta por titulo")
                titulo = input("Ingresa el titulo del libro:")
                Biblioteca.consultar_libro(titulo, "", "")
            
            
            if op_lib == 2:
                print("Consulta por categoria")
                categoria = input("Ingresa la categoria del libro:")
                Biblioteca.consultar_libro("", categoria, "")
            
            
            if op_lib == 3:
                print("Consulta por autor")
                autor = input("Ingresa el autor del libro:")
                Biblioteca.consultar_libro("", "", autor)
            
            
            else:
                print("Opcion invalida") 
                
        
        if opc_b == 3:
            print("Estos son los usuarios inscritos: ")
            print("")
            Usuarios.listar_usu()
        
        
        if opc_b == 4:
            print("Estos son los libros en la biblioteca: ")
            print("")
            Biblioteca.listar_lib()
        
        
        if opc_b == 5:
            print("Agregar libro")
            titulo = input("Ingrese el titulo completo del libro: ")
            isbn = input("Ingrese codigo de 4 numeros: ")
            autor = input("Ingrese nombre completo del autor o alias: ")
            categoria = input("Ingrese la categoria o categorias del libro: ")
            cantidad = int(input("Ingrese la cantidad de libros que ingresan de este titulo: "))
            Biblioteca.agregar(titulo, isbn, autor, categoria, cantidad)
            
            
        if opc_b == 6:
            titulo = input("Pon el titulo del libro que desea eliminar:")
            Biblioteca.eliminar(titulo)
        
        
        if opc_b == 7:
            print("Saliendo del sistema...")
            break
        
        
        else:
            print("Opcion invalida. ") #! Menu de biblioteca
    


    if opcion == 2:
        login=input("Ingresa el correo con el que estas inscrito: ")
        for i in range(Biblioteca.cont_usuarios):
            if Usuarios.email[i]==login:
                pasword=input("Ingrese su contraseña: ")
                for j in range(Biblioteca.cont_usuarios):
                    if Usuarios.pasword[j]==pasword:
                        print("--------------------------------------------")
                        print("\n Menu:")
                        print("""
                        1. Pedir libro
                        2. Devolver libro
                        3. Listar libros que he pedido
                        4. Salir del sistema
                        """)  
                        print("--------------------------------------------")  
                        opc_u = int(input("Ingresa una opcion: "))

                        if opc_u == 1:
                            print("\n Solicitud de libro")
                            titulo = input("Ingrese el titulo del libro que desea pedir: ")
                            fecha_ingreso = input("Ingresa la fecha de ingreso del libro en formato dd/mm/aaaa: ")
                            Usuarios.tipo_de_membresia=tipo_de_membresia
                            Usuarios.pedir_libro(titulo,tipo_de_membresia,fecha_ingreso)


                        if opc_u == 2:
                            titulo = input("Ingrese el titulo del libro que desea devolver: ")
                            Usuarios.devolver_lib(titulo)


                        if opc_u == 3:
                            print("Estos son los libros que has pedido hasta el momento: ")
                            print("")
                            Usuarios.listar_lib_usu()


                        if opc_u==4:
                            print("Saliendo del sistema...")
                            break
                        else:
                            print("Opcion no valida")
                            continue
                    else:
                        print("Contraseña incorrecta")
                        continue
            else:
                print("Usuario invalido")
                continue
       


        
    if opcion == 3:
        print("\nInscripción de Usuario:")
        nombre = input("Nombre: ")
        documento = input("Documento: ")
        email = input("Email: ")
        pasword=input("Ingresa la contraseña de tu usuario: ")
        print("""Membresias:
              1. Estandar: Maximo tiempo de prestamo de un libro son 7 dias
              2. VIP: Maximo tiempo de prestamo de un libro son 15 dias""")
        print("")
        tipo_de_membresia = int(input("Escoja el tipo de membresia que desea: "))
        if tipo_de_membresia==1:
            tipo_de_membresia="Estandar"
        if tipo_de_membresia==2:
            tipo_de_membresia="VIP"
        else:
            print("Opcion no valida")
            continue
        Usuarios.inscribir(nombre, documento, email, tipo_de_membresia,pasword)


    if opcion== 4:
        print("Saliendo del sistema...")
        break
    
    
    else:
        print("Opcion invalida.")
        
        
        

