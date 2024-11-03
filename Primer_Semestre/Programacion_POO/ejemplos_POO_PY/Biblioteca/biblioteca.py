class Biblioteca:
    
    nombre=[]
    documento=[]
    email=[]
    tipo_de_membresia=[]
    pasword=[]
    
    titulo=[]
    categoria=[]
    autor=[]
    isbn=[]
    fecha_entrega=[]
    fecha_devolucion=[]
    cantidad=[]
    
    cont_usuarios=0
    cont_libros=0
    
    
    @staticmethod
    def inscribir(nombre, documento, email, tipo_de_membresia, pasword):
        Biblioteca.nombre.append(nombre)
        Biblioteca.documento.append(documento)
        Biblioteca.email.append(email)
        Biblioteca.tipo_de_membresia.append(tipo_de_membresia)
        Biblioteca.pasword.append(pasword)
        Biblioteca.cont_usuarios+=1
        print("Usuario inscrito correctamente.")
    
    @staticmethod
    def consultar(documento):
        encontrado=False
        for j in range(Biblioteca.cont_usuarios):
            if Biblioteca.documento[j]==documento:
                print("--------------------------------------------")
                print(f"Nombre: {Biblioteca.nombre[j]}")
                print(f"Documento: {Biblioteca.documento[j]}")
                print(f"Email: {Biblioteca.email[j]}")
                print(f"Tipo de membresia: {Biblioteca.tipo_de_membresia[j]}")
                print("--------------------------------------------")
                encontrado=True
                break
            
        if not encontrado:
            print("No se encontró ningún usuario correspondiente.")
                
    @staticmethod
    def consultar_libro(titulo,categoria,autor):
        encontrado=False
        for i in range(Biblioteca.cont_libros):
            if Biblioteca.titulo[i]==titulo or Biblioteca.categoria[i] == categoria or Biblioteca.autor[i]==autor:
                print("--------------------------------------------")
                print(f"Nombre: {Biblioteca.titulo[i]}")
                print(f"Categoria: {Biblioteca.categoria[i]}")
                print(f"Autor: {Biblioteca.autor[i]}")
                print(f"Codigo: {Biblioteca.isbn[i]}")
                print("--------------------------------------------")
                encontrado=True
                break
            
        if not encontrado:
            print("No se encontró ningún libro correspondiente.")
    
    @staticmethod
    def listar_lib():
        if Biblioteca.cont_libros == 0:
            print("No hay libros en la biblioteca.")
            return
        
        for i in range(Biblioteca.cont_libros):
            print("--------------------------------------------")
            print(f"Nombre: {Biblioteca.titulo[i]}")
            print(f"Categoria: {Biblioteca.categoria[i]}")
            print(f"Codigo: {Biblioteca.isbn[i]}")
            print(f"Autor: {Biblioteca.autor[i]}")
            print(f"Cantidad: {Biblioteca.cantidad[i]}")
            print("--------------------------------------------")
    
    @staticmethod
    def listar_usu():
        if Biblioteca.cont_usuarios == 0:
            print("No hay usuarios inscritos.")
            return
        
        for j in range(Biblioteca.cont_usuarios):
            print("--------------------------------------------")
            print(f"Nombre: {Biblioteca.nombre[j]}")
            print(f"Documento: {Biblioteca.documento[j]}")
            print(f"Email: {Biblioteca.email[j]}")
            print(f"Tipo de membresia: {Biblioteca.tipo_de_membresia[j]}")
            print("--------------------------------------------")
    
    @staticmethod
    def agregar(titulo, isbn, autor, categoria, cantidad):
        Biblioteca.titulo.append(titulo)
        Biblioteca.isbn.append(isbn)
        Biblioteca.autor.append(autor)
        Biblioteca.categoria.append(categoria)
        Biblioteca.cantidad.append(cantidad)
        Biblioteca.cont_libros+=1
        print("Libro agregado correctamente.")
        
    @staticmethod
    def eliminar(titulo):
        if titulo in Biblioteca.titulo:
            indice_a_eliminar = Biblioteca.titulo.index(titulo)
            del Biblioteca.titulo[indice_a_eliminar]
            del Biblioteca.isbn[indice_a_eliminar]
            del Biblioteca.autor[indice_a_eliminar]
            del Biblioteca.categoria[indice_a_eliminar]
            del Biblioteca.cantidad[indice_a_eliminar]
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


