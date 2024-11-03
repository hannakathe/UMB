from machine import Pin, PWM
import wifi
import mqttClient
import time
import oled


def callback(var):
    print(var)
    oled_display = oled.Oled()
    oled_screen = oled_display.obtener_oled() 
    oled_screen.fill(0) 
    oled_screen.text(var, 10, 10) 
    oled_screen.show()
    
wifi.Wifi()
client = mqttClient.MqttClient(callback)



# Definir el pin al que está conectado el sensor de fuego y el buzzer
fire_sensor_pin = Pin(27, Pin.IN)  # Cambia 27 por el pin que uses en tu ESP32 para el sensor
buzzer_pin = Pin(26, Pin.OUT)      # Cambia 26 por el pin que uses para el buzzer
print("Marcador 1: Iniciando el programa...")

# Definir las notas de la canción de Mario Bros (frecuencias en Hz)
tones = {
    'E7': 2637, 'E6': 1319, 'A6': 1760, 'B6': 1976, 'C7': 2093,
    'D7': 2349, 'G6': 1568, 'F6': 1397, 'C6': 1047,
    'A5': 880,  'B5': 987,  'D6': 1175, 'G5': 784,  'E5': 659,
}

# Definir la duración de las notas (en milisegundos)
melody = [
    ('E7', 200), ('E7', 200), (0, 200), ('E7', 200), (0, 100), ('C7', 200), 
    ('E7', 200), (0, 100), ('G6', 200), (0, 300), ('G5', 200), (0, 300),
    ('C6', 200), ('G5', 200), ('E5', 200), ('A5', 200), ('B5', 200), ('A6', 200), 
    ('G6', 200), ('E6', 200), ('C6', 200), ('D6', 200), ('B5', 200), (0, 200)
]



# Función para reproducir las notas
def play_tone(pin, frequency, duration):
    if frequency == 0:  # Si es "0", es un silencio
        time.sleep_ms(duration)
    else:
        pwm = PWM(pin)
        pwm.freq(frequency)
        pwm.duty(512)  # Definir el duty cycle al 50%
        time.sleep_ms(duration)
        pwm.deinit()  # Detener la señal PWM

def play_mario():
    print("Marcador 7: Reproduciendo melodía de Mario...")
    for note, duration in melody:
        frequency = tones.get(note, 0)  # Obtener la frecuencia de la nota o silencio
        play_tone(buzzer_pin, frequency, duration)

# Verifica si hay fuego y publica en MQTT
def check_fire():
    if fire_sensor_pin.value() == 0:
        print("Marcador 8: ¡Fuego detectado!")
        play_mario()
        client.publish("1")
    else:
        print("Marcador 9: No hay fuego.")
        client.publish("0")
        


# Bucle principal
def main():
    try:
        print("Marcador 12: Iniciando el programa principal...")

        while True:
            check_fire()
            time.sleep(1)  # Esperar 1 segundo antes de verificar nuevamente
    except KeyboardInterrupt:
        print("Marcador 13: Detenido por el usuario.")
    
main()