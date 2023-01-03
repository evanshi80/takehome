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
        countriesCache.clear();;
        continentCountriesCache.clear();

        //
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
        countryCodes.forEach(
                countryCode -> {
                    // Get the country object from the cache
                    Country country = countriesCache.get(countryCode);
                    if (country == null) {
                        log.warn("Country code {} is not found", countryCode);
                        return;
                    }
                    // Get the continent code from the country object
                    String continentCode = country.getContinent().getCode();
                    // Get the OtherCountriesResponse object from the map
                    OtherCountriesResponse otherCountriesResponse = result.get(continentCode);
                    if (otherCountriesResponse == null) {
                        // If the OtherCountriesResponse object is not found in the map,
                        // create a new one and put it into the map
                        otherCountriesResponse = new OtherCountriesResponse();
                        otherCountriesResponse.setContinentName(country.getContinent().getName());
                        result.put(continentCode, otherCountriesResponse);
                    }
                    // Add the country code to the OtherCountriesResponse object
                    otherCountriesResponse.getCountryCodes().add(countryCode);

                    // Fetch all the countries in the same continent from the cache
                    // and add them into the  sorted set of other countries
                    // If the country code is already in the sorted set, it will be ignored.
                    // The sorted set will not have duplications and sorted by default.
                    List<Country> otherCountries = continentCountriesCache.get(continentCode);
                    SortedSet<String> otherCountryCodes = otherCountriesResponse.getOtherCountryCodes();
                    otherCountries.stream()
                            .filter(c -> !c.getCode().equals(countryCode))
                            .forEach(c -> otherCountryCodes.add(c.getName()));

                    result.put(continentCode, otherCountriesResponse);
                }
        );

        // Just return the collection of OtherCountriesResponse objects
        // In this case the collection will be converted to JSON Array in the REST Controller
        return result.values();
    }

}
