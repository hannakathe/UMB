from machine import Pin, SoftI2C, ADC
import ssd1306

class Oled:
    def __init__(self):
        i2c = SoftI2C(scl=Pin(32), sda=Pin(33))
        oled_width = 128
        oled_height = 64
        self.oled = ssd1306.SSD1306_I2C(oled_width, oled_height, i2c)

    def obtener_oled(self):
        return self.oled