from abc import ABC, abstractmethod
import requests

class Clima(ABC):
    def __init__(self, temperatura, humedad, presion):
        self.temperatura = temperatura
        self.humedad = humedad
        self.presion = presion

    @abstractmethod
    def obtener_condiciones(self):
        pass

class CalculosClimaCalido:
    def __init__(self, clima: Clima):
        self.clima = clima

    @abstractmethod
    def determinar_clima(self):
        pass

    def soleado(self, temperatura, humedad, presion):
        return (
            temperatura >= 30
            and temperatura <= 35
            and humedad >= 40
            and humedad <= 45
            and presion >= 1005
            and presion <= 1010
        )
    def parcialmente_nublado(self, temperatura, humedad, presion):
        return (
            temperatura >= 16
            and temperatura <= 19
            and humedad >= 85
            and humedad <= 90
            and presion >= 1000
            and presion <= 1005
        )
    def lluvioso(self, temperatura, humedad, presion):
        return (
            temperatura >= 20
            and temperatura <= 24
            and humedad >= 70
            and humedad <= 85
            and presion >= 980
            and presion <= 1000
        )
    def viento_fuerte(self, temperatura, humedad, presion):
        return (
            temperatura >= 18
            and temperatura <= 23
            and humedad >= 65
            and humedad <= 70
            and presion >= 980
            and presion <= 1010
        )
    def niebla(self, temperatura, humedad, presion):
        return (
            temperatura >= 11
            and temperatura <= 15
            and humedad >= 98
            and humedad <= 100
            and presion >= 980
            and presion <= 990
        )
    def heladas(self, temperatura, humedad, presion):
        return (
            temperatura >= 0
            and temperatura <= 5
            and humedad >= 38
            and humedad <= 43
            and presion >= 990
            and presion <= 1000
        )
    def tormenta(self, temperatura, humedad, presion):
        return (
            temperatura >= 24
            and temperatura <= 27
            and humedad >= 93
            and humedad <= 97
            and presion >= 998
            and presion <= 1002
        )
    def calor_extremo(self, temperatura, humedad, presion):
        return (
            temperatura >= 40
            and temperatura <= 45
            and humedad >= 18
            and humedad <= 22
            and presion >= 1010
            and presion <= 1015
        )
    def frio_extremo(self, temperatura, humedad, presion):
        return (
            temperatura >= -10
            and temperatura <= 0
            and humedad >= 40
            and humedad <= 45
            and presion >= 980
            and presion <= 990
        )
        
class CalculosClimaFrio:
    def __init__(self, clima: Clima):
        self.clima = clima

    @abstractmethod
    def determinar_clima(self):
        pass

    def soleado(self, temperatura, humedad, presion):
        return (
            temperatura >= 20
            and temperatura <= 25
            and humedad >= 23
            and humedad <= 27
            and presion >= 1013
            and presion <= 1017
        )
    def parcialmente_nublado(self, temperatura, humedad, presion):
        return (
            temperatura >= 7
            and temperatura <= 12
            and humedad >= 75
            and humedad <= 80
            and presion >= 1006
            and presion <= 1011
        )
    def lluvioso(self, temperatura, humedad, presion):
        return (
            temperatura >= 16
            and temperatura <= 20
            and humedad >= 60
            and humedad <= 65
            and presion >= 1008
            and presion <= 1012
        )
    def nieve(self, temperatura, humedad, presion):
        return (
            temperatura >= -5
            and temperatura <= 5
            and humedad >= 70
            and humedad <= 75
            and presion >= 993
            and presion <= 1002
        )
    def viento_fuerte(self, temperatura, humedad, presion):
        return (
            temperatura >= 13
            and temperatura <= 17
            and humedad >= 40
            and humedad <= 45
            and presion >= 1020
            and presion <= 1025
        )
    def niebla(self, temperatura, humedad, presion):
        return (
            temperatura >= 5
            and temperatura <= 10
            and humedad >= 90
            and humedad <= 97
            and presion >= 998
            and presion <= 1002
        )
    def heladas(self, temperatura, humedad, presion):
        return (
            temperatura >= -15
            and temperatura <= -10
            and humedad >= 33
            and humedad <= 37
            and presion >= 1003
            and presion <= 1007
        )
    def tormenta(self, temperatura, humedad, presion):
        return (
            temperatura >= 18
            and temperatura <= 23
            and humedad >= 85
            and humedad <= 90
            and presion >= 1003
            and presion <= 1010
        )
    def calor_extremo(self, temperatura, humedad, presion):
        return (
            temperatura >= 30
            and temperatura <= 35
            and humedad >= 30
            and humedad <= 40
            and presion >= 1013
            and presion <= 1017
        )
    def frio_extremo(self, temperatura, humedad, presion):
        return (
            temperatura >= -23
            and temperatura <= -18
            and humedad >= 23
            and humedad <= 27
            and presion >= 998
            and presion <= 1003
        )

