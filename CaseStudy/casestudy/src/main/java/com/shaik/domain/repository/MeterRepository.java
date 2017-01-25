package com.shaik.domain.repository;

import com.shaik.domain.entity.EMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
@Repository
public interface MeterRepository extends JpaRepository<EMeter, Long> {
    EMeter findByProfileAndMonthAndConnectionId(String profile, Month month, String connectionID);
    List<EMeter> findByConnectionIdOrderByMonth(String connectionId);
}
