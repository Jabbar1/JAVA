package com.shaik.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.Month;

/**
 * Created by jabbars on 1/23/2017.
 */
@JsonDeserialize(builder = Meter.Builder.class)
public class Meter {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("connectionID")
    private String connectionID;

    @JsonProperty("profile")
    private String profile;

    @JsonProperty("month")
    private Month month;

    @JsonProperty("reading")
    private Double reading;

    public Meter(Builder builder) {
        this.id = builder.id;
        this.connectionID =  builder.connectionID;
        this.profile =  builder.profile;
        this.month =  builder.month;
        this.reading =  builder.reading;
    }

    public Long getId() {
        return id;
    }

    public String getConnectionID() {
        return connectionID;
    }

    public String getProfile() {
        return profile;
    }

    public Month getMonth() {
        return month;
    }

    public Double getReading() {
        return reading;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class Builder{

        private Long id;
        private String connectionID;
        private String profile;
        private Month month;
        private Double reading;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder connectionID(String connectionID) {
            this.connectionID = connectionID;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder month(Month month) {
            this.month = month;
            return this;
        }

        public Builder reading(Double reading) {
            this.reading = reading;
            return this;
        }

        public Meter build(){
            return new Meter(this);
        }
    }
}
