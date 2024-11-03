import tkinter as tk
from PIL import Image, ImageTk


class ImageManager:
    def __init__(self, tipo_clima):
        self.tipo_clima = tipo_clima

    def get_image(self, tipo_dato=None):
        images = {
            "Soleado": "ui/soleado.png",
            "Parcialmente nublado": "ui/parcialmente_nublado.png",
            "Lluvioso": "ui/lluvioso.png",
            "Nevado": "ui/nevado.png",
            "Vientos fuertes": "ui/viento_fuerte.png",
            "Nublado": "ui/nublado.png",
            "Heladas": "ui/heladas.png",
            "Tormentas": "ui/tormenta.png",
            "Calores extremos": "ui/temperaturas_extremas.png",
            "Frios extremos": "ui/temperaturas_extremas.png",
              
        }
        return images.get(tipo_dato or self.tipo_clima, "")

    def create_image_label(self, parent, tipo_dato=None, width=None, height=None, x=0, y=0):
        image_path = self.get_image(tipo_dato)
        if image_path:
            imagen_tk = tk.PhotoImage(file=image_path)
            if width and height:
                imagen_tk = imagen_tk.subsample(width=width, height=height)
            label_imagen = tk.Label(parent, image=imagen_tk)
            label_imagen.image = imagen_tk
            label_imagen.place(x=x, y=y)
            return label_imagen
        return None
    
    