package com.openshift.evg.roadshow.parks.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jmorales on 25/08/16.
 */
@Configuration
public class ParksAppConfig {

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new ParkReadConverter());
        return new CustomConversions(converterList);
    }
}
