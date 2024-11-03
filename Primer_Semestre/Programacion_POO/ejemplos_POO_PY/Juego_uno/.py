import random

class Carta:
    def __init__(self, color, valor):
        self.color = color
        self.valor = valor

    def __str__(self):
        return f"{self.color} {self.valor}"

class Mazo:
    def __init__(self):
        self.cartas = []
        self.colores = ["Rojo", "Verde", "Azul", "Amarillo"]
        self.valores = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Saltar", "Reversa", "TomaDos"]

        for color in self.colores:
            for valor in self.valores:
                self.cartas.append(Carta(color, valor))

    def mezclar(self):
        random.shuffle(self.cartas)

    def sacar_carta(self):
        return self.cartas.pop()

class Jugador:
    def __init__(self, nombre):
        self.nombre = nombre
        self.mano = []

    def tomar_carta(self, carta):
        self.mano.append(carta)

    def jugar_carta(self, carta):
        self.mano.remove(carta)

    def mostrar_mano(self):
        print(f"Mano de {self.nombre}:")
        for i, carta in enumerate(self.mano):
            print(f"{i+1}. {carta}")

def mostrar_mano_y_opciones(jugador):
    jugador.mostrar_mano()
    print("Opciones:")
    print("1. Jugar carta")
    print("2. Tomar carta del mazo")
    opcion = input("Seleccione una opción: ")
    return opcion

def seleccionar_carta(jugador):
    while True:
        opcion_carta = input("Seleccione el número de carta que desea jugar o '0' para volver: ")
        if opcion_carta == '0':
            return None
        try:
            index_carta = int(opcion_carta) - 1
            carta_seleccionada = jugador.mano[index_carta]
            return carta_seleccionada
        except (ValueError, IndexError):
            print("Selección inválida. Intente nuevamente.")






def main():
    print("Bienvenido al juego Uno!")
    num_jugadores = int(input("Ingrese el número de jugadores: "))
    jugadores = []
    for i in range(num_jugadores):
        nombre = input(f"Ingrese el nombre del jugador {i+1}: ")
        jugadores.append(Jugador(nombre))

    mazo = Mazo()
    mazo.mezclar()

    jugador_actual = random.choice(jugadores)
    print(f"Comienza el juego. {jugador_actual.nombre} va primero.")

    while True:
        print("\n-----------------------------------------")
        print(f"Es el turno de {jugador_actual.nombre}")
        opcion = mostrar_mano_y_opciones(jugador_actual)

        if opcion == '1':
            carta_seleccionada = seleccionar_carta(jugador_actual)
            if carta_seleccionada is None:
                continue
            print(f"{jugador_actual.nombre} ha jugado {carta_seleccionada}")
            jugador_actual.jugar_carta(carta_seleccionada)
        elif opcion == '2':
            carta_nueva = mazo.sacar_carta()
            jugador_actual.tomar_carta(carta_nueva)
            print(f"{jugador_actual.nombre} ha tomado una carta del mazo.")
        else:
            print("Opción inválida. Intente nuevamente.")
            continue

        # Verificar si el jugador ganó
        if len(jugador_actual.mano) == 0:
            print(f"\n¡Felicidades! {jugador_actual.nombre} ha ganado el juego Uno.")
            break

        # Cambiar al siguiente jugador
        index_jugador_actual = jugadores.index(jugador_actual)
        index_jugador_siguiente = (index_jugador_actual + 1) % len(jugadores)
        jugador_actual = jugadores[index_jugador_siguiente]

if __name__ == "__main__":
    main()



import random

class UnoGame:
    def __init__(self, players):
        self.players = players
        self.deck = self.generate_deck()
        self.current_card = self.deck.pop(0)
        self.turn = 0
        self.direction = 1  # 1 for clockwise, -1 for counterclockwise

    def generate_deck(self):
        colors = ['Red', 'Green', 'Blue', 'Yellow']
        special_cards = ['Skip', 'Reverse', 'Draw Two']
        normal_cards = [str(number) for number in range(1, 10)]
        deck = []

        for color in colors:
            for card in normal_cards:
                deck.append(f"{color} {card}")
                deck.append(f"{color} {card}")
            for special_card in special_cards:
                deck.append(f"{color} {special_card}")
                deck.append(f"{color} {special_card}")

        numbers_only_cards = [str(number) for number in range(0, 10)]
        for card in numbers_only_cards:
            deck.append(f"Number {card}")

        # Adding Wild and Wild Draw Four cards
        for _ in range(4):
            deck.append("Wild")
            deck.append("Wild Draw Four")

        random.shuffle(deck)
        return deck

    def next_player(self):
        self.turn = (self.turn + self.direction) % len(self.players)

    def play(self):
        print("Welcome to Uno!")
        print(f"The starting card is: {self.current_card}")
        print(f"Player {self.players[self.turn]}'s turn.")

        while True:
            current_player = self.players[self.turn]
            self.show_hand(current_player)
            card_played = input("Enter the card you want to play or 'draw' to draw a card: ").strip().lower()

            if card_played == 'draw':
                self.draw_card(current_player)
            elif self.is_valid_play(card_played):
                self.current_card = card_played
                current_player.remove(card_played)
                print(f"Player {current_player}'s card: {card_played}")
                if len(current_player) == 0:
                    print(f"Player {current_player} wins!")
                    break
                self.next_player()
            else:
                print("Invalid move. Try again.")

    def show_hand(self, player):
        print(f"Your hand: {', '.join(player)}")

    def draw_card(self, player):
        if len(self.deck) == 0:
            print("No cards left in the deck. Skipping turn.")
        else:
            player.append(self.deck.pop(0))
            print("Card drawn.")

    def is_valid_play(self, card_played):
        return card_played.split()[0] in self.current_card.split()[0] or \
               card_played.split()[1] in self.current_card.split()[1] or \
               card_played.split()[0] == "Wild" or \
               self.current_card.split()[0] == "Wild"

def main():
    num_players = int(input("Enter the number of players: "))
    players = [[] for _ in range(num_players)]

    for i in range(num_players):
        player_name = input(f"Enter name for Player {i + 1}: ")
        print(f"Welcome, {player_name}!")

    uno_game = UnoGame(players)
    uno_game.play()

if __name__ == "__main__":
    main()
