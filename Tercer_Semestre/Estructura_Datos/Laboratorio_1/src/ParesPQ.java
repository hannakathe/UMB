import java.util.Scanner;

public class ParesPQ {
    public static void generadorPares(){
        Scanner scanner =new Scanner(System.in);
        System.out.print("Ingresa el número inicial (Entero): ");
        int p = scanner.nextInt();
        
        System.out.print("Ingresa el número final (Entero): ");
        int q = scanner.nextInt();


        for (int i=p;i<=q;i++){
            if (i%2==0) {
                System.out.println(i+" Es un numero par");
            } else{
                System.out.println(i+" No es par");
            }
        }
        scanner.close();

    }
}
