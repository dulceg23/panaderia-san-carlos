/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.manager;

import com.panaderia.panaderiaweb.config.ConexionBD;
import com.panaderia.panaderiaweb.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class UsuarioManager {


    // ====================================================
    // LISTAR USUARIOS
    // ====================================================
    public List<Usuario> listarUsuarios()
            throws Exception {

        List<Usuario> lista =
                new ArrayList<>();


        String sql =
                "SELECT " +
                "ID_USUARIO, " +
                "NOMBRE_COMPLETO, " +
                "NOMBRE_USUARIO, " +
                "ID_ROL, " +
                "ROL, " +
                "ID_SUCURSAL, " +
                "SUCURSAL, " +
                "ESTADO, " +
                "FECHA_CREACION " +
                "FROM VW_USUARIOS " +
                "ORDER BY ID_USUARIO";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        mapearUsuario(rs)
                );
            }
        }


        return lista;
    }


    // ====================================================
    // BUSCAR USUARIO
    // ====================================================
    public Usuario buscarPorId(
            int idUsuario
    ) throws Exception {


        String sql =
                "SELECT " +
                "ID_USUARIO, " +
                "NOMBRE_COMPLETO, " +
                "NOMBRE_USUARIO, " +
                "ID_ROL, " +
                "ROL, " +
                "ID_SUCURSAL, " +
                "SUCURSAL, " +
                "ESTADO, " +
                "FECHA_CREACION " +
                "FROM VW_USUARIOS " +
                "WHERE ID_USUARIO = ?";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idUsuario
            );


            try (
                ResultSet rs =
                        ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapearUsuario(
                            rs
                    );
                }
            }
        }


        return null;
    }


    // ====================================================
    // MAPEAR USUARIO
    // ====================================================
    private Usuario mapearUsuario(
            ResultSet rs
    ) throws Exception {


        Usuario usuario =
                new Usuario();


        usuario.setIdUsuario(
                rs.getInt(
                        "ID_USUARIO"
                )
        );


        usuario.setNombreCompleto(
                rs.getString(
                        "NOMBRE_COMPLETO"
                )
        );


        usuario.setNombreUsuario(
                rs.getString(
                        "NOMBRE_USUARIO"
                )
        );


        usuario.setIdRol(
                rs.getInt(
                        "ID_ROL"
                )
        );


        usuario.setRol(
                rs.getString(
                        "ROL"
                )
        );


        int idSucursal =
                rs.getInt(
                        "ID_SUCURSAL"
                );


        if (rs.wasNull()) {

            usuario.setIdSucursal(
                    null
            );

        } else {

            usuario.setIdSucursal(
                    idSucursal
            );
        }


        usuario.setSucursal(
                rs.getString(
                        "SUCURSAL"
                )
        );


        usuario.setEstado(
                rs.getString(
                        "ESTADO"
                )
        );


        usuario.setFechaCreacion(
                rs.getTimestamp(
                        "FECHA_CREACION"
                )
        );


        return usuario;
    }


    // ====================================================
    // LISTAR ROLES
    // ====================================================
    public List<Map<String, Object>>
            listarRoles() throws Exception {


        List<Map<String, Object>> lista =
                new ArrayList<>();


        String sql =
                "SELECT " +
                "ID_ROL, " +
                "NOMBRE " +
                "FROM ROL " +
                "WHERE ESTADO = 'ACTIVO' " +
                "ORDER BY ID_ROL";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery()
        ) {

            while (rs.next()) {

                Map<String, Object> rol =
                        new LinkedHashMap<>();


                rol.put(
                        "idRol",
                        rs.getInt(
                                "ID_ROL"
                        )
                );


                rol.put(
                        "nombre",
                        rs.getString(
                                "NOMBRE"
                        )
                );


                lista.add(
                        rol
                );
            }
        }


        return lista;
    }


    // ====================================================
    // OBTENER NOMBRE DEL ROL
    // ====================================================
    public String obtenerNombreRol(
            int idRol
    ) throws Exception {


        String sql =
                "SELECT NOMBRE " +
                "FROM ROL " +
                "WHERE ID_ROL = ? " +
                "AND ESTADO = 'ACTIVO'";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idRol
            );


            try (
                ResultSet rs =
                        ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getString(
                            "NOMBRE"
                    );
                }
            }
        }


        return null;
    }


    // ====================================================
    // CONTAR ADMINISTRADORES ACTIVOS
    // ====================================================
    public int contarAdminsActivos()
            throws Exception {


        String sql =
                "SELECT COUNT(*) TOTAL " +
                "FROM USUARIO U " +
                "INNER JOIN ROL R " +
                "ON U.ID_ROL = R.ID_ROL " +
                "WHERE U.ESTADO = 'ACTIVO' " +
                "AND R.NOMBRE = 'ADMIN' " +
                "AND R.ESTADO = 'ACTIVO'";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery()
        ) {

            if (rs.next()) {

                return rs.getInt(
                        "TOTAL"
                );
            }
        }


        return 0;
    }


    // ====================================================
    // CREAR USUARIO
    // ====================================================
    public int crearUsuario(
            Usuario usuario
    ) throws Exception {


        String sql =
                "{call PKG_USUARIOS.CREAR_USUARIO(?,?,?,?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setString(
                    1,
                    usuario.getNombreCompleto()
            );


            cs.setString(
                    2,
                    usuario.getNombreUsuario()
            );


            cs.setString(
                    3,
                    usuario.getPassword()
            );


            cs.setInt(
                    4,
                    usuario.getIdRol()
            );


            if (
                usuario.getIdSucursal()
                == null
            ) {

                cs.setNull(
                        5,
                        Types.NUMERIC
                );

            } else {

                cs.setInt(
                        5,
                        usuario.getIdSucursal()
                );
            }


            cs.registerOutParameter(
                    6,
                    Types.NUMERIC
            );


            cs.execute();


            return cs.getInt(6);
        }
    }


    // ====================================================
    // MODIFICAR USUARIO
    // ====================================================
    public void modificarUsuario(

            int idUsuario,
            Usuario usuario

    ) throws Exception {


        String sql =
                "{call PKG_USUARIOS.MODIFICAR_USUARIO(?,?,?,?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idUsuario
            );


            cs.setString(
                    2,
                    usuario.getNombreCompleto()
            );


            cs.setString(
                    3,
                    usuario.getNombreUsuario()
            );


            cs.setInt(
                    4,
                    usuario.getIdRol()
            );


            if (
                usuario.getIdSucursal()
                == null
            ) {

                cs.setNull(
                        5,
                        Types.NUMERIC
                );

            } else {

                cs.setInt(
                        5,
                        usuario.getIdSucursal()
                );
            }


            cs.execute();
        }
    }


    // ====================================================
    // CAMBIAR ESTADO
    // ====================================================
    public void cambiarEstado(

            int idUsuario,
            String estado

    ) throws Exception {


        String sql =
                "{call PKG_USUARIOS.CAMBIAR_ESTADO(?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idUsuario
            );


            cs.setString(
                    2,
                    estado
            );


            cs.execute();
        }
    }


    // ====================================================
    // CAMBIAR CONTRASEÑA
    // ====================================================
    public void cambiarPassword(

            int idUsuario,
            String password

    ) throws Exception {


        String sql =
                "{call PKG_USUARIOS.CAMBIAR_PASSWORD(?,?)}";


        try (
            Connection conn =
                    ConexionBD.getConnection();

            CallableStatement cs =
                    conn.prepareCall(sql)
        ) {


            cs.setInt(
                    1,
                    idUsuario
            );


            cs.setString(
                    2,
                    password
            );


            cs.execute();
        }
    }
}