function Euler
fprintf('El siguiente programa realiza el metodo de euler\n\n');
%INGRESO DE DATOS
f=inline(input('Ingrese la funcion: ','s'))
a=input('Ingrese el punto inicial de x: ');
b=input('Ingrese el punto final de x: ');
y=input('Ingrese la condicion inicial de y: ');
n=input('Ingrese el numero de pasos a alcanzar: ');
%Calculo de h
h=(b-a)/n;
%Encabezado de la tabla
fprintf('\n \n \t \t EULER \t \t \n \n');
fprintf('\ti  \txi        \t \tyi      \t     \tGamma     \n')
for i=1:n
    %Calculo del gamma
gamma=f(a,y);
a=a+h;
y=y+h*gamma;
fprintf('\t%d  \t%f         \t%f           \t%f     \n', i,a,y,gamma)
end
