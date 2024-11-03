
def es_primo(numero):
    if numero <= 1:
        return False
    for i in range(2, int(numero**0.5) + 1):
        if numero % i == 0:
            return False
    return True

def imprimir_numeros_primos(n):
    print(f"Los números primos hasta {n} son:")
    for num in range(2, n + 1):
        if es_primo(num):
            print(num, end=" ")

# Cambia el valor de 'limite' según tus necesidades
limite = 10
imprimir_numeros_primos(limite)


def es_primo(numero):
    if numero <= 1:
        return False
    for i in range(2, numero):
        if numero % i == 0:
            return False
    return True

def imprimir_numeros_primos(n):
    print(f"Los números primos hasta {n} son:")
    for num in range(2, n + 1):
        if es_primo(num):
            print(num, end=" ")

# Cambia el valor de 'limite' según tus necesidades
limite = 100
imprimir_numeros_primos(limite)


