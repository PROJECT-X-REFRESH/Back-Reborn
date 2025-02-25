package com.reborn.back.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "addressS", length = 20, nullable = false)
    private String addressS;

    @Column(name = "addressG", length = 20, nullable = false)
    private String addressG;

    @Column(name = "addressM", length = 20, nullable = false)
    private String addressM;

    @Column(name = "addressD", length = 20, nullable = false)
    private String addressD;
}