package com.shaik.service.impl;

import com.shaik.app.NewApplication;
import com.shaik.exception.NotFoundException;
import com.shaik.model.Fraction;
import com.shaik.service.operations.FractionOperations;
import config.DataConfig;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
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
public class FractionTemplateTest extends AbstractTestExecutionListener {

    @Inject
    @Named("useCaseFractionTemplate")
    private FractionOperations<Fraction, Long> fractionOperations;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    @Sql({"classpath:truncate_tables.sql"})
    public void create() throws Exception {

        Fraction fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD").build();
        fraction = fractionOperations.create(fraction);
        assertThat("Fraction is Not Saved", fraction.getId(), is(notNullValue()));
    }

    @Test
    @Sql({"classpath:truncate_tables.sql"})
    public void update() throws Exception {
        Fraction.Builder fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD");
        Fraction created = fractionOperations.create(fraction.build());
        assertThat("Fraction is Not Saved", created.getId(), is(notNullValue()));
        // Update
        fraction.month(Month.APRIL);
        Fraction updated = fractionOperations.update(created.getId(), fraction.build());
        assertThat("Fraction is Not Updated", updated.getMonth(), is(Month.APRIL));
        assertThat("Fraction is Not Updated", updated.getId(), is(created.getId()));
    }

    @Test
    public void findAll() throws Exception {

        Fraction.Builder fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD");
        Fraction created = fractionOperations.create(fraction.build());
        assertThat("Fraction is Not Saved", created.getId(), is(notNullValue()));
        List<Fraction> found = fractionOperations.findAll();
        assertThat("Fraction is Not Saved", found.isEmpty(), is(false));

    }

    @Test
    public void find() throws Exception {

        Fraction.Builder fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD");
        Fraction created = fractionOperations.create(fraction.build());
        assertThat("Fraction is Not Saved", created.getId(), is(notNullValue()));
        Fraction found = fractionOperations.find(created.getId());
        assertThat("Fraction is Not Saved", found.getId(), is(created.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {

        Fraction.Builder fraction = new Fraction.Builder().fraction(0.3).month(Month.AUGUST).profile("ABCD");
        Fraction created = fractionOperations.create(fraction.build());
        assertThat("Fraction is Not Saved", created.getId(), is(notNullValue()));
        fractionOperations.delete(created.getId());
        // Try to find after deleting a Record
        Fraction found = fractionOperations.find(created.getId());
    }

}