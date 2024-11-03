from abc import ABC, abstractmethod
import math

class Figura(ABC):
    def __init__(self):
        pass
        
    @abstractmethod
    def calcular_area():
        pass
    

class Cuadrado(Figura):
    def __init__(self, lado):
        super().__init__()
        self.lado = lado
        
    def calcular_area(self):
        return self.lado**2
    

class Rectangulo(Figura):
    def __init__(self, base, altura):
        super().__init__()
        self.base = base
        self.altura = altura
        
    def calcular_area(self):
        return self.base * self.altura

class Triangulo(Figura):
    def __init__(self, base, altura):
        super().__init__()
        self.base = base
        self.altura = altura
        
    def calcular_area(self):
        return (self.base * self.altura) / 2

class Rombo(Figura):
    def __init__(self, diagonal_l, diagonal_c):
        super().__init__()
        self.diagonal_l = diagonal_l
        self.diagonal_c = diagonal_c
        
    def calcular_area(self):
        return (self.diagonal_l * self.diagonal_c) / 2

class Pentagono(Figura):
    def __init__(self, lado, apotema):
        super().__init__()
        self.lado = lado
        self.apotema = apotema
    
    def calcular_area(self):
        return (5 * self.lado * self.apotema) / 2

class Hexagono(Figura):
    def __init__(self, lado, apotema):
        super().__init__()
        self.lado = lado
        self.apotema = apotema
        
    def calcular_area(self):
        return (3 * math.sqrt(3) * self.apotema**2) / 2

class Circulo(Figura):
    def __init__(self, radio):
        super().__init__()
        self.radio = radio
        
    def calcular_area(self):
        return math.pi * self.radio**2

class Trapecio(Figura):
    def __init__(self, base_larga, base_corta, altura):
        super().__init__()
        self.base_larga = base_larga
        self.base_corta = base_corta
        self.altura = altura
        
    def calcular_area(self):
        return ((self.base_larga + self.base_corta) / 2) * self.altura

class Paralelogramo(Figura):
    def __init__(self, base, altura):
        super().__init__()
        self.base = base
        self.altura = altura
        
    def calcular_area(self):
        return self.base * self.altura

class Gestor_Figuras():
    def __init__(self):
        self.figuras = []
    
    def agregar_figura(self, figura):
        self.figuras.append(figura)
    
    def imprimir_areas(self):
        for figura in self.figuras:
            area = figura.calcular_area()
            print(f"El área de la {figura.__class__.__name__} es: {area}")

def main():            
    gestor_figuras = Gestor_Figuras()
    
    while True:
        print(""" 
              
            Figuras geometricas: 
            
            1. Cuadrado
            2. Rectangulo
            3. Triangulo
            4. Rombo
            5. Pentagono
            6. Hexagono
            7. Circulo
            8. Trapecio
            9. Paralelogramo
            10. Historial de areas calculadas
            11. Salir 
            
            """)
        
        opcion=input("Ingrese el número de la figura que desea calcular o '11' para salir: ")
        
        match opcion:
            
            case '1':
                lado = float(input("Ingrese la longitud del lado del cuadrado: "))
                cuadrado = Cuadrado(lado)
                gestor_figuras.agregar_figura(cuadrado)
                area = cuadrado.calcular_area()
                print(f"El área del cuadrado es: {area}")
            
            case '2':
                lado_1 = float(input("Ingrese el lado 1 del rectángulo: "))
                lado_2 = float(input("Ingrese el lado 2 del rectángulo: "))
                rectangulo = Rectangulo(lado_1, lado_2)
                gestor_figuras.agregar_figura(rectangulo)
                area = rectangulo.calcular_area()
                print(f"El área del rectangulo es: {area}")
                
            case '3':
                base = float(input("Ingrese la base del triángulo: "))
                altura = float(input("Ingrese la altura del triángulo: "))
                triangulo = Triangulo(base, altura)
                gestor_figuras.agregar_figura(triangulo)
                area = triangulo.calcular_area()
                print(f"El área del triángulo es: {area}")
                
            case '4':
                diagonal_l = float(input("Ingrese la diagonal mayor del rombo: "))
                diagonal_c = float(input("Ingrese la diagonal menor del rombo: "))
                rombo = Rombo(diagonal_l, diagonal_c)
                gestor_figuras.agregar_figura(rombo)
                area = rombo.calcular_area()
                print(f"El área del rombo es: {area}")
                
            case '5':
                lado = float(input("Ingrese el lado del pentágono: "))
                apotema = float(input("Ingrese la apotema del pentágono: "))
                pentagono = Pentagono(lado, apotema)
                gestor_figuras.agregar_figura(pentagono)
                area = pentagono.calcular_area()
                print(f"El área del pentagono es: {area}")
                
            case '6':
                lado = float(input("Ingrese el lado del hexágono: "))
                apotema = float(input("Ingrese la apotema del hexágono: "))
                hexagono = Hexagono(lado, apotema)
                gestor_figuras.agregar_figura(hexagono)
                area = hexagono.calcular_area()
                print(f"El área del hexagono es: {area}")
            
            case '7':
                radio = float(input("Ingrese el radio del círculo: "))
                circulo = Circulo(radio)
                gestor_figuras.agregar_figura(circulo)
                area = circulo.calcular_area()
                print(f"El área del círculo es: {area}")
            
            case '8':
                base_l = float(input("Ingrese la base larga del trapecio: "))
                base_c = float(input("Ingrese la base corta del trapecio: "))
                altura = float(input("Ingrese la altura del trapecio: "))
                trapecio = Trapecio(base_l,base_c,altura)
                gestor_figuras.agregar_figura(trapecio)
                area = trapecio.calcular_area()
                print(f"El área del trapecio es: {area}")
            
            case '9':
                base = float(input("Ingrese la base del paralelogramo: "))
                altura = float(input("Ingrese la altura del paralelogramo: "))
                paralelogramo = Paralelogramo(base,altura)
                gestor_figuras.agregar_figura(paralelogramo)
                area = paralelogramo.calcular_area()
                print(f"El área del paralelogramo es: {area}")
            
            case '10':
                print("Historial de calculos.")
                gestor_figuras.imprimir_areas()

            case '11':
                print("Saliendo del programa...")
                break
            
            case _:
                print("Opción no válida. Intente de nuevo.")

if __name__ == '__main__':
    main()