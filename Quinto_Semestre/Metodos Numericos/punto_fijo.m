function PuntoFijo_2016
fprintf('\nEl siguiente programa determina el punto fijo para una funcion f(x)\n')

% Ingreso de la funcion
expr = input('Ingresar la funcion f(x): ','s');
f = str2func(['@(x) ' expr]);

x = input('Ingrese un numero inicial: ');
if ~isreal(f(x))
    fprintf('La funcion no es real. \n');
    return
end

n = input('Ingrese el numero maximo de iteraciones permitidas: ');
tol = input('Ingrese el error permitido: ');

fprintf('Ite \t   X(i) \t   f(Xi) \t   Error\n');

for i = 1:n

    e = abs(f(x) - x);
    fprintf('%2.0f \t %f \t %f \t %f\n', i, x, f(x), e);

    if (e < tol)
        fprintf('El punto fijo es: %f \n', f(x));
        return
    end

    if ~isreal(f(x))
        fprintf('El valor es complejo, no hay raiz real\n');
        return
    end

    x = f(x);

end

fprintf('Se alcanzo el maximo de iteraciones permitidas.\n');
fprintf('Ultima aproximacion: %f \n', x);

end
