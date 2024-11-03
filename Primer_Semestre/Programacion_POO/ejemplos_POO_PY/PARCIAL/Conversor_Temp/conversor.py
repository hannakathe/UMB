import requests

#! TENER EN CUENTA

#! EL MODULO requests DEBE SER IMPORTADO, SI NO ESTA INSTALLADO SE INSTALA DE ESTA FORMA
# python -m pip install requests ó pip install requests
#! DICHA INSTALACION SE DEBE REALIZAR DESDE LA TERMINAL O CONSOLA

#! SE RECOMIENDA CORRER EL CODIGO EN VERSIONES 3.7.9 O ANTERIORES PARA EFICIENCIA DE INSTALACION DE REQUEST


class Ciudad:
    def __init__(self, ciudad, temp_ciudad):
        self.ciudad = ciudad
        self.temp_ciudad = temp_ciudad
        
    
    @staticmethod
    def obtener_temperatura(ciudad):
        url = f'https://api.openweathermap.org/data/2.5/weather?q={ciudad}&appid=06d4164c7de9051bf78e610a114d9d28&units=metric'
        respuesta = requests.get(url)
        datos = respuesta.json()

        if respuesta.status_code == 200:
            temp_ciudad = datos['main']['temp']
            return temp_ciudad
        else:
            print(f'Error al obtener la temperatura: {datos["message"]}')
            return None
    

class ConvTemp:
    def __init__(self, kelvin, fahrenheit):
        self.kelvin = kelvin
        self.fahrenheit = fahrenheit

    @staticmethod
    def convertir_temperatura(temp_ciudad, opcion_conversion):
        if opcion_conversion == '1':
            return temp_ciudad + 273.15
        elif opcion_conversion == '2':
            return (temp_ciudad * 9 / 5) + 32
        else:
            return None
            

def main():
    while True:
        ciudad = input("""Ingresa el nombre de la ciudad para obtener su temperatura o
escribe "exit" para salir: """)

        if ciudad.lower() == 'exit':
            break

        temperatura_ac_ciudad = Ciudad.obtener_temperatura(ciudad)

        if temperatura_ac_ciudad is not None:
            print(f'La temperatura actual en {ciudad} es de {temperatura_ac_ciudad}°C.')
        else:
            print('No se pudo obtener la temperatura.')

        opcion_conversion = input("""Ingresa el tipo de conversión que deseas realizar:
                    1. Celsius a Kelvin
                    2. Celsius a Fahrenheit
                    
                    Ingrese tu opción: """)

        temperatura_convertida = ConvTemp.convertir_temperatura(temperatura_ac_ciudad, opcion_conversion)

        if temperatura_convertida is not None:
            if opcion_conversion == '1':
                print("")
                print(f'La temperatura convertida en {ciudad} es de {temperatura_convertida}°K.')
                print("")
            elif opcion_conversion == '2':
                print("")
                print(f'La temperatura convertida en {ciudad} es de {temperatura_convertida}°F.')
                print("")
        else:
            print('Opción de conversión no válida.')


if __name__ == '__main__':
    main()
    


        
