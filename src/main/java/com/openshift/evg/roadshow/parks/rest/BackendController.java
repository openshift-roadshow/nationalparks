package com.openshift.evg.roadshow.parks.rest;

import com.openshift.evg.roadshow.rest.gateway.model.Backend;
import com.openshift.evg.roadshow.rest.gateway.model.Coordinates;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides information about this backend
 *
 * Created by jmorales on 26/09/16.
 */
@RequestMapping("/ws/info")
@RestController
public class BackendController{

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
    public Backend get() {
        return new Backend("nationalparks","National Parks", new Coordinates("47.039304", "14.505178"), 4);
    }
}
