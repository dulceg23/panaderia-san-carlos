package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.InventarioManager;
import com.panaderia.panaderiaweb.manager.SeguridadUtil;

import com.panaderia.panaderiaweb.model.Inventario;
import com.panaderia.panaderiaweb.model.MovimientoInventario;

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


@Path("inventario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventarioController {


    private final InventarioManager manager =
            new InventarioManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /inventario
    // ADMIN / PRODUCCION
    // ====================================================
    @GET
    public Response listarInventario(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarAcceso(
                    authorization
            );


            List<Inventario> inventario =
                    manager.listarInventario();


            return Response
                    .ok(inventario)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /inventario/{idProducto}
    // ====================================================
    @GET
    @Path("{idProducto}")
    public Response buscarInventario(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("idProducto")
            int idProducto

    ) {

        try {

            validarAcceso(
                    authorization
            );


            Inventario inventario =
                    manager.buscarPorProducto(
                            idProducto
                    );


            if (inventario == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Producto no encontrado en inventario."
                );
            }


            return Response
                    .ok(inventario)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /inventario/bajo
    // ====================================================
    @GET
    @Path("bajo")
    public Response listarExistenciasBajas(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarAcceso(
                    authorization
            );


            List<Inventario> lista =
                    manager.listarExistenciaBaja();


            return Response
                    .ok(lista)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /inventario/entrada
    // ====================================================
    @POST
    @Path("entrada")
    public Response registrarEntrada(

            @HeaderParam("Authorization")
            String authorization,

            MovimientoInventario movimiento

    ) {

        try {

            validarAcceso(
                    authorization
            );


            if (
                movimiento == null
                ||
                movimiento.getIdProducto()
                <= 0
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar un producto válido."
                );
            }


            if (
                movimiento.getCantidad()
                <= 0
            ) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La cantidad debe ser mayor que cero."
                );
            }


            manager.registrarEntrada(
                    movimiento
            );


            return respuesta(
                    Response.Status.CREATED,
                    true,
                    "Entrada de inventario registrada correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /inventario/movimientos
    // ====================================================
    @GET
    @Path("movimientos")
    public Response listarMovimientos(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarAcceso(
                    authorization
            );


            List<MovimientoInventario> movimientos =
                    manager.listarMovimientos();


            return Response
                    .ok(movimientos)
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
    // RESPUESTAS
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