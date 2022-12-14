package com.example.takehome.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Country {
    private String code;
    private String name;
    private Continent continent;
}
