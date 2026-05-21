function Heun_2016
fprintf('El siguiente programa realiza el metodo de heun\n\n');

% Ingreso de datos
expr = input('Ingrese la funcion f(x,y): ','s');
f = str2func(['@(x,y) ' expr]);

x0 = input('Ingrese el punto de x0: ');
x1 = input('Ingrese el punto donde se va a evaluar x1: ');
y0 = input('Ingrese la condicion inicial y0: ');
n = input('Ingrese el numero de pasos a alcanzar: ');

h = (x1 - x0)/n;

fprintf(' i \txi \t\t yi \n');

for i = 1:n

k1 = f(x0,y0);
k2 = f(x0 + 2*h/3, y0 + 2*h*k1/3);

y0 = y0 + h*(k1 + 3*k2)/4;
x0 = x0 + h;

fprintf('%2.0d \t%f \t%f \n', i, x0, y0);

end

end

