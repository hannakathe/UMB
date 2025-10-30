// EchoClient.java
// Cliente que se conecta al servidor y recibe la respuesta tipo "eco".

import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) {
        String host = "localhost"; // Dirección del servidor (puede ser IP)
        int port = 5000;           // Puerto del servidor (debe coincidir con el del servidor)

        // try-with-resources: cierra automáticamente los recursos
        try (
            Socket socket = new Socket(host, port); // Conecta con el servidor
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado al servidor en " + host + ":" + port);
            String texto;

            // Bucle que lee del teclado y envía al servidor
            while (true) {
                System.out.print("Tú: ");
                texto = entradaUsuario.readLine(); // Lo que escribe el usuario
                if (texto == null) break;

                // Envía el texto al servidor
                salidaServidor.println(texto);

                // Lee la respuesta (eco)
                String respuesta = entradaServidor.readLine();
                System.out.println("Servidor: " + respuesta);

                // Si el usuario escribió "bye", termina el bucle
                if ("bye".equalsIgnoreCase(texto.trim())) break;
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
