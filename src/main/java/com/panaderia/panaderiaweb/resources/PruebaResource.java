package com.panaderia.panaderiaweb.resources;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("prueba")
public class PruebaResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response prueba() {

        String json = "{"
                + "\"exito\": true,"
                + "\"mensaje\": \"API de Panaderia funcionando correctamente\""
                + "}";

        return Response.ok(json).build();
    }
}