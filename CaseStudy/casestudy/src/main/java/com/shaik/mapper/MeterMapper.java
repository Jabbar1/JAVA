package com.shaik.mapper;

import com.shaik.domain.entity.EMeter;
import com.shaik.model.Meter;

/**
 * Created by jabbars on 1/23/2017.
 */
public class MeterMapper {

    public static EMeter map(Meter meter){
        return new EMeter(
                meter.getConnectionID(),
                meter.getProfile(),
                meter.getMonth(),
                meter.getReading());
    }

    public static EMeter map(EMeter dbMeter, Meter updated){
        dbMeter.setConnectionID(updated.getProfile());
        dbMeter.setProfile(updated.getProfile());
        dbMeter.setMonth(updated.getMonth());
        dbMeter.setReading(updated.getReading());
        return dbMeter;
    }

    public static Meter map(EMeter meter){
        return new Meter.Builder()
                .id(meter.getId())
                .connectionID(meter.getConnectionID())
                .profile(meter.getProfile())
                .month(meter.getMonth())
                .reading(meter.getReading())
                .build();
    }
}
