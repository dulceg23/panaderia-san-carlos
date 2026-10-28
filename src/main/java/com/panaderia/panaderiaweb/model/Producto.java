package com.panaderia.panaderiaweb.model;

public class Producto {

    private int idProducto;
    private int idTipoProducto;
    private String tipoProducto;
    private String nombre;
    private String unidadMedida;
    private int estado;
    private double existencia;
    private double existenciaMinima;

    public Producto() {
    }

    public Producto(int idProducto, int idTipoProducto,
                    String tipoProducto, String nombre,
                    String unidadMedida, int estado,
                    double existencia, double existenciaMinima) {

        this.idProducto = idProducto;
        this.idTipoProducto = idTipoProducto;
        this.tipoProducto = tipoProducto;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.estado = estado;
        this.existencia = existencia;
        this.existenciaMinima = existenciaMinima;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(int idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getExistencia() {
        return existencia;
    }

    public void setExistencia(double existencia) {
        this.existencia = existencia;
    }

    public double getExistenciaMinima() {
        return existenciaMinima;
    }

    public void setExistenciaMinima(double existenciaMinima) {
        this.existenciaMinima = existenciaMinima;
    }
}