function Int_Trapecio
%Ingreso de datos
a=input('Ingrese el extremo inferior del intervalo: ');
b=input('Ingrese el extremo superior del intervalo: ');
f=inline(input('Ingrese la funci?n a integrar: ','s'));
n=input('Ingrese el n?mero de intervalos: ');
h=(b-a)/(n);
T=0;
%Inicio del algoritmo
for i=1:n-1
        T=T + f(a+i*h);
end
T=h*(f(a)+2*T + f(b))/2;
fprintf('TRAPECIO COM \t %f  \n', T);
