package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.RecetaManager;
import com.panaderia.panaderiaweb.manager.SeguridadUtil;

import com.panaderia.panaderiaweb.model.DetalleReceta;
import com.panaderia.panaderiaweb.model.Receta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("recetas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaController {


    private final RecetaManager manager =
            new RecetaManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /recetas
    // ADMIN / PRODUCCION
    // ====================================================
    @GET
    public Response listarRecetas(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarAcceso(
                    authorization
            );


            List<Receta> recetas =
                    manager.listarRecetas();


            return Response
                    .ok(recetas)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /recetas/{id}
    // ====================================================
    @GET
    @Path("{id}")
    public Response buscarReceta(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id

    ) {

        try {

            validarAcceso(
                    authorization
            );


            Receta receta =
                    manager.buscarPorId(id);


            if (receta == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Receta no encontrada."
                );
            }


            return Response
                    .ok(receta)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /recetas
    // ====================================================
    @POST
    public Response crearReceta(

            @HeaderParam("Authorization")
            String authorization,

            Receta receta

    ) {

        try {

            validarAcceso(
                    authorization
            );


            if (receta == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos de la receta."
                );
            }


            int idReceta =
                    manager.crearReceta(
                            receta
                    );


            Map<String, Object> datos =
                    new LinkedHashMap<>();


            datos.put(
                    "exito",
                    true
            );


            datos.put(
                    "mensaje",
                    "Receta creada correctamente."
            );


            datos.put(
                    "idReceta",
                    idReceta
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
    // POST /recetas/{id}/ingredientes
    // ====================================================
    @POST
    @Path("{id}/ingredientes")
    public Response agregarIngrediente(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            DetalleReceta detalle

    ) {

        try {

            validarAcceso(
                    authorization
            );


            if (detalle == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar el ingrediente."
                );
            }


            manager.agregarIngrediente(
                    id,
                    detalle
            );


            return respuesta(
                    Response.Status.CREATED,
                    true,
                    "Ingrediente agregado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /recetas/{id}/ingredientes/{idInsumo}
    // ====================================================
    @PUT
    @Path("{id}/ingredientes/{idInsumo}")
    public Response modificarIngrediente(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            @PathParam("idInsumo")
            int idInsumo,

            DetalleReceta detalle

    ) {

        try {

            validarAcceso(
                    authorization
            );


            if (detalle == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar los datos del ingrediente."
                );
            }


            manager.modificarIngrediente(
                    id,
                    idInsumo,
                    detalle.getCantidad()
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Ingrediente modificado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // DELETE /recetas/{id}/ingredientes/{idInsumo}
    // ====================================================
    @DELETE
    @Path("{id}/ingredientes/{idInsumo}")
    public Response eliminarIngrediente(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            @PathParam("idInsumo")
            int idInsumo

    ) {

        try {

            validarAcceso(
                    authorization
            );


            manager.eliminarIngrediente(
                    id,
                    idInsumo
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Ingrediente eliminado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /recetas/{id}/estado/{estado}
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

            validarAcceso(
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


            manager.cambiarEstado(
                    id,
                    estado
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Estado de la receta actualizado correctamente."
            );


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


        /*
         * Conservamos los mensajes
         * RAISE_APPLICATION_ERROR del package.
         */
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