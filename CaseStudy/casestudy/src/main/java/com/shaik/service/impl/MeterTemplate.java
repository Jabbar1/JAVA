package com.shaik.service.impl;

import com.shaik.domain.entity.EFraction;
import com.shaik.domain.entity.EMeter;
import com.shaik.domain.repository.FractionRepository;
import com.shaik.domain.repository.MeterRepository;
import com.shaik.exception.DataDuplicateException;
import com.shaik.exception.InvalidInputException;
import com.shaik.exception.NotFoundException;
import com.shaik.mapper.MeterMapper;
import com.shaik.model.Meter;
import com.shaik.service.operations.MeterOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jabbars on 1/23/2017.
 */
@Service
@Named("caseStudyMeterTemplate")
public class MeterTemplate implements MeterOperations<Meter, Long> {

    private MeterRepository meterRepository;
    private FractionRepository fractionRepository;

    @Inject
    public MeterTemplate(MeterRepository meterRepository, FractionRepository fractionRepository) {
        this.meterRepository = meterRepository;
        this.fractionRepository = fractionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meter create(Meter request) {
        isMeterDataValid(request.getId(), request);
        EMeter meter = MeterMapper.map(request);
        meter = meterRepository.save(meter);
        return MeterMapper.map(meter);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meter update(Long id, Meter request) {
        isMeterDataValid(id, request);
        EMeter dBMeter = findOne(id);
        // Validations
        dBMeter = MeterMapper.map(dBMeter, request);
        dBMeter = meterRepository.save(dBMeter);
        return MeterMapper.map(dBMeter);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Meter> findAll() {
        List<EMeter> meters = meterRepository.findAll();
        return meters.stream().map(MeterMapper::map).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Meter find(Long id) {
        EMeter meter = findOne(id);
        return MeterMapper.map(meter);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        find(id);
        meterRepository.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Meter> readFromCsv(String filePath) {

        String line = "";
        String cvsSplitBy = ",";
        List<Meter> meters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             FileWriter fw = new FileWriter(FILENAME);
             BufferedWriter bw = new BufferedWriter(fw)) {

            while ((line = br.readLine()) != null) {
                String[] meter = line.split(cvsSplitBy);
                Meter data = new Meter.Builder().connectionID(meter[0])
                        .profile(meter[1])
                        .month(Month.valueOf(meter[2]))
                        .reading(Double.parseDouble(meter[3])).build();
                try {
                    data = create(data);
                } catch (Exception e) {
                    bw.write(e.getMessage());
                }
                meters.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meters;
    }

    private EMeter findOne(Long id) {
        EMeter meter = meterRepository.findOne(id);
        if (ObjectUtils.isEmpty(meter)) {
            throw new NotFoundException("Given Meter Reading with ID " + id + " Not found. pLease provide valid data");
        }
        return meter;
    }

    private void isMeterDataValid(Long id, Meter request) {
        // Validations
        ///v0
        EMeter meter = meterRepository.findByProfileAndMonthAndConnectionId(request.getProfile(), request.getMonth(), request.getConnectionID());
        if (!ObjectUtils.isEmpty(meter) && !ObjectUtils.nullSafeEquals(meter.getId(), id)) {
            throw new DataDuplicateException(" A Reading exists with given data, should be unique");
        }
        //v1
        List<EMeter> meters = meterRepository.findByConnectionIdOrderByMonth(request.getConnectionID());
        if (!meters.isEmpty() && meters.get(meters.size() - 1).getReading() < request.getReading()) {
            throw new InvalidInputException("meter reading for a month should not be lower than the previous one");
        }
        //v2
        List<EFraction> fraction = fractionRepository.findByProfile(request.getProfile());
        if (fraction.isEmpty()) {
            throw new NotFoundException("For a Given Profile " + request.getProfile() + " Fraction Data is required. pLease create");
        }
        //v3
        // Not Clear
    }
}
