package com.example.takehome.service;

import com.example.takehome.domain.Country;
import com.example.takehome.domain.OtherCountriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Yufan Shi
 * CountryDataService class is to provide the logic to
 * accept a list of country codes and returns a list of OtherCountriesResponse objects.
 * Each OtherCountriesResponse object contains the requested country codes and
 * other countries in the same continent as requested country codes(if they are in the same continents).
 */
@Service
@Slf4j
public class BasicCountryDataService implements CountryDataService {

    // Cache all the countries, store them in HashMap
    // use their each Country Code as Key
    private static final Map<String, Country> countriesCache = new HashMap<>();

    // Cache all the countries in each continent, store them in HashMap
    // use their each Continent Code as Key
    private static final Map<String, List<Country>> continentCountriesCache = new HashMap<>();

    // Static method to initialize the cache
    // This method will be called when the application starts
    // It accepts a list of countries, which is from one remote service or local file
    // in this case, it is from a GraphQL endpoint.
    public static void initializeCache(List<Country> countries) {
        for (Country country : countries) {
            // First, cache country data with the country code.
            countriesCache.put(country.getCode(), country);
            // Second, cache all the countries in the same continent.
            String continentCode = country
                    .getContinent()
                    .getCode();
            List<Country> continentCountries =
                    continentCountriesCache
                            .getOrDefault(continentCode, new ArrayList<Country>());

            continentCountries.add(country);
            continentCountriesCache.put(continentCode, continentCountries);
        }
    }

    // This method is to accept a list of country codes and returns a list of OtherCountriesResponse objects.
    // Each OtherCountriesResponse object contains the requested country codes and
    // other countries in the same continent as requested country codes(if they are in the same continents).
    @Override
    public Collection<OtherCountriesResponse> findOtherCountries(List<String> countryCodes) {
        if (countryCodes == null || countryCodes.isEmpty()) {
            log.info("No country code is provided");
            return Collections.emptyList();
        }
        // Create a map to store the OtherCountriesResponse objects
        // use the continent code as the key
        // which will be used to group the countries in the same continent
        Map<String, OtherCountriesResponse> result = new HashMap<>();
        log.debug("Received country codes: {}", countryCodes);
        // Loop through the requested country codes
        for (String countryCode : countryCodes) {
            log.debug("Processing country code: {}", countryCode);
            // Get the country data from the cache
            Country country = countriesCache.get(countryCode);
            if (country == null) {
                // If the country code is not in the cache, skip it
                // [Note] In this case, we can throw an exception to notify the user
                // that the country code is not valid.
                // But in this case, we just skip it.
                // Because the country code is not valid, it will not be included in the response.
                // We also could add a new field in the response to indicate the country code is invalid.
                // All depends on the business requirements.
                log.info("The country code {} is not valid.", countryCode);
                continue;
            }

            String continentCode = country.getContinent().getCode();

            // Get the other countries in the same continent from the temporary result hashmap
            // If the continent code is not in the result hashmap, create a new OtherCountriesResponse object
            // and put it into the result hashmap
            OtherCountriesResponse otherCountriesResponse = result.containsKey(continentCode) ?
                    result.get(continentCode) : new OtherCountriesResponse();
            otherCountriesResponse.getCountryCodes().add(countryCode);
            otherCountriesResponse.setContinentName(country.getContinent().getName());

            // Fetch all the countries in the same continent from the cache
            // and add them into the  sorted set of other countries
            // If the country code is already in the sorted set, it will be ignored.
            // The sorted set will not have duplications and sorted by default.
            List<Country> otherCountries = continentCountriesCache.get(continentCode);
            SortedSet<String> otherCountryCodes = otherCountriesResponse.getOtherCountryCodes();
            for (Country otherCountry : otherCountries) {
                String otherCountryCode = otherCountry.getCode();
                // Only add country codes not requested by the user
                if (!countryCodes.contains(otherCountryCode))
                    otherCountryCodes.add(otherCountryCode);
            }
            result.put(continentCode, otherCountriesResponse);
        }
        // Just return the collection of OtherCountriesResponse objects
        // In this case the collection will be converted to JSON Array in the REST Controller
        return result.values();
    }

}
