package com.akivamu.example;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
public class Laptop {
    @Id
    @GeneratedValue
    private Long id;
    private final String serialNo;
    private final String employeeName;

    public Laptop() {
        this.serialNo = null;
        this.employeeName = null;
    }
}
