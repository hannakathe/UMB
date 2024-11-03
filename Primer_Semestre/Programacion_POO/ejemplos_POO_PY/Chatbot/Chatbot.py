import random
import re

class Preguntas():
    def __init__(self):
        self._pares = [
            [r"bien?", ["Me alegro que estes bien. Yo estoy feliz de que estes aqui, como te puedo ayudar? "]],

            [r"que es python", ["""Python es un lenguaje de programación ampliamente utilizado en las aplicaciones web, el 
                                desarrollo de software, la ciencia de datos y el machine learning (ML). Los desarrolladores 
                                utilizan Python porque es eficiente y fácil de aprender, además de que se puede ejecutar en 
                                muchas plataformas diferentes."""]],

            [r"comentarios en python", ["""Los comentarios en Python comienzan con el carácter numeral, '#', 
                                                           y se extienden hasta el final visible de la línea. 
                                                           Un comentario quizás aparezca al comienzo de la línea o 
                                                           seguido de espacios en blanco o código, pero no dentro de 
                                                           una cadena de caracteres.

                                                           Ejemplo: 
                                                           #This is a comment
                                                           print("Hello, World!")"""]],

            [r"variables en python", ["""En Python, las variables se definen y se asignan valores utilizando el símbolo 
                                                        “=”. Por ejemplo, para crear una variable llamada "x" y asignarle el valor "5", 
                                                        se utiliza el operador de asignación "=" 

                                                        Ejemplo:
                                                        x= "5"
                                                        """]],

            [r"tipos de datos de python", ["""En python existen estos tipos de datos: 
                                                    Tipo de texto:	str
                                                    Tipos numéricos:	int, float, complex
                                                    Tipos de secuencia:	list, tuple, range
                                                    Tipo de mapeo:	dict
                                                    Tipos de conjuntos:	set,frozenset
                                                    Tipo booleano:	bool
                                                    Tipos binarios:	bytes, bytearray, memoryview
                                                    Ninguno Tipo:	NoneType"""]],

            [r"listas en python", ["""Para crear una lista en Python, simplemente hay que encerrar una secuencia 
                                                 de elementos separados por comas entre corchetes [] 

                                                 Ejemplo: 
                                                 thislist = ["apple", "banana", "cherry"]
                                                 """]],

            [r"if y else en python", ["""En Python, if y else son estructuras de control de flujo que permiten ejecutar 
                                             un bloque de código si se cumple una determinada condición 

                                                Ejemplo:
                                                a = 200
                                                b = 33
                                                if b > a:
                                                  print("b es mas grande que a")
                                                else:
                                                  print("b no es mas grande que a")"""]],

            [r"bucles for en python", ["""El bucle o ciclo for se utiliza para iterar sobre una objeto 
                                                                iterable y ejecutar un bloque de código para cada elemento en la 
                                                                secuencia de dicho objeto. 

                                                                Ejemplo:
                                                                print("Comienzo")
                                                                for i in [0, 1, 2]:
                                                                    print("Hola ", end="")
                                                                print()
                                                                print("Final")"""]],

            [r"bucles while en python", ["""Un bucle while permite repetir la ejecución de un grupo de 
                                                                  instrucciones mientras se cumpla una condición (es decir, mientras la 
                                                                  condición tenga el valor True).

                                                                    Ejemplo:
                                                                    i = 1
                                                                    while i <= 3:
                                                                        print(i)
                                                                        i += 1
                                                                    print("Programa terminado")"""]],

            [r"funciones en python", ["""Para crear una función en Python, puedes usar la palabra reservada def. 
                                                     Luego, debes asignarle un nombre o identificador que se utilizará para invocarla. 
                                                     Después del nombre, debes incluir entre paréntesis un listado opcional de parámetros. 
                                                     La cabecera de la función termina con dos puntos. 

                                                        Ejemplo:
                                                        def my_function():
                                                              print("Hello from a function")

                                                        my_function()"""]],

            [r"clases y objetos en python", ["""Para crear una clase vamos a emplear la palabra reservada class seguido
                                                           de un nombre escrito en minúscula, a excepción de la primera letra de cada palabra,
                                                           que se escribe en mayúscula, y sin guiones bajos.

                                                            Ejemplo: 

                                                            class Alumno:
                                                                pass 
                                                            
                                                            Para crear un objeto en Python, puedes usar el nombre de la clase y agregar paréntesis. 
                                                            Por ejemplo, para crear un objeto de la clase MiClase, puedes usar el código MiClase"""]], 
            
            [r"gracias", ["""De nada, tienes otra pregunta? """]],
        ]

    def obtener_pares(self):
        return self._pares

class Chatbot():
    def __init__(self):
        self.preguntas = Preguntas()

    def chatear(self):
        print("Hola, ¿cómo estás?")
        print("")
        while True:
            entrada = input("Tú: ")
            if entrada.lower() == "finalizar":
                print("Chatbot: Chao, fue un placer conversar contigo.")
                break
            for patron, respuestas in self.preguntas.obtener_pares():
                if re.search(patron, entrada):
                    print("")
                    print("Chatbot:", random.choice(respuestas))
                    print("")
                    break
            else:
                print("Chatbot: No entiendo, ¿puedes reformular tu pregunta?")


if __name__ == "__main__":
    chatbot = Chatbot()
    chatbot.chatear()

