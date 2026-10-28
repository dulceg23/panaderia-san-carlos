/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.model;

import java.util.Date;

public class Usuario {

    private Integer idUsuario;

    private String nombreCompleto;

    private String nombreUsuario;

    private String password;

    private Integer idRol;

    private String rol;

    private Integer idSucursal;

    private String sucursal;

    private String estado;

    private Date fechaCreacion;


    public Usuario() {
    }


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(
            String nombreCompleto
    ) {
        this.nombreCompleto = nombreCompleto;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(
            String nombreUsuario
    ) {
        this.nombreUsuario = nombreUsuario;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password
    ) {
        this.password = password;
    }


    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }


    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(
            Integer idSucursal
    ) {
        this.idSucursal = idSucursal;
    }


    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(
            String sucursal
    ) {
        this.sucursal = sucursal;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(
            String estado
    ) {
        this.estado = estado;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            Date fechaCreacion
    ) {
        this.fechaCreacion = fechaCreacion;
    }
}