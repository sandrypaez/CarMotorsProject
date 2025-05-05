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

    public String getEmail() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
