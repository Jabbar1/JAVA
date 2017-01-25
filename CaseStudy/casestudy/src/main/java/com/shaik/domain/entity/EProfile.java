package com.shaik.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jabbars on 1/23/2017.
 */
@Entity
@Table(name = "PROFILE")
public class EProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    public EProfile() {
    }
}
