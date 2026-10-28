/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.AuthManager;
import com.panaderia.panaderiaweb.model.LoginRequest;
import com.panaderia.panaderiaweb.model.UsuarioSesion;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {


    private final AuthManager manager =
            new AuthManager();


    // ----------------------------------------------------
    // POST /auth/login
    // ----------------------------------------------------
    @POST
    @Path("login")
    public Response login(
            LoginRequest login
    ) {


        try {


            UsuarioSesion usuario =
                    manager.login(login);


            return Response
                    .ok(usuario)
                    .build();


        } catch (IllegalArgumentException e) {


            return error(
                    Response.Status.BAD_REQUEST,
                    e.getMessage()
            );


        } catch (Exception e) {


            e.printStackTrace();


            String mensaje =
                    e.getMessage();


            if (mensaje != null
                    && mensaje.contains(
                            "ORA-20089"
                    )) {


                return error(
                        Response.Status.UNAUTHORIZED,
                        "Usuario o contraseña incorrectos."
                );
            }


            if (mensaje != null
                    && (
                    mensaje.contains("ORA-20090")
                    ||
                    mensaje.contains("ORA-20091")
                    )) {


                return error(
                        Response.Status.FORBIDDEN,
                        "El usuario no tiene acceso al sistema."
                );
            }


            return error(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    mensaje
            );
        }
    }


    // ----------------------------------------------------
    // GET /auth/sesion
    // ----------------------------------------------------
    @GET
    @Path("sesion")
    public Response validarSesion(
            @HeaderParam("Authorization")
            String authorization
    ) {


        try {


            String token =
                    obtenerToken(
                            authorization
                    );


            if (token == null) {


                return error(
                        Response.Status.UNAUTHORIZED,
                        "Sesión no proporcionada."
                );
            }


            UsuarioSesion usuario =
                    manager.validarSesion(
                            token
                    );


            return Response
                    .ok(usuario)
                    .build();


        } catch (Exception e) {


            e.printStackTrace();


            return error(
                    Response.Status.UNAUTHORIZED,
                    "La sesión no es válida o ha expirado."
            );
        }
    }


    // ----------------------------------------------------
    // POST /auth/logout
    // ----------------------------------------------------
    @POST
    @Path("logout")
    public Response logout(
            @HeaderParam("Authorization")
            String authorization
    ) {


        try {


            String token =
                    obtenerToken(
                            authorization
                    );


            if (token == null) {


                return error(
                        Response.Status.UNAUTHORIZED,
                        "Sesión no proporcionada."
                );
            }


            manager.cerrarSesion(
                    token
            );


            Map<String, Object> respuesta =
                    new LinkedHashMap<>();


            respuesta.put(
                    "exito",
                    true
            );


            respuesta.put(
                    "mensaje",
                    "Sesión cerrada correctamente."
            );


            return Response
                    .ok(respuesta)
                    .build();


        } catch (Exception e) {


            e.printStackTrace();


            return error(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }


    // ----------------------------------------------------
    // EXTRAER TOKEN
    // ----------------------------------------------------
    private String obtenerToken(
            String authorization
    ) {


        if (authorization == null
                || authorization.trim().isEmpty()) {

            return null;
        }


        String prefijo =
                "Bearer ";


        if (!authorization.startsWith(
                prefijo
        )) {

            return null;
        }


        String token =
                authorization
                        .substring(
                                prefijo.length()
                        )
                        .trim();


        if (token.isEmpty()) {

            return null;
        }


        return token;
    }


    // ----------------------------------------------------
    // RESPUESTA DE ERROR
    // ----------------------------------------------------
    private Response error(
            Response.Status estado,
            String mensaje
    ) {


        Map<String, Object> respuesta =
                new LinkedHashMap<>();


        respuesta.put(
                "exito",
                false
        );


        respuesta.put(
                "mensaje",
                mensaje
        );


        return Response
                .status(estado)
                .entity(respuesta)
                .build();
    }
}