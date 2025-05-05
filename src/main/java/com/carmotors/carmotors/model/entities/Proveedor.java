package com.carmotors.carmotors.model.entities;

public class Proveedor {
    private int id;
    private String nombre;
    private String nit;
    private String contacto;
    private String frecuenciaVisita;

    public Proveedor() {}

    public Proveedor(int id, String nombre, String nit, String contacto, String frecuenciaVisita) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.contacto = contacto;
        this.frecuenciaVisita = frecuenciaVisita;
    }

    // Getters and Setters
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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getFrecuenciaVisita() {
        return frecuenciaVisita;
    }

    public void setFrecuenciaVisita(String frecuenciaVisita) {
        this.frecuenciaVisita = frecuenciaVisita;
    }
}