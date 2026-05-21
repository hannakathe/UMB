function Euler_Mejorado
fprintf('El siguiente programa realiza el metodo de euler\n\n');
%Ingreso de datos
f=inline(input('Ingrese la funcion: ','s'));
a=input('Ingrese el punto inicial de x: ');
b=input('Ingrese el punto final de x: ');
y=input('Ingrese la condicion inicial de y: ');
n=input('Ingrese el numero de pasos a alcanzar: ');
%Calculo de h
h=(b-a)/n;
%Encabezado de la tabla
fprintf('\n \n \t \t EULER MEJORADO \t \t \n \n');
fprintf('\ti  \txi        \t \tyi      \t     \tGamma     \n')
for i=0:n-1
    %Calculo del gamma
    k1=f(a,y);
    k2=f(a+h,y+h*k1);
    gamma=(k1+k2)/2;
    a=a+h;
    y=y+h*gamma;
    fprintf('\t%d  \t%f         \t%f           \t%f     \n', i,a,y,gamma)
end
