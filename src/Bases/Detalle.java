package Bases;

import java.util.Date;

public class Detalle {

    private int id_detVenta;
    private String servicio;
    private Date fecha;
    private double precio;
    private int id_venta;

    public Detalle() {
    }

    public Detalle(int id_detVenta, String servicio, Date fecha, double precio, int id_venta) {
        this.id_detVenta = id_detVenta;
        this.servicio = servicio;
        this.fecha = fecha;
        this.precio = precio;
        this.id_venta = id_venta;
    }

    public int getId_detVenta() {
        return id_detVenta;
    }

    public void setId_detVenta(int id_detVenta) {
        this.id_detVenta = id_detVenta;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public void setFecha(String fecha) {
    }

    }
