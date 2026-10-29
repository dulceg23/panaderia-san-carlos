/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.panaderia.panaderiaweb.config;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


@Provider
public class CorsFilter
        implements ContainerRequestFilter,
                   ContainerResponseFilter {


    @Override
    public void filter(
            ContainerRequestContext requestContext
    ) throws IOException {


        if (
            "OPTIONS".equalsIgnoreCase(
                    requestContext.getMethod()
            )
        ) {

            requestContext.abortWith(
                    Response
                            .ok()
                            .build()
            );
        }
    }


    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {


        String origen =
                requestContext.getHeaderString(
                        "Origin"
                );


        if (origenPermitido(origen)) {

            responseContext
                    .getHeaders()
                    .putSingle(
                            "Access-Control-Allow-Origin",
                            origen
                    );


            responseContext
                    .getHeaders()
                    .putSingle(
                            "Vary",
                            "Origin"
                    );
        }


        responseContext
                .getHeaders()
                .putSingle(
                        "Access-Control-Allow-Headers",
                        "Origin, Content-Type, Accept, Authorization"
                );


        responseContext
                .getHeaders()
                .putSingle(
                        "Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS"
                );


        responseContext
                .getHeaders()
                .putSingle(
                        "Access-Control-Max-Age",
                        "3600"
                );
    }


    private boolean origenPermitido(
            String origen
    ) {


        if (
            origen == null
            ||
            origen.trim().isEmpty()
        ) {

            return false;
        }


        if (
            "http://localhost:5501"
                    .equalsIgnoreCase(origen)
        ) {

            return true;
        }


        if (
            "http://127.0.0.1:5501"
                    .equalsIgnoreCase(origen)
        ) {

            return true;
        }


        return origen.startsWith(
                    "https://"
               )
               &&
               origen.endsWith(
                    ".trycloudflare.com"
               );
    }
}