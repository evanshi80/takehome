package com.example.takehome.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String code;
    private String name;
    private Continent continent;
}
