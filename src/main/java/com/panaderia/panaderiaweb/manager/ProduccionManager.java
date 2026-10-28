package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.Produccion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

public class ProduccionManager {

    // ----------------------------------------------------
    // LISTAR TODA LA PRODUCCION
    // ----------------------------------------------------
    public List<Produccion> listarProduccion() throws Exception {

        List<Produccion> lista = new ArrayList<>();

        String sql =
                "SELECT ID_PRODUCCION, FECHA, ID_PRODUCTO, " +
                "PRODUCTO, CANTIDAD, ESTADO " +
                "FROM VW_PRODUCCION " +
                "ORDER BY ID_PRODUCCION DESC";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Produccion produccion = new Produccion();

                produccion.setIdProduccion(
                        rs.getInt("ID_PRODUCCION")
                );

                produccion.setFecha(
                        rs.getTimestamp("FECHA")
                );

                produccion.setIdProducto(
                        rs.getInt("ID_PRODUCTO")
                );

                produccion.setProducto(
                        rs.getString("PRODUCTO")
                );

                produccion.setCantidad(
                        rs.getDouble("CANTIDAD")
                );

                produccion.setEstado(
                        rs.getString("ESTADO")
                );

                lista.add(produccion);
            }
        }

        return lista;
    }


    // ----------------------------------------------------
    // BUSCAR PRODUCCION POR ID
    // ----------------------------------------------------
    public Produccion buscarPorId(
            int idProduccion
    ) throws Exception {

        String sql =
                "SELECT ID_PRODUCCION, FECHA, ID_PRODUCTO, " +
                "PRODUCTO, CANTIDAD, ESTADO " +
                "FROM VW_PRODUCCION " +
                "WHERE ID_PRODUCCION = ?";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProduccion);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Produccion produccion = new Produccion();

                    produccion.setIdProduccion(
                            rs.getInt("ID_PRODUCCION")
                    );

                    produccion.setFecha(
                            rs.getTimestamp("FECHA")
                    );

                    produccion.setIdProducto(
                            rs.getInt("ID_PRODUCTO")
                    );

                    produccion.setProducto(
                            rs.getString("PRODUCTO")
                    );

                    produccion.setCantidad(
                            rs.getDouble("CANTIDAD")
                    );

                    produccion.setEstado(
                            rs.getString("ESTADO")
                    );

                    return produccion;
                }
            }
        }

        return null;
    }


    // ----------------------------------------------------
    // REGISTRAR PRODUCCION
    // ----------------------------------------------------
    public int registrarProduccion(
            Produccion produccion
    ) throws Exception {

        String sql =
                "{call PKG_INVENTARIO_PRODUCCION." +
                "REGISTRAR_PRODUCCION(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(
                    1,
                    produccion.getIdProducto()
            );

            cs.setDouble(
                    2,
                    produccion.getCantidad()
            );

            cs.registerOutParameter(
                    3,
                    Types.NUMERIC
            );

            cs.execute();

            return cs.getInt(3);
        }
    }
}