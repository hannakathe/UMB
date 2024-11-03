from random import *
import random

class Juego:
    def __init__(self, jugadores):
        self.jugadores = jugadores
        self.mano = self.generar_mano()
        self.carta_actual = self.mano.pop(0)
        self.turno = 0
        self.direccion = 1  # 1 for clockwise, -1 for counterclockwise


class Carta:
    def generar_mano(self):
        self.numeros=['1', '2', '3', '4', '5', '6', '7', '8', '9', '0']
        self.colores=['azul', 'rojo', 'amarillo', 'verde']
        self.comodines=['+2', 'Reversa', 'Bloqueo']
        self.com_espe=['+4','Cambio de color']
        self.baraja_gene=[]
        

class Barajar:        

    def barajar(self):
        for numero in self.numeros:
            for color in self.colores:
                self.baraja_gene.append(Carta(color, numero, "", ""))
        for comodin in self.comodines:
            for color in self.colores:
                self.baraja_gene.append(Carta(color, "", comodin, ""))
        for com_esp in self.com_espe:
            self.baraja_gene.append(Carta("", "", "", com_esp))
            
        random.shuffle(self.baraja_gene)
        return self.baraja_gene

    

    def siguiente_jugador(self):
        self.turno = (self.turno + self.direccion) % len(self.jugadores)

    def play(self):
        print("Bienvenido al juego!")
        print(f"La carta que comienza el juego es: {self.carta_actual}")
        print(f"Jugador {self.jugadores[self.turno]}'s turno.")

        while True:
            jugador_actual = self.jugadores[self.turno]
            self.mostrar_mano(jugador_actual)
            carta_jugada = input("Ingresa la carta que deseas jugar o toma una carta: ").strip().lower()

            if carta_jugada == 'Tomar':
                self.Tomar(jugador_actual)
            elif self.validad_jugada(carta_jugada):
                self.carta_actual = carta_jugada
                jugador_actual.remove(carta_jugada)
                print(f"Jugador {jugador_actual}'s carta: {carta_jugada}")
                if len(jugador_actual) == 0:
                    print(f"Jugador {jugador_actual} gana!")
                    break
                self.siguiente_jugador()
            else:
                print("Movimiento invalido, intenta de nuevo")

    def mostrar_mano(self, jugador):
        print(f"Tu mano: {', '.join(jugador)}")

    def Tomar(self, jugador):
        if len(self.mano) == 0:
            print("No hay mas cartas en la mano, salta turno")
        else:
            jugador.append(self.mano.pop(0))
            print("Card drawn.")

    def validad_jugada(self, carta_jugada):
        return carta_jugada.split()[0] in self.carta_actual.split()[0] or \
               carta_jugada.split()[1] in self.carta_actual.split()[1] or \
               carta_jugada.split()[0] == "Wild" or \
               self.carta_actual.split()[0] == "Wild"

def main():
    num_jugadores = int(input("Ingrese el numero de jugadores: "))
    jugadores = [[] for _ in range(num_jugadores)]

    for i in range(num_jugadores):
        nombre_jug = input(f"Ingrese el numero del jugador {i + 1}: ")
        print(f"Bienvenido, {nombre_jug}!")

    juego_uno = Juego(jugadores)
    juego_uno.play()

if __name__ == "__main__":
    main()
