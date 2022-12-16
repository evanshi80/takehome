package com.example.takehome.controller;

import com.example.takehome.domain.OtherCountriesResponse;
import com.example.takehome.service.CountryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class DefaultRestController {

    private CountryDataService countryDataService;

    @Autowired
    public DefaultRestController(CountryDataService countryDataService) {
        this.countryDataService = countryDataService;
    }

    @PostMapping("find_other_countries")
    public Collection<OtherCountriesResponse> findOtherCountries(@RequestBody List<String> countryCodes) {
        return countryDataService.findOtherCountries(countryCodes);
    }
}
