package com.example.takehome.bootstrap;

import com.example.takehome.domain.Country;
import com.example.takehome.service.CountryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yufan Shi
 * Do bootstrap data preparation
 * Pop up caches of countries and continents by invoking
 * GraphQL endpoint https://countries.trevorblades.com/graphql to retrieve countries and continents
 * to speed up queries
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final  HttpGraphQlClient httpGraphQlClient =
            HttpGraphQlClient.builder().build();

    @Autowired
    private CountryDataService countryDataService;

    @Override
    public void run(String... args) throws Exception {
        //
        String httpRequestDocument = """
                  {
                    countries {
                      name
                      code
                      continent {
                        code
                        name
                      }
                    }
                  }
                """;
       List<Country> countries
               = httpGraphQlClient.mutate()
               .url("https://countries.trevorblades.com/graphql")
               .build()
               .document(httpRequestDocument)
               .retrieve("countries")
               .toEntityList(Country.class)
               .block();

        CountryDataService.popupCache(countries);

        List<String> req = new ArrayList<String>();
        req.add("CA");
        req.add("US");
        req.add("CN");
        countryDataService.findOtherCountries(req);

    }
}