class ClimaTipico:
    def __init__(self, ciudad, fecha):
        self.ciudad = ciudad
        self.fecha = fecha

    def obtener_clima_ciudad_fecha(self):
        url = f'https://api.openweathermap.org/data/2.5/weather?q={self.ciudad}&appid=06d4164c7de9051bf78e610a114d9d28&units=metric'
        respuesta = requests.get(url)
        datos = respuesta.json()

        if respuesta.ok:
            temperatura = datos['main']['temp']
            return "Cálido" if temperatura > 20 else "Frío"
        else:
            print("Error al obtener datos del clima.")
            return None

class CalculoClima(Clima, CalculosClimaCalido, CalculosClimaFrio):
    def __init__(self, ciudad, fecha, temperatura, humedad, presion):
        Clima.__init__(self, temperatura, humedad, presion)
        CalculosClimaCalido.__init__(self, self)
        CalculosClimaFrio.__init__(self, self)
        
        self.ciudad = ciudad
        self.fecha = fecha

    def determinar_clima(self):
        condiciones = self.obtener_condiciones()

        if self.temperatura >=25 :
            print(f"El clima en {self.ciudad} el {self.fecha} es normalmente Calido.")

            if self.soleado(*condiciones):
                return "Soleado"
            if self.parcialmente_nublado(*condiciones):
                return "Parcialmente nublado"
            if self.lluvioso(*condiciones):
                return "Lluvioso"
            if self.nieve(*condiciones):  # Este método no debería estar aquí en cálido
                return "Nevado"
            if self.viento_fuerte(*condiciones):
                return "Vientos fuertes"
            if self.niebla(*condiciones):
                return "Nublado"
            if self.heladas(*condiciones):
                return "Heladas"
            if self.tormenta(*condiciones):
                return "Tormentas"
            if self.calor_extremo(*condiciones):
                return "Calores extremos"
            if self.frio_extremo(*condiciones):  # Este método no debería estar aquí en cálido
                return "Frios extremos"
            return "Condiciones no coinciden con ningún clima listado aquí"
        
        
        else:
            print(f"El clima en {self.ciudad} el {self.fecha} es normalmente Frio.")
            if self.soleado(*condiciones):
                return "Soleado"
            if self.parcialmente_nublado(*condiciones):
                return "Parcialmente nublado"
            if self.lluvioso(*condiciones):
                return "Lluvioso"
            if self.nieve(*condiciones):
                return "Nevado"
            if self.viento_fuerte(*condiciones):
                return "Vientos fuertes"
            if self.niebla(*condiciones):
                return "Nublado"
            if self.heladas(*condiciones):
                return "Heladas"
            if self.tormenta(*condiciones):
                return "Tormentas"
            if self.calor_extremo(*condiciones):
                return "Calores extremos"
            if self.frio_extremo(*condiciones):
                return "Frios extremos"
            return "Condiciones no coinciden con ningún clima listado aquí"  
        
    
    def obtener_condiciones(self):
        return (self.temperatura, self.humedad, self.presion)


