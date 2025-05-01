package com.carmotors.carmotors.model.entities;

public class Vehiculo {
    private int id;
    private int idCliente;
    private String marca;
    private String modelo;
    private String placa;
    private String tipo;

    public Vehiculo() {}

    public Vehiculo(int id, int idCliente, String marca, String modelo, String placa, String tipo) {
        this.id = id;
        this.idCliente = idCliente;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.tipo = tipo;
    }

    // Getters and Setters
    // ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
