package com.shaik.rest;

import com.shaik.model.Fraction;
import com.shaik.service.operations.FractionOperations;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jabbars on 1/23/2017.
 */
@RestController("FractionResource")
@RequestMapping(value = "v1/api/fraction")
public class FractionResource extends BaseResource<Fraction,Long> {

    FractionOperations<Fraction,Long> fractionFractionOperations;

    @Inject
    public FractionResource(@Named("useCaseFractionTemplate")FractionOperations<Fraction,Long> fractionFractionOperations) {
        super(fractionFractionOperations);
        this.fractionFractionOperations = fractionFractionOperations;
    }
}
