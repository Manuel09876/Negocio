package Bases;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    private static ConcurrentHashMap<Integer, Socket> mapaDeSockets = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int puerto = 12345; // Puerto de escucha del servidor

        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Nueva conexión desde " + clienteSocket.getInetAddress());

                int trabajadorId = autenticarTrabajador(clienteSocket);

                if (trabajadorId > 0) {  // Solo añadir si la autenticación es exitosa
                    mapaDeSockets.put(trabajadorId, clienteSocket);
                    new Thread(new ManejadorCliente(clienteSocket, trabajadorId)).start();
                } else {
                    clienteSocket.close(); // Cerrar el socket si la autenticación falla
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int autenticarTrabajador(Socket clienteSocket) {
        // Lógica de autenticación aquí...
        return 1; // Supongamos que el ID es 1 para este ejemplo
    }

    public static void enviarMensajeCerrarSesion(int trabajadorId) {
        Socket clienteSocket = mapaDeSockets.get(trabajadorId);
        if (clienteSocket != null) {
            try {
                PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
                out.println("CERRAR_SESION");
                out.flush(); // Asegúrate de que el mensaje se envíe inmediatamente
                clienteSocket.close(); // Cerrar el socket después de enviar el mensaje
                mapaDeSockets.remove(trabajadorId); // Remover del mapa después de cerrar
                System.out.println("Mensaje de cierre de sesión enviado a trabajador ID: " + trabajadorId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró un socket para el trabajador ID: " + trabajadorId);
        }
    }

    public static boolean isTrabajadorConectado(int trabajadorId) {
        return mapaDeSockets.containsKey(trabajadorId);
    }
}
