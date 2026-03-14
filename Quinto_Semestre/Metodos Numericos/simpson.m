function Reglas_Simpson
fprintf('\n El siguiente programa determina las integrales numericas para una funcion \n');

a = input('Ingrese el extremo inferior del intervalo: ');
b = input('Ingrese el extremo superior del intervalo: ');

expr = input('Ingrese la funcion a integrar: ','s');
f = str2func(['@(x) ' expr]);

n = input('Ingrese el numero de subintervalos: ');

h = (b-a)/n;

S3M = f(a) + f(b);
S8M = f(a) + f(b);

for i = 1:n-1

    % Simpson 1/3
    if mod(i,2) == 1
        S3M = S3M + 4*f(a + i*h);
    else
        S3M = S3M + 2*f(a + i*h);
    end

    % Simpson 3/8
    if mod(i,3) == 0
        S8M = S8M + 2*f(a + i*h);
    else
        S8M = S8M + 3*f(a + i*h);
    end

end

S3M = S3M*h/3;
S8M = S8M*3*h/8;

fprintf('SIMP 1/3 \t %f \n', S3M);
fprintf('SIMP 3/8 \t %f \n', S8M);

end
