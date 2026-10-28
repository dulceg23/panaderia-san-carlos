/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.LoginRequest;
import com.panaderia.panaderiaweb.model.UsuarioSesion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class AuthManager {


    // ----------------------------------------------------
    // INICIAR SESION
    // ----------------------------------------------------
    public UsuarioSesion login(
            LoginRequest login
    ) throws Exception {


        if (login == null) {

            throw new IllegalArgumentException(
                    "Debe ingresar usuario y contraseña."
            );
        }


        if (login.getUsuario() == null
                || login.getUsuario().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El usuario es obligatorio."
            );
        }


        if (login.getPassword() == null
                || login.getPassword().isEmpty()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }


        UsuarioSesion usuario =
                new UsuarioSesion();


        String sqlAutenticar =
                "{call PKG_USUARIOS.AUTENTICAR(?,?,?,?,?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sqlAutenticar)
        ) {


            cs.setString(
                    1,
                    login.getUsuario()
            );


            cs.setString(
                    2,
                    login.getPassword()
            );


            cs.registerOutParameter(
                    3,
                    Types.NUMERIC
            );


            cs.registerOutParameter(
                    4,
                    Types.VARCHAR
            );


            cs.registerOutParameter(
                    5,
                    Types.NUMERIC
            );


            cs.registerOutParameter(
                    6,
                    Types.VARCHAR
            );


            cs.registerOutParameter(
                    7,
                    Types.NUMERIC
            );


            cs.execute();


            usuario.setIdUsuario(
                    cs.getInt(3)
            );


            usuario.setNombreCompleto(
                    cs.getString(4)
            );


            usuario.setIdRol(
                    cs.getInt(5)
            );


            usuario.setRol(
                    cs.getString(6)
            );


            int idSucursal =
                    cs.getInt(7);


            if (cs.wasNull()) {

                usuario.setIdSucursal(null);

            } else {

                usuario.setIdSucursal(
                        idSucursal
                );
            }


            crearSesion(
                    conn,
                    usuario
            );


            return usuario;
        }
    }


    // ----------------------------------------------------
    // CREAR SESION
    // ----------------------------------------------------
    private void crearSesion(
            Connection conn,
            UsuarioSesion usuario
    ) throws Exception {


        String sql =
                "{call PKG_USUARIOS.CREAR_SESION(?,?)}";


        try (
            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    usuario.getIdUsuario()
            );


            cs.registerOutParameter(
                    2,
                    Types.VARCHAR
            );


            cs.execute();


            usuario.setToken(
                    cs.getString(2)
            );
        }
    }


    // ----------------------------------------------------
    // VALIDAR SESION
    // ----------------------------------------------------
    public UsuarioSesion validarSesion(
            String token
    ) throws Exception {


        if (token == null
                || token.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Token no proporcionado."
            );
        }


        String sql =
                "{call PKG_USUARIOS.VALIDAR_SESION(?,?,?,?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setString(
                    1,
                    token
            );


            cs.registerOutParameter(
                    2,
                    Types.NUMERIC
            );


            cs.registerOutParameter(
                    3,
                    Types.VARCHAR
            );


            cs.registerOutParameter(
                    4,
                    Types.NUMERIC
            );


            cs.registerOutParameter(
                    5,
                    Types.VARCHAR
            );


            cs.registerOutParameter(
                    6,
                    Types.NUMERIC
            );


            cs.execute();


            UsuarioSesion usuario =
                    new UsuarioSesion();


            usuario.setIdUsuario(
                    cs.getInt(2)
            );


            usuario.setNombreCompleto(
                    cs.getString(3)
            );


            usuario.setIdRol(
                    cs.getInt(4)
            );


            usuario.setRol(
                    cs.getString(5)
            );


            int idSucursal =
                    cs.getInt(6);


            if (cs.wasNull()) {

                usuario.setIdSucursal(null);

            } else {

                usuario.setIdSucursal(
                        idSucursal
                );
            }


            usuario.setToken(token);


            return usuario;
        }
    }


    // ----------------------------------------------------
    // CERRAR SESION
    // ----------------------------------------------------
    public void cerrarSesion(
            String token
    ) throws Exception {


        if (token == null
                || token.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Token no proporcionado."
            );
        }


        String sql =
                "{call PKG_USUARIOS.CERRAR_SESION(?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setString(
                    1,
                    token
            );


            cs.execute();
        }
    }
}
