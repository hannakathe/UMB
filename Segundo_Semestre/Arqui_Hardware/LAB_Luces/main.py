from boot import Ejercicio1, Ejercicio2, Ejercicio3, Ejercicio4, Ejercicio5, Ejercicio6, SecuenciaCarro
from machine import Pin
import time

# Configuración de pines y variables
leds_position = [32, 33, 25, 26, 27, 14, 12, 13]
pause = 0.5
parpadeos = 1

# Crear instancias de los ejercicios
ej1 = Ejercicio1(leds_position, pause)
ej2 = Ejercicio2(leds_position, pause)
ej3 = Ejercicio3(leds_position, pause)
ej4 = Ejercicio4(leds_position, pause, parpadeos)
ej5 = Ejercicio5(leds_position, pause)
ej6 = Ejercicio6(leds_position, pause, parpadeos)
carro=SecuenciaCarro(leds_position)


# Configuración de los botones
s1 = Pin(21, Pin.IN, Pin.PULL_UP)
s2 = Pin(19, Pin.IN,Pin.PULL_UP)
s3 = Pin(18, Pin.IN,Pin.PULL_UP)

def apagar_leds():
    for pin in leds_position:
        led = Pin(pin, Pin.OUT)
        led.value(0)

# Bucle principal

while True:
    if s1.value() == 0 and s2.value() == 0 and s3.value() == 1: #SECUENCIA 1D
        ej1.encendido_individual(reverse=True, button_pin=s3) 
    elif s1.value() == 0 and s2.value() == 1 and s3.value() == 0: #SECUENCIA 2I
        ej2.secuencia_izquierda_a_derecha(button_pin=s2)
    elif s1.value() == 0 and s2.value() == 1 and s3.value() == 1: #SECUENCIA 3DI
        ej3.parpadeo_secuencial(18,19)
    elif s1.value() == 1 and s2.value() == 0 and s3.value() == 0: #SECUENCIA 4 , SE PRENDEN TODAS LAS LUCES SI SE DEJA DE PRECIONAR EL BOTON
        ej4.parpadeo_individual(button_pin=s1)
    elif s1.value() == 1 and s2.value() == 0 and s3.value() == 1: # SECUENCIA 5, CADA LED PARPADEA 3 VECES
        ej5.parpadeo_general(21,18)
    elif s1.value() == 1 and s2.value() == 1 and s3.value() == 0:
        ej6.parpadeo_parcial(21,19,2,5)
    elif s1.value() == 1 and s2.value() == 1 and s3.value() == 1: # SECUENCIA 7 - Los tres botones presionados
        carro = SecuenciaCarro(leds_position)
        carro.run(2)

    else:
        apagar_leds()

    time.sleep(0.1)  # Pausa para evitar múltiples activaciones y reducir carga en el microcontrolador



    
    





