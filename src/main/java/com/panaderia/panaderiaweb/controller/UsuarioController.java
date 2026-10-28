/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.SeguridadUtil;
import com.panaderia.panaderiaweb.manager.UsuarioManager;

import com.panaderia.panaderiaweb.model.Usuario;
import com.panaderia.panaderiaweb.model.UsuarioSesion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {


    private final UsuarioManager manager =
            new UsuarioManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /usuarios
    // ====================================================
    @GET
    public Response listarUsuarios(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            List<Usuario> usuarios =
                    manager.listarUsuarios();


            return Response
                    .ok(usuarios)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /usuarios/roles
    // ====================================================
    @GET
    @Path("roles")
    public Response listarRoles(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            List<Map<String, Object>> roles =
                    manager.listarRoles();


            return Response
                    .ok(roles)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /usuarios
    // ====================================================
    @POST
    public Response crearUsuario(

            @HeaderParam("Authorization")
            String authorization,

            Usuario usuario

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (usuario == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos del usuario."
                );
            }


            int idUsuario =
                    manager.crearUsuario(
                            usuario
                    );


            Map<String, Object> datos =
                    new LinkedHashMap<>();


            datos.put(
                    "exito",
                    true
            );


            datos.put(
                    "mensaje",
                    "Usuario creado correctamente."
            );


            datos.put(
                    "idUsuario",
                    idUsuario
            );


            return Response
                    .status(
                            Response.Status.CREATED
                    )
                    .entity(datos)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /usuarios/{id}
    // ====================================================
    @PUT
    @Path("{id}")
    public Response modificarUsuario(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            Usuario usuario

    ) {

        try {

            UsuarioSesion admin =
                    seguridad.validarAdmin(
                            authorization
                    );


            if (usuario == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos del usuario."
                );
            }


            Usuario actual =
                    manager.buscarPorId(
                            id
                    );


            if (actual == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Usuario no encontrado."
                );
            }


            if (
                usuario.getIdRol()
                == null
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe seleccionar un rol."
                );
            }


            String nuevoRol =
                    manager.obtenerNombreRol(
                            usuario.getIdRol()
                    );


            if (nuevoRol == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "El rol seleccionado no es válido."
                );
            }


            /*
             * EL ADMIN QUE TIENE SESIÓN
             * NO PUEDE QUITARSE SU PROPIO ROL
             */
            if (
                admin.getIdUsuario()
                == id
                &&
                !"ADMIN".equalsIgnoreCase(
                        nuevoRol
                )
            ) {

                return respuesta(
                        Response.Status.CONFLICT,
                        false,
                        "No puede quitarse su propio rol de administrador."
                );
            }


            /*
             * NO DEJAR EL SISTEMA SIN ADMIN
             */
            boolean eraAdminActivo =
                    "ADMIN".equalsIgnoreCase(
                            actual.getRol()
                    )
                    &&
                    "ACTIVO".equalsIgnoreCase(
                            actual.getEstado()
                    );


            boolean seguiraAdmin =
                    "ADMIN".equalsIgnoreCase(
                            nuevoRol
                    );


            if (
                eraAdminActivo
                &&
                !seguiraAdmin
                &&
                manager.contarAdminsActivos()
                <= 1
            ) {

                return respuesta(
                        Response.Status.CONFLICT,
                        false,
                        "No puede cambiar el rol del último administrador activo."
                );
            }


            manager.modificarUsuario(
                    id,
                    usuario
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Usuario modificado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /usuarios/{id}/estado/{estado}
    // ====================================================
    @PUT
    @Path("{id}/estado/{estado}")
    public Response cambiarEstado(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            @PathParam("estado")
            String estado

    ) {

        try {

            UsuarioSesion admin =
                    seguridad.validarAdmin(
                            authorization
                    );


            if (estado == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar el estado."
                );
            }


            estado =
                    estado
                            .trim()
                            .toUpperCase();


            if (
                !"ACTIVO".equals(estado)
                &&
                !"INACTIVO".equals(estado)
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "El estado debe ser ACTIVO o INACTIVO."
                );
            }


            Usuario usuario =
                    manager.buscarPorId(
                            id
                    );


            if (usuario == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Usuario no encontrado."
                );
            }


            /*
             * NO DESACTIVARSE A SÍ MISMO
             */
            if (
                admin.getIdUsuario()
                == id
                &&
                "INACTIVO".equals(
                        estado
                )
            ) {

                return respuesta(
                        Response.Status.CONFLICT,
                        false,
                        "No puede desactivar su propio usuario."
                );
            }


            /*
             * NO DESACTIVAR AL ÚLTIMO ADMIN
             */
            boolean esAdminActivo =
                    "ADMIN".equalsIgnoreCase(
                            usuario.getRol()
                    )
                    &&
                    "ACTIVO".equalsIgnoreCase(
                            usuario.getEstado()
                    );


            if (
                esAdminActivo
                &&
                "INACTIVO".equals(
                        estado
                )
                &&
                manager.contarAdminsActivos()
                <= 1
            ) {

                return respuesta(
                        Response.Status.CONFLICT,
                        false,
                        "No puede desactivar al último administrador activo."
                );
            }


            manager.cambiarEstado(
                    id,
                    estado
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Estado actualizado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /usuarios/{id}/password
    // ====================================================
    @PUT
    @Path("{id}/password")
    public Response cambiarPassword(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            Map<String, String> datos

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            Usuario usuario =
                    manager.buscarPorId(
                            id
                    );


            if (usuario == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Usuario no encontrado."
                );
            }


            if (
                datos == null
                ||
                datos.get("password")
                == null
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar la nueva contraseña."
                );
            }


            String password =
                    datos
                            .get("password")
                            .trim();


            if (
                password.length()
                < 6
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La contraseña debe tener al menos 6 caracteres."
                );
            }


            manager.cambiarPassword(
                    id,
                    password
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Contraseña actualizada correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // ERRORES
    // ====================================================
    private Response manejarError(
            Exception e
    ) {

        e.printStackTrace();


        String mensaje =
                e.getMessage() != null
                ? e.getMessage()
                : "Error interno del servidor.";


        String texto =
                mensaje.toUpperCase();


        if (
            e instanceof SecurityException
        ) {

            return respuesta(
                    Response.Status.FORBIDDEN,
                    false,
                    mensaje
            );
        }


        if (
            e instanceof IllegalArgumentException
            ||
            texto.contains("SESION")
            ||
            texto.contains("SESIÓN")
            ||
            texto.contains("TOKEN")
            ||
            texto.contains("EXPIRAD")
        ) {

            return respuesta(
                    Response.Status.UNAUTHORIZED,
                    false,
                    mensaje
            );
        }


        /*
         * ERRORES DEL PACKAGE ORACLE
         */
        if (
            texto.contains("ORA-20")
        ) {

            return respuesta(
                    Response.Status.BAD_REQUEST,
                    false,
                    mensaje
            );
        }


        return respuesta(
                Response.Status.INTERNAL_SERVER_ERROR,
                false,
                mensaje
        );
    }


    // ====================================================
    // RESPUESTA JSON
    // ====================================================
    private Response respuesta(

            Response.Status status,
            boolean exito,
            String mensaje

    ) {

        Map<String, Object> respuesta =
                new LinkedHashMap<>();


        respuesta.put(
                "exito",
                exito
        );


        respuesta.put(
                "mensaje",
                mensaje
        );


        return Response
                .status(status)
                .entity(respuesta)
                .build();
    }
}