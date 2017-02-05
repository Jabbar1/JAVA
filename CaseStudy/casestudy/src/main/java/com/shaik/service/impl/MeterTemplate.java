package com.shaik.service.impl;

import com.shaik.domain.entity.EFraction;
import com.shaik.domain.entity.EMeter;
import com.shaik.domain.repository.FractionRepository;
import com.shaik.domain.repository.MeterRepository;
import com.shaik.exception.DataDuplicateException;
import com.shaik.exception.InvalidInputException;
import com.shaik.exception.NotFoundException;
import com.shaik.mapper.MeterMapper;
import com.shaik.model.FileDetails;
import com.shaik.model.Meter;
import com.shaik.service.operations.MeterOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.inject.Named;
import java.io.*;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jabbars on 1/23/2017.
 */
@Service
@Named("caseStudyMeterTemplate")
public class MeterTemplate extends BaseTemplate<Meter, EMeter, Long>
        implements MeterOperations<Meter, Long> {

    Logger LOGGER = LoggerFactory.getLogger(MeterTemplate.class);

    private MeterRepository meterRepository;
    private FractionRepository fractionRepository;

    public MeterTemplate(MeterRepository meterRepository, FractionRepository fractionRepository) {
        super(meterRepository, MeterMapper.entity, MeterMapper.model, MeterMapper.update);
        this.meterRepository = meterRepository;
        this.fractionRepository = fractionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meter create(Meter request) {
        // Validations
        isMeterDataValid(request.getId(), request);
        return super.create(request);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meter update(Long id, Meter request) {
        // Validations
        isMeterDataValid(id, request);
        return super.update(id, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Meter> readFromCsv(FileDetails file) throws IOException {

        List<EMeter> validRecords = validateAndReadData(file);
        return validRecords.stream().map(MeterMapper.entity).collect(Collectors.toList());
    }


    @Override
    List<EMeter> validateAndReadData(FileDetails file) throws IOException {
        List<EMeter> validRecords = new ArrayList<>();

        Boolean fileReadSuccessFully = Boolean.TRUE;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getFilePath()))) {
            fileWriter = new FileWriter(file.getLogPath());
            bufferedWriter = new BufferedWriter(fileWriter);

            Set<EMeter> meterData = extractDataFromFile(bufferedReader);
            Map<String, List<EMeter>> groupedByProfile = meterData.stream().collect(Collectors.groupingBy(EMeter::getProfile));

            for (Map.Entry<String, List<EMeter>> meter : groupedByProfile.entrySet()) {
                // Get December month Reading for the profile
                List<EMeter> dataForProfile = meter.getValue();
                dataForProfile.sort(Comparator.comparing(EMeter::getMonth));
                // Get Year Consumption of a Profile
                Double yearConsumption = dataForProfile.get(dataForProfile.size() - 1).getReading();
                Double previousMonthMeterReading = 0.0;
                // Validate Data and select only valid records
                for (EMeter currentMonthMeter : dataForProfile) {
                    try {
                        validateMeterData(meter.getKey(), yearConsumption, previousMonthMeterReading, currentMonthMeter);
                        //If Data Valid then add to Valid Records
                        validRecords.add(currentMonthMeter);
                    } catch (Exception e) {
                        LOGGER.info(" Error {}" + e.getMessage());
                        fileReadSuccessFully = Boolean.FALSE;
                        bufferedWriter.write(e.getMessage());
                    }
                    previousMonthMeterReading = currentMonthMeter.getReading();
                }
            }
            // Save all Valid Records
            validRecords = meterRepository.save(validRecords);

        } catch (Exception e) {
            fileReadSuccessFully = Boolean.FALSE;
            LOGGER.info(" Error {}" + e.getMessage());
            bufferedWriter.write("" + e.getMessage());
        }
        if (fileReadSuccessFully) {
            File fileLocation = new File(file.getFilePath());
            if (fileLocation.delete()) {
                bufferedWriter.write("FILE deleted successfully");
            }
        }
        return validRecords;
    }

    private void validateMeterData(String profile, Double yearConsumption, Double previousMonthMeterReading, EMeter currentMonthMeter) {
        Double fractionOfMonthAndProfile = 0.0;

        // Get Fraction value for month and profile
        EFraction fractionOfProfile = fractionRepository.findByProfileAndMonth(profile, currentMonthMeter.getMonth());
        if (ObjectUtils.isEmpty(fractionOfProfile)) {
            throw new NotFoundException("For a Given Profile " + profile + " and Month" + currentMonthMeter.getMonth() + " Fraction Data not found");
        }
        if (currentMonthMeter.getReading() < previousMonthMeterReading) {
            throw new InvalidInputException("meter reading for a month should not be lower than the previous one for profile " + profile);
        }
        EMeter dataExists = meterRepository.findByProfileAndMonthAndConnectionId(profile, currentMonthMeter.getMonth(), currentMonthMeter.getConnectionID());
        if (!ObjectUtils.isEmpty(dataExists)) {
            throw new DataDuplicateException(" A Reading exists with given data. should be unique");
        }
        fractionOfMonthAndProfile = fractionOfProfile.getFraction();

        Double value = yearConsumption * fractionOfMonthAndProfile;
        Double tolerance = value * 0.25; //
        Double minRange = value - tolerance;
        Double maxRange = value + tolerance;

        Double consumption = currentMonthMeter.getReading() - previousMonthMeterReading;
        if (!(consumption >= minRange && consumption <= maxRange)) {
            throw new InvalidInputException("Meter consumption for the month " + currentMonthMeter.getMonth() + " not with in the range for profile " + profile);
        }
    }



    @Override
     Set<EMeter> extractDataFromFile(BufferedReader br) throws IOException {

        String line = "";
        String cvsSplitBy = ",";
        Set<EMeter> meters = new HashSet<>();

        while ((line = br.readLine()) != null) {
            String[] meter = line.split(cvsSplitBy);
            EMeter data = new EMeter(
                    meter[0],                       // connectionID
                    meter[1],                       // Profile
                    Month.valueOf(meter[2]),        // Month
                    Double.parseDouble(meter[3])    // MeterReading
            );
            meters.add(data);
        }
        return meters;
    }

    private void isMeterDataValid(Long id, Meter request) {
        // Validations
        EMeter meter = meterRepository.findByProfileAndMonthAndConnectionId(request.getProfile(), request.getMonth(), request.getConnectionID());
        if (!ObjectUtils.isEmpty(meter) && ObjectUtils.nullSafeEquals(meter.getId(), id)) {
            throw new DataDuplicateException(" A Reading exists with given data, should be unique");
        }
        List<EMeter> meters = meterRepository.findByConnectionIdOrderByMonth(request.getConnectionID());
        if (!meters.isEmpty() && meters.get(meters.size() - 1).getReading() < request.getReading()) {
            throw new InvalidInputException("meter reading for a month should not be lower than the previous one");
        }
        EFraction fraction = fractionRepository.findByProfileAndMonth(request.getProfile(), request.getMonth());
        if (ObjectUtils.isEmpty(fraction)) {
            throw new NotFoundException("For a Given Profile " + request.getProfile() + " Fraction Data is required. pLease create");
        }
    }
}
