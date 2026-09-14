package com.openshift.evg.roadshow.parks.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.openshift.evg.roadshow.parks.model.Park;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jmorales on 11/08/16.
 */
@Startup
@ApplicationScoped
public class MongoDBConnection {

    private static final String FILENAME = "/nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Inject
    Config config;

    private MongoDatabase mongoDB = null;

    public MongoDBConnection() {
    }

    private String getProperty(String name, String defaultValue) {
        return config.getOptionalValue(name, String.class).orElse(defaultValue);
    }

    @PostConstruct
    public void initConnection() {
        String mongoHost = getProperty("mongodb.server.host", "127.0.0.1"); // env var MONGODB_SERVER_HOST takes precedence
        String mongoPort = getProperty("mongodb.server.port", "27017"); // env var MONGODB_SERVER_PORT takes precedence
        String mongoUri = getProperty("uri", "");
        String mongoUser = getProperty("mongodb.user", "mongodb"); // env var MONGODB_USER takes precedence
        String mongoPassword = getProperty("mongodb.password", "mongodb"); // env var MONGODB_PASSWORD takes precedence
        String mongoDBName = getProperty("mongodb.database", "mongodb"); // env var MONGODB_DATABASE takes precedence
        String dbServiceName = getProperty("database.service.name", "");

        try {
        	// If mongoUri is set, we use this, else, we use mongoHost and mongoPort
        	// This will come in this form (mongodb://127.0.0.1:27017)
        	if (mongoUri!=null && ! "".equals(mongoUri)){
        		Pattern pattern = Pattern.compile("mongodb?://([^:^/]*):?(\\d*)?");
        		Matcher matcher = pattern.matcher(mongoUri);
        		if (matcher.find()){
        			mongoHost = matcher.group(1);
        			mongoPort = matcher.group(2);
        			// We assume all information comes in the binding format
        	        mongoUser = getProperty("username", "mongodb");
        	        mongoPassword = getProperty("password", "mongodb");
        	        mongoDBName = getProperty("database_name", "mongodb");
        		}
        	} else if (dbServiceName !=null && ! "".equals(dbServiceName)) {
                mongoHost = dbServiceName;
            }

            String mongoURI = "mongodb://" + mongoUser + ":" + mongoPassword + "@" + mongoHost + ":" + mongoPort + "/" + mongoDBName;
            System.out.println("[INFO] Connection string: " + mongoURI);
            MongoClient mongoClient = MongoClients.create(mongoURI);
            mongoDB = mongoClient.getDatabase(mongoDBName);
        } catch (Exception e) {
            System.out.println("[ERROR] Creating the mongoDB. " + e.getMessage());
            mongoDB = null;
        }
    }

    /*
     * Load from embedded list of parks using FILENAME
     */
    public List<Document> loadParks() {
        System.out.println("[DEBUG] MongoDBConnection.loadParks()");

        try {
            return loadParks(MongoDBConnection.class.getResourceAsStream(FILENAME));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
        return new ArrayList<Document>();
    }

    public List<Document> loadParks(String fileLocation) {
        System.out.println("[DEBUG] MongoDBConnection.loadParks(" + fileLocation + ")");

        try {
            return loadParks(new FileInputStream(new File(fileLocation)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
        return new ArrayList<Document>();
    }

    public List<Document> loadParks(InputStream is) {
        System.out.println("[DEBUG] MongoDBConnection.loadParks(InputStream)");

        List<Document> docs = new ArrayList<Document>();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            String currentLine = null;
            int i = 1;
            while ((currentLine = in.readLine()) != null) {
                String s = currentLine.toString();
                // System.out.println("line "+ i++ + ": " + s);
                Document doc = Document.parse(s);
                docs.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading parks. Return empty list");
            }
        }
        return docs;
    }


    /**
     *
     */
    public void clear() {
        System.out.println("[DEBUG] MongoDBConnection.clear()");
        if (mongoDB != null) {
            try{
                mongoDB.getCollection(COLLECTION).drop();
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
    }


    /**
     * @param parks
     */
    public void init(List<Document> parks) {
        System.out.println("[DEBUG] MongoDBConnection.init(...)");
        if (mongoDB != null) {
            try {
                mongoDB.getCollection(COLLECTION).drop();
                mongoDB.getCollection(COLLECTION).insertMany(parks);
                mongoDB.getCollection(COLLECTION).createIndex(new Document("coordinates", "2d"));
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }

    }

    /**
     * @return
     */
    public long sizeInDB() {
        long size = 0;

        if (mongoDB != null) {
            try {
                size = mongoDB.getCollection(COLLECTION).countDocuments();
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return size;
    }

    /**
     * @param parks
     */
    public void insert(List<Document> parks) {
        if (mongoDB != null) {
            try {
                mongoDB.getCollection(COLLECTION).insertMany(parks);
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }

    }

    /**
     * @return
     */
    public List<Park> getAll() {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();

        if (mongoDB != null) {
            try {
                MongoCollection<Document> parks = mongoDB.getCollection(COLLECTION);
                MongoCursor<Document> cursor = parks.find().iterator();
                try {
                    while (cursor.hasNext()) {
                        allParksList.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return allParksList;
    }

    public List<Park> getWithin(float lat1, float lon1, float lat2, float lon2) {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();

        if (mongoDB != null) {
            try {
                MongoCollection<Document> parks = mongoDB.getCollection(COLLECTION);
                // make the query object. The corners are List<Double> and not double[]:
                // org.bson.Document has no codec for Java arrays, only for Iterable.
                List<List<Double>> boxList = new ArrayList<List<Double>>();
                boxList.add(List.of((double) lat2, (double) lon2));
                boxList.add(List.of((double) lat1, (double) lon1));
                Document boxQuery = new Document("$box", boxList);
                Document spatialQuery = new Document("pos", new Document("$within", boxQuery));
                System.out.println("Using spatial query: " + spatialQuery.toString());

                MongoCursor<Document> cursor = parks.find(spatialQuery).iterator();
                try {
                    while (cursor.hasNext()) {
                        allParksList.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return allParksList;
    }

    /**
     * @param query
     * @return
     */
    public List<Park> getByQuery(Document query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");
        List<Park> parks = new ArrayList<Park>();
        if (mongoDB != null) {
            try {
                MongoCursor<Document> cursor = mongoDB.getCollection(COLLECTION).find(query).iterator();
                try {
                    while (cursor.hasNext()) {
                        parks.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return parks;
    }
}
