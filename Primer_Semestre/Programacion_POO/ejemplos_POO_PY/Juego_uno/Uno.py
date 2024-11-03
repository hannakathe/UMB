from random import *

class Juego:
    def __init__(self):
        
        self.jugadores=[]
        self.baraja_cartas=Barajar().mezclar_car()
        self. carta_actual=self.baraja_cartas.pop()
        self.turno=0
        self.direccion=1
        self.cantidad_cartas=0
        
    def repartir(self):
        pass      
    
    def turno_jugador(self,jugador):
        print(f"Turno de {jugador.jugador_N}")




class Carta:
    def __init__(self, color, numero, comodin , comodin_esp):
        self.color = color
        self.numero = numero
        self.comodin = comodin
        self.comodin_esp = comodin_esp
    

    def _carta_normal_(self):
        return f"{self.numero}-{self.color}"
    
    def _comodin_color_(self):
        return f"{self.comodin}-{self.color}"
    
    def _comodin_especial_(self):
        return f"{self.comodin_esp}"




class Jugador:
    def __init__(self,jugador_name):
        self.jugador_name=jugador_name
        self.mano_jug=[]
    
    def agregar_jug(self,jugador_name):
        self.jugador_name.append(jugador_name)
        self.cont_jugadores+=1
        print("Jugador agregado. Bienvenid@")
    
    def agregar_jugada(self, carta):
        self.mano_jug.append(carta)
    
    def _car_jugada_(self):
        return f"{self.jugador_N} - {self.mano}"

    def _get_carta_(self,carta):
        self.mano_jug.append(carta)
    
     
     
     
class Barajar:
    
    def __init__(self):
        self.numeros=['1', '2', '3', '4', '5', '6', '7', '8', '9', '0']
        self.colores=['azul', 'rojo', 'amarillo', 'verde']
        self.comodines=['+2', 'Reversa', 'Bloqueo']
        self.com_espe=['+4','Cambio de color']
        self.baraja_gene=[]
    
    def barajar(self):
        for numero in self.numeros:
            for color in self.colores:
                self.baraja_gene.append(Carta(color, numero, "", ""))
        for comodin in self.comodines:
            for color in self.colores:
                self.baraja_gene.append(Carta(color, "", comodin, ""))
        for com_esp in self.com_espe:
            self.baraja_gene.append(Carta("", "", "", com_esp))
        shuffle(self.baraja_gene)
        return self.baraja_gene
        
    
    


while True:
    print("--------------------------------------------")
    print("\n Menu:")
    print("""
    1. Jugar
    2. Salir
    """)
    print("--------------------------------------------")
    opcion = int(input("Ingresa una opcion: "))
    
    
    if opcion==1:
        num_jugadores = int(input("Ingrese el numero de jugadores que van a jugar: "))
        jugadores = []
        for i in range(num_jugadores):
            jugador_name = input(f"Ingrese el nombre del jugador {i+1}: ")
            jugadores.append(Jugador(jugador_name))
        juego = Juego()
        juego.jugadores = jugadores
        for jugador in jugadores:
            print(jugador.mostrar_mano())
            
        
    if opcion==2:
        print("Saliendo del juego...")
        break
        