package com.panaderia.panaderiaweb.resources;

import com.panaderia.panaderiaweb.config.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("conexion")
public class ConexionResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response probarConexion() {

        String sql = "SELECT COUNT(*) AS TOTAL FROM PRODUCTO";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            int totalProductos = 0;

            if (rs.next()) {
                totalProductos = rs.getInt("TOTAL");
            }

            String json = "{"
                    + "\"exito\": true,"
                    + "\"mensaje\": \"Conexion con Oracle correcta\","
                    + "\"productos\": " + totalProductos
                    + "}";

            return Response.ok(json).build();

        } catch (Exception e) {

            e.printStackTrace();

            String mensaje = e.getMessage();

            if (mensaje != null) {
                mensaje = mensaje.replace("\"", "'");
            }

            String json = "{"
                    + "\"exito\": false,"
                    + "\"mensaje\": \"" + mensaje + "\""
                    + "}";

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(json)
                    .build();
        }
    }
}