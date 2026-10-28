package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.DetalleReceta;
import com.panaderia.panaderiaweb.model.Receta;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecetaManager {

    // ----------------------------------------------------
    // LISTAR TODAS LAS RECETAS
    // ----------------------------------------------------
    public List<Receta> listarRecetas() throws Exception {

        Map<Integer, Receta> mapa = new LinkedHashMap<>();

        String sql =
                "SELECT " +
                "R.ID_RECETA, " +
                "R.ID_PRODUCTO_FINAL, " +
                "PF.NOMBRE AS PRODUCTO_FINAL, " +
                "R.DESCRIPCION, " +
                "R.ESTADO, " +
                "DR.ID_INSUMO, " +
                "I.NOMBRE AS INGREDIENTE, " +
                "DR.CANTIDAD, " +
                "I.UNIDAD_MEDIDA " +
                "FROM RECETA R " +
                "INNER JOIN PRODUCTO PF " +
                "ON R.ID_PRODUCTO_FINAL = PF.ID_PRODUCTO " +
                "LEFT JOIN DETALLE_RECETA DR " +
                "ON R.ID_RECETA = DR.ID_RECETA " +
                "LEFT JOIN PRODUCTO I " +
                "ON DR.ID_INSUMO = I.ID_PRODUCTO " +
                "ORDER BY R.ID_RECETA, DR.ID_INSUMO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int idReceta = rs.getInt("ID_RECETA");

                Receta receta = mapa.get(idReceta);

                if (receta == null) {

                    receta = new Receta();

                    receta.setIdReceta(idReceta);
                    receta.setIdProductoFinal(
                            rs.getInt("ID_PRODUCTO_FINAL")
                    );
                    receta.setProductoFinal(
                            rs.getString("PRODUCTO_FINAL")
                    );
                    receta.setDescripcion(
                            rs.getString("DESCRIPCION")
                    );
                    receta.setEstado(
                            rs.getInt("ESTADO")
                    );

                    mapa.put(idReceta, receta);
                }

                if (rs.getObject("ID_INSUMO") != null) {

                    DetalleReceta detalle =
                            new DetalleReceta();

                    detalle.setIdInsumo(
                            rs.getInt("ID_INSUMO")
                    );

                    detalle.setNombreInsumo(
                            rs.getString("INGREDIENTE")
                    );

                    detalle.setCantidad(
                            rs.getDouble("CANTIDAD")
                    );

                    detalle.setUnidadMedida(
                            rs.getString("UNIDAD_MEDIDA")
                    );

                    receta.getIngredientes()
                            .add(detalle);
                }
            }
        }

        return new ArrayList<>(mapa.values());
    }

    // ----------------------------------------------------
    // BUSCAR RECETA POR ID
    // ----------------------------------------------------
    public Receta buscarPorId(int idReceta) throws Exception {

        String sql =
                "SELECT " +
                "R.ID_RECETA, " +
                "R.ID_PRODUCTO_FINAL, " +
                "PF.NOMBRE AS PRODUCTO_FINAL, " +
                "R.DESCRIPCION, " +
                "R.ESTADO, " +
                "DR.ID_INSUMO, " +
                "I.NOMBRE AS INGREDIENTE, " +
                "DR.CANTIDAD, " +
                "I.UNIDAD_MEDIDA " +
                "FROM RECETA R " +
                "INNER JOIN PRODUCTO PF " +
                "ON R.ID_PRODUCTO_FINAL = PF.ID_PRODUCTO " +
                "LEFT JOIN DETALLE_RECETA DR " +
                "ON R.ID_RECETA = DR.ID_RECETA " +
                "LEFT JOIN PRODUCTO I " +
                "ON DR.ID_INSUMO = I.ID_PRODUCTO " +
                "WHERE R.ID_RECETA = ? " +
                "ORDER BY DR.ID_INSUMO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idReceta);

            try (ResultSet rs = ps.executeQuery()) {

                Receta receta = null;

                while (rs.next()) {

                    if (receta == null) {

                        receta = new Receta();

                        receta.setIdReceta(
                                rs.getInt("ID_RECETA")
                        );

                        receta.setIdProductoFinal(
                                rs.getInt("ID_PRODUCTO_FINAL")
                        );

                        receta.setProductoFinal(
                                rs.getString("PRODUCTO_FINAL")
                        );

                        receta.setDescripcion(
                                rs.getString("DESCRIPCION")
                        );

                        receta.setEstado(
                                rs.getInt("ESTADO")
                        );
                    }

                    if (rs.getObject("ID_INSUMO") != null) {

                        DetalleReceta detalle =
                                new DetalleReceta();

                        detalle.setIdInsumo(
                                rs.getInt("ID_INSUMO")
                        );

                        detalle.setNombreInsumo(
                                rs.getString("INGREDIENTE")
                        );

                        detalle.setCantidad(
                                rs.getDouble("CANTIDAD")
                        );

                        detalle.setUnidadMedida(
                                rs.getString("UNIDAD_MEDIDA")
                        );

                        receta.getIngredientes()
                                .add(detalle);
                    }
                }

                return receta;
            }
        }
    }

    // ----------------------------------------------------
    // CREAR RECETA
    // ----------------------------------------------------
    public int crearReceta(Receta receta) throws Exception {

        String sql =
                "{call PKG_RECETAS.CREAR_RECETA(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(
                    1,
                    receta.getIdProductoFinal()
            );

            cs.setString(
                    2,
                    receta.getDescripcion()
            );

            cs.registerOutParameter(
                    3,
                    Types.NUMERIC
            );

            cs.execute();

            return cs.getInt(3);
        }
    }

    // ----------------------------------------------------
    // AGREGAR INGREDIENTE
    // ----------------------------------------------------
    public void agregarIngrediente(
            int idReceta,
            DetalleReceta detalle
    ) throws Exception {

        String sql =
                "{call PKG_RECETAS.AGREGAR_INGREDIENTE(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);

            cs.setInt(
                    2,
                    detalle.getIdInsumo()
            );

            cs.setDouble(
                    3,
                    detalle.getCantidad()
            );

            cs.execute();
        }
    }

    // ----------------------------------------------------
    // MODIFICAR CANTIDAD DE INGREDIENTE
    // ----------------------------------------------------
    public void modificarIngrediente(
            int idReceta,
            int idInsumo,
            double cantidad
    ) throws Exception {

        String sql =
                "{call PKG_RECETAS.MODIFICAR_INGREDIENTE(?,?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);
            cs.setDouble(3, cantidad);

            cs.execute();
        }
    }

    // ----------------------------------------------------
    // ELIMINAR INGREDIENTE
    // ----------------------------------------------------
    public void eliminarIngrediente(
            int idReceta,
            int idInsumo
    ) throws Exception {

        String sql =
                "{call PKG_RECETAS.ELIMINAR_INGREDIENTE(?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);

            cs.execute();
        }
    }

    // ----------------------------------------------------
    // CAMBIAR ESTADO DE RECETA
    // ----------------------------------------------------
    public void cambiarEstado(
            int idReceta,
            int estado
    ) throws Exception {

        String sql =
                "{call PKG_RECETAS.CAMBIAR_ESTADO(?,?)}";

        try (
            Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);
            cs.setInt(2, estado);

            cs.execute();
        }
    }
}