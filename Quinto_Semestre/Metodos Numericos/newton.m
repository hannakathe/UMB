function Newton_2016
fprintf('\n\nEl siguiente programa determina una raiz a partir del metodo de Newton\n');

% Ingreso de datos
expr = input('Ingrese la funcion: ','s');
f = str2func(['@(x) ' expr]);

x0 = input('Introduzca la condicion inicial: ');
n = input('Indique el numero maximo de iteraciones permitidas: ');
tol = input('Indique el error permitido para la raiz: ');

fprintf('\n\nIte \tX(i) \t\tfX(i) \t\tdfX(i) \tX(i+1) \tError\n');

% Aplicación del algoritmo
for i = 1:n

    if ~isreal(f(x0))
        fprintf('La funcion en el valor no es real \n');
        return
    end

    % Derivada numérica centrada
    h = 1e-8;
    df = (f(x0+h) - f(x0-h)) / (2*h);

    if ~isreal(df)
        fprintf('La derivada en el valor no es real \n');
        return
    end

    if df == 0
        fprintf('La derivada es cero. El metodo no puede continuar.\n');
        return
    end

    x1 = x0 - (f(x0)/df);
    ex = abs(x1 - x0);

    fprintf('%2.0d \t%f \t%f \t%f \t%f \t%f\n', i, x0, f(x0), df, x1, ex);

    if (ex < tol)
        fprintf('\nRaiz aproximada: %f\n', x1);
        return
    end

    x0 = x1;

end

fprintf('\nSe alcanzo el maximo de iteraciones permitidas.\n');
fprintf('Ultima aproximacion: %f\n', x0);

end
