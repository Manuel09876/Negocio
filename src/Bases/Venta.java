package Bases;

import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Venta {
    

    public int id;
    private String empresa;
    private String cliente;
    private String vendedor;
    private double total;

    public Venta() {
    }

    public Venta(int id, String empresa, String cliente, String vendedor, double total, int r) {
        this.id = id;
        this.empresa = empresa;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.total = total;
        this.r = r;
    }
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    
    PreparedStatement ps;
    int r;
    public int RegistrarVenta(Venta v) {
        Connection connection = null;

        String sql = "INSERT INTO ventas (empresa, cliente, trabajador, total)VALUES(?,?,?,?)";
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, getEmpresa());
            ps.setString(2, getCliente());
            ps.setString(3, getVendedor());
            ps.setDouble(4, getTotal());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return r;
    }
    
     public int RegistrarDetalle(Detalle Dv) {
         Connection connection = null;
        String sql = "INSERT INTO detalle_ventas (servicio, fecha, precio, id_venta)values(?,?,?,?)";
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, Dv.getServicio());
            ps.setDate(2, (java.sql.Date) Dv.getFecha());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getId_venta());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return r;
    }

}
