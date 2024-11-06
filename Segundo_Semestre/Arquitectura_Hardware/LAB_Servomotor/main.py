# main.py -- put your code here!

#------------------------------ [IMPORT]------------------------------------
import network, time
from machine import Pin, ADC, Timer
from umqtt.simple import MQTTClient

#--------------------------- [OBJETOS]---------------------------------------
temporiza = Timer(0)  # se instancia un objeto de la clase Timer
prev_weather = 0

# MQTT Server Parameters
MQTT_CLIENT_ID = "pepaping-777"
MQTT_BROKER    = "broker.hivemq.com"
MQTT_USER      = ""
MQTT_PASSWORD  = ""
topic_pub      = 'lobodinamita/bum'

#----------------------[ CONECTAR WIFI ]---------------------------------------------------------#
print("Conectando al WiFi", end="")
sta_if = network.WLAN(network.STA_IF)
sta_if.active(True)
sta_if.connect('iPhone de Harold', 'harold123')
while not sta_if.isconnected():
    print(".", end="")
    time.sleep(0.1)
print(" Connected!")

#----------------------[ CONECTAR BROKER ]---------------------------------------------------------#
print("Connecting to MQTT server... ", end="")
client = MQTTClient(MQTT_CLIENT_ID, MQTT_BROKER, user=MQTT_USER, password=MQTT_PASSWORD)
client.connect()
print("Connected!")

#----------------------[ MAPEO DEL POTENCIÓMETRO ]------------------------------------------------#
# Función para mapear un valor de entrada a un rango de salida
def mapear(valor_sensor, min_entrada, max_entrada, min_salida, max_salida):
    return min_salida + (max_salida - min_salida) * (valor_sensor - min_entrada) / (max_entrada - min_entrada)

#----------------------[ TIMER INTERRUPCIÓN ]-----------------------------------------------------#
# Configurar el potenciómetro en un pin de entrada analógica
potenciometro = ADC(Pin(34))
potenciometro.atten(ADC.ATTN_11DB)  # Rango de lectura del ADC (0-4095)

# Función que publica el valor del ángulo basado en el potenciómetro
def desborde(timer):
    global prev_weather
    
    # Leer valor del potenciómetro (ADC de 0 a 4095)
    valor_potenciometro = potenciometro.read()
    
    # Mapear el valor del potenciómetro a ángulo (0-180 grados)
    angulo = mapear(valor_potenciometro, 0, 4095, 0, 180)

    # Publicar el ángulo al servidor MQTT
    mensaje = f"Ángulo: {int(angulo)} grados"
    client.publish(topic_pub, mensaje)
    
    print(f"Ángulo publicado: {int(angulo)} grados")

# Configurar el temporizador para ejecutar la función cada 1 segundo (1000 ms)
temporiza.init(period=1000, mode=Timer.PERIODIC, callback=desborde)
