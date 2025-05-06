package com.carmotors.carmotors.model.entities;

import java.util.Date;

public class Factura {
    private int id;
    private int idOrden;
    private Date fechaEmision;
    private double subtotal;
    private double impuestos;
    private double total;
    private String cufe;
    private String qrUrl;
    private String email;
    private String vehiculo;
    private String nombreCliente;
    private String documentoCliente;
    private String direccionCliente;
    private String descripcionServicio;
    private double precioUnitario;
    private String telefono;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getImpuestos() { return impuestos; }
    public void setImpuestos(double impuestos) { this.impuestos = impuestos; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getCufe() { return cufe; }
    public void setCufe(String cufe) { this.cufe = cufe; }

    public String getQrUrl() { return qrUrl; }
    public void setQrUrl(String qrUrl) { this.qrUrl = qrUrl; }

    public String getEmail() { return email != null ? email : ""; }
    public void setEmail(String email) { this.email = email; }

    public String getVehiculo() { return vehiculo != null ? vehiculo : ""; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }

    public String getNombreCliente() { return nombreCliente != null ? nombreCliente : ""; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getDocumentoCliente() { return documentoCliente != null ? documentoCliente : ""; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }

    public String getDireccionCliente() { return direccionCliente != null ? direccionCliente : ""; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }

    public String getDescripcionServicio() { return descripcionServicio != null ? descripcionServicio : ""; }
    public void setDescripcionServicio(String descripcionServicio) { this.descripcionServicio = descripcionServicio; }

    public Object getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getTelefono() { return telefono != null ? telefono : ""; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}