package com.carmotors.carmotors.model.entities;

public class OrdenServicio {
    private int id;
    private int idVehiculo;
    private int idServicio;
    private String estado;
    private String fechaInicio;
    private String fechaFin;

    public OrdenServicio() {}

    public OrdenServicio(int id, int idVehiculo, int idServicio, String estado,
                         String fechaInicio, String fechaFin) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.idServicio = idServicio;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters and Setters
    // ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
