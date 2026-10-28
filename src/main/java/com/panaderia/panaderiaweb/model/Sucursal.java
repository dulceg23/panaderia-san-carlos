package com.panaderia.panaderiaweb.model;

public class Sucursal {

    private int idSucursal;
    private String nombre;
    private int estado;

    public Sucursal() {
    }

    public Sucursal(int idSucursal, String nombre, int estado) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}