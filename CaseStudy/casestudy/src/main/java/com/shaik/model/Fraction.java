package com.shaik.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.NotNull;
import java.time.Month;
import java.util.Objects;

/**
 * Created by jabbars on 1/23/2017.
 */
@JsonDeserialize(builder = Fraction.Builder.class)
public class Fraction {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("profile")
    @NotNull
    private String profile;

    @JsonProperty("month")
    @NotNull
    private Month month;

    @JsonProperty("fraction")
    @NotNull
    private Double fraction;

    public Fraction(Builder builder) {
        this.id = builder.id;
        this.profile = builder.profile;
        this.month = builder.month;
        this.fraction = builder.fraction;
    }

    public Long getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public Month getMonth() {
        return month;
    }

    public Double getFraction() {
        return fraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return Objects.equals(profile, fraction.profile) &&
                month == fraction.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, month);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class Builder{
        private Long id;
        private String profile;
        private Month month;
        private Double fraction;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder fraction(Double fraction) {
            this.fraction = fraction;
            return this;
        }
        public Fraction build(){
            return new Fraction(this);
        }
    }
}
