import datetime


#! TENER EN CUENTA

#! TENER EN CUENTA YA QUE PYTHON NO CUENTA CON UN MODULO SWITCH
#! EN ESTE CASO SE DEBE USAR EL METODO MATCH-CASE PERO ESTE SOLO ESTA
#! DISPONIBLE EN VERSIONES DE PYTHON COMO LA 3.12.2 O SUPERIORES


class MetaRecaudo:
    def __init__(self, monto_objetivo):
        self.monto_objetivo = monto_objetivo
        self.fecha_establecimiento = datetime.date.today()
        self.estado = "No cumplida"
        self.ahorro_actual = 0


class BancoDigital:
    def __init__(self):
        self.saldo_total = 0
        self.metas_recaudo = []
        self.usuarios = {}
        self.usuario_actual = None

    def autenticar_usuario(self):
        if self.usuario_actual is None:
            usuario = input("Ingrese su usuario: ")
            contraseña = input("Ingrese su contraseña: ")
            if usuario in self.usuarios and self.usuarios[usuario] == contraseña:
                self.usuario_actual = usuario
                print("Autenticación exitosa.")
                return True
            else:
                print("Usuario o contraseña incorrectos. Intente nuevamente.")
                return False
        else:
            return True

    def inscribir_usuario(self):
        while True:
            print("----- Menú de Inscripción -----")
            print("")
            print("1. Ingresar nuevo usuario")
            print("2. Ingresar al menú principal")
            print("3. Salir del sistema")
            print("")
            print("-------------------------------")
            print("")
            opcion = input("Seleccione una opción: ")
            if opcion == "1":
                usuario = input("Ingrese el nuevo usuario: ")
                contraseña = input("Ingrese la contraseña del nuevo usuario: ")
                self.usuarios[usuario] = contraseña
                print("Usuario inscrito correctamente.")
            elif opcion == "2":
                print("Ingresando al menú principal.")
                if self.autenticar_usuario():
                    self.menu_principal()
            elif opcion == "3":
                print("Saliendo del sistema.")
                exit()
            else:
                print("Opción no válida. Por favor, seleccione una opción válida.")

    def menu_principal(self):
        while True:
            print("--------- Menú Principal ----------")
            print("")
            print("1. Ver saldo total")
            print("2. Ingresar dinero")
            print("3. Enviar dinero")
            print("4. Retirar dinero en cajero")
            print("5. Retirar dinero en punto físico")
            print("6. Establecer meta de ahorro")
            print("7. Ver metas de ahorro")
            print("8. Salir")
            print("")
            print("------------------------------------")
            print("")
            opcion = input("Seleccione una opción: ")

            match opcion:
                case "1":
                    self.ver_saldo_total()
                case "2":
                    monto = float(input("Ingrese el monto a ingresar: "))
                    self.ingresar_dinero(monto)
                case "3":
                    monto = float(input("Ingrese el monto a enviar: "))
                    self.enviar_dinero(monto)
                case "4":
                    monto = float(input("Ingrese el monto a retirar en cajero: "))
                    self.retirar_dinero(monto, "cajero")
                case "5":
                    monto = float(input("Ingrese el monto a retirar en punto físico: "))
                    self.retirar_dinero(monto, "punto físico")
                case "6":
                    monto_objetivo = float(input("Ingrese el monto objetivo de ahorro: "))
                    self.establecer_meta_de_ahorro(monto_objetivo)
                case "7":
                    self.ver_metas_de_ahorro()
                case "8":
                    print("Saliendo del programa.")
                    return
                case _:
                    print("Opción no válida. Por favor, seleccione una opción válida.")

    def ver_saldo_total(self):
        print(f"Saldo total: COP {self.saldo_total}")

    def ingresar_dinero(self, monto):
        self.saldo_total += monto
        print(f"Se ingresó correctamente ${monto}. Saldo actual: {self.saldo_total}")

    def enviar_dinero(self, monto):
        if self.saldo_total >= monto:
            self.saldo_total -= monto
            print(f"Se envió correctamente ${monto}. Saldo actual: {self.saldo_total}")
        else:
            print("Saldo insuficiente para realizar la transacción.")

    def retirar_dinero(self, monto, tipo):
        if tipo == "cajero":
            if self.saldo_total >= monto:
                self.saldo_total -= monto
                print(f"Se retiró correctamente ${monto} en cajero. Saldo actual: {self.saldo_total}")
            else:
                print("Saldo insuficiente para realizar la transacción.")
        elif tipo == "punto físico":
            print("Retiro en punto físico aún no implementado.")
        else:
            print("Tipo de retiro no válido.")

    def establecer_meta_de_ahorro(self, monto_objetivo):
        nueva_meta = MetaRecaudo(monto_objetivo)
        self.metas_recaudo.append(nueva_meta)
        print(f"Meta de ahorro establecida correctamente. Monto objetivo: ${monto_objetivo}")

    def ver_metas_de_ahorro(self):
        if self.metas_recaudo:
            print("Metas de ahorro:")
            for meta in self.metas_recaudo:
                print(meta)
        else:
            print("No hay metas de ahorro establecidas.")

    def ejecutar(self):
        self.inscribir_usuario()

banco = BancoDigital()
banco.ejecutar()

