function Int_PuntoMedio
%Ingreso de datos
a=input('Ingrese el extremo inferior del intervalo: ');
b=input('Ingrese el extremo superior del intervalo: ');
f=inline(input('Ingrese la funcion a integrar: ','s'));
n=input('Ingrese el n?mero de intervalos: ');
h=(b-a)/(n);
PM=0;
%Inicio del algoritmo
for i=1:n
        PM=PM + f(a+(2*i-1)*h/2);
end
PM=h*(PM);
fprintf('PUNTO MED COM \t %f  \n', PM);
