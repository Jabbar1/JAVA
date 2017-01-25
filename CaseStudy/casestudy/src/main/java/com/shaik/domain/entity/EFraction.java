package com.shaik.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.util.Objects;

/**
 * Created by jabbars on 1/23/2017.
 */
@Entity
@Table(name = "FRACTION")
public class EFraction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PROFILE")
    @NotNull
    private String profile;

    @Column(name = "MONTH")
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(name = "FRACTION")
    @NotNull
    private Double fraction;

    public EFraction() {
    }

    public EFraction(String profile, Month month, Double fraction) {
        this.profile = profile;
        this.month = month;
        this.fraction = fraction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Double getFraction() {
        return fraction;
    }

    public void setFraction(Double fraction) {
        this.fraction = fraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EFraction fraction = (EFraction) o;
        return Objects.equals(id, fraction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
