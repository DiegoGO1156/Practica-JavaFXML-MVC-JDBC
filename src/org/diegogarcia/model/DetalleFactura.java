/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.model;

/**
 *
 * @author diego
 */
public class DetalleFactura {
    private int detalleId;
    private int facturaId;
    private int productoId;
    private String factura;
    private String producto;
    
    public DetalleFactura(){
        
    }

    public DetalleFactura(int detalleId, String factura, String producto) {
        this.detalleId = detalleId;
        this.factura = factura;
        this.producto = producto;
    }

    public int getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }
    
    
    
}
