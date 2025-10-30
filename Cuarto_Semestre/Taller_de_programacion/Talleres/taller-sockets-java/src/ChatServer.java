// ChatServer.java
// Servidor de chat que permite varios clientes conectados al mismo tiempo.

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 6000; // Puerto para el chat
    // Colección segura para varios hilos (cada cliente tiene un hilo)
    private static final Set<ClientHandler> clientes = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Servidor de chat escuchando en el puerto " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Espera conexiones indefinidamente
            while (true) {
                Socket socket = serverSocket.accept(); // Cliente nuevo
                System.out.println("Nuevo cliente conectado: " + socket.getInetAddress());

                // Crea un manejador (hilo) para el nuevo cliente
                ClientHandler cliente = new ClientHandler(socket);
                clientes.add(cliente);

                // Inicia el hilo
                new Thread(cliente).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor de chat: " + e.getMessage());
        }
    }

    // Método para enviar mensaje a todos los clientes conectados
    public static void broadcast(String mensaje, ClientHandler remitente) {
        for (ClientHandler cliente : clientes) {
            if (cliente != remitente) { // Evita enviarse a sí mismo
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    // Eliminar cliente cuando se desconecta
    public static void eliminarCliente(ClientHandler cliente) {
        clientes.remove(cliente);
    }

    // Clase interna: cada cliente tiene su propio hilo
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter salida;
        private String nombre;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        // Envía un mensaje a este cliente
        public void enviarMensaje(String msg) {
            if (salida != null) salida.println(msg);
        }

        @Override
        public void run() {
            try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true)
            ) {
                this.salida = escritor;

                // Solicita nombre de usuario
                escritor.println("Introduce tu nombre:");
                this.nombre = entrada.readLine();
                System.out.println(nombre + " se ha unido al chat.");

                // Notifica a los demás
                broadcast(">> " + nombre + " se ha unido al chat.", this);

                String mensaje;
                // Bucle de comunicación
                while ((mensaje = entrada.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(mensaje.trim())) break;
                    System.out.println(nombre + ": " + mensaje);
                    broadcast(nombre + ": " + mensaje, this);
                }

            } catch (IOException e) {
                System.err.println("Error con cliente: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                eliminarCliente(this);
                broadcast(">> " + nombre + " ha salido del chat.", this);
                System.out.println(nombre + " desconectado.");
            }
        }
    }
}
