package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.ProductoManager;
import com.panaderia.panaderiaweb.manager.SeguridadUtil;

import com.panaderia.panaderiaweb.model.Producto;

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


@Path("productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoController {


    private final ProductoManager manager =
            new ProductoManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /productos
    // ADMIN / PRODUCCION / SUCURSAL
    // ====================================================
    @GET
    public Response listarProductos(

            @HeaderParam("Authorization")
            String authorization

    ) {

        try {

            validarLectura(
                    authorization
            );


            List<Producto> productos =
                    manager.listarProductos();


            return Response
                    .ok(productos)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // GET /productos/{id}
    // ADMIN / PRODUCCION / SUCURSAL
    // ====================================================
    @GET
    @Path("{id}")
    public Response buscarProducto(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id

    ) {

        try {

            validarLectura(
                    authorization
            );


            Producto producto =
                    manager.buscarPorId(
                            id
                    );


            if (producto == null) {

                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Producto no encontrado."
                );
            }


            return Response
                    .ok(producto)
                    .build();


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // POST /productos
    // SOLO ADMIN
    // ====================================================
    @POST
    public Response crearProducto(

            @HeaderParam("Authorization")
            String authorization,

            Producto producto

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (producto == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos del producto."
                );
            }


            int idProducto =
                    manager.crearProducto(
                            producto
                    );


            Map<String, Object> datos =
                    new LinkedHashMap<>();


            datos.put(
                    "exito",
                    true
            );


            datos.put(
                    "mensaje",
                    "Producto creado correctamente."
            );


            datos.put(
                    "idProducto",
                    idProducto
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
    // PUT /productos/{id}
    // SOLO ADMIN
    // ====================================================
    @PUT
    @Path("{id}")
    public Response modificarProducto(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            Producto producto

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (producto == null) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos del producto."
                );
            }


            manager.modificarProducto(
                    id,
                    producto
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Producto modificado correctamente."
            );


        } catch (Exception e) {

            return manejarError(e);
        }
    }


    // ====================================================
    // PUT /productos/{id}/estado/{estado}
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
    // PUT /productos/{id}/existencia-minima/{cantidad}
    // SOLO ADMIN
    // ====================================================
    @PUT
    @Path("{id}/existencia-minima/{cantidad}")
    public Response cambiarExistenciaMinima(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id,

            @PathParam("cantidad")
            double cantidad

    ) {

        try {

            seguridad.validarAdmin(
                    authorization
            );


            if (cantidad < 0) {

                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La existencia mínima no puede ser negativa."
                );
            }


            manager.cambiarExistenciaMinima(
                    id,
                    cantidad
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Existencia mínima actualizada correctamente."
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
                "PRODUCCION",
                "SUCURSAL"
        );
    }


    // ====================================================
    // MANEJO DE ERRORES
    // ====================================================
    private Response manejarError(
            Exception e
    ) {

        e.printStackTrace();


        String mensaje =
                e.getMessage() != null
                ? e.getMessage()
                : "Error interno del servidor.";


        String mensajeUpper =
                mensaje.toUpperCase();


        /*
         * No envió token o el token
         * tiene formato incorrecto.
         */
        if (
            e instanceof IllegalArgumentException
            ||
            mensajeUpper.contains(
                    "SESION NO VALIDA"
            )
            ||
            mensajeUpper.contains(
                    "SESIÓN NO VÁLIDA"
            )
            ||
            mensajeUpper.contains(
                    "SESION HA EXPIRADO"
            )
            ||
            mensajeUpper.contains(
                    "SESIÓN HA EXPIRADO"
            )
        ) {

            return respuesta(
                    Response.Status.UNAUTHORIZED,
                    false,
                    mensaje
            );
        }


        /*
         * Está autenticado pero
         * su rol no tiene permiso.
         */
        if (
            e instanceof SecurityException
        ) {

            return respuesta(
                    Response.Status.FORBIDDEN,
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