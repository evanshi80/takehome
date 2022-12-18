package com.example.takehome.service;

import com.example.takehome.domain.Continent;
import com.example.takehome.domain.Country;
import com.example.takehome.domain.OtherCountriesResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicCountryDataServiceTest {
    BasicCountryDataService basicCountryDataService = new BasicCountryDataService();

    @BeforeAll
    static void setup() {
        List<Country> countries = new ArrayList<Country>();
        // init the countries list
        // three in north america
        countries.add(
                new Country("CA", "Canada",
                        new Continent("NA", "North America")));
        countries.add(
                new Country("US", "United States",
                        new Continent("NA", "North America")));
        countries.add(
                new Country("MXN", "Mexico",
                        new Continent("NA", "North America")));
        // two in south america
        countries.add(
                new Country("BR", "Brazil",
                        new Continent("SA", "South America")));
        countries.add(
                new Country("AR", "Argentina",
                    new Continent("SA", "South America")));
        // one in europe
        countries.add(
                new Country("UK", "United Kingdom",
                    new Continent("EU", "Europe")));
        // do the cache initialization
        BasicCountryDataService.initializeCache(countries);
    }

    @Test
    void findOtherCountries() {

        // Generate a test case for the method findOtherCountries
        // The test case should cover the following scenarios:
        // 1. The input is null
        assertEquals(0, basicCountryDataService.findOtherCountries(null).size());

        // 2. The input is empty
        assertEquals(0, basicCountryDataService.findOtherCountries(new ArrayList<>()).size());
        // 3. The input is a list of country codes that are not in the same continent
        ArrayList<String> requestedCountryCodes = new ArrayList<>() {{
            add("US");
            add("AR");
        }};
        Collection<OtherCountriesResponse> otherCountriesResponse = basicCountryDataService.findOtherCountries(requestedCountryCodes);

        // To verify the result, we need to check the following:
        // The size of the result is 2
        assertEquals(2, otherCountriesResponse.size());
        OtherCountriesResponse firstResponse = otherCountriesResponse.stream().findFirst().get();

        // The requested country code should be in the response and be reduced to one element
        // and should equal to "US"
        assertEquals(1,
                firstResponse.getCountryCodes().size());
        assertEquals(true,
                firstResponse.getCountryCodes().contains("US"));
        // The continent code should equal to "North America"
        assertEquals(true,
                firstResponse.getContinentName().equals("North America"));
        // The other countries in the same continent should not include the requested country code
        assertEquals(false,
                firstResponse.getOtherCountryCodes().containsAll(firstResponse.getCountryCodes()));

        OtherCountriesResponse secondResponse = otherCountriesResponse.stream().skip(1).findFirst().get();
        // The requested country code should be in the response and be reduced to one element
        // and should equal to "US"
        assertEquals(1,
                secondResponse.getCountryCodes().size());
        assertEquals(true,
                secondResponse.getCountryCodes().contains("AR"));
        // The continent code should equal to "South America"
        assertEquals(true,
                secondResponse.getContinentName().equals("South America"));
        // The other countries in the same continent should not include the requested country code
        assertEquals(false,
                secondResponse.getOtherCountryCodes().containsAll(secondResponse.getCountryCodes()));

        // 4. The input is a list of country codes that are in the same continent
        // this will return a list of OtherCountriesResponse objects not including the input country request
        requestedCountryCodes = new ArrayList<String>() {{
            add("US");
            add("MXN");
        }};
        otherCountriesResponse = basicCountryDataService.findOtherCountries(requestedCountryCodes);
        // The size of the otherCountriesResponse should be 1
        assertEquals(1, otherCountriesResponse.size());

        firstResponse = otherCountriesResponse.stream().findFirst().get();
        // The response's country codes should be the same as the input requestedCountryCodes
        assertEquals(requestedCountryCodes,
                firstResponse.getCountryCodes());
        // The continent code should equal to "North America"
        assertEquals(true,
                firstResponse.getContinentName().equals("North America"));
        // The other countries in the same continent should not include the requested country code
        assertEquals(false,
                firstResponse.getOtherCountryCodes().containsAll(firstResponse.getCountryCodes()));
        // There should be no more elements in the otherCountriesResponse
        assertEquals(0,otherCountriesResponse.stream().skip(1).count());

        // 5. The input is a list of country codes that are in the same continent,
        // but some of them are not in the cache or invalid
        requestedCountryCodes = new ArrayList<String>() {{
            add("US");
            add("MXN"); // will find 1 country in North America other than US and MXN
            add("CN"); // will be ignored
        }};
        otherCountriesResponse = basicCountryDataService.findOtherCountries(requestedCountryCodes);
        // The size of the otherCountriesResponse should be 1
        assertEquals(1, otherCountriesResponse.size());
        // The first response should have 2 country codes
        firstResponse = otherCountriesResponse.stream().findFirst().get();
        assertEquals(2, firstResponse.getCountryCodes().size());
        // The "CN" should be ignored
        assertEquals(true,
                firstResponse.getCountryCodes().stream().noneMatch(code -> code.equals("CN")));
        // 6. The input is a list of country codes that are not in the same continent,
        requestedCountryCodes =new ArrayList<String>() {{
            add("US"); // Will find 2 countries
            add("UK"); // Will find 0 country
            add("AR"); // Will find 1 country
        }};
        otherCountriesResponse = basicCountryDataService.findOtherCountries(requestedCountryCodes);
        // The size of the otherCountriesResponse should be 3
        assertEquals(3, otherCountriesResponse.size());
    }
}