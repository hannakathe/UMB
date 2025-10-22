import sqlite3

# Conectar a la base de datos (se crea si no existe)
conn = sqlite3.connect("Usuarios_UMB.db")
c = conn.cursor()

# ---------------------------
# Crear tabla Pais
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Pais (
    cod_pais INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_pais TEXT NOT NULL
)
""")

# ---------------------------
# Crear tabla Ciudad
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Ciudad (
    cod_ciudad INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_ciudad TEXT NOT NULL,
    cod_pais INTEGER,
    FOREIGN KEY(cod_pais) REFERENCES Pais(cod_pais)
)
""")

# ---------------------------
# Crear tabla Cliente
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Cliente (
    tipoID TEXT,
    nroID TEXT PRIMARY KEY,
    nombres TEXT NOT NULL,
    correo TEXT,
    direccion TEXT,
    celular TEXT,
    cod_ciudad INTEGER,
    cod_pais INTEGER,
    FOREIGN KEY(cod_ciudad) REFERENCES Ciudad(cod_ciudad),
    FOREIGN KEY(cod_pais) REFERENCES Pais(cod_pais)
)
""")

# ---------------------------
# Insertar datos en Pais (de todos los continentes)
# ---------------------------
paises = [
    # América
    ("Colombia",),
    ("México",),
    ("Argentina",),
    ("Estados Unidos",),
    ("Brasil",),
    ("Canadá",),
    ("Chile",),
    ("Perú",),
    ("Ecuador",),
    ("Venezuela",),

    # Europa
    ("España",),
    ("Francia",),
    ("Alemania",),
    ("Italia",),
    ("Reino Unido",),
    ("Portugal",),
    ("Países Bajos",),
    ("Suecia",),
    ("Noruega",),
    ("Suiza",),

    # Asia
    ("China",),
    ("Japón",),
    ("India",),
    ("Corea del Sur",),
    ("Arabia Saudita",),
    ("Israel",),
    ("Tailandia",),
    ("Indonesia",),
    ("Vietnam",),
    ("Filipinas",),

    # África
    ("Sudáfrica",),
    ("Nigeria",),
    ("Egipto",),
    ("Marruecos",),
    ("Kenia",),
    ("Ghana",),
    ("Argelia",),
    ("Etiopía",),
    ("Túnez",),
    ("Senegal",),

    # Oceanía
    ("Australia",),
    ("Nueva Zelanda",),
    ("Fiyi",)
]
c.executemany("INSERT INTO Pais(nombre_pais) VALUES(?)", paises)

# ---------------------------
# Insertar datos en Ciudad (principales de cada país)
# ---------------------------
ciudades = [
    # América
    ("Bogotá", 1), ("Medellín", 1), ("Cali", 1), ("Barranquilla", 1), ("Cartagena", 1),
    ("Ciudad de México", 2), ("Guadalajara", 2), ("Monterrey", 2), ("Puebla", 2), ("Tijuana", 2),
    ("Buenos Aires", 3), ("Córdoba", 3), ("Rosario", 3), ("Mendoza", 3), ("La Plata", 3),
    ("Nueva York", 4), ("Los Ángeles", 4), ("Chicago", 4), ("Houston", 4), ("Miami", 4),
    ("São Paulo", 5), ("Río de Janeiro", 5), ("Brasilia", 5), ("Salvador", 5), ("Fortaleza", 5),
    ("Toronto", 6), ("Vancouver", 6), ("Montreal", 6), ("Calgary", 6), ("Ottawa", 6),
    ("Santiago", 7), ("Valparaíso", 7), ("Concepción", 7), ("Antofagasta", 7), ("Temuco", 7),
    ("Lima", 8), ("Arequipa", 8), ("Cusco", 8), ("Trujillo", 8), ("Piura", 8),
    ("Quito", 9), ("Guayaquil", 9), ("Cuenca", 9), ("Ambato", 9), ("Manta", 9),
    ("Caracas", 10), ("Maracaibo", 10), ("Valencia", 10), ("Barquisimeto", 10), ("Mérida", 10),

    # Europa
    ("Madrid", 11), ("Barcelona", 11), ("Valencia", 11), ("Sevilla", 11), ("Bilbao", 11),
    ("París", 12), ("Lyon", 12), ("Marsella", 12), ("Toulouse", 12), ("Niza", 12),
    ("Berlín", 13), ("Múnich", 13), ("Hamburgo", 13), ("Fráncfort", 13), ("Colonia", 13),
    ("Roma", 14), ("Milán", 14), ("Nápoles", 14), ("Turín", 14), ("Florencia", 14),
    ("Londres", 15), ("Manchester", 15), ("Birmingham", 15), ("Liverpool", 15), ("Leeds", 15),
    ("Lisboa", 16), ("Oporto", 16), ("Coímbra", 16), ("Braga", 16), ("Faro", 16),
    ("Ámsterdam", 17), ("Róterdam", 17), ("La Haya", 17), ("Eindhoven", 17), ("Utrecht", 17),
    ("Estocolmo", 18), ("Gotemburgo", 18), ("Malmö", 18), ("Uppsala", 18), ("Västerås", 18),
    ("Oslo", 19), ("Bergen", 19), ("Trondheim", 19), ("Stavanger", 19), ("Tromsø", 19),
    ("Zúrich", 20), ("Ginebra", 20), ("Basilea", 20), ("Berna", 20), ("Lausana", 20),

    # Asia
    ("Pekín", 21), ("Shanghái", 21), ("Cantón", 21), ("Shenzhen", 21), ("Chongqing", 21),
    ("Tokio", 22), ("Osaka", 22), ("Kioto", 22), ("Nagoya", 22), ("Yokohama", 22),
    ("Nueva Delhi", 23), ("Bombay", 23), ("Bangalore", 23), ("Calcuta", 23), ("Chennai", 23),
    ("Seúl", 24), ("Busan", 24), ("Incheon", 24), ("Daegu", 24), ("Gwangju", 24),
    ("Riad", 25), ("Jeddah", 25), ("Medina", 25), ("Dammam", 25), ("Tabuk", 25),
    ("Tel Aviv", 26), ("Jerusalén", 26), ("Haifa", 26), ("Eilat", 26), ("Ashdod", 26),
    ("Bangkok", 27), ("Chiang Mai", 27), ("Pattaya", 27), ("Phuket", 27), ("Nonthaburi", 27),
    ("Yakarta", 28), ("Surabaya", 28), ("Bandung", 28), ("Medan", 28), ("Denpasar", 28),
    ("Hanói", 29), ("Ciudad Ho Chi Minh", 29), ("Da Nang", 29), ("Hue", 29), ("Can Tho", 29),
    ("Manila", 30), ("Cebú", 30), ("Dávao", 30), ("Quezon", 30), ("Zamboanga", 30),

    # África
    ("Ciudad del Cabo", 31), ("Johannesburgo", 31), ("Pretoria", 31), ("Durban", 31), ("Bloemfontein", 31),
    ("Lagos", 32), ("Abuya", 32), ("Kano", 32), ("Port Harcourt", 32), ("Ibadan", 32),
    ("El Cairo", 33), ("Alejandría", 33), ("Giza", 33), ("Luxor", 33), ("Asuán", 33),
    ("Casablanca", 34), ("Marrakech", 34), ("Rabat", 34), ("Fez", 34), ("Tánger", 34),
    ("Nairobi", 35), ("Mombasa", 35), ("Kisumu", 35), ("Eldoret", 35), ("Nakuru", 35),
    ("Acra", 36), ("Kumasi", 36), ("Tamale", 36), ("Sekondi", 36), ("Tema", 36),
    ("Argel", 37), ("Orán", 37), ("Constantina", 37), ("Annaba", 37), ("Tlemcen", 37),
    ("Addis Abeba", 38), ("Dire Dawa", 38), ("Mekelle", 38), ("Gondar", 38), ("Adama", 38),
    ("Túnez", 39), ("Sfax", 39), ("Susa", 39), ("Kairuán", 39), ("Bizerta", 39),
    ("Dakar", 40), ("Thiès", 40), ("Saint-Louis", 40), ("Kaolack", 40), ("Ziguinchor", 40),

    # Oceanía
    ("Sídney", 41), ("Melbourne", 41), ("Brisbane", 41), ("Perth", 41), ("Adelaida", 41),
    ("Auckland", 42), ("Wellington", 42), ("Christchurch", 42), ("Hamilton", 42), ("Dunedin", 42),
    ("Suva", 43), ("Nadi", 43), ("Lautoka", 43)
]
c.executemany("INSERT INTO Ciudad(nombre_ciudad, cod_pais) VALUES(?, ?)", ciudades)

# ---------------------------
# Insertar algunos clientes de ejemplo
# ---------------------------
clientes = [
    ("CC", "1001", "Juan Pérez", "juanperez@mail.com", "Calle 1 #10-20", "3001234567", 1, 1),
    ("CC", "1002", "María Gómez", "mariagomez@mail.com", "Calle 2 #15-30", "3009876543", 2, 1),
    ("CC", "2001", "Ana Torres", "anatorres@mail.com", "Av. Reforma 100", "3111234567", 6, 2),
    ("CC", "3001", "Sofía Díaz", "sofiadiaz@mail.com", "Calle Florida 120", "3121234567", 11, 3),
    ("CC", "4001", "James Smith", "jamessmith@mail.com", "5th Avenue 500", "3205557890", 16, 4),
    ("CC", "5001", "Lucas Oliveira", "lucasoliveira@mail.com", "Rua das Flores 120", "3198887777", 21, 5)
]
c.executemany("""
INSERT INTO Cliente(tipoID, nroID, nombres, correo, direccion, celular, cod_ciudad, cod_pais)
VALUES(?,?,?,?,?,?,?,?)
""", clientes)

# ---------------------------
# Guardar cambios y cerrar conexión
# ---------------------------
conn.commit()
conn.close()

print("✅ Base de datos creada correctamente con países de todos los continentes.")
