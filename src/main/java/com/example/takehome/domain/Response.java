package com.example.takehome.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
* Created by Yufan Shi
* Response object to hold structured data with other countries in the
* same continent as requested country codes(if they are in the same continents).
 **/
@Getter
@Setter
@ToString
public class Response {
    // Represent the requested country codes by the user.
    private List<String> countryCodes = new ArrayList<>();
    private String continentName;
    // No duplications and sorted
    private SortedSet<String> otherCountryCodes = new TreeSet<>();
}
