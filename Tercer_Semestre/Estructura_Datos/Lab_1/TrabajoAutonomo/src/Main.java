import generadores.NumerosPares;
import generadores.MultiplosDeTres;
import generadores.SumaMultiplos;
import otros.RecorrerMatrizInversa;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Actividad de trabajo autonomo...");
        System.out.println("");

        System.out.println("");
        System.out.println("---- Recorrer Matriz Inversa ----");
        System.out.println("");

        RecorrerMatrizInversa matrizInversa = new RecorrerMatrizInversa();
        matrizInversa.imprimirMatrizInversa();

        System.out.println("");
        System.out.println("---- Generador numeros pares entre p y q ----");
        System.out.println("");

        NumerosPares.generadorPares();

        System.out.println("");
        System.out.println("---- Generador multiplos de 3 ----");
        System.out.println("");

        MultiplosDeTres.generadorMultiplo();

        System.out.println("");
        System.out.println("---- Generador multiplos de 3 ----");
        System.out.println("");

        SumaMultiplos.generarSuma();

        
        
    }
}
