package conectar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conectar {

    public static final String URL = "jdbc:mysql://localhost:3306/business2";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return con;
    }

    

}
