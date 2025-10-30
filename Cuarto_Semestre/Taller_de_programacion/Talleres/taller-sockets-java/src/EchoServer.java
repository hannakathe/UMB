// EchoServer.java
// Servidor que escucha en un puerto TCP y responde con el mismo mensaje (eco).

import java.io.*;   // Librerías para entrada/salida (streams)
import java.net.*;  // Librerías para sockets

public class EchoServer {
    public static void main(String[] args) {
        int port = 5000; // Puerto en el que el servidor escucha conexiones
        System.out.println("Servidor iniciado en el puerto " + port);

        // try-with-resources: asegura que el ServerSocket se cierre al terminar
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            // Bucle infinito para aceptar múltiples conexiones consecutivas
            while (true) {
                System.out.println("Esperando conexión de cliente...");
                // Espera una conexión de cliente (bloquea hasta que alguien se conecte)
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                // Flujo de entrada: lo que recibe del cliente
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                // Flujo de salida: lo que envía al cliente
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                String mensaje;

                // Lee líneas del cliente hasta que reciba "bye"
                while ((mensaje = input.readLine()) != null) {
                    System.out.println("Recibido del cliente: " + mensaje);

                    // Envía el mismo texto de vuelta (eco)
                    output.println("Eco: " + mensaje);

                    // Si el cliente escribe "bye", cierra la conexión
                    if ("bye".equalsIgnoreCase(mensaje.trim())) {
                        System.out.println("Cliente desconectado.");
                        break;
                    }
                }

                // Cerrar la conexión del cliente actual
                clientSocket.close();
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
