package com.reborn.back.domain.counseller;

import com.reborn.back.domain.entity.Address;
import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "counseller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counseller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coId")
    private Integer id;

    @Column(name = "coName", length = 50)
    private String name;

    @Column(name = "coContact", length = 255)
    private String contact;

    @Column(name = "coStart")
    private LocalDateTime start;

    @Column(name = "coEnd")
    private LocalDateTime end;

    @Lob
    @Column(name = "coInfo", columnDefinition = "longtext")
    private String info;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "coLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "coLongitude")),
            @AttributeOverride(name = "addressS", column = @Column(name = "coAddressS", length = 20, nullable = false)),
            @AttributeOverride(name = "addressG", column = @Column(name = "coAddressG", length = 20, nullable = false)),
            @AttributeOverride(name = "addressM", column = @Column(name = "coAddressM", length = 20, nullable = false)),
            @AttributeOverride(name = "addressD", column = @Column(name = "coAddressD", length = 20, nullable = false))
    })
    private Address address;
}
