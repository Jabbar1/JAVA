package com.shaik.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.util.Objects;

/**
 * Created by jabbars on 1/23/2017.
 */
@Entity
@Table(name = "METER")
public class EMeter {
    /*ConnectionID,Profile,Month,EMeter reading */


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CONNECTION_ID")
    private String connectionId;

    @Column(name = "PROFILE")
    @NotNull
    private String profile;

    @Column(name = "MONTH")
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(name = "READING")
    private Double reading;

    public EMeter() {
    }

    public EMeter(String connectionID, String profile, Month month, Double reading) {
        this.connectionId = connectionID;
        this.profile = profile;
        this.month = month;
        this.reading = reading;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnectionID() {
        return connectionId;
    }

    public void setConnectionID(String connectionID) {
        this.connectionId = connectionID;
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

    public Double getReading() {
        return reading;
    }

    public void setReading(Double reading) {
        this.reading = reading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EMeter meter = (EMeter) o;
        return Objects.equals(id, meter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
