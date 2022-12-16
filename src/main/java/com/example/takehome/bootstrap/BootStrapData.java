package com.example.takehome.bootstrap;

import com.example.takehome.domain.Country;
import com.example.takehome.service.CountryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Yufan Shi
 * Do bootstrap data preparation
 * Pop up caches of countries and continents by invoking
 * GraphQL endpoint https://countries.trevorblades.com/graphql to retrieve countries and continents
 * to speed up queries
 */
@Component
@Slf4j
public class BootStrapData implements CommandLineRunner {

    private final HttpGraphQlClient httpGraphQlClient =
            HttpGraphQlClient.builder().build();

    @Autowired
    private CountryDataService countryDataService;

    @Override
    public void run(String... args) throws Exception {
        // Create a GraphQL query to retrieve all countries
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
        try {
            // Invoke GraphQL endpoint to retrieve all countries
            List<Country> countries
                    = httpGraphQlClient.mutate()
                    .url("https://countries.trevorblades.com/graphql")
                    .build()
                    .document(httpRequestDocument)
                    .retrieve("countries")
                    .toEntityList(Country.class)
                    .block();
            // Initialize the cache
            CountryDataService.initializeCache(countries);
        } catch (Exception e) {
            // Upon any exception, log the error and exit the application
            log.error("Error occurred when retrieving countries from GraphQL endpoint", e);
            throw new RuntimeException("Failed to retrieve countries from GraphQL endpoint");
        }

    }
}
