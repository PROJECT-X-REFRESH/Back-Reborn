package com.reborn.back.domain.user;

import com.reborn.back.domain.entity.Address;
import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uiId")
    private Integer id;

    @Column(name = "uiImg", length = 255)
    private String img;

    @Lob
    @Column(name = "uiBio", columnDefinition = "longtext")
    private String bio;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "uiLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "uiLongitude")),
            @AttributeOverride(name = "addressS", column = @Column(name = "uiAddressS", length = 20, nullable = false)),
            @AttributeOverride(name = "addressG", column = @Column(name = "uiAddressG", length = 20, nullable = false)),
            @AttributeOverride(name = "addressM", column = @Column(name = "uiAddressM", length = 20, nullable = false)),
            @AttributeOverride(name = "addressD", column = @Column(name = "uiAddressD", length = 20, nullable = false))
    })
    private Address address;

    // FK: uid → User(uid)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}