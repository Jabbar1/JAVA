package com.shaik.mapper;

import com.shaik.domain.entity.EFraction;
import com.shaik.model.Fraction;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by jabbars on 1/23/2017.
 */
public class FractionMapper {

    public static Function<EFraction, Fraction> entity = new Function<EFraction, Fraction>() {

        @Override
        public Fraction apply(EFraction fraction) {
            return new Fraction.Builder()
                    .id(fraction.getId())
                    .fraction(fraction.getFraction())
                    .profile(fraction.getProfile())
                    .month(fraction.getMonth()).build();
        }
    };

    public static Function<Fraction, EFraction> model = new Function<Fraction, EFraction>() {

        @Override
        public EFraction apply(Fraction fraction) {
            return new EFraction(
                    fraction.getProfile(),
                    fraction.getMonth(),
                    fraction.getFraction()
            );
        }
    };

    public static BiFunction<Fraction, EFraction, EFraction> update = new BiFunction<Fraction, EFraction, EFraction>() {
        @Override
        public EFraction apply(Fraction updated, EFraction dbFraction) {
            dbFraction.setFraction(updated.getFraction());
            dbFraction.setMonth(updated.getMonth());
            dbFraction.setProfile(updated.getProfile());
            return dbFraction;
        }
    };
}
