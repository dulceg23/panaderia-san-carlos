package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.ProduccionManager;
import com.panaderia.panaderiaweb.manager.SeguridadUtil;

import com.panaderia.panaderiaweb.model.Produccion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("produccion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProduccionController {


    private final ProduccionManager manager =
            new ProduccionManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /produccion
    // ADMIN / PRODUCCION
    // ====================================================
    @GET
    public Response listarProduccion(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarAcceso(
                    authorization
            );


            List<Produccion> lista =
                    manager.listarProduccion();


            return Response
                    .ok(lista)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /produccion/{id}
    // ====================================================
    @GET
    @Path("{id}")
    public Response buscarProduccion(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id

    ) {

        try {

            validarAcceso(
                    authorization
            );


            Produccion produccion =
                    manager.buscarPorId(
                            id
                    );


            if (produccion == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Producción no encontrada."
                );
            }


            return Response
                    .ok(produccion)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /produccion
    // ====================================================
    @POST
    public Response registrarProduccion(

            @HeaderParam("Authorization")
            String authorization,

            Produccion produccion

    ) {

        try {

            validarAcceso(
                    authorization
            );


            if (
                produccion == null
                ||
                produccion.getIdProducto()
                <= 0
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar un producto válido."
                );
            }


            if (
                produccion.getCantidad()
                <= 0
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La cantidad debe ser mayor que cero."
                );
            }


            int idProduccion =
                    manager.registrarProduccion(
                            produccion
                    );


            Map<String, Object> datos =
                    new LinkedHashMap<>();


            datos.put(
                    "exito",
                    true
            );


            datos.put(
                    "mensaje",
                    "Producción registrada correctamente."
            );


            datos.put(
                    "idProduccion",
                    idProduccion
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
    // ADMIN / PRODUCCION
    // ====================================================
    private void validarAcceso(
            String authorization
    ) throws Exception {

        seguridad.validarProduccion(
                authorization
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