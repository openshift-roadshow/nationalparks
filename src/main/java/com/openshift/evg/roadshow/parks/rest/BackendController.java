package com.openshift.evg.roadshow.parks.rest;

import com.openshift.evg.roadshow.rest.gateway.model.Backend;
import com.openshift.evg.roadshow.rest.gateway.model.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Provides information about this backend
 *
 * Created by jmorales on 26/09/16.
 */
@Path("/ws/info")
@ApplicationScoped
public class BackendController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Backend get() {
        return new Backend("nationalparks","National Parks", new Coordinates("47.039304", "14.505178"), 4);
    }
}
