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

        if ("OPTIONS".equalsIgnoreCase(
                requestContext.getMethod())) {

            requestContext.abortWith(
                    Response.ok().build()
            );
        }
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {

        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Origin",
                "*"
        );

        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization"
        );

        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Credentials",
                "true"
        );

        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );
    }
}