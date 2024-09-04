package Bases;

import java.time.LocalDate;

public class PeriodoPago {
    private int numeroPeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public PeriodoPago(int numeroPeriodo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.numeroPeriodo = numeroPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getNumeroPeriodo() {
        return numeroPeriodo;
    }

    public void setNumeroPeriodo(int numeroPeriodo) {
        this.numeroPeriodo = numeroPeriodo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    
}

