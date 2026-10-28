package com.panaderia.panaderiaweb.model;

import java.util.ArrayList;
import java.util.List;

public class Receta {

    private int idReceta;
    private int idProductoFinal;
    private String productoFinal;
    private String descripcion;
    private int estado;
    private List<DetalleReceta> ingredientes;

    public Receta() {
        ingredientes = new ArrayList<>();
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdProductoFinal() {
        return idProductoFinal;
    }

    public void setIdProductoFinal(int idProductoFinal) {
        this.idProductoFinal = idProductoFinal;
    }

    public String getProductoFinal() {
        return productoFinal;
    }

    public void setProductoFinal(String productoFinal) {
        this.productoFinal = productoFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<DetalleReceta> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<DetalleReceta> ingredientes) {
        this.ingredientes = ingredientes;
    }
}