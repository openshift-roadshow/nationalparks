package com.openshift.evg.roadshow.parks.model;

/**
 * A national park, as served by /ws/data/all and /ws/data/within.
 *
 * The Spring Data annotations (@Document, @Id, @Indexed) were removed: Spring Data
 * MongoDB was disabled in this application, the collection is read through the plain
 * driver, and the "coordinates" 2d index is created by MongoDBConnection.init().
 *
 * "id" used to be an org.bson.types.ObjectId and was serialized by Jackson as an
 * object of driver internals (counter, machineIdentifier, processIdentifier, ...).
 * Those accessors no longer exist in the modern driver, so it is now the ObjectId
 * hex string — the same shape the Javascript backend returns.
 */
public class Park {

    private String id;

    private Object toponymName;
    private Object name;

    private Coordinates position;

    private Object longitude;
    private Object latitude;

    private Object countryCode;
    private Object countryName;

    public Park() {
    }

    public Park(String id, Object name) {
        this.id = id;
        this.name = name;
    }

    public Object getToponymName() {
        return toponymName;
    }

    public void setToponymName(Object toponymName) {
        this.toponymName = toponymName;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Object countryCode) {
        this.countryCode = countryCode;
    }

    public Object getCountryName() {
        return countryName;
    }

    public void setCountryName(Object countryName) {
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "NationalPark{" +
                "id=" + id +
                ", name=" + name +
                ", toponymName=" + toponymName +
                ", coordinates=" + position +
                ", countryCode=" + countryCode +
                ", countryName=" + countryName +
                '}';
    }
}
