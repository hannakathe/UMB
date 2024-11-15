import network
import time


class Wifi:
    def __init__(self):

        print("Conectando al WiFi", end="")
        sta_if = network.WLAN(network.STA_IF)
        sta_if.active(True)
        sta_if.connect('Hannah', 'hanna2024')
        # sta_if.connect('FAMILIA CAMARGO', 'FAMILIA.CASTELLANOS1013')
        while not sta_if.isconnected():
            print(".", end="")
            time.sleep(0.1)
        print(" Connected!")