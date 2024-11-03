"""print(
    Menu: 
    
    1. Lunes
    2. Martes
    3. Miercoles
    4. Jueves
    5. Viernes
    6. Sabado
    7. Domingo
    8. Salir
    )

dia= int(input("Ingrese el numero correpondiente al dia: "))

def semana():
    match dia:
        case 1:
            print("Haz elegido el dia Lunes")
        case 2:
            print("Haz elegido el dia Martes")
        case 3:
            print("Haz elegido el dia Miercoles")
        case 4:
            print("Haz elegido el dia Jueves")
        case 5:
            print("Haz elegido el dia Viernes")
        case 6:
            print("Haz elegido el dia Sabado")
        case 7:
            print("Haz elegido el dia Domingo")
        case 8:
            print("Fuera de rango")"""



print("""
    Menu: 
    
    1. Sumar
    2. Restar
    3. Multiplicar
    4. Dividir
    5. Residuo
    7. Salir
    """)

num_1= int(input("Ingrese el primer numero: "))
num_2= int(input("Ingrese el segundo numero: "))
operacion=input("Ingresa el simbolo de la operacion: ")

def semana():
    match operacion:
        case 1:
            resu=num_1+num_2
            print(resu) 
        case "-":
            resu=num_1-num_2
            return resu
        case "*":
            resu=num_1*num_2
            return resu
        case "/":
            resu=num_1/num_2
            return resu
        case "%":
            resu=num_1%num_2
            return resu
        case "_":
            print("Fuera de rango")