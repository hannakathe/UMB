import tkinter as tk
from image_manager import ImageManager
from PIL import Image, ImageTk


class FrameResultado(tk.Toplevel):
    def __init__(self, parent, tipo_clima, ciudad, fecha, temperatura, humedad, presion):
        super().__init__(parent)
        self.parent = parent
        self.tipo_clima = tipo_clima
        self.ciudad = ciudad
        self.fecha = fecha
        self.temperatura = temperatura
        self.humedad = humedad
        self.presion = presion
        self.config(bg="white")
        self.title("Resultado del Clima")
        width=1000
        height=500
        self.update_idletasks()  # Asegurarse de que la ventana está actualizada
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        # Calcular la posición x e y para centrar la ventana
        x = (screen_width // 2) - (width // 2)
        y = (screen_height // 2) - (height // 2)
        self.geometry(f"{width}x{height}+{x}+{y}")
        self.title("SkyScape - Simulación del clima")
        imagen_icono = Image.open("ui/icono_ventana.png")  
        self.icon_photo = ImageTk.PhotoImage(imagen_icono)
        self.iconphoto(True, self.icon_photo)

        self.image_manager = ImageManager(tipo_clima)
        self.create_widgets()

    def create_widgets(self):
        image_label = self.image_manager.create_image_label(self)
        if image_label:
            image_label.pack(padx=60, pady=60)

        resultado_label = tk.Label(
            self,
            text=f"El clima es: {self.tipo_clima}",
            font=("Comic Sans MS", 16),
            fg="#4361EE",
            bg="white",
        )
        resultado_label.pack(padx=20, pady=20)

        
        info_label = tk.Label(
            self,
            text=f"Ciudad: {self.ciudad}\n"
                 f"Fecha: {self.fecha}\n"
                 f"Temperatura: {self.temperatura}°C\n"
                 f"Humedad: {self.humedad}%\n"
                 f"Presión: {self.presion} hPa",
            font=("Comic Sans MS", 14),
            fg="#343a40",
            bg="white",
        )
        info_label.pack(pady=10)
        
    def show(self):
        self.transient(self.parent)
        self.grab_set()
        self.wait_window()
