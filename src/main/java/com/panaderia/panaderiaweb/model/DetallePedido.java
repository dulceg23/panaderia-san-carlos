/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.model;

/**
 *
 * @author lenovo
 */
public class DetallePedido {
    
    
    private int idProducto;
    private String producto;
    private double cantidad;

    public DetallePedido() {
    }

    public DetallePedido(
            int idProducto,
            String producto,
            double cantidad
    ) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    
}
