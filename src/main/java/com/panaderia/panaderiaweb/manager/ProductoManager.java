package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.Producto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

public class ProductoManager {

    // ----------------------------------------------------
    // LISTAR TODOS LOS PRODUCTOS
    // ----------------------------------------------------
    public List<Producto> listarProductos() throws Exception {

        List<Producto> lista = new ArrayList<>();

        String sql =
                "SELECT " +
                "P.ID_PRODUCTO, " +
                "P.ID_TIPO_PRODUCTO, " +
                "TP.NOMBRE AS TIPO_PRODUCTO, " +
                "P.NOMBRE, " +
                "P.UNIDAD_MEDIDA, " +
                "P.ESTADO, " +
                "I.EXISTENCIA, " +
                "I.EXISTENCIA_MINIMA " +
                "FROM PRODUCTO P " +
                "INNER JOIN TIPO_PRODUCTO TP " +
                "ON P.ID_TIPO_PRODUCTO = TP.ID_TIPO_PRODUCTO " +
                "INNER JOIN INVENTARIO I " +
                "ON P.ID_PRODUCTO = I.ID_PRODUCTO " +
                "ORDER BY P.ID_PRODUCTO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Producto producto = new Producto();

                producto.setIdProducto(
                        rs.getInt("ID_PRODUCTO"));

                producto.setIdTipoProducto(
                        rs.getInt("ID_TIPO_PRODUCTO"));

                producto.setTipoProducto(
                        rs.getString("TIPO_PRODUCTO"));

                producto.setNombre(
                        rs.getString("NOMBRE"));

                producto.setUnidadMedida(
                        rs.getString("UNIDAD_MEDIDA"));

                producto.setEstado(
                        rs.getInt("ESTADO"));

                producto.setExistencia(
                        rs.getDouble("EXISTENCIA"));

                producto.setExistenciaMinima(
                        rs.getDouble("EXISTENCIA_MINIMA"));

                lista.add(producto);
            }
        }

        return lista;
    }

    // ----------------------------------------------------
    // BUSCAR PRODUCTO POR ID
    // ----------------------------------------------------
    public Producto buscarPorId(int idProducto) throws Exception {

        String sql =
                "SELECT " +
                "P.ID_PRODUCTO, " +
                "P.ID_TIPO_PRODUCTO, " +
                "TP.NOMBRE AS TIPO_PRODUCTO, " +
                "P.NOMBRE, " +
                "P.UNIDAD_MEDIDA, " +
                "P.ESTADO, " +
                "I.EXISTENCIA, " +
                "I.EXISTENCIA_MINIMA " +
                "FROM PRODUCTO P " +
                "INNER JOIN TIPO_PRODUCTO TP " +
                "ON P.ID_TIPO_PRODUCTO = TP.ID_TIPO_PRODUCTO " +
                "INNER JOIN INVENTARIO I " +
                "ON P.ID_PRODUCTO = I.ID_PRODUCTO " +
                "WHERE P.ID_PRODUCTO = ?";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Producto producto = new Producto();

                    producto.setIdProducto(
                            rs.getInt("ID_PRODUCTO"));

                    producto.setIdTipoProducto(
                            rs.getInt("ID_TIPO_PRODUCTO"));

                    producto.setTipoProducto(
                            rs.getString("TIPO_PRODUCTO"));

                    producto.setNombre(
                            rs.getString("NOMBRE"));

                    producto.setUnidadMedida(
                            rs.getString("UNIDAD_MEDIDA"));

                    producto.setEstado(
                            rs.getInt("ESTADO"));

                    producto.setExistencia(
                            rs.getDouble("EXISTENCIA"));

                    producto.setExistenciaMinima(
                            rs.getDouble("EXISTENCIA_MINIMA"));

                    return producto;
                }
            }
        }

        return null;
    }

    // ----------------------------------------------------
    // CREAR PRODUCTO
    // LLAMA PKG_PRODUCTOS.CREAR_PRODUCTO
    // ----------------------------------------------------
    public int crearProducto(Producto producto) throws Exception {

        String sql =
                "{call PKG_PRODUCTOS.CREAR_PRODUCTO(?,?,?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(
                    1,
                    producto.getIdTipoProducto()
            );

            cs.setString(
                    2,
                    producto.getNombre()
            );

            cs.setString(
                    3,
                    producto.getUnidadMedida()
            );

            cs.setDouble(
                    4,
                    producto.getExistenciaMinima()
            );

            cs.registerOutParameter(
                    5,
                    Types.NUMERIC
            );

            cs.execute();

            return cs.getInt(5);
        }
    }

    // ----------------------------------------------------
    // MODIFICAR PRODUCTO
    // ----------------------------------------------------
    public void modificarProducto(
            int idProducto,
            Producto producto
    ) throws Exception {

        String sql =
                "{call PKG_PRODUCTOS.MODIFICAR_PRODUCTO(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idProducto);
            cs.setString(2, producto.getNombre());
            cs.setString(3, producto.getUnidadMedida());

            cs.execute();
        }
    }

    // ----------------------------------------------------
    // CAMBIAR ESTADO
    // ----------------------------------------------------
    public void cambiarEstado(
            int idProducto,
            int estado
    ) throws Exception {

        String sql =
                "{call PKG_PRODUCTOS.CAMBIAR_ESTADO(?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idProducto);
            cs.setInt(2, estado);

            cs.execute();
        }
    }

    // ----------------------------------------------------
    // CAMBIAR EXISTENCIA MINIMA
    // ----------------------------------------------------
    public void cambiarExistenciaMinima(
            int idProducto,
            double existenciaMinima
    ) throws Exception {

        String sql =
                "{call PKG_PRODUCTOS.ACTUALIZAR_EXISTENCIA_MINIMA(?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idProducto);
            cs.setDouble(2, existenciaMinima);

            cs.execute();
        }
    }
}