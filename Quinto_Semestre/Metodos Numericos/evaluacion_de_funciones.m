function Evaluacion_Derivadas_Orden_Superior
fprintf('\nEl siguiente programa evalua un esquema de diferencias \n en un valor dado. \n');

% Ingreso de datos
expr = input('Ingrese la funcion f(x): ','s');
f = str2func(['@(x) ' expr]);

A = input('Ingrese los coeficientes de cada termino en forma de vector: ');
b = input('Ingrese los coeficientes de cada h en forma de vector: ');
x = input('Ingrese el punto donde desea evaluar el esquema: ');
h = input('Ingrese el valor de h para aproximar el esquema: ');
k = input('Ingrese el orden de la derivada k : ');
a = input('Ingrese el valor que acompaña la potencia: ');

% Verificación básica
if length(A) ~= length(b)
    error('Los vectores A y b deben tener la misma longitud.');
end

n = length(A);
S = 0;

% Evaluación
for i = 1:n
    S = S + A(i)*f(x + h*b(i));
end

fprintf('\n El valor en el esquema es: %f \n', S/(a*h^k));

end



function Evaluacion_Esquemas
fprintf('Este programa permite calcular derivadas sobre datos \n');

% Ingreso de datos
x = input('Ingrese el vector x: ');
y = input('Ingrese el vector y: ');
k = input('Ingrese la posicion donde se evaluará: ');
n = input('Ingrese la derivada a calcular: ');

% Verificaciones básicas
if length(x) ~= length(y)
    error('Los vectores x e y deben tener la misma longitud.');
end

l = length(x);                 % Determinar la longitud del vector
h = x - x(k)*ones(1,l);        % Vector de valores de h
A = ones(l,l);                 % Construcción de la matriz
b = zeros(l,1);                % Vector columna del sistema

for s = 2:l
    A(s,:) = h.^(s-1);
    if s == n+1
        b(s) = factorial(n);
    end
end

r = y * (A \ b);               % Evaluación del esquema
fprintf('El valor corresponde a: %8.4f \n', r);

end
