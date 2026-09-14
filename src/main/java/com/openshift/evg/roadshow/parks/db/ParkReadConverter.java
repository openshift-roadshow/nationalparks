package com.openshift.evg.roadshow.parks.db;

import com.openshift.evg.roadshow.parks.model.Coordinates;
import com.openshift.evg.roadshow.parks.model.Park;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Converts between a Document in MongoDB and a Park
 *
 * Created by jmorales on 25/08/16.
 *
 * REMOVED: the Converter<DBObject, Park> implementation and its @ReadingConverter
 * annotation. They were registered through ParksAppConfig into Spring Data MongoDB,
 * which NationalParksApplication explicitly disabled
 * (@EnableAutoConfiguration(exclude = {MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})),
 * so that path never ran. Every caller used the static convert(Document) below.
 * com.mongodb.DBObject is also gone from the modern mongodb-driver-sync.
 */
public class ParkReadConverter {

    private ParkReadConverter() {
    }

    public static final Park convert(Document source) {
        ObjectId objectId = source.getObjectId("_id");
        Park park = new Park(objectId != null ? objectId.toHexString() : null,
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
