package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.DetallePedido;
import com.panaderia.panaderiaweb.model.Pedido;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PedidoManager {


    // ====================================================
    // LISTAR TODOS LOS PEDIDOS
    // ADMIN / PRODUCCION
    // ====================================================
    public List<Pedido> listarPedidos()
            throws Exception {

        return consultarPedidos(null);
    }


    // ====================================================
    // LISTAR PEDIDOS DE UNA SUCURSAL
    // ====================================================
    public List<Pedido> listarPedidosPorSucursal(
            int idSucursal
    ) throws Exception {

        return consultarPedidos(idSucursal);
    }


    // ====================================================
    // CONSULTA GENERAL DE PEDIDOS
    // ====================================================
    private List<Pedido> consultarPedidos(
            Integer idSucursal
    ) throws Exception {

        Map<Integer, Pedido> mapa =
                new LinkedHashMap<>();


        String sql =
                "SELECT " +
                "PE.ID_PEDIDO, " +
                "PE.ID_SUCURSAL, " +
                "S.NOMBRE AS SUCURSAL, " +
                "PE.FECHA, " +
                "PE.TURNO, " +
                "PE.ESTADO, " +
                "DP.ID_PRODUCTO, " +
                "P.NOMBRE AS PRODUCTO, " +
                "DP.CANTIDAD " +
                "FROM PEDIDO PE " +
                "INNER JOIN SUCURSAL S " +
                "ON PE.ID_SUCURSAL = S.ID_SUCURSAL " +
                "LEFT JOIN DETALLE_PEDIDO DP " +
                "ON PE.ID_PEDIDO = DP.ID_PEDIDO " +
                "LEFT JOIN PRODUCTO P " +
                "ON DP.ID_PRODUCTO = P.ID_PRODUCTO ";


        if (idSucursal != null) {

            sql +=
                    "WHERE PE.ID_SUCURSAL = ? ";
        }


        sql +=
                "ORDER BY PE.ID_PEDIDO DESC, " +
                "DP.ID_PRODUCTO";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {


            if (idSucursal != null) {

                ps.setInt(
                        1,
                        idSucursal
                );
            }


            try (
                ResultSet rs =
                        ps.executeQuery()
            ) {


                while (rs.next()) {


                    int idPedido =
                            rs.getInt(
                                    "ID_PEDIDO"
                            );


                    Pedido pedido =
                            mapa.get(
                                    idPedido
                            );


                    if (pedido == null) {


                        pedido =
                                new Pedido();


                        pedido.setIdPedido(
                                idPedido
                        );


                        pedido.setIdSucursal(
                                rs.getInt(
                                        "ID_SUCURSAL"
                                )
                        );


                        pedido.setSucursal(
                                rs.getString(
                                        "SUCURSAL"
                                )
                        );


                        pedido.setFecha(
                                rs.getTimestamp(
                                        "FECHA"
                                )
                        );


                        pedido.setTurno(
                                rs.getString(
                                        "TURNO"
                                )
                        );


                        pedido.setEstado(
                                rs.getString(
                                        "ESTADO"
                                )
                        );


                        mapa.put(
                                idPedido,
                                pedido
                        );
                    }


                    if (
                        rs.getObject(
                                "ID_PRODUCTO"
                        )
                        != null
                    ) {


                        DetallePedido detalle =
                                new DetallePedido();


                        detalle.setIdProducto(
                                rs.getInt(
                                        "ID_PRODUCTO"
                                )
                        );


                        detalle.setProducto(
                                rs.getString(
                                        "PRODUCTO"
                                )
                        );


                        detalle.setCantidad(
                                rs.getDouble(
                                        "CANTIDAD"
                                )
                        );


                        pedido
                                .getDetalles()
                                .add(detalle);
                    }
                }
            }
        }


        return new ArrayList<>(
                mapa.values()
        );
    }


    // ====================================================
    // BUSCAR PEDIDO POR ID
    // ====================================================
    public Pedido buscarPorId(
            int idPedido
    ) throws Exception {


        String sql =
                "SELECT " +
                "PE.ID_PEDIDO, " +
                "PE.ID_SUCURSAL, " +
                "S.NOMBRE AS SUCURSAL, " +
                "PE.FECHA, " +
                "PE.TURNO, " +
                "PE.ESTADO, " +
                "DP.ID_PRODUCTO, " +
                "P.NOMBRE AS PRODUCTO, " +
                "DP.CANTIDAD " +
                "FROM PEDIDO PE " +
                "INNER JOIN SUCURSAL S " +
                "ON PE.ID_SUCURSAL = S.ID_SUCURSAL " +
                "LEFT JOIN DETALLE_PEDIDO DP " +
                "ON PE.ID_PEDIDO = DP.ID_PEDIDO " +
                "LEFT JOIN PRODUCTO P " +
                "ON DP.ID_PRODUCTO = P.ID_PRODUCTO " +
                "WHERE PE.ID_PEDIDO = ? " +
                "ORDER BY DP.ID_PRODUCTO";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {


            ps.setInt(
                    1,
                    idPedido
            );


            try (
                ResultSet rs =
                        ps.executeQuery()
            ) {


                Pedido pedido = null;


                while (rs.next()) {


                    if (pedido == null) {


                        pedido =
                                new Pedido();


                        pedido.setIdPedido(
                                rs.getInt(
                                        "ID_PEDIDO"
                                )
                        );


                        pedido.setIdSucursal(
                                rs.getInt(
                                        "ID_SUCURSAL"
                                )
                        );


                        pedido.setSucursal(
                                rs.getString(
                                        "SUCURSAL"
                                )
                        );


                        pedido.setFecha(
                                rs.getTimestamp(
                                        "FECHA"
                                )
                        );


                        pedido.setTurno(
                                rs.getString(
                                        "TURNO"
                                )
                        );


                        pedido.setEstado(
                                rs.getString(
                                        "ESTADO"
                                )
                        );
                    }


                    if (
                        rs.getObject(
                                "ID_PRODUCTO"
                        )
                        != null
                    ) {


                        DetallePedido detalle =
                                new DetallePedido();


                        detalle.setIdProducto(
                                rs.getInt(
                                        "ID_PRODUCTO"
                                )
                        );


                        detalle.setProducto(
                                rs.getString(
                                        "PRODUCTO"
                                )
                        );


                        detalle.setCantidad(
                                rs.getDouble(
                                        "CANTIDAD"
                                )
                        );


                        pedido
                                .getDetalles()
                                .add(detalle);
                    }
                }


                return pedido;
            }
        }
    }


    // ====================================================
    // COMPROBAR QUE EL PEDIDO PERTENECE A SUCURSAL
    // ====================================================
    public boolean pedidoPerteneceASucursal(
            int idPedido,
            int idSucursal
    ) throws Exception {


        String sql =
                "SELECT COUNT(*) AS TOTAL " +
                "FROM PEDIDO " +
                "WHERE ID_PEDIDO = ? " +
                "AND ID_SUCURSAL = ?";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {


            ps.setInt(
                    1,
                    idPedido
            );


            ps.setInt(
                    2,
                    idSucursal
            );


            try (
                ResultSet rs =
                        ps.executeQuery()
            ) {


                if (rs.next()) {

                    return rs.getInt(
                            "TOTAL"
                    ) > 0;
                }
            }
        }


        return false;
    }


    // ====================================================
    // CREAR PEDIDO
    // ====================================================
    public int crearPedido(
            Pedido pedido
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.CREAR_PEDIDO(?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    pedido.getIdSucursal()
            );


            cs.setString(
                    2,
                    pedido.getTurno()
            );


            cs.registerOutParameter(
                    3,
                    Types.NUMERIC
            );


            cs.execute();


            return cs.getInt(3);
        }
    }


    // ====================================================
    // AGREGAR PRODUCTO AL PEDIDO
    // ====================================================
    public void agregarDetalle(
            int idPedido,
            DetallePedido detalle
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.AGREGAR_DETALLE(?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idPedido
            );


            cs.setInt(
                    2,
                    detalle.getIdProducto()
            );


            cs.setDouble(
                    3,
                    detalle.getCantidad()
            );


            cs.execute();
        }
    }


    // ====================================================
    // MODIFICAR CANTIDAD
    // ====================================================
    public void modificarDetalle(
            int idPedido,
            int idProducto,
            double cantidad
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.MODIFICAR_DETALLE(?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idPedido
            );


            cs.setInt(
                    2,
                    idProducto
            );


            cs.setDouble(
                    3,
                    cantidad
            );


            cs.execute();
        }
    }


    // ====================================================
    // ELIMINAR PRODUCTO DEL PEDIDO
    // ====================================================
    public void eliminarDetalle(
            int idPedido,
            int idProducto
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.ELIMINAR_DETALLE(?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idPedido
            );


            cs.setInt(
                    2,
                    idProducto
            );


            cs.execute();
        }
    }


    // ====================================================
    // CAMBIAR ESTADO
    // SOLO ADMIN / PRODUCCION DESDE CONTROLLER
    // ====================================================
    public void cambiarEstado(
            int idPedido,
            String estado
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.CAMBIAR_ESTADO(?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idPedido
            );


            cs.setString(
                    2,
                    estado.toUpperCase()
            );


            cs.execute();
        }
    }


    // ====================================================
    // DESPACHAR
    // ====================================================
    public void despacharPedido(
            int idPedido
    ) throws Exception {


        String sql =
                "{call PKG_PEDIDOS.DESPACHAR_PEDIDO(?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idPedido
            );


            cs.execute();
        }
    }
}