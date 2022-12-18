package com.example.takehome.service;

import com.example.takehome.domain.OtherCountriesResponse;

import java.util.Collection;
import java.util.List;
/**
 * Created by Yufan Shi
 * CountryDataService implementations should be to provide the business logic to
 * accept a list of country codes and returns a list of OtherCountriesResponse objects.
 */
public interface CountryDataService {

    // This method is to accept a list of country codes and returns a list of OtherCountriesResponse objects.
    // Each OtherCountriesResponse object contains the requested country codes and
    // other countries in the same continent as requested country codes(if they are in the same continents).
    Collection<OtherCountriesResponse> findOtherCountries(List<String> countryCodes);
}
