package com.example.takehome.service;

import com.example.takehome.domain.Country;
import com.example.takehome.domain.Response;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountryDataService {
    // Use Country Code as Key
    private static Map<String, Country> countriesCache = new HashMap<>();
    // Use Continent Code as Key
    private static Map<String, List<Country>> continentCountriesCache = new HashMap<>();

    public static void popupCache(List<Country> countries) {
        for (Country country:countries) {
            // First, cache country data with the country code.
            countriesCache.put(country.getCode(),country);
            // Second, cache all the countries in the same continent.
            String continentCode = country
                    .getContinent()
                    .getCode();
            List<Country> continentCountries =
                    continentCountriesCache
                            .getOrDefault(continentCode,new ArrayList<Country>());

            continentCountries.add(country);
            continentCountriesCache.put(continentCode,continentCountries);
        }
        //System.out.println(continentCountriesCache);
    }

    public Collection<Response> findOtherCountries(List<String> countryCodes) {
       Map<String, Response> temp = new HashMap<>();
       for (String countryCode: countryCodes) {
           Country country = countriesCache.get(countryCode);
           String continentCode = country.getContinent().getCode();

           Response response = temp.containsKey(continentCode) ? temp.get(continentCode): new Response();
           response.getCountryCodes().add(countryCode);
           response.setContinentName(country.getContinent().getName());

           List<Country> otherCountries = continentCountriesCache.get(continentCode);
           SortedSet<String> otherCountryCodes = response.getOtherCountryCodes();
           for (Country otherCountry : otherCountries) {
               String otherCountryCode = otherCountry.getCode();
               // Only add country codes not requested by the user
               if (!countryCodes.contains(otherCountryCode))
                   otherCountryCodes.add(otherCountryCode);
           }

           temp.put(continentCode,response);
       }
      return temp.values();
    }

}
