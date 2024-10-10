package Bases;

import java.util.Date;

public class OrdenServicio {

    int id;
    Date fecha;
    int id_empresa; 
    int id_cliente;
    int frecuencia;
    Date inicio;
    Date fin;
    int id_venta;
    String estado;

    public OrdenServicio() {
    }

    public OrdenServicio(int id, Date fecha, int id_empresa, int id_cliente, int frecuencia, Date inicio, Date fin, int id_venta, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.id_empresa = id_empresa;
        this.id_cliente = id_cliente;
        this.frecuencia = frecuencia;
        this.inicio = inicio;
        this.fin = fin;
        this.id_venta = id_venta;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
}
