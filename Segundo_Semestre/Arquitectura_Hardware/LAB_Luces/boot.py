from machine import Pin
from utime import sleep

class Leds:
    def __init__(self, positions, pause=0.5, parpadeos=1):
        self.leds = [Pin(pos, Pin.OUT) for pos in positions]
        self.pause = pause
        self.parpadeos = parpadeos

class Ejercicio1(Leds):
    def encendido_individual(self, button_pin, reverse=False):
        indices = range(len(self.leds))
        if reverse:
            indices = reversed(indices)
        for i in indices:
            self._encender_unico_led(i)
            sleep(self.pause)
            if not Pin(button_pin, Pin.IN).value():
                break
                
    def _encender_unico_led(self, index):
        for i, led in enumerate(self.leds):
            led.value(1 if i == index else 0)

class Ejercicio2(Leds):
    def secuencia_izquierda_a_derecha(self, button_pin):
        for i in range(len(self.leds)):
            self._encender_unico_led(i)
            sleep(self.pause)
            if not Pin(button_pin, Pin.IN).value():
                break
    
    def secuencia_derecha_a_izquierda(self, button_pin):
        for i in reversed(range(len(self.leds))):
            self._encender_unico_led(i)
            sleep(self.pause)
            if not Pin(button_pin, Pin.IN).value():
                break

    def secuencia_alternante(self, button_pin):
        for i in range(0, len(self.leds), 2):
            self._encender_y_apagar_leds([i])
            sleep(self.pause)
            if not Pin(button_pin, Pin.IN).value():
                break
        for i in range(1, len(self.leds), 2):
            self._encender_y_apagar_leds([i])
            sleep(self.pause)
            if not Pin(button_pin, Pin.IN).value():
                break
    
    def _encender_unico_led(self, index):
        for i, led in enumerate(self.leds):
            led.value(1 if i == index else 0)
    
    def _encender_y_apagar_leds(self, indices):
        for i, led in enumerate(self.leds):
            led.value(1 if i in indices else 0)

class Ejercicio3(Leds):
    def parpadeo_secuencial(self, button_pin, button_pin1):
        while Pin(button_pin, Pin.IN).value() and Pin(button_pin1, Pin.IN).value():
            # Recorrido de derecha a izquierda
            for led in reversed(self.leds):
                led.value(1)
                sleep(self.pause)
                if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                    break
                led.value(0)
                sleep(self.pause)
                if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                    break
            
            # Verificar si los botones siguen presionados antes de invertir la secuencia
            if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                break

            # Recorrido de izquierda a derecha
            for led in self.leds:
                led.value(1)
                sleep(self.pause)
                if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                    break
                led.value(0)
                sleep(self.pause)
                if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                    break

class Ejercicio4(Leds):
    def parpadeo_individual(self, button_pin, reverse=False):
        leds = self.leds[::-1] if reverse else self.leds
        for led in leds:
            for _ in range(self.parpadeos * 2):
                led.value(not led.value())
                sleep(self.pause)
                if not Pin(button_pin, Pin.IN).value():
                    break

class Ejercicio5(Leds):
    def parpadeo_general(self, button_pin, button_pin1, reverse=False):
        while Pin(button_pin, Pin.IN).value() and Pin(button_pin1, Pin.IN).value():
            leds = self.leds[::-1] if reverse else self.leds
            for _ in range(self.parpadeos * 2):
                for led in leds:
                    for _ in range(3):  
                        led.value(1)  
                        sleep(0.05)  
                        led.value(0)  
                        sleep(0.5)  
                        if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                            break
                    if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                        break
                if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                    break

class Ejercicio6(Leds):
    def parpadeo_parcial(self, button_pin, button_pin1, inicio, final, reverse=False):
        while Pin(button_pin, Pin.IN).value() and Pin(button_pin1, Pin.IN).value():
            leds = self.leds[inicio:final]
            if reverse:
                leds = reversed(leds)
            for led in leds:
                for _ in range(self.parpadeos * 2):
                    led.value(not led.value())
                    sleep(self.pause)
                    if not Pin(button_pin, Pin.IN).value() or not Pin(button_pin1, Pin.IN).value():
                        break



