package com.shaik.mapper;

import com.shaik.domain.entity.EFraction;
import com.shaik.model.Fraction;

/**
 * Created by jabbars on 1/23/2017.
 */
public class FractionMapper {

    public static EFraction map(Fraction fraction) {
        return new EFraction(
                fraction.getProfile(),
                fraction.getMonth(),
                fraction.getFraction()
        );
    }

    public static EFraction map(EFraction dbFraction, Fraction updated) {
        dbFraction.setFraction(updated.getFraction());
        dbFraction.setMonth(updated.getMonth());
        dbFraction.setProfile(updated.getProfile());
        return dbFraction;
    }

    public static Fraction map(EFraction fraction) {
        return new Fraction.Builder()
                .id(fraction.getId())
                .fraction(fraction.getFraction())
                .profile(fraction.getProfile())
                .month(fraction.getMonth()).build();
    }
}
