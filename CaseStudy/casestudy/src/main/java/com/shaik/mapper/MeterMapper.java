package com.shaik.mapper;

import com.shaik.domain.entity.EMeter;
import com.shaik.model.Meter;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by jabbars on 1/23/2017.
 */
public class MeterMapper {


    public static Function<EMeter, Meter> entity = new Function<EMeter, Meter>() {

        @Override
        public Meter apply(EMeter meter) {
            return new Meter.Builder()
                    .id(meter.getId())
                    .connectionID(meter.getConnectionID())
                    .profile(meter.getProfile())
                    .month(meter.getMonth())
                    .reading(meter.getReading())
                    .build();
        }
    };

    public static Function<Meter, EMeter> model = new Function<Meter, EMeter>() {

        @Override
        public EMeter apply(Meter meter) {
            return new EMeter(
                    meter.getConnectionID(),
                    meter.getProfile(),
                    meter.getMonth(),
                    meter.getReading());
        }
    };

    public static BiFunction<Meter, EMeter, EMeter> update = new BiFunction<Meter, EMeter, EMeter>() {
        @Override
        public EMeter apply(Meter updated, EMeter dbMeter) {
            dbMeter.setConnectionID(updated.getProfile());
            dbMeter.setProfile(updated.getProfile());
            dbMeter.setMonth(updated.getMonth());
            dbMeter.setReading(updated.getReading());
            return dbMeter;
        }
    };
}