class SecuenciaCarro:
    cantidad_leds = 0
    cantidad_mitad_leds = 0
    pines_leds = []

    def __init__(self, pines_leds):
        self.pines_leds = pines_leds
        self.cantidad_leds = len(pines_leds)
        self.cantidad_mitad_leds = int(self.cantidad_leds / 2)

    # Funciones generales
    def ejecutarLeds(self, leds, estado, esperar=0):
        for i in leds:
            i.value(estado)
        sleep(esperar)

    def apagarLeds(self, leds, esperar=0):
        self.ejecutarLeds(leds, 0, esperar)

    def encenderLeds(self, leds, esperar=0):
        self.ejecutarLeds(leds, 1, esperar)

    def encenderApagarVariosLedsAlMismoTiempo(self, leds, cantidad_parpadeo=1):
        for i in range(cantidad_parpadeo):
            esperar = 0.1
            self.encenderLeds(leds, esperar)
            self.apagarLeds(leds, esperar)

    # Secuencias carro
    def unLedEnLinea(self, leds):
        for i in leds:
            i.value(1)
            sleep(0.1)
            i.value(0)

    def variosLedsEnLinea(self, leds):
        leds_estado = []
        def runEstados(leds_estado):
            sleep(0.15)
            for o in leds_estado:
                if (o[1] == 0):
                    o[1] = 1
                elif (o[1] == 1):
                    o[1] = 2
                elif (o[1] == 2):
                    o[1] = 3
                    o[0].value(1)
                elif (o[1] == 3):
                    o[1] = 0
                    o[0].value(0)
                leds_estado[0][0].value(1)

        for i in leds:
            leds_estado.append([i, 1])
            runEstados(leds_estado)

        size = len(leds) * 2
        while size:
            size = size - 1
            runEstados(leds_estado)

    def encenderMitadPorMitad(self, leds):
        self.encenderApagarVariosLedsAlMismoTiempo(leds[:self.cantidad_mitad_leds], 3)
        self.encenderApagarVariosLedsAlMismoTiempo(leds[self.cantidad_mitad_leds:], 3)

    def encenderLedsParalelos(self, leds, cantidad_repeticiones=1, adentro_hacia_afuera=False):
        rango = range(self.cantidad_mitad_leds)
        if adentro_hacia_afuera:
            rango = reversed(rango)
        for o in range(cantidad_repeticiones):
            for i in rango:
                temp_leds = [leds[i], leds[self.cantidad_leds - i - 1]]
                self.encenderApagarVariosLedsAlMismoTiempo(temp_leds, 3)
            self.encenderApagarVariosLedsAlMismoTiempo(leds, 3)

    def encender_todos_adentro_hacia_afuera(self, leds, cantidad_repeticiones=1, afuera_hacia_dentro=False):
        esperar = 0.1
        for i in range(cantidad_repeticiones):
            for i in reversed(range(self.cantidad_mitad_leds)):
                temp_leds = [leds[i], leds[self.cantidad_leds - i - 1]]
                self.encenderLeds(temp_leds, esperar)
            for i in range(self.cantidad_mitad_leds):
                temp_leds = [leds[i], leds[self.cantidad_leds - i - 1]]
                self.apagarLeds(temp_leds, esperar)

    def encender_parcial_adentro_hacia_afuera(self, leds, cantidad_repeticiones=1, afuera_hacia_dentro=False):
        esperar = 0.05
        for i in range(cantidad_repeticiones):
            for i in reversed(range(self.cantidad_mitad_leds)):
                temp_leds = [leds[i], leds[self.cantidad_leds - i - 1]]
                self.encenderApagarVariosLedsAlMismoTiempo(temp_leds, esperar)
            for i in range(self.cantidad_mitad_leds):
                temp_leds = [leds[i], leds[self.cantidad_leds - i - 1]]
                self.encenderApagarVariosLedsAlMismoTiempo(temp_leds, esperar)

    def run(self, cantidad_repeticiones, un_led_en_linea=True, varios_leds_en_linea=True, mitad_por_mitad=True, leds_paralelos=True, todos_adentro_hacia_afuera=True, parcial_adentro_hacia_afuera=True):
        leds = [Pin(i, Pin.OUT) for i in self.pines_leds]
        self.apagarLeds(leds)

        while True:
            if un_led_en_linea:
                temp_cantidad = cantidad_repeticiones
                while temp_cantidad:
                    temp_cantidad = temp_cantidad - 1
                    self.unLedEnLinea(leds)
                    self.unLedEnLinea(leds[::-1])
                    self.apagarLeds(leds)

            if varios_leds_en_linea:
                temp_cantidad = cantidad_repeticiones
                while temp_cantidad:
                    temp_cantidad = temp_cantidad - 1
                    self.variosLedsEnLinea(leds)
                    self.apagarLeds(leds)
                    self.variosLedsEnLinea(leds[::-1])
                    self.apagarLeds(leds)

            if mitad_por_mitad:
                temp_cantidad = cantidad_repeticiones
                while temp_cantidad:
                    temp_cantidad = temp_cantidad - 1
                    self.encenderMitadPorMitad(leds)
                    self.apagarLeds(leds)

            if leds_paralelos:
                self.encenderLedsParalelos(leds, cantidad_repeticiones)
                sleep(0.1)
                self.apagarLeds(leds)
                self.encenderLedsParalelos(leds, cantidad_repeticiones, True)
                self.apagarLeds(leds)

            if todos_adentro_hacia_afuera:
                self.encender_todos_adentro_hacia_afuera(leds, cantidad_repeticiones)
                self.apagarLeds(leds)

            if parcial_adentro_hacia_afuera:
                self.encender_parcial_adentro_hacia_afuera(leds, cantidad_repeticiones)
                self.apagarLeds(leds)

            self.encenderApagarVariosLedsAlMismoTiempo(leds, 3)




