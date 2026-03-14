function IntNumTab
fprintf('\n El siguiente programa determina las integrales numericas para una funcion \n')

X = input('Ingrese los valores de x: ');
Y = input('Ingrese los valores de y: ');

if length(X) ~= length(Y)
    error('Los vectores X y Y deben tener la misma longitud');
end

n = length(X);
h = X(2) - X(1);

% Simpson 1/3
S3M = 0;

for i = 0:n-1
    if i == 0 || i == n-1
        S3M = S3M + Y(1+i);
    else
        if mod(i,2) == 1
            S3M = S3M + 4*Y(1+i);
        else
            S3M = S3M + 2*Y(1+i);
        end
    end
end

S3M = S3M*h/3;
fprintf('\n1/3 de Simpson: %f', S3M);

% Simpson 3/8
S8M = 0;

for i = 0:n-1
    if i == 0 || i == n-1
        S8M = S8M + Y(1+i);
    else
        if mod(i,3) == 0
            S8M = S8M + 2*Y(1+i);
        else
            S8M = S8M + 3*Y(1+i);
        end
    end
end

S8M = S8M*3*h/8;
fprintf('\n3/8 de Simpson: %f\n', S8M);

end
