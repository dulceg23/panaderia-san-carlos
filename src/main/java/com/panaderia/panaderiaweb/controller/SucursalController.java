package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.SeguridadUtil;
import com.panaderia.panaderiaweb.manager.SucursalManager;
import com.panaderia.panaderiaweb.model.Sucursal;

import java.sql.SQLIntegrityConstraintViolationException;
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


@Path("sucursales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SucursalController {


    private final SucursalManager manager =
            new SucursalManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /sucursales
    // ADMIN / PRODUCCION
    // ====================================================
    @GET
    public Response listarSucursales(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarLectura(
                    authorization
            );


            List<Sucursal> sucursales =
                    manager.listarSucursales();


            return Response
                    .ok(sucursales)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /sucursales/{id}
    // ADMIN / PRODUCCION
    // ====================================================
    @GET
    @Path("{id}")
    public Response buscarSucursal(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id

    ) {

        try {

            validarLectura(
                    authorization
            );


            Sucursal sucursal =
                    manager.buscarPorId(id);


            if (sucursal == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Sucursal no encontrada."
                );
            }


            return Response
                    .ok(sucursal)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /sucursales
    // SOLO ADMIN
    // ====================================================
    @POST
    public Response crearSucursal(

            @HeaderParam("Authorization")
            String authorization,

            Sucursal sucursal

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (
                sucursal == null
                ||
                sucursal.getNombre() == null
                ||
                sucursal
                        .getNombre()
                        .trim()
                        .isEmpty()
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "El nombre de la sucursal es obligatorio."
                );
            }


            int idSucursal =
                    manager.crearSucursal(
                            sucursal
                    );


            Map<String, Object> datos =
                    new LinkedHashMap<>();


            datos.put(
                    "exito",
                    true
            );


            datos.put(
                    "mensaje",
                    "Sucursal creada correctamente."
            );


            datos.put(
                    "idSucursal",
                    idSucursal
            );


            return Response
                    .status(
                            Response.Status.CREATED
                    )
                    .entity(datos)
                    .build();


        } catch (
            SQLIntegrityConstraintViolationException e
        ) {

            return respuesta(
                    Response.Status.CONFLICT,
                    false,
                    "Ya existe una sucursal con ese nombre."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /sucursales/{id}
    // SOLO ADMIN
    // ====================================================
    @PUT
    @Path("{id}")
    public Response modificarSucursal(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            Sucursal sucursal

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (
                sucursal == null
                ||
                sucursal.getNombre() == null
                ||
                sucursal
                        .getNombre()
                        .trim()
                        .isEmpty()
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "El nombre de la sucursal es obligatorio."
                );
            }


            boolean actualizado =
                    manager.modificarSucursal(
                            id,
                            sucursal
                    );


            if (!actualizado) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Sucursal no encontrada."
                );
            }


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Sucursal modificada correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /sucursales/{id}/estado/{estado}
    // SOLO ADMIN
    // ====================================================
    @PUT
    @Path("{id}/estado/{estado}")
    public Response cambiarEstado(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            @PathParam("estado")
            int estado

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (
                estado != 0
                &&
                estado != 1
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "El estado debe ser 0 o 1."
                );
            }


            boolean actualizado =
                    manager.cambiarEstado(
                            id,
                            estado
                    );


            if (!actualizado) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Sucursal no encontrada."
                );
            }


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Estado de la sucursal actualizado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PERMISO DE LECTURA
    // ====================================================
    private void validarLectura(
            String authorization
    ) throws Exception {

        seguridad.validarRoles(
                authorization,
                "ADMIN",
                "PRODUCCION"
        );
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
            texto.contains("ORA-00001")
        ) {

            return respuesta(
                    Response.Status.CONFLICT,
                    false,
                    "Ya existe una sucursal con ese nombre."
            );
        }


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
            esErrorSesion(texto)
        ) {

            return respuesta(
                    Response.Status.UNAUTHORIZED,
                    false,
                    mensaje
            );
        }


        return respuesta(
                Response.Status.BAD_REQUEST,
                false,
                mensaje
        );
    }


    private boolean esErrorSesion(
            String mensaje
    ) {

        return mensaje.contains("SESION")
                ||
                mensaje.contains("SESIÓN")
                ||
                mensaje.contains("TOKEN")
                ||
                mensaje.contains("EXPIRAD");
    }


    // ====================================================
    // RESPUESTA
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