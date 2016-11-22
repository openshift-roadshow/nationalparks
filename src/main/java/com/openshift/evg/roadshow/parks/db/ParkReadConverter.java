package com.openshift.evg.roadshow.parks.db;

import com.mongodb.DBObject;
import com.openshift.evg.roadshow.parks.model.Coordinates;
import com.openshift.evg.roadshow.parks.model.Park;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

/**
 * Converts between a DBObject in MongoDB and a Park
 *
 * Created by jmorales on 25/08/16.
 */
@ReadingConverter
public class ParkReadConverter implements Converter<DBObject, Park> {


    public Park convert(DBObject source) {
        Park park = new Park((ObjectId) source.get("_id"),
                (String) source.get("name"));
        park.setToponymName((String) source.get("toponymName"));

        Coordinates cord = new Coordinates((List) source.get("coordinates"));
        park.setPosition(cord);
        park.setLatitude(cord.getLatitude());
        park.setLongitude(cord.getLongitude());

        park.setCountryCode((String) source.get("countryCode"));
        park.setCountryName((String) source.get("countryName"));
        return park;
    }

    public static final Park convert(Document source) {
        Park park = new Park((ObjectId) source.get("_id"),
                (String) source.get("name"));
        park.setToponymName((String) source.get("toponymName"));

        Coordinates cord = new Coordinates((List) source.get("coordinates"));
        park.setPosition(cord);
        park.setLatitude(cord.getLatitude());
        park.setLongitude(cord.getLongitude());

        park.setCountryCode((String) source.get("countryCode"));
        park.setCountryName((String) source.get("countryName"));
        return park;
    }
}
