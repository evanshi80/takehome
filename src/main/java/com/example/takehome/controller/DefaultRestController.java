package com.example.takehome.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.example.takehome.domain.OtherCountriesResponse;
import com.example.takehome.service.CountryDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class DefaultRestController {


    // just to test the Nacos Config Service
    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    private CountryDataService countryDataService;

    public DefaultRestController(CountryDataService countryDataService) {
        this.countryDataService = countryDataService;
    }

    @PostMapping("findOtherCountries")
    public Collection<OtherCountriesResponse> findOtherCountries(@RequestBody List<String> countryCodes) {
        System.out.println("useLocalCache: " + useLocalCache);
        return countryDataService.findOtherCountries(countryCodes);
    }
}
