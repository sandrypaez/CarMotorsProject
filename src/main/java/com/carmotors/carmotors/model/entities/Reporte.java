package com.carmotors.carmotors.model.entities;

import java.time.LocalDate;

public class Reporte {
    private int id;
    private String tipoReporte; // e.g., "Inventario", "Mantenimiento", "Clientes", etc.
    private String descripcion; // Detailed description of the report
    private LocalDate fechaGeneracion; // Date the report was generated
    private String datos; // JSON or formatted string with report data

    public Reporte() {
    }

    public Reporte(int id, String tipoReporte, String descripcion, LocalDate fechaGeneracion, String datos) {
        this.id = id;
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.fechaGeneracion = fechaGeneracion;
        this.datos = datos;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }
}