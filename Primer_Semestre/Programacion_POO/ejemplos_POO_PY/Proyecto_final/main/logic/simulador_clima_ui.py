import tkinter as tk
from tkinter import messagebox
from PIL import Image, ImageTk
from frame_ingreso import FrameIngreso
from frame_resultado import FrameResultado
from simulador_clima_logic import CalculoClima

#!EN ESTE FRAME SE CORRE EL CODIGO

class VentanaPrincipal(tk.Tk): #sobrecarga de metodos 
    def __init__(self, clima_calculator):
        super().__init__()

        width=1000
        height=500
        self.update_idletasks()  # Asegurarse de que la ventana está actualizada
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        # Calcular la posición x e y para centrar la ventana en la pantalla
        x = (screen_width // 2) - (width // 2)
        y = (screen_height // 2) - (height // 2)
        self.geometry(f"{width}x{height}+{x}+{y}")
        self.clima_calculator = clima_calculator
        self.config(bg="white")
        self.crear_contenido()
        self.title("SkyScape")
        imagen_icono = Image.open("ui/icono_ventana.png")  # Cambia "ruta_de_tu_icono.png" por la ruta de tu icono en formato PNG
        self.icon_photo = ImageTk.PhotoImage(imagen_icono)
        self.iconphoto(True, self.icon_photo)
        
        
        
    def salir_pantalla_completa(self, event=None):
        self.attributes("-fullscreen", False)

    def crear_contenido(self):
        label_bienvenida = tk.Label(
            self,
            text="Bienvenido a SkyScape",
            font=("Comic Sans MS", 24, "bold"),
            pady=10,
            bg="white",
        )
        label_bienvenida.pack(side=tk.TOP, fill=tk.X)
        label_bienvenida2 = tk.Label(
            self,
            text="Simulación del Clima",
            font=("Comic Sans MS", 30, "bold"),
            pady=10,
            fg="#4361EE",
            bg="white",
        )
        label_bienvenida2.pack(side=tk.TOP, fill=tk.X)

        texto = """SkyScape es una herramienta avanzada de simulación del clima que te permite explorar y comprender
        los diferentes fenómenos meteorológicos de forma interactiva."""
        label_bienvenida = tk.Label(
            self, text=texto, font=("Comic Sans MS", 14), pady=10, bg="white", fg="#000814"
        )
        label_bienvenida.pack(side=tk.TOP, fill=tk.X)

        self.frame_ingreso = FrameIngreso(self)
        self.frame_ingreso.pack()

        boton_calcular = tk.Button(
            self,
            text="Calcular Clima",
            relief=tk.RAISED,
            command=self.calcular_clima,
            width=15,
            font=("Comic Sans MS", 10, "bold"),
        )
        boton_calcular.pack(pady=10)

        
        boton_salir = tk.Button(
            self,
            text="Salir",
            relief=tk.RAISED,
            command=self.salir_programa,
            width=10,
            font=("Comic Sans MS", 10, "bold"),
        )
        boton_salir.place(relx=0.98, rely=0.98, anchor="se")

    def salir_programa(self):
        self.destroy()

    def calcular_clima(self):
        try:
            ciudad = self.frame_ingreso.entrada_ciudad.get()
            fecha = self.frame_ingreso.entrada_fecha.get()
            temperatura = int(self.frame_ingreso.entrada_temperatura.get())
            humedad = int(self.frame_ingreso.entrada_humedad.get())
            presion = int(self.frame_ingreso.entrada_presion_at.get())

            clima = CalculoClima(ciudad, fecha, temperatura, humedad, presion)
            tipo_clima = clima.determinar_clima()

            # Crear la ventana de resultado antes de cerrar la ventana principal
            resultado = FrameResultado(self, tipo_clima,ciudad,fecha,temperatura,humedad, presion)
            resultado.show()
            

        except ValueError:
            messagebox.showerror(
                "Error",
                "Ingrese valores numéricos válidos para temperatura, humedad y presión.",
            )

if __name__ == "__main__":
    app = VentanaPrincipal(CalculoClima)
    app.mainloop()
