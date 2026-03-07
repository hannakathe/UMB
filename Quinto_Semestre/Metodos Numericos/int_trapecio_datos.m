function Int_Trapecio_datos
%Ingreso de datos
X = input('Ingrese los valores de x: ');
Y = input('Ingrese los valores de y: ');

if length(X) ~= length(Y)
    error('Los vectores X y Y deben tener la misma longitud');
end

n = length(X);
h = (X(n) - X(1))/(n-1);

TM = 0;

for i = 0:n-1
    if i == 0 || i == n-1
        TM = TM + Y(1+i);
    else
        TM = TM + 2*Y(1+i);
    end
end

TM = h*TM/2;

fprintf('\nTrapecio: %f\n', TM);

end
