class Trabajador:
    def __init__(self, nombre, documento, salario_base, descuentos, aux_transporte):
        self.nombre = nombre
        self.documento = documento
        self.salario_base = salario_base
        self.descuentos = descuentos  # 4% salud + 4% pension
        self.aux_transporte = aux_transporte  # 162000.0

    def calcular_salario_total(self, horas_extras, valor_hora_extra):
        salario_total = self.salario_base
        
        # Incremento del 10% por ley
        salario_total += salario_total * 0.1  

        # Calculo horas extras
        salario_extra = horas_extras * valor_hora_extra
        salario_total += salario_extra

        # Calculo descuentos
        descuentos_total = salario_total * self.descuentos
        salario_total -= descuentos_total

        # Agregar auxilio de transporte
        if self.salario_base <= 2000000:
            salario_total += self.aux_transporte

        return salario_total, salario_extra, descuentos_total


class Horas_Extras(Trabajador):
    def calcular_horas_extras(self, valor_hora_extra, horas_extras):
        salario_extra = horas_extras * valor_hora_extra
        return salario_extra


class Registro_Trabajadores:
    def __init__(self):
        self.trabajadores = []

    def agregar_trabajador(self, trabajador):
        self.trabajadores.append(trabajador)

    def listar_trabajadores(self):
        for trabajador in self.trabajadores:
            print(f"Nombre: {trabajador.nombre}, Documento: {trabajador.documento}")        


def main():
    registro = Registro_Trabajadores()

    while True:
        nombre = input("Ingrese el nombre del trabajador (o 'exit' para salir): ")
        if nombre.lower() == 'exit':
            break
        documento = input("Ingrese el documento del trabajador: ")
        salario_base = float(input("Ingresa el salario base: "))
        horas_extras = int(input("Ingrese la cantidad de horas extras (Tener en cuenta que solo se toman horas enteras): "))

        if horas_extras <= 0:
            print("No tienes horas extras")
            salario_total, salario_extra, descuentos_total = Trabajador(nombre, documento, salario_base, 0.08, 162000.0).calcular_salario_total(0, 0)
            
            print("\n----- Comprobante de Pago Nomina -----")
            print("_________________________________________")
            print("\n------- Informacion Trabajador -------")
            print("_________________________________________")
            print(f"Nombre          |  {nombre}")
            print(f"Documento       |  {documento}")
            print("_________________________________________")
            print(f"Horas Extras    |  {horas_extras}")
            print(f"Salario Base    |  {salario_base}")
            print(f"Salario Extra   |  {salario_extra}")
            print(f"Descuentos      |  {descuentos_total}")
            print("_________________________________________")
            print(f"Salario Total   |  {salario_total}")
            print("_________________________________________")
            
            registro.agregar_trabajador(Trabajador(nombre, documento, salario_base, 0.08, 162000.0))


        else:
            
            print("""Tipo de horas extras trabajadas
                      1. Hora extra diurna
                      2. Hora extra nocturna
                      3. Hora extra dominical festivos (diurna)
                      4. Hora extra dominical festivos (nocturna)""")
            op_extras = int(input("Ingresa el tipo de horas extras trabajadas: "))

            trabajador = Horas_Extras(nombre, documento, salario_base, 0.08, 162000.0)

            if op_extras == 1:
                salario_total, salario_extra, descuentos_total = trabajador.calcular_salario_total(horas_extras, 6915.0)
            elif op_extras == 2:
                salario_total, salario_extra, descuentos_total = trabajador.calcular_salario_total(horas_extras, 9681.0)
            elif op_extras == 3:
                salario_total, salario_extra, descuentos_total = trabajador.calcular_salario_total(horas_extras, 11064.0)
            elif op_extras == 4:
                salario_total, salario_extra, descuentos_total = trabajador.calcular_salario_total(horas_extras, 13830.0)
            else:
                print("Opción de horas extras no válida")
                return

            registro.agregar_trabajador(trabajador)

            print("\n----- Comprobante de Pago Nomina -----")
            print("_________________________________________")
            print("\n------- Informacion Trabajador -------")
            print("_________________________________________")
            print(f"Nombre          |  {trabajador.nombre}")
            print(f"Documento       |  {trabajador.documento}")
            print("_________________________________________")
            print(f"Horas Extras    |  {horas_extras}")
            print(f"Salario Base    |  {salario_base}")
            print(f"Salario Extra   |  {salario_extra}")
            print(f"Descuentos      |  {descuentos_total}")
            print("_________________________________________")
            print(f"Salario Total   |  {salario_total}")
            print("_________________________________________")


            listar_todos = input("¿Deseas listar todos los trabajadores? (s/n): ")
            if listar_todos.lower() == 's':
                print("\n----- Lista de Trabajadores -----")
                registro.listar_trabajadores()
            if listar_todos.lower() == 'n':
                print("Saliento del sistema...")
                break
            


if __name__ == '__main__':
    main()


    
    