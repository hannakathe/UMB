import tkinter as tk


class FrameIngreso(tk.Frame):
    def __init__(self, parent):
        super().__init__(parent, bg="white")
        self.parent = parent
        self.crear_widgets()

    def convertir_mayusculas(self, event):
        texto = self.entrada_ciudad.get()
        self.entrada_ciudad.delete(0, tk.END)
        self.entrada_ciudad.insert(0, texto.upper())
        
    def crear_widgets(self):
        
        
        label_ciudad = tk.Label(
            self, text="Ciudad:", font=("Comic Sans MS", 10, "bold"), bg="white",fg="#343a40"
        )
        label_ciudad.grid(row=0, column=0, padx=10, pady=5)
        self.entrada_ciudad = tk.Entry(self, width=15,relief=tk.FLAT,highlightbackground="#343a40", highlightthickness=1)
        self.entrada_ciudad.grid(row=0, column=1, padx=10, pady=5)
        self.entrada_ciudad.bind("<KeyRelease>", self.convertir_mayusculas)

        label_fecha = tk.Label(
            self, text="Fecha (DD/MM/AAAA):", font=("Comic Sans MS", 10, "bold"), bg="white",fg="#343a40"
        )
        label_fecha.grid(row=1, column=0, padx=10, pady=5)
        self.entrada_fecha = tk.Entry(self, width=15,relief=tk.FLAT,highlightbackground="#343a40", highlightthickness=1)
        self.entrada_fecha.grid(row=1, column=1, padx=10, pady=5)

        label_temperatura = tk.Label(
            self, text="Temperatura (°C):", font=("Comic Sans MS", 10, "bold"), bg="white",fg="#343a40"
        )
        label_temperatura.grid(row=2, column=0, padx=10, pady=5)
        self.entrada_temperatura = tk.Entry(self, width=15,relief=tk.FLAT,highlightbackground="#343a40", highlightthickness=1)
        self.entrada_temperatura.grid(row=2, column=1, padx=10, pady=5)

        label_humedad = tk.Label(
            self, text="Humedad (%):", font=("Comic Sans MS", 10, "bold"), bg="white",fg="#343a40"
        )
        label_humedad.grid(row=3, column=0, padx=10, pady=5)
        self.entrada_humedad = tk.Entry(self, width=15,relief=tk.FLAT,highlightbackground="#343a40", highlightthickness=1)
        self.entrada_humedad.grid(row=3, column=1, padx=10, pady=5)

        label_presion_at = tk.Label(
            self,text="Presión Atmosférica (hPa):",font=("Comic Sans MS", 10, "bold"),bg="white",fg="#343a40"
        )
        label_presion_at.grid(row=4, column=0, padx=10, pady=5)
        self.entrada_presion_at = tk.Entry(self, width=15,relief=tk.FLAT,highlightbackground="#343a40", highlightthickness=1)
        self.entrada_presion_at.grid(row=4, column=1, padx=10, pady=5)
