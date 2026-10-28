package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.Sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class SucursalManager {

    // ----------------------------------------------------
    // LISTAR TODAS LAS SUCURSALES
    // ----------------------------------------------------
    public List<Sucursal> listarSucursales() throws Exception {

        List<Sucursal> lista = new ArrayList<>();

        String sql =
                "SELECT ID_SUCURSAL, NOMBRE, ESTADO " +
                "FROM SUCURSAL " +
                "ORDER BY ID_SUCURSAL";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Sucursal sucursal = new Sucursal();

                sucursal.setIdSucursal(
                        rs.getInt("ID_SUCURSAL")
                );

                sucursal.setNombre(
                        rs.getString("NOMBRE")
                );

                sucursal.setEstado(
                        rs.getInt("ESTADO")
                );

                lista.add(sucursal);
            }
        }

        return lista;
    }

    // ----------------------------------------------------
    // BUSCAR SUCURSAL POR ID
    // ----------------------------------------------------
    public Sucursal buscarPorId(int idSucursal) throws Exception {

        String sql =
                "SELECT ID_SUCURSAL, NOMBRE, ESTADO " +
                "FROM SUCURSAL " +
                "WHERE ID_SUCURSAL = ?";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idSucursal);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Sucursal sucursal = new Sucursal();

                    sucursal.setIdSucursal(
                            rs.getInt("ID_SUCURSAL")
                    );

                    sucursal.setNombre(
                            rs.getString("NOMBRE")
                    );

                    sucursal.setEstado(
                            rs.getInt("ESTADO")
                    );

                    return sucursal;
                }
            }
        }

        return null;
    }

    // ----------------------------------------------------
    // CREAR SUCURSAL
    // ----------------------------------------------------
    public int crearSucursal(Sucursal sucursal) throws Exception {

        Connection conn = null;

        try {

            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            int idSucursal;

            String sqlId =
                    "SELECT SEQ_SUCURSAL.NEXTVAL FROM DUAL";

            try (
                PreparedStatement ps =
                        conn.prepareStatement(sqlId);

                ResultSet rs =
                        ps.executeQuery()
            ) {

                rs.next();
                idSucursal = rs.getInt(1);
            }

            String sql =
                    "INSERT INTO SUCURSAL " +
                    "(ID_SUCURSAL, NOMBRE, ESTADO) " +
                    "VALUES (?, ?, 1)";

            try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
            ) {

                ps.setInt(1, idSucursal);

                ps.setString(
                        2,
                        sucursal.getNombre()
                                .trim()
                                .toUpperCase()
                );

                ps.executeUpdate();
            }

            conn.commit();

            return idSucursal;

        } catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // ----------------------------------------------------
    // MODIFICAR SUCURSAL
    // ----------------------------------------------------
    public boolean modificarSucursal(
            int idSucursal,
            Sucursal sucursal
    ) throws Exception {

        Connection conn = null;

        try {

            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            String sql =
                    "UPDATE SUCURSAL " +
                    "SET NOMBRE = ? " +
                    "WHERE ID_SUCURSAL = ?";

            int filas;

            try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
            ) {

                ps.setString(
                        1,
                        sucursal.getNombre()
                                .trim()
                                .toUpperCase()
                );

                ps.setInt(
                        2,
                        idSucursal
                );

                filas = ps.executeUpdate();
            }

            if (filas == 0) {

                conn.rollback();

                return false;
            }

            conn.commit();

            return true;

        } catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // ----------------------------------------------------
    // CAMBIAR ESTADO
    // ----------------------------------------------------
    public boolean cambiarEstado(
            int idSucursal,
            int estado
    ) throws Exception {

        if (estado != 0 && estado != 1) {
            throw new Exception(
                    "El estado debe ser 0 o 1"
            );
        }

        Connection conn = null;

        try {

            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            String sql =
                    "UPDATE SUCURSAL " +
                    "SET ESTADO = ? " +
                    "WHERE ID_SUCURSAL = ?";

            int filas;

            try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
            ) {

                ps.setInt(1, estado);
                ps.setInt(2, idSucursal);

                filas = ps.executeUpdate();
            }

            if (filas == 0) {

                conn.rollback();

                return false;
            }

            conn.commit();

            return true;

        } catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}