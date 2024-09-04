package Bases;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class Cliente {

    private Socket socket;
    private int trabajadorId;

    public Cliente(int trabajadorId, String host, int puerto) {
        this.trabajadorId = trabajadorId;

        try {
            this.socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor en " + host + ":" + puerto);

            new Thread(this::escucharServidor).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharServidor() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equals("CERRAR_SESION")) {
                    JOptionPane.showMessageDialog(null, "Su sesión ha sido cerrada por el administrador.");
                    System.exit(0); // Cierra la aplicación del trabajador
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int trabajadorId = 1; // El ID del trabajador que estás simulando
        String host = "localhost";
        int puerto = 12345;

        new Cliente(trabajadorId, host, puerto);
    }
}
