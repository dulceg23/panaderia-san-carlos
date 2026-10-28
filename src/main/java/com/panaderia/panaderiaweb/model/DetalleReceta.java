package com.panaderia.panaderiaweb.model;

public class DetalleReceta {

    private int idInsumo;
    private String nombreInsumo;
    private double cantidad;
    private String unidadMedida;

    public DetalleReceta() {
    }

    public DetalleReceta(int idInsumo, String nombreInsumo,
                         double cantidad, String unidadMedida) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}