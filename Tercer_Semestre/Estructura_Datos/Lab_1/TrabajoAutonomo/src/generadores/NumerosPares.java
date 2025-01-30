package generadores;
import java.util.Scanner;

public class NumerosPares {
    public static void generadorPares(){
        Scanner scanner =new Scanner(System.in);
        System.out.print("Ingresa el número inicial (Entero): ");
        int p = scanner.nextInt();
        
        System.out.print("Ingresa el número final (Entero): ");
        int q = scanner.nextInt();


        for (int i=p;i<=q;i++){
            if (i%2==0) {
                System.out.println(i+" Es un numero par");
            }
        }
        scanner.close();
    }
}
