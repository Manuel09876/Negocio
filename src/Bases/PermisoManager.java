package Bases;

import Presentation.VentanaPrincipal;
import conectar.Conectar;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PermisoManager {
private Connection connection; // Declaración del atributo 'connection'

    public PermisoManager(Connection connection) {
        this.connection = connection;
    }

    public void guardarPermisoMenuPrincipal(int rolId, String nombreMenu, boolean isVisible, boolean activo) {

        String sql = """
        INSERT INTO roles_menus (id_rol, id_menu, visualizar, activo)
        VALUES (?, (SELECT id_menu FROM menus WHERE LOWER(nombre_menu) = LOWER(?)), ?, ?)
        ON DUPLICATE KEY UPDATE visualizar = VALUES(visualizar), activo = VALUES(activo);
        """;

        try {
            
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ps.setString(2, nombreMenu);
                ps.setBoolean(3, isVisible);
                ps.setBoolean(4, activo);
                ps.executeUpdate();
                System.out.println("Permisos para el menú principal '" + nombreMenu + "' guardados exitosamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    public boolean existeIdMenu(int idMenu) {
        String sql = "SELECT COUNT(*) FROM menus WHERE id_menu = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMenu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean existeIdSubmenu(int idSubmenu) {
        String sql = "SELECT COUNT(*) FROM submenus WHERE id_submenu = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idSubmenu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeIdRol(int rolId) {
        String sql = "SELECT COUNT(*) FROM roles WHERE id_rol = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void guardarPermiso(int rolId, int menuId, int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar, boolean activo) {
        // Validación de existencia de IDs
        if (!existeIdRol(rolId)) {
            System.out.println("Error: El id_rol " + rolId + " no existe en la tabla roles.");
            return;
        }
        if (!existeIdMenu(menuId)) {
            System.out.println("Error: El id_menu " + menuId + " no existe en la tabla menus.");
            return;
        }
        if (!existeIdSubmenu(submenuId)) {
            System.out.println("Error: El id_submenu " + submenuId + " no existe en la tabla submenus.");
            return;
        }

        // Preparar la consulta para insertar o actualizar permisos
        String sql = """
        INSERT INTO roles_menus_submenus (id_rol, id_menu, id_submenu, visualizar, agregar, editar, eliminar, activo)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE visualizar = ?, agregar = ?, editar = ?, eliminar = ?, activo = ?;
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            ps.setInt(2, menuId);
            ps.setInt(3, submenuId);
            ps.setBoolean(4, visualizar);
            ps.setBoolean(5, agregar);
            ps.setBoolean(6, editar);
            ps.setBoolean(7, eliminar);
            ps.setBoolean(8, activo);
            // Para el UPDATE
            ps.setBoolean(9, visualizar);
            ps.setBoolean(10, agregar);
            ps.setBoolean(11, editar);
            ps.setBoolean(12, eliminar);
            ps.setBoolean(13, activo);
            ps.executeUpdate();
            System.out.println("Permisos guardados para rolId: " + rolId + ", menuId: " + menuId + ", submenuId: " + submenuId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarPermisosPorRol(int rolId) {
        String sqlSubmenus = "UPDATE roles_menus_submenus SET activo = 0 WHERE id_rol = ?";
        String sqlMenus = "UPDATE roles_menus SET activo = 0 WHERE id_rol = ?";

        try (PreparedStatement psSubmenus = connection.prepareStatement(sqlSubmenus);
             PreparedStatement psMenus = connection.prepareStatement(sqlMenus)) {

            // Desactivar permisos para submenús
            psSubmenus.setInt(1, rolId);
            int rowsAffectedSubmenus = psSubmenus.executeUpdate();
            System.out.println("Permisos de submenús desactivados: " + rowsAffectedSubmenus);

            // Desactivar permisos para menús principales (si aplica)
            psMenus.setInt(1, rolId);
            int rowsAffectedMenus = psMenus.executeUpdate();
            System.out.println("Permisos de menús desactivados: " + rowsAffectedMenus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void aplicarPermisosSubmenu(JPanel menuPanel, String nombreSubmenu, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        // Encuentra el submenú y aplica los permisos correspondientes
//        for (Component component : menuPanel.getComponents()) {
//            if (component instanceof JPanel) {
//                JPanel submenuPanel = (JPanel) component;
//                String submenuName = submenuPanel.getName(); // Asegúrate de establecer el nombre del panel adecuadamente
//
//                if (submenuName != null && submenuName.equals(nombreSubmenu)) {
//                    submenuPanel.setVisible(visualizar);
//
//                    // Aplicar permisos a botones
//                    for (Component subComponent : submenuPanel.getComponents()) {
//                        if (subComponent instanceof JButton) {
//                            JButton button = (JButton) subComponent;
//                            String buttonName = button.getName();
//
//                            switch (buttonName) {
//                                case "btnGuardar":
//                                    button.setEnabled(agregar);
//                                    break;
//                                case "btnModificar":
//                                    button.setEnabled(editar);
//                                    break;
//                                case "btnEliminar":
//                                    button.setEnabled(eliminar);
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    //Para que no se vean los Menus en la Ventana Principal
    public void ocultarSubMenus(String menuPrincipal, VentanaPrincipal ventanaPrincipal) {
        // Lógica para ocultar todos los submenús bajo el menú principal proporcionado.
        switch (menuPrincipal) {
            case "Administracion":
                ventanaPrincipal.getMenuUsuarios().setVisible(false);
                ventanaPrincipal.getMenuTipoUsuarios().setVisible(false);
                ventanaPrincipal.getMenuAsignacionPermisos().setVisible(false);
                ventanaPrincipal.getMenuEmpresas().setVisible(false);
                ventanaPrincipal.getMenuTrabajadores().setVisible(false);
                ventanaPrincipal.getMenuTarifario().setVisible(false);
                ventanaPrincipal.getMenuProductos().setVisible(false);
                ventanaPrincipal.getMenuFormularios().setVisible(false);
                ventanaPrincipal.getMenuProveedor().setVisible(false);
                ventanaPrincipal.getMenuConvenios().setVisible(false);
                ventanaPrincipal.getMenuBusquedaConvenios().setVisible(false);
                ventanaPrincipal.getMenuAsignaciondeTrabajos().setVisible(false);
                ventanaPrincipal.getMenuPuestoDeTrabajo().setVisible(false);
                ventanaPrincipal.getMenuFormaDePago().setVisible(false);
                ventanaPrincipal.getMenuMenusSubmenus().setVisible(false);
                // Oculta todos los submenús del menú "Administracion"
                break;
            // Repite para otros menús principales
            case "Admision":
                ventanaPrincipal.getMenuClientes().setVisible(false);
                ventanaPrincipal.getMenuMarcas().setVisible(false);
                ventanaPrincipal.getMenuUnidades().setVisible(false);
                ventanaPrincipal.getMenutipo_pagosgenerales().setVisible(false);
                ventanaPrincipal.getMenuTipoProMat().setVisible(false);
                ventanaPrincipal.getMenuTipoMaqVe().setVisible(false);
                ventanaPrincipal.getMenuLocalizacion().setVisible(false);
                ventanaPrincipal.getMenuConfiguracion().setVisible(false);
                // Oculta todos los submenús del menú "Administracion"
                break;
            case "Registros":
                ventanaPrincipal.getMenuOrdenes().setVisible(false);
                ventanaPrincipal.getMenuVerOrdenes().setVisible(false);
                ventanaPrincipal.getMenuVentas().setVisible(false);
                ventanaPrincipal.getMenuCompraProMat().setVisible(false);
                ventanaPrincipal.getMenuCompraEquyVehi().setVisible(false);
                ventanaPrincipal.getMenuGastosGenerales().setVisible(false);
                ventanaPrincipal.getMenuKardex().setVisible(false);
                ventanaPrincipal.getMenuCotizaciones().setVisible(false);
                ventanaPrincipal.getMenuCancelaciones().setVisible(false);
                // Oculta todos los submenús del menú "Administracion"
                break;
            case "Reportes":
                ventanaPrincipal.getMenuTrabajosRealizados().setVisible(false);
                ventanaPrincipal.getMenuEstadisticas().setVisible(false);
                ventanaPrincipal.getMenuDeudasPorPagar().setVisible(false);
                ventanaPrincipal.getMenuDeudasPorCobrar().setVisible(false);
                ventanaPrincipal.getMenuHorasTrabajadas().setVisible(false);
                ventanaPrincipal.getMenuSueldos().setVisible(false);
                ventanaPrincipal.getMenuStock().setVisible(false);
                ventanaPrincipal.getMenuTrabajos().setVisible(false);
                ventanaPrincipal.getMenuPresupuesto().setVisible(false);
                // Oculta todos los submenús del menú "Administracion"
                break;
        }
    }
}