class Usuarios:
    
    libros_pedidos=[]
    cont_lib_usu=0
    
    @staticmethod
    def pedir_lib(titulo):
        disponible = False
        for i in range(Biblioteca.cont_libros):
            if Biblioteca.titulo[i] == titulo:
                Usuarios.libros_pedidos.append(titulo)
                disponible = True
                Usuarios.cont_lib_usu+=1
                print("Solicitud de libro aceptada")
                break
        if not disponible:
            print("Libro solicitado no disponible")
    
    @staticmethod
    def devolver_lib(titulo):
        if titulo in Usuarios.libros_pedidos:
            Biblioteca.eliminar_libro_pedido(titulo)
            print("Libro devuelto correctamente")
        else:
            print("El libro no está en la lista de libros pedidos")
    
    @staticmethod
    def listar_lib_usu():
        if Usuarios.cont_lib_usu == 0:
            print("No hay libros en la biblioteca.")
            return
        
        for i in range(Biblioteca.cont_libros):
            print("--------------------------------------------")
            print(f"Nombre: {Biblioteca.titulo[i]}")
            print(f"Categoria: {Biblioteca.categoria[i]}")
            print(f"Codigo: {Biblioteca.isbn[i]}")
            print(f"Autor: {Biblioteca.autor[i]}")
            print("--------------------------------------------")
            

while True:
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
        opc_Bl = int(input("Ingresa una opcion: "))
        
        if opc_Bl == 1:
            print("Consultar Usuario: ")
            doc = input("Ingrese el documento: ")
            Biblioteca.consultar(doc)
        
        if opc_Bl == 2:
            print("--------------------------------------------")
            print("\n Escoja tipo de búsqueda:")
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
        
        if opc_Bl == 3:
            print("Estos son los usuarios inscritos")
            print("")
            Biblioteca.listar_usu()
        
        if opc_Bl == 4:
            print("Estos son los libros en la biblioteca")
            print("")
            Biblioteca.listar_lib()
        
        if opc_Bl == 5:
            print("Agregar libro")
            titulo = input("Ingrese el titulo completo del libro: ")
            isbn = input("Ingrese codigo de 4 numeros: ")
            autor = input("Ingrese nombre completo del autor o alias: ")
            categoria = input("Ingrese la categoria o categorias del libro: ")
            cantidad = int(input("Ingrese la cantidad de libros que ingresan de este titulo: "))
            Biblioteca.agregar(titulo, isbn, autor, categoria, cantidad)
              
        if opc_Bl == 6:
            titulo = input("Pon el titulo del libro que desea eliminar:")
            Biblioteca.eliminar(titulo)
          
        if opc_Bl == 7:
            print("Saliendo del sistema...")
            break
        
        else:
            print("Opcion invalida. ") 
    
    
    if opcion == 2:
        login=input("Ingresa el correo con el que estas inscrito: ")
        for i in range(Biblioteca.cont_usuarios):
            if Biblioteca.email[i]==login:
                pasword=input("Ingrese su contraseña: ")
                for j in range(Biblioteca.cont_usuarios):
                    if Biblioteca.pasword[j]==pasword:
                        print("--------------------------------------------")
                        print("\n Menu:")
                        print("""
                        1. Pedir libro
                        2. Devolver libro
                        3. Listar libros que he pedido
                        4. Salir del sistema
                        """)  
                        print("--------------------------------------------")  
                        opc_Us = int(input("Ingresa una opcion: "))


                        if opc_Us == 1:
                            print("\n Solicitud de libro")
                            titulo = input("Ingrese el titulo del libro que desea pedir: ")
                            Usuarios.pedir_lib(titulo)
                        if opc_Us == 2:
                            titulo = input("Ingrese el titulo del libro que desea devolver: ")
                            Usuarios.devolver_lib(titulo)
                        if opc_Us == 3:
                            print("Estos son los libros que has pedido hasta el momento: ")
                            print("")
                            Usuarios.listar_lib_usu()
                        if opc_Us==4:
                            print("Saliendo del sistema...")
                            break
                        else:
                            print("Opcion invalida.")
                    else:
                        print("Contraseña incorrecta")       
            else:
                print("Usuario invalido")
        
        
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
        Biblioteca.inscribir(nombre, documento, email, tipo_de_membresia,pasword)
    
    
    if opcion == 4:
        print("Saliendo del sistema...")
        break
    
    
    else:
        print("Opcion invalida.") 
