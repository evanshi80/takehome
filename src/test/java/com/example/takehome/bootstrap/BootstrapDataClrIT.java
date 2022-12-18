package com.example.takehome.bootstrap;

import com.example.takehome.domain.OtherCountriesResponse;
import com.example.takehome.service.BasicCountryDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
@Slf4j
class BootstrapDataClrIT {
    BootstrapDataCLR bootstrapDataCLR;
    BasicCountryDataService basicCountryDataService;
    @Autowired
    public BootstrapDataClrIT(BootstrapDataCLR bootstrapDataCLR,
                              BasicCountryDataService basicCountryDataService) {
        this.bootstrapDataCLR = bootstrapDataCLR;
        this.basicCountryDataService = basicCountryDataService;
    }

    @Test
    void run() {
        try {
            // Invoke the run method of BootstrapDataCLR
            // to mock the behavior of the application when it starts up
            // to initialize the cache
            bootstrapDataCLR.run();
            // Just to make sure the cache is initialized
            ArrayList<String> requestedCountryCodes = new ArrayList<>() {{
                add("US");
                add("CA");
            }};
            Collection<OtherCountriesResponse> otherCountriesResponse
                    = basicCountryDataService.findOtherCountries(requestedCountryCodes);
            Assertions.assertEquals(1, otherCountriesResponse.size());
        } catch (Exception e) {
            log.error("Error occurred while testing BootstrapDataCLR", e);
        }

    }
}