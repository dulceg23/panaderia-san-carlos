/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.model;

public class UsuarioSesion {

    private int idUsuario;

    private String nombreCompleto;

    private int idRol;

    private String rol;

    private Integer idSucursal;

    private String token;


    public UsuarioSesion() {
    }


    public int getIdUsuario() {
        return idUsuario;
    }


    public void setIdUsuario(int idUsuario) {
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


    public int getIdRol() {
        return idRol;
    }


    public void setIdRol(int idRol) {
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


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }
}
