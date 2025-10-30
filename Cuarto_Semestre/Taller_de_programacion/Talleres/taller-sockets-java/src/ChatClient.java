// ChatClient.java
// Cliente de chat que se conecta al servidor y envía/recibe mensajes.

import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost"; // IP o nombre del servidor
        int port = 6000;           // Debe coincidir con el puerto del servidor

        try (
            Socket socket = new Socket(host, port);
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado al chat en " + host + ":" + port);

            // Hilo secundario para escuchar mensajes del servidor (asíncrono)
            Thread lector = new Thread(() -> {
                String mensajeServidor;
                try {
                    while ((mensajeServidor = entradaServidor.readLine()) != null) {
                        System.out.println(mensajeServidor);
                    }
                } catch (IOException e) {
                    System.err.println("Conexión cerrada con el servidor.");
                }
            });

            lector.start(); // Inicia el hilo de escucha

            // Bucle para enviar mensajes desde consola
            String linea;
            while ((linea = entradaUsuario.readLine()) != null) {
                salidaServidor.println(linea);
                if ("exit".equalsIgnoreCase(linea.trim())) break;
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente de chat: " + e.getMessage());
        }
    }
}
