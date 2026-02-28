function Comprobacion_Esquemas
fprintf('\nEl siguiente programa comprueba el orden de una derivada \n y determina su error de aproximacion \n');

% Ingreso de los datos
A = input('Ingrese los coeficientes de cada termino en forma de vector: ');
b = input('Ingrese los coeficientes de cada h en forma de vector: ');

% Verificación básica
if length(A) ~= length(b)
    error('Los vectores A y b deben tener la misma longitud.');
end

% Comprobación del esquema
if sum(A) ~= 0
    fprintf('El esquema no aproxima ninguna derivada. \n');
    return
else
    S = 0;
    m = 0;

    while abs(S) < 2^(-10)
        m = m + 1;
        S = A * (b.^m)';   % Producto fila por columna
    end

    fprintf('El orden de la derivada es: %d \n', m);

    S = 0;
    l = m;

    while abs(S) < 2^(-10)
        l = l + 1;
        S = A * (b.^l)';
    end

    fprintf('El orden de aproximacion es: %d \n', l - m);
    return
end

end
