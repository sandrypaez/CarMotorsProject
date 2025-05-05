package com.carmotors.carmotors.model.entities;

import java.sql.Date;

public class OrdenServicio {
    private int id;
    private int idVehiculo;
    private int idServicio;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;
    private Date recordatorioFecha;
    private boolean recordatorioEnviado;

    public OrdenServicio() {
    }

    public OrdenServicio(int id, int idVehiculo, int idServicio, String estado, Date fechaInicio, Date fechaFin, Date recordatorioFecha, boolean recordatorioEnviado) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.idServicio = idServicio;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.recordatorioFecha = recordatorioFecha;
        this.recordatorioEnviado = recordatorioEnviado;
    }

    // Getters y Setters
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getRecordatorioFecha() {
        return recordatorioFecha;
    }

    public void setRecordatorioFecha(Date recordatorioFecha) {
        this.recordatorioFecha = recordatorioFecha;
    }

    public boolean isRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }
}