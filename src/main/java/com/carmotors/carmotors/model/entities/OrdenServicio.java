package com.carmotors.carmotors.model.entities;

import java.sql.Date;

public class OrdenServicio {

    private int id;
    private int idVehiculo;
    private int idServicio;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;

    public OrdenServicio() {
    }

    public OrdenServicio(int id, int idVehiculo, int idServicio, String estado, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.idServicio = idServicio;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    // Métodos que aún no están implementados
    public void setVehicleId(int aInt) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setClientId(int clientId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setMaintenanceType(String string) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setDescription(String string) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setLaborCost(double aDouble) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setStatus(String string) {
        this.estado = string;
    }

    public void setStartDate(Date date) {
        this.fechaInicio = date;
    }

    public void setEndDate(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
