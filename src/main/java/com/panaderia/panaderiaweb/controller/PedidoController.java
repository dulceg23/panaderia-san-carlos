/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.controller;

import com.panaderia.panaderiaweb.manager.PedidoManager;
import com.panaderia.panaderiaweb.manager.SeguridadUtil;

import com.panaderia.panaderiaweb.model.DetallePedido;
import com.panaderia.panaderiaweb.model.Pedido;
import com.panaderia.panaderiaweb.model.UsuarioSesion;

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


@Path("pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoController {


    private final PedidoManager manager =
            new PedidoManager();


    private final SeguridadUtil seguridad =
            new SeguridadUtil();


    // ====================================================
    // GET /pedidos
    // ADMIN / PRODUCCION = TODOS
    // SUCURSAL = SOLO LOS SUYOS
    // ====================================================
    @GET
    public Response listarPedidos(

            @HeaderParam("Authorization")
            String authorization

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            List<Pedido> pedidos;


            if (
                "SUCURSAL".equalsIgnoreCase(
                        usuario.getRol()
                )
            ) {


                if (
                    usuario.getIdSucursal()
                    == null
                ) {


                    return respuesta(
                            Response.Status.FORBIDDEN,
                            false,
                            "El usuario no tiene una sucursal asignada."
                    );
                }


                pedidos =
                        manager
                                .listarPedidosPorSucursal(
                                        usuario.getIdSucursal()
                                );


            } else {


                pedidos =
                        manager.listarPedidos();
            }


            return Response
                    .ok(pedidos)
                    .build();


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // GET /pedidos/{id}
    // ====================================================
    @GET
    @Path("{id}")
    public Response buscarPedido(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int id

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            Pedido pedido =
                    manager.buscarPorId(
                            id
                    );


            if (pedido == null) {


                return respuesta(
                        Response.Status.NOT_FOUND,
                        false,
                        "Pedido no encontrado."
                );
            }


            if (
                "SUCURSAL".equalsIgnoreCase(
                        usuario.getRol()
                )
            ) {


                if (
                    usuario.getIdSucursal()
                    == null
                    ||
                    pedido.getIdSucursal()
                    !=
                    usuario.getIdSucursal()
                ) {


                    return respuesta(
                            Response.Status.FORBIDDEN,
                            false,
                            "No tiene permiso para consultar este pedido."
                    );
                }
            }


            return Response
                    .ok(pedido)
                    .build();


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // POST /pedidos
    // ====================================================
    @POST
    public Response crearPedido(

            @HeaderParam("Authorization")
            String authorization,

            Pedido pedido

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            if (
                pedido == null
            ) {


                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe enviar los datos del pedido."
                );
            }


            if (
                pedido.getTurno() == null
                ||
                pedido
                        .getTurno()
                        .trim()
                        .isEmpty()
            ) {


                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar el turno."
                );
            }


            /*
             * Si el usuario pertenece a una sucursal,
             * IGNORAMOS cualquier idSucursal recibido
             * desde el frontend.
             *
             * Siempre utilizamos la sucursal asociada
             * a su cuenta.
             */

            if (
                "SUCURSAL".equalsIgnoreCase(
                        usuario.getRol()
                )
            ) {


                if (
                    usuario.getIdSucursal()
                    == null
                ) {


                    return respuesta(
                            Response.Status.FORBIDDEN,
                            false,
                            "El usuario no tiene una sucursal asignada."
                    );
                }


                pedido.setIdSucursal(
                        usuario.getIdSucursal()
                );


            } else {


                /*
                 * ADMIN y PRODUCCION
                 * pueden indicar la sucursal.
                 */

                if (
                    pedido.getIdSucursal()
                    <= 0
                ) {


                    return respuesta(
                            Response.Status.BAD_REQUEST,
                            false,
                            "Debe indicar una sucursal válida."
                    );
                }
            }


            pedido.setTurno(
                    pedido
                            .getTurno()
                            .trim()
                            .toUpperCase()
            );


            int idPedido =
                    manager.crearPedido(
                            pedido
                    );


            Map<String, Object> respuesta =
                    new LinkedHashMap<>();


            respuesta.put(
                    "exito",
                    true
            );


            respuesta.put(
                    "mensaje",
                    "Pedido creado correctamente."
            );


            respuesta.put(
                    "idPedido",
                    idPedido
            );


            return Response
                    .status(
                            Response.Status.CREATED
                    )
                    .entity(respuesta)
                    .build();


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // POST /pedidos/{id}/detalles
    // ====================================================
    @POST
    @Path("{id}/detalles")
    public Response agregarDetalle(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int idPedido,

            DetallePedido detalle

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            Response acceso =
                    validarAccesoPedido(
                            usuario,
                            idPedido
                    );


            if (acceso != null) {

                return acceso;
            }


            if (
                detalle == null
                ||
                detalle.getIdProducto()
                <= 0
            ) {


                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "Debe indicar un producto válido."
                );
            }


            if (
                detalle.getCantidad()
                <= 0
            ) {


                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La cantidad debe ser mayor que cero."
                );
            }


            manager.agregarDetalle(
                    idPedido,
                    detalle
            );


            return respuesta(
                    Response.Status.CREATED,
                    true,
                    "Producto agregado al pedido correctamente."
            );


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // PUT /pedidos/{id}/detalles/{idProducto}
    // ====================================================
    @PUT
    @Path("{id}/detalles/{idProducto}")
    public Response modificarDetalle(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int idPedido,

            @PathParam("idProducto")
            int idProducto,

            DetallePedido detalle

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            Response acceso =
                    validarAccesoPedido(
                            usuario,
                            idPedido
                    );


            if (acceso != null) {

                return acceso;
            }


            if (
                detalle == null
                ||
                detalle.getCantidad()
                <= 0
            ) {


                return respuesta(
                        Response.Status.BAD_REQUEST,
                        false,
                        "La cantidad debe ser mayor que cero."
                );
            }


            manager.modificarDetalle(
                    idPedido,
                    idProducto,
                    detalle.getCantidad()
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Cantidad modificada correctamente."
            );


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // DELETE /pedidos/{id}/detalles/{idProducto}
    // ====================================================
    @DELETE
    @Path("{id}/detalles/{idProducto}")
    public Response eliminarDetalle(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int idPedido,

            @PathParam("idProducto")
            int idProducto

    ) {


        try {


            UsuarioSesion usuario =
                    seguridad.validarPedidos(
                            authorization
                    );


            Response acceso =
                    validarAccesoPedido(
                            usuario,
                            idPedido
                    );


            if (acceso != null) {

                return acceso;
            }


            manager.eliminarDetalle(
                    idPedido,
                    idProducto
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Producto eliminado del pedido correctamente."
            );


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // PUT /pedidos/{id}/estado/{estado}
    // SOLO ADMIN / PRODUCCION
    // ====================================================
    @PUT
    @Path("{id}/estado/{estado}")
    public Response cambiarEstado(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int idPedido,

            @PathParam("estado")
            String estado

    ) {


        try {


            /*
             * SUCURSAL NO puede cambiar estados.
             */

            seguridad.validarProduccion(
                    authorization
            );


            manager.cambiarEstado(
                    idPedido,
                    estado
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Estado del pedido actualizado correctamente."
            );


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // PUT /pedidos/{id}/despachar
    // SOLO ADMIN / PRODUCCION
    // ====================================================
    @PUT
    @Path("{id}/despachar")
    public Response despacharPedido(

            @HeaderParam("Authorization")
            String authorization,

            @PathParam("id")
            int idPedido

    ) {


        try {


            seguridad.validarProduccion(
                    authorization
            );


            manager.despacharPedido(
                    idPedido
            );


            return respuesta(
                    Response.Status.OK,
                    true,
                    "Pedido despachado correctamente."
            );


        } catch (Exception e) {


            return manejarError(
                    e
            );
        }
    }


    // ====================================================
    // VALIDAR ACCESO AL PEDIDO
    // ====================================================
    private Response validarAccesoPedido(

            UsuarioSesion usuario,

            int idPedido

    ) throws Exception {


        Pedido pedido =
                manager.buscarPorId(
                        idPedido
                );


        if (pedido == null) {


            return respuesta(
                    Response.Status.NOT_FOUND,
                    false,
                    "Pedido no encontrado."
            );
        }


        /*
         * ADMIN y PRODUCCION pueden trabajar
         * con cualquier pedido.
         */

        if (
            !"SUCURSAL".equalsIgnoreCase(
                    usuario.getRol()
            )
        ) {


            return null;
        }


        /*
         * SUCURSAL solo puede trabajar
         * con pedidos propios.
         */

        if (
            usuario.getIdSucursal()
            == null
        ) {


            return respuesta(
                    Response.Status.FORBIDDEN,
                    false,
                    "El usuario no tiene una sucursal asignada."
            );
        }


        if (
            pedido.getIdSucursal()
            !=
            usuario.getIdSucursal()
        ) {


            return respuesta(
                    Response.Status.FORBIDDEN,
                    false,
                    "No tiene permiso para modificar este pedido."
            );
        }


        return null;
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


    // ====================================================
    // MANEJO DE ERRORES
    // ====================================================
    private Response manejarError(
            Exception e
    ) {


        e.printStackTrace();


        /*
         * No tiene token o el token
         * tiene un formato incorrecto.
         */

        if (
            e instanceof IllegalArgumentException
        ) {


            return respuesta(
                    Response.Status.UNAUTHORIZED,
                    false,
                    e.getMessage()
            );
        }


        /*
         * Tiene sesión pero su rol
         * no tiene permiso.
         */

        if (
            e instanceof SecurityException
        ) {


            return respuesta(
                    Response.Status.FORBIDDEN,
                    false,
                    e.getMessage()
            );
        }


        return error(
                e
        );
    }


    private Response error(
            Exception e
    ) {


        String mensaje =
                e.getMessage() != null
                ? e.getMessage()
                : "Error interno del servidor";


        return respuesta(
                Response.Status.BAD_REQUEST,
                false,
                mensaje
        );
    }
}