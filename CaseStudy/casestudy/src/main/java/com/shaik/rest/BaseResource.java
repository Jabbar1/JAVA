package com.shaik.rest;

import com.shaik.model.FileDetails;
import com.shaik.service.operations.BaseOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
public abstract class BaseResource<L,ID> {

    protected BaseOperations<L,ID> baseOperations;


    public BaseResource(BaseOperations<L,ID> baseOperations) {
        this.baseOperations = baseOperations;
    }


    /**
     * Create
     *
     * @param meter
     * @return
     */
    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public L create(@RequestBody L meter) {
        return baseOperations.create(meter);
    }

    /**
     * Update
     *
     * @param id
     * @param meter
     * @return
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public L update(@PathVariable(value = "id")ID id,
                        @RequestBody L meter) {
        return baseOperations.update(id,meter);
    }

    /**
     * Find All
     *
     * @return
     */
    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<L> findAll() {
        return baseOperations.findAll();
    }

    /**
     * Find One record
     *
     * @param id
     * @return
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public L find(@PathVariable("id") ID id) {
        return baseOperations.find(id);
    }

    /**
     * Delete data
     *
     * @param id
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") ID id) {
        baseOperations.delete(id);
    }

    /**
     *
     * @return
     */
    @RequestMapping(
            value = "/file",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<L> readDataFromFile(@RequestBody FileDetails details) {
       return baseOperations.readFromCsv(details);
    }

}
