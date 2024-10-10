package conectar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class Conectar {

    private static Conectar instancia;
    private final Queue<Connection> pool;
    private final int MAX_CONEXIONES = 10; // Límite de conexiones

    // Constructor privado para evitar múltiples instancias
    private Conectar() {
        pool = new LinkedList<>();
        inicializarPool();
    }

    // Método para obtener la instancia única (Singleton)
    public static synchronized Conectar getInstancia() {
        if (instancia == null) {
            instancia = new Conectar();
        }
        return instancia;
    }

    // Método para inicializar el pool de conexiones
    private void inicializarPool() {
        for (int i = 0; i < MAX_CONEXIONES; i++) {
            pool.add(crearConexion());
        }
    }

    // Método para crear una nueva conexión
    private Connection crearConexion() {
        try {
            String url = "jdbc:mysql://localhost:3306/business2";  // Cambia el nombre de tu base de datos
            String usuario = "root";
            String clave = "";  // Cambia la contraseña de MySQL
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear una nueva conexión a la base de datos", e);
        }
    }

    // Método para obtener una conexión del pool
    public synchronized Connection obtenerConexion() {
        if (pool.isEmpty()) {
            return crearConexion(); // Crea una nueva conexión si el pool está vacío
        }
        return pool.poll(); // Saca una conexión del pool
    }

    // Método para devolver una conexión al pool
    public synchronized void devolverConexion(Connection conexion) {
        if (conexion != null && pool.size() < MAX_CONEXIONES) {
            pool.add(conexion);
        } else {
            cerrarConexion(conexion); // Cierra la conexión si el pool está lleno o es nula
        }
    }

    // Método para cerrar una conexión a la base de datos
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar la conexión.");
            }
        }
    }

    // Método para cerrar todas las conexiones en el pool al finalizar el sistema
    public synchronized void cerrarTodasLasConexiones() {
        while (!pool.isEmpty()) {
            cerrarConexion(pool.poll());
        }
    }
}
