package com.shaik.service.impl;

import com.shaik.app.DBConfig;
import com.shaik.app.NewApplication;
import com.shaik.exception.NotFoundException;
import com.shaik.model.Fraction;
import com.shaik.model.Meter;
import com.shaik.service.operations.FractionOperations;
import com.shaik.service.operations.MeterOperations;
import config.DataConfig;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.inject.Inject;
import javax.inject.Named;

import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by jabbars on 1/24/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NewApplication.class, TestConfig.class})
@TestExecutionListeners(listeners = {
        DataConfig.class,
        MeterTemplateTest.class
})
public class MeterTemplateTest extends AbstractTestExecutionListener {

    @Inject
    @Named("caseStudyMeterTemplate")
    private MeterOperations<Meter, Long> meterMeterOperations;


    @Inject
    @Named("useCaseFractionTemplate")
    private FractionOperations<Fraction, Long> fractionOperations;

    @Before
    public void setUp() throws Exception {

    }

    @Test(expected = NotFoundException.class)
    public void canNotCreateWithoutFraction() throws Exception {
        Meter meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("A").reading(10.09).build();
        meter = meterMeterOperations.create(meter);
        assertThat("Meter Reading is not saved", meter.getId(), is(notNullValue()));
    }

    @Test
    public void create() throws Exception {

        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
        Meter meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("ABCD").reading(10.09).build();
        meter = meterMeterOperations.create(meter);
        assertThat("Meter Reading is not saved", meter.getId(), is(notNullValue()));
    }

    @Test
    public void update() throws Exception {
        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCDE").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
        Meter.Builder meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("ABCDE").reading(10.09);
        Meter created = meterMeterOperations.create(meter.build());
        assertThat("Meter Reading is not saved", created.getId(), is(notNullValue()));
        // Update
        meter.month(Month.DECEMBER);
        Meter updated = meterMeterOperations.update(created.getId(),meter.build());
        assertThat("Meter Reading is not saved", updated.getId(), is(notNullValue()));
        assertThat("Meter Reading is not saved", updated.getMonth(), is(Month.DECEMBER));
    }

    @Test
    public void findAll() throws Exception {
        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCDEFG").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
        Meter meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("ABCDEFG").reading(10.09).build();
        meter = meterMeterOperations.create(meter);
        assertThat("Meter Reading is not saved", meter.getId(), is(notNullValue()));
        List<Meter> meters = meterMeterOperations.findAll();
        assertThat("Meter Reading is not saved", meters.isEmpty(), is(false));

    }

    @Test
    public void find() throws Exception {
        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCDEFGE").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
        Meter meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("ABCDEFGE").reading(10.09).build();
        meter = meterMeterOperations.create(meter);
        assertThat("Meter Reading is not saved", meter.getId(), is(notNullValue()));
        Meter found = meterMeterOperations.find(meter.getId());
        assertThat("Meter Reading is not saved", found.getId(), is(meter.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {

        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCDEFGE").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
        Meter meter = new Meter.Builder().connectionID("Con_ID").month(Month.APRIL).profile("ABCDEFGE").reading(10.09).build();
        meter = meterMeterOperations.create(meter);
        assertThat("Meter Reading is not saved", meter.getId(), is(notNullValue()));
        meterMeterOperations.delete(meter.getId());
        Meter found = meterMeterOperations.find(meter.getId());
        assertThat("Meter Reading is not saved", found.getId(), is(meter.getId()));
    }

}