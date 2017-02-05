package com.shaik.service.impl;

import com.shaik.domain.entity.EFraction;
import com.shaik.domain.repository.FractionRepository;
import com.shaik.exception.FractionExceedException;
import com.shaik.mapper.FractionMapper;
import com.shaik.model.FileDetails;
import com.shaik.model.Fraction;
import com.shaik.service.operations.FractionOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jabbars on 1/23/2017.
 */
@Service
@Named("useCaseFractionTemplate")
public class FractionTemplate extends BaseTemplate<Fraction, EFraction, Long>
        implements FractionOperations<Fraction, Long> {

    Logger LOGGER = LoggerFactory.getLogger(FractionTemplate.class);

    private FractionRepository fractionRepository;

    @Inject
    public FractionTemplate(FractionRepository fractionRepository) {
        super(fractionRepository, FractionMapper.entity, FractionMapper.model, FractionMapper.update);
        this.fractionRepository = fractionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Fraction create(Fraction request) {
        isFractionLimitExceedsForProfile(request);
        return super.create(request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Fraction update(Long id, Fraction request) {
        EFraction fraction = findOne(id);
        isFractionLimitExceedsForProfile(request);
        return super.update(id, request);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Fraction> findAll() {
        List<EFraction> fractions = fractionRepository.findAllByOrderByMonth();
        return fractions.stream()
                .map(FractionMapper.entity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Fraction> readFromCsv(FileDetails file) throws IOException {
        List<EFraction> validRecords = validateAndReadData(file);
        return validRecords.stream()
                .map(FractionMapper.entity)
                .collect(Collectors.toList());
    }

    @Override
    List<EFraction> validateAndReadData(FileDetails file) throws IOException {

        List<EFraction> validRecords = new ArrayList<>();
        Boolean fileReadSuccessFully = Boolean.TRUE;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file.getFilePath()))) {
            fileWriter = new FileWriter(file.getLogPath());
            bufferedWriter = new BufferedWriter(fileWriter);

            Set<EFraction> meterData = extractDataFromFile(br);
            Map<String, List<EFraction>> groupedByProfile = meterData.stream()
                    .collect(Collectors.groupingBy(EFraction::getProfile));

            for (Map.Entry<String, List<EFraction>> fraction : groupedByProfile.entrySet()) {

                Double totalFraction = fraction.getValue().stream().mapToDouble(EFraction::getFraction).sum();
                if (totalFraction > 1) {
                    String message = "For a Profile " + fraction.getKey() + " Sum of all Fractions Should be <=1";
                    LOGGER.info("Error {}", message);
                    fileReadSuccessFully = Boolean.FALSE;
                    bufferedWriter.write("" + message);
                } else {
                    validRecords.addAll(fraction.getValue());
                }
            }
            fractionRepository.save(validRecords);

        } catch (Exception e) {
            bufferedWriter.write("" + e.getMessage());
            fileReadSuccessFully = Boolean.FALSE;
        }
        if (fileReadSuccessFully) {
            File fileLocation = new File(file.getFilePath());
            if (fileLocation.delete()) {
                bufferedWriter.write("FILE deleted successfully");
            }
        }
        return validRecords;
    }

    @Override
    Set<EFraction> extractDataFromFile(BufferedReader br) throws IOException {

        String line = "";
        String cvsSplitBy = ",";
        Set<EFraction> fractions = new HashSet<>();

        while ((line = br.readLine()) != null) {
            String[] fraction = line.split(cvsSplitBy);
            EFraction data = new EFraction(
                    fraction[1],                    // Profile
                    Month.valueOf(fraction[0]),     // Month
                    Double.parseDouble(fraction[2]) // Fraction
            );
            fractions.add(data);
        }
        return fractions;
    }

    private void isFractionLimitExceedsForProfile(Fraction request) {
        List<EFraction> fractions = fractionRepository.findByProfile(request.getProfile());
        Double existingProfileFraction = request.getFraction();
        for (EFraction f : fractions) {
            existingProfileFraction += f.getFraction();
        }
        if (existingProfileFraction > 1) {
            throw new FractionExceedException("For a Profile Sum of all Fractions Should be <=1");
        }
    }
}
