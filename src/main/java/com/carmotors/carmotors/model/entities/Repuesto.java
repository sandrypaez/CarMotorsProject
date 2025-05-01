package com.carmotors.carmotors.model.entities;

public class Repuesto {
    private int id;
    private String nombre;
    private String tipo;
    private String marca;
    private String modelo;
    private int idProveedor;
    private int cantidadStock;
    private int nivelMinimoStock;
    private String fechaIngreso;
    private int vidaUtilDias;
    private String estado;

    public Repuesto() {}

    public Repuesto(int id, String nombre, String tipo, String marca, String modelo,
                    int idProveedor, int cantidadStock, int nivelMinimoStock,
                    String fechaIngreso, int vidaUtilDias, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.idProveedor = idProveedor;
        this.cantidadStock = cantidadStock;
        this.nivelMinimoStock = nivelMinimoStock;
        this.fechaIngreso = fechaIngreso;
        this.vidaUtilDias = vidaUtilDias;
        this.estado = estado;
    }

    // Getters and Setters
    // ...

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public int getNivelMinimoStock() {
        return nivelMinimoStock;
    }

    public void setNivelMinimoStock(int nivelMinimoStock) {
        this.nivelMinimoStock = nivelMinimoStock;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getVidaUtilDias() {
        return vidaUtilDias;
    }

    public void setVidaUtilDias(int vidaUtilDias) {
        this.vidaUtilDias = vidaUtilDias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
