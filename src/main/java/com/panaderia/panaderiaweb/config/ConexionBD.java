package com.panaderia.panaderiaweb.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/XEPDB1";

    private static final String USUARIO =
            "PANADERIA";

    private static final String PASSWORD =
            System.getenv("PANADERIA_DB_PASSWORD");


    static {

        try {

            Class.forName(
                    "oracle.jdbc.OracleDriver"
            );

            System.out.println(
                    "Driver Oracle cargado correctamente."
            );

        } catch (ClassNotFoundException e) {

            throw new ExceptionInInitializerError(
                    "No se encontró el driver JDBC de Oracle."
            );
        }
    }


    public static Connection getConnection()
            throws SQLException {

        if (
            PASSWORD == null
            ||
            PASSWORD.trim().isEmpty()
        ) {

            throw new SQLException(
                    "No se encuentra configurada "
                    + "PANADERIA_DB_PASSWORD."
            );
        }


        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}