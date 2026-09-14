package com.openshift.evg.roadshow.parks.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Healthz endpoint for liveness and readiness of the application
 *
 * Kept alongside the MicroProfile Health endpoints under /q/health: the workshop
 * configures the readiness and liveness probes against /ws/healthz/.
 *
 * Created by jmorales on 11/08/16.
 */
@Path("/ws/healthz")
@ApplicationScoped
public class Healthz {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String healthz() {
        return "OK";
    }
}
