package com.shaik.service.impl;

import com.shaik.domain.entity.EFraction;
import com.shaik.domain.repository.FractionRepository;
import com.shaik.exception.FractionExceedException;
import com.shaik.mapper.FractionMapper;
import com.shaik.model.FileDetails;
import com.shaik.model.Fraction;
import com.shaik.service.operations.FractionOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Named("useCaseFractionTemplate")
public class FractionTemplate extends BaseTemplate<Fraction, EFraction, Long>
        implements FractionOperations<Fraction, Long> {

    private FractionRepository fractionRepository;

    @Inject
    public FractionTemplate(FractionRepository fractionRepository) {
        super(fractionRepository, FractionMapper.entity, FractionMapper.model, FractionMapper.update);
        this.fractionRepository = fractionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Fraction create(Fraction request) {
        isFractionLimitExceedsForProfile(request.getProfile());
        return super.create(request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Fraction update(Long id, Fraction request) {
        EFraction fraction = findOne(id);
        isFractionLimitExceedsForProfile(request.getProfile());
        return super.update(id, request);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Fraction> findAll() {
        List<EFraction> fractions = fractionRepository.findAllByOrderByMonth();
        return fractions.stream()
                .map(FractionMapper.entity::apply)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Fraction> readFromCsv(FileDetails file) {

        String line = "";
        String cvsSplitBy = ",";
        Boolean fileReadSuccessFully = Boolean.TRUE;
        List<Fraction> meters = new ArrayList<>();
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file.getFilePath()))) {
            fileWriter = new FileWriter(file.getLogPath());
            bufferedWriter = new BufferedWriter(fileWriter);

            while ((line = br.readLine()) != null) {
                String[] fraction = line.split(cvsSplitBy);
                Fraction data = new Fraction.Builder()
                        .month(Month.valueOf(fraction[0]))
                        .profile(fraction[1])
                        .fraction(Double.parseDouble(fraction[2])).build();
                try {
                    data = create(data);
                    meters.add(data);
                } catch (Exception e) {
                    fileReadSuccessFully = Boolean.FALSE;
                    bufferedWriter.write(""+e.getMessage());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bufferedWriter.write(""+e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            fileReadSuccessFully = Boolean.FALSE;
        }
        if (fileReadSuccessFully) {
            File fileLocation = new File(file.getFilePath());
            fileLocation.delete();
        }
        return meters;
    }

    private void isFractionLimitExceedsForProfile(String profile) {
        List<EFraction> fractions = fractionRepository.findByProfile(profile);
        Double existingProfileFraction = 0.0;
        for (EFraction f : fractions) {
            existingProfileFraction += f.getFraction();
        }
        if (existingProfileFraction > 1) {
            throw new FractionExceedException("For a Profile Sum of all Fractions Should be <=1");
        }
    }
}
