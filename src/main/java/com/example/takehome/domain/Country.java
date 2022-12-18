package com.example.takehome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Country {
    private String code;
    private String name;
    private Continent continent;
}
