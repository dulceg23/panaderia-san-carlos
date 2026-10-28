
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lenovo
 */
package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.model.UsuarioSesion;

public class SeguridadUtil {


    private final AuthManager authManager =
            new AuthManager();


    // ----------------------------------------------------
    // OBTENER USUARIO AUTENTICADO
    // ----------------------------------------------------
    public UsuarioSesion obtenerUsuario(
            String authorization
    ) throws Exception {


        if (authorization == null
                || authorization.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe iniciar sesión."
            );
        }


        if (!authorization.startsWith(
                "Bearer "
        )) {

            throw new IllegalArgumentException(
                    "Token de sesión no válido."
            );
        }


        String token =
                authorization
                        .substring(7)
                        .trim();


        if (token.isEmpty()) {

            throw new IllegalArgumentException(
                    "Token de sesión no válido."
            );
        }


        return authManager.validarSesion(
                token
        );
    }


    // ----------------------------------------------------
    // VALIDAR UNO O VARIOS ROLES
    // ----------------------------------------------------
    public UsuarioSesion validarRoles(
            String authorization,
            String... rolesPermitidos
    ) throws Exception {


        UsuarioSesion usuario =
                obtenerUsuario(
                        authorization
                );


        for (String rol :
                rolesPermitidos) {


            if (
                rol.equalsIgnoreCase(
                        usuario.getRol()
                )
            ) {

                return usuario;
            }
        }


        throw new SecurityException(
                "No tiene permiso para realizar esta operación."
        );
    }


    // ----------------------------------------------------
    // VALIDAR ADMIN
    // ----------------------------------------------------
    public UsuarioSesion validarAdmin(
            String authorization
    ) throws Exception {


        return validarRoles(
                authorization,
                "ADMIN"
        );
    }


    // ----------------------------------------------------
    // ADMIN O PRODUCCION
    // ----------------------------------------------------
    public UsuarioSesion validarProduccion(
            String authorization
    ) throws Exception {


        return validarRoles(
                authorization,
                "ADMIN",
                "PRODUCCION"
        );
    }


    // ----------------------------------------------------
    // USUARIOS QUE PUEDEN TRABAJAR CON PEDIDOS
    // ----------------------------------------------------
    public UsuarioSesion validarPedidos(
            String authorization
    ) throws Exception {


        return validarRoles(
                authorization,
                "ADMIN",
                "PRODUCCION",
                "SUCURSAL"
        );
    }
}