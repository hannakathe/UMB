from machine import Pin
from umqtt.simple import MQTTClient
import time

class MqttClient :
    def __init__(self, callback):
      MQTT_CLIENT_ID = "estacion-incendio"
      MQTT_BROKER    = "broker.hivemq.com"
      MQTT_USER      = ""
      MQTT_PASSWORD  = ""
      self.MQTT_TOPIC      = "alarma/piso-3"
      MQTT_TOPIC_SUSCRIBE   = "alarma/piso-3"

      led = Pin(2, Pin.OUT)
      led.value(0)
      def connect(): 
        print("Connecting to MQTT server... ", end="")
        client = MQTTClient(MQTT_CLIENT_ID, MQTT_BROKER, user=MQTT_USER, password=MQTT_PASSWORD)
        client.set_callback(callback)
        client.connect()
        client.subscribe(MQTT_TOPIC_SUSCRIBE)
        client.subscribe("alarma/piso-3")
        led.value(1)
        return client

      while True:
        try:
          self.client = connect()
          break
        except:
          print("Connection failed, retrying in 5 seconds...")
          time.sleep(5)

    def publish(self, msg):
        self.client.publish(self.MQTT_TOPIC, msg)