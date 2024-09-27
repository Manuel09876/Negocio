package Bases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PermisoManager {
    
    private Connection connect;

    public PermisoManager(Connection connection) {
        this.connect = connection;
    }

    public void guardarPermiso(int rolId, int menuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        String sql = "INSERT INTO roles_menus_submenus (id_rol, id_menu, visualizar, agregar, editar, eliminar) "
                   + "VALUES (?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE visualizar = ?, agregar = ?, editar = ?, eliminar = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            ps.setInt(2, menuId);
            ps.setBoolean(3, visualizar);
            ps.setBoolean(4, agregar);
            ps.setBoolean(5, editar);
            ps.setBoolean(6, eliminar);
            ps.setBoolean(7, visualizar);
            ps.setBoolean(8, agregar);
            ps.setBoolean(9, editar);
            ps.setBoolean(10, eliminar);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
