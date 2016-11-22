package com.openshift.evg.roadshow.parks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application launcher
 *
 * Created by jmorales on 24/08/16.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.openshift.evg.roadshow.parks.rest,com.openshift.evg.roadshow.parks.db")
@EnableAutoConfiguration(exclude={ MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
public class NationalParksApplication {

    public static void main(String[] args) {
        SpringApplication.run(NationalParksApplication.class, args);
    }

}
