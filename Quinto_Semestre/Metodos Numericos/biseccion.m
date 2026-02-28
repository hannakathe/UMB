function Biseccion
fprintf('\n\nEl siguiente programa determina las raices de una funcion \nf(x) usando el metodo de biseccion \n\n')

% Ingreso de datos
expr = input('Ingrese la funcion: ','s');
f = str2func(['@(x) ' expr]);

a = input('Ingrese el valor del extremo inferior del intervalo: ');
if ~isreal(f(a))
    fprintf('La funcion en dicho extremo no es real \n');
    return
end

b = input('Ingrese el valor del extremo superior del intervalo: ');
if ~isreal(f(b))
    fprintf('La funcion en dicho extremo no es real \n');
    return
end

tol = input('Ingrese el error tolerado: ');
Ite = input('Ingrese el maximo de iteraciones a permitir: ');

% Validación de condiciones
if (f(a)*f(b) > 0)
    fprintf('\nLos valores de a y b no sirven para el programa debido a que poseen el mismo signo. \n')
    return
end

if (f(a) == 0)
    fprintf('La raiz del polinomio esta en: %f \n\n',a);
    return
end

if (f(b) == 0)
    fprintf('La raiz del polinomio esta en: %f \n\n',b);
    return
end

% Proceso iterativo
fprintf('\n \t Ite \t c \t\t f(c) \t\t Error \n')

for n = 1:Ite

    c = a + (b-a)/2;

    if ~isreal(f(c))
        fprintf('La funcion en dicho punto no es real \n');
        return
    end

    e = (b-a)/2;

    fprintf('\t%d \t%f \t%f \t%f \n', n, c, f(c), e);

    if e < tol || f(c) == 0
        fprintf('\nAproximacion final: %f \n\n', c);
        return
    end

    if (f(a)*f(c) < 0)
        b = c;
    else
        a = c;
    end

end

fprintf('\nSe alcanzo el maximo de iteraciones permitidas.\n');
fprintf('Aproximacion final: %f \n\n', c);

end
