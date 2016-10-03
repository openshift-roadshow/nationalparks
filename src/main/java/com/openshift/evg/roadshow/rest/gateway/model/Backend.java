package com.openshift.evg.roadshow.rest.gateway.model;

/**
 * This represents a backend. Once a backend is registered, a call to the
 * backend to get this information about it will be issued.
 *
 * Created by jmorales on 24/08/16.
 */
public class Backend {
    private String id;
    private String displayName;

    private Coordinates center = new Coordinates("0", "0");
    private int zoom = 1;


    public Backend() {
    }

    public Backend(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public Backend(String id, String displayName, Coordinates center, int zoom) {
        this.id = id;
        this.displayName = displayName;
        this.center = center;
        this.zoom = zoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Coordinates getCenter() {
        return center;
    }

    public void setCenter(Coordinates center) {
        this.center = center;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "Backend{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", center='" + center + '\'' +
                ", zoom='" + zoom + '\'' +
                '}';
    }

}
