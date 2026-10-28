package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.Inventario;
import com.panaderia.panaderiaweb.model.MovimientoInventario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class InventarioManager {

    // ----------------------------------------------------
    // LISTAR INVENTARIO
    // ----------------------------------------------------
    public List<Inventario> listarInventario() throws Exception {

        List<Inventario> lista = new ArrayList<>();

        String sql =
                "SELECT ID_PRODUCTO, TIPO_PRODUCTO, PRODUCTO, " +
                "EXISTENCIA, EXISTENCIA_MINIMA, UNIDAD_MEDIDA, ESTADO " +
                "FROM VW_INVENTARIO " +
                "ORDER BY ID_PRODUCTO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Inventario inventario = new Inventario();

                inventario.setIdProducto(
                        rs.getInt("ID_PRODUCTO")
                );

                inventario.setTipoProducto(
                        rs.getString("TIPO_PRODUCTO")
                );

                inventario.setProducto(
                        rs.getString("PRODUCTO")
                );

                inventario.setExistencia(
                        rs.getDouble("EXISTENCIA")
                );

                inventario.setExistenciaMinima(
                        rs.getDouble("EXISTENCIA_MINIMA")
                );

                inventario.setUnidadMedida(
                        rs.getString("UNIDAD_MEDIDA")
                );

                inventario.setEstado(
                        rs.getInt("ESTADO")
                );

                lista.add(inventario);
            }
        }

        return lista;
    }


    // ----------------------------------------------------
    // BUSCAR INVENTARIO POR PRODUCTO
    // ----------------------------------------------------
    public Inventario buscarPorProducto(
            int idProducto
    ) throws Exception {

        String sql =
                "SELECT ID_PRODUCTO, TIPO_PRODUCTO, PRODUCTO, " +
                "EXISTENCIA, EXISTENCIA_MINIMA, UNIDAD_MEDIDA, ESTADO " +
                "FROM VW_INVENTARIO " +
                "WHERE ID_PRODUCTO = ?";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Inventario inventario =
                            new Inventario();

                    inventario.setIdProducto(
                            rs.getInt("ID_PRODUCTO")
                    );

                    inventario.setTipoProducto(
                            rs.getString("TIPO_PRODUCTO")
                    );

                    inventario.setProducto(
                            rs.getString("PRODUCTO")
                    );

                    inventario.setExistencia(
                            rs.getDouble("EXISTENCIA")
                    );

                    inventario.setExistenciaMinima(
                            rs.getDouble("EXISTENCIA_MINIMA")
                    );

                    inventario.setUnidadMedida(
                            rs.getString("UNIDAD_MEDIDA")
                    );

                    inventario.setEstado(
                            rs.getInt("ESTADO")
                    );

                    return inventario;
                }
            }
        }

        return null;
    }


    // ----------------------------------------------------
    // LISTAR EXISTENCIAS BAJAS
    // ----------------------------------------------------
    public List<Inventario> listarExistenciaBaja()
            throws Exception {

        List<Inventario> lista = new ArrayList<>();

        String sql =
                "SELECT ID_PRODUCTO, TIPO_PRODUCTO, PRODUCTO, " +
                "EXISTENCIA, EXISTENCIA_MINIMA, UNIDAD_MEDIDA " +
                "FROM VW_EXISTENCIA_BAJA " +
                "ORDER BY ID_PRODUCTO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Inventario inventario =
                        new Inventario();

                inventario.setIdProducto(
                        rs.getInt("ID_PRODUCTO")
                );

                inventario.setTipoProducto(
                        rs.getString("TIPO_PRODUCTO")
                );

                inventario.setProducto(
                        rs.getString("PRODUCTO")
                );

                inventario.setExistencia(
                        rs.getDouble("EXISTENCIA")
                );

                inventario.setExistenciaMinima(
                        rs.getDouble("EXISTENCIA_MINIMA")
                );

                inventario.setUnidadMedida(
                        rs.getString("UNIDAD_MEDIDA")
                );

                lista.add(inventario);
            }
        }

        return lista;
    }


    // ----------------------------------------------------
    // REGISTRAR ENTRADA
    // ----------------------------------------------------
    public void registrarEntrada(
            MovimientoInventario movimiento
    ) throws Exception {

        String sql =
                "{call PKG_INVENTARIO_PRODUCCION." +
                "ENTRADA_INVENTARIO(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(
                    1,
                    movimiento.getIdProducto()
            );

            cs.setDouble(
                    2,
                    movimiento.getCantidad()
            );

            cs.setString(
                    3,
                    movimiento.getDescripcion()
            );

            cs.execute();
        }
    }


    // ----------------------------------------------------
    // LISTAR MOVIMIENTOS
    // ----------------------------------------------------
    public List<MovimientoInventario> listarMovimientos()
            throws Exception {

        List<MovimientoInventario> lista =
                new ArrayList<>();

        String sql =
                "SELECT ID_MOVIMIENTO, ID_PRODUCTO, PRODUCTO, " +
                "FECHA, TIPO_MOVIMIENTO, CANTIDAD, " +
                "DESCRIPCION, UNIDAD_MEDIDA " +
                "FROM VW_MOVIMIENTOS_INVENTARIO " +
                "ORDER BY ID_MOVIMIENTO DESC";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                MovimientoInventario movimiento =
                        new MovimientoInventario();

                movimiento.setIdMovimiento(
                        rs.getInt("ID_MOVIMIENTO")
                );

                movimiento.setIdProducto(
                        rs.getInt("ID_PRODUCTO")
                );

                movimiento.setProducto(
                        rs.getString("PRODUCTO")
                );

                movimiento.setFecha(
                        rs.getTimestamp("FECHA")
                );

                movimiento.setTipoMovimiento(
                        rs.getString("TIPO_MOVIMIENTO")
                );

                movimiento.setCantidad(
                        rs.getDouble("CANTIDAD")
                );

                movimiento.setDescripcion(
                        rs.getString("DESCRIPCION")
                );

                movimiento.setUnidadMedida(
                        rs.getString("UNIDAD_MEDIDA")
                );

                lista.add(movimiento);
            }
        }

        return lista;
    }
}