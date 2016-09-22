package com.openshift.evg.roadshow.parks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application launcher
 *
 * Created by jmorales on 24/08/16.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.openshift.evg.roadshow.parks.rest,com.openshift.evg.roadshow.parks.db")
public class NationalParksApplication {

    public static void main(String[] args) {
        SpringApplication.run(NationalParksApplication.class, args);
    }

}
