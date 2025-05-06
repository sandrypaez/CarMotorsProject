package com.carmotors.carmotors.model.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Servicio {
    private int idServicio;
    private String tipo; // "Preventivo" o "Correctivo"
    private String descripcion;
    private BigDecimal costoManoObra;
    private int tiempoEstimado;

    // Constructor vacío
    public Servicio() {
    }

    // Constructor completo
    public Servicio(int idServicio, String tipo, String descripcion, BigDecimal costoManoObra, int tiempoEstimado) {
        this.idServicio = idServicio;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.costoManoObra = costoManoObra;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Getters y Setters
    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(BigDecimal costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "Servicio{" +
                "idServicio=" + idServicio +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", costoManoObra=" + costoManoObra +
                ", tiempoEstimado=" + tiempoEstimado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio)) return false;
        Servicio servicio = (Servicio) o;
        return idServicio == servicio.idServicio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServicio);
    }
}
