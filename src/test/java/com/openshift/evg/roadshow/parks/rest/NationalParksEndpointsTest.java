package com.openshift.evg.roadshow.parks.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Smoke tests for the endpoints the parksmap frontend and the OpenShift probes
 * rely on. They deliberately avoid /ws/data/*, which needs a MongoDB instance.
 */
@QuarkusTest
public class NationalParksEndpointsTest {

    @Test
    public void backendInfoDescribesThisBackend() {
        given()
                .when().get("/ws/info")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", is("nationalparks"))
                .body("displayName", is("National Parks"))
                .body("center.latitude", is("47.039304"))
                .body("center.longitude", is("14.505178"))
                .body("zoom", is(4));
    }

    @Test
    public void healthzAnswersOnTheUrlTheProbesUse() {
        // The workshop points the readiness and liveness probes at /ws/healthz/,
        // with the trailing slash.
        given()
                .when().get("/ws/healthz/")
                .then()
                .statusCode(200)
                .body(is("OK"));
    }

    @Test
    public void healthzAnswersWithoutTrailingSlash() {
        given()
                .when().get("/ws/healthz")
                .then()
                .statusCode(200)
                .body(is("OK"));
    }
}
