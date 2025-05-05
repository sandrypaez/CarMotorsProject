package com.carmotors.carmotors.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nombre;
    private String identificacion;
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private double discountPercentage;
    private int rewardPoints;
    private LocalDate fechaCompra;
    private List<OrdenServicio> serviceHistory;

    // Constructor vacío
    public Cliente() {
        this.serviceHistory = new ArrayList<>();
    }

    // Constructor completo
    public Cliente(int id, String nombre, String identificacion, String telefono, String correoElectronico, String direccion, double discountPercentage, int rewardPoints, LocalDate fechaCompra) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.discountPercentage = discountPercentage;
        this.rewardPoints = rewardPoints;
        this.fechaCompra = fechaCompra;
        this.serviceHistory = new ArrayList<>();
    }

    // Constructor para compatibilidad con FacturaDAO
    public Cliente(int id, String nombre, String identificacion, String telefono, String correoElectronico, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.discountPercentage = 0.0;
        this.rewardPoints = 0;
        this.fechaCompra = null;
        this.serviceHistory = new ArrayList<>();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public List<OrdenServicio> getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(List<OrdenServicio> serviceHistory) {
        this.serviceHistory = serviceHistory;
    }
}