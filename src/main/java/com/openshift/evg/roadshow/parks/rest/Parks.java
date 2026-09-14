package com.openshift.evg.roadshow.parks.rest;

import com.openshift.evg.roadshow.parks.db.MongoDBConnection;
import com.openshift.evg.roadshow.parks.model.Park;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Cross-origin requests are allowed through quarkus.http.cors in
 * application.properties, which replaces the @CrossOrigin annotations.
 */
@Path("/ws/data")
@ApplicationScoped
public class Parks {

    private final MongoDBConnection con;

    @Inject
    public Parks(MongoDBConnection connection) {
        this.con = connection;
    }

    @GET
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    public String load() {
        System.out.println("[INFO] load()");
        List<Document> l = con.loadParks();
        con.init(l);
        return "Items inserted in database: " + con.sizeInDB();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Park> getAllParks() {
        System.out.println("[DEBUG] getAllParks");

        return con.getAll();
    }

    @GET
    @Path("/within")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Park> findParksWithin(
            @QueryParam("lat1") float lat1,
            @QueryParam("lon1") float lon1,
            @QueryParam("lat2") float lat2,
            @QueryParam("lon2") float lon2) {
        System.out.println("[DEBUG] findParksWithin(" + lat1 + "," + lon1 + "," + lat2 + "," + lon2 + ")");

        // make the query object. The corners are List<Double> and not double[]:
        // org.bson.Document has no codec for Java arrays, only for Iterable.
        List<List<Double>> boxList = new ArrayList<List<Double>>();
        boxList.add(List.of((double) lat1, (double) lon1));
        boxList.add(List.of((double) lat2, (double) lon2));

        Document boxQuery = new Document("$box", boxList);

        Document spatialQuery = new Document("coordinates", new Document("$within", boxQuery));
        System.out.println("Using spatial query: " + spatialQuery.toString());

        return con.getByQuery(spatialQuery);
    }

    @GET
    @Path("/centered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findParksCentered(@QueryParam("lat") float lat, @QueryParam("lon") float lon, @QueryParam("maxDistance") int maxDistance, @QueryParam("minDistance") int minDistance) {

        // TODO: Implement this
        // The Spring MVC version returned null, which Spring rendered as 200 with an
        // empty body. JAX-RS turns a null entity into 204, so the response is built
        // explicitly to keep the endpoint's contract unchanged.
        return Response.ok().build();
    }

}
