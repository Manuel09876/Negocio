package Bases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ManejadorCliente implements Runnable {

    private Socket socket;
    private int trabajadorId;

    public ManejadorCliente(Socket socket, int trabajadorId) {
        this.socket = socket;
        this.trabajadorId = trabajadorId;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                // Verificar el mensaje entrante
                System.out.println("Mensaje recibido del servidor: " + mensaje);

                if (mensaje.equals("CERRAR_SESION")) {
                    cerrarSesion();  // Llama al método para cerrar la sesión
                    break;  // Salir del bucle después de cerrar la sesión
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();  // Asegúrate de cerrar el socket si aún no está cerrado
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cerrarSesion() {
        System.out.println("Sesión cerrada para trabajador ID: " + trabajadorId);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();  // Cerrar el socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Muestra un mensaje de cierre de sesión y cierra la aplicación
        JOptionPane.showMessageDialog(null, "Su sesión ha sido cerrada por el administrador.");
        System.exit(0);  // Cierra la aplicación del cliente
    }
}
