package com.shaik.domain.repository;

import com.shaik.domain.entity.EFraction;
import com.shaik.model.Fraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
@Repository
public interface FractionRepository extends BaseRepository<EFraction,Long> {

    List<EFraction> findByProfile(String profile);
    EFraction findByProfileAndMonth(String profile, Month month);
    List<EFraction> findAllByOrderByMonth();
}
