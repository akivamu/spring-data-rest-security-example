package com.akivamu.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wc {
    @Id
    @GeneratedValue
    private Long id;
    private final String name, description;

    Wc() {
        this.name = null;
        this.description = null;
    }
}
