package com.shaik.rest;

import com.shaik.service.operations.CrudOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
public abstract class CrudRestResource<L,ID> {

    protected CrudOperations<L,ID> crudOperations;


    public CrudRestResource(CrudOperations<L,ID> crudOperations) {
        this.crudOperations = crudOperations;
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
        return crudOperations.create(meter);
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
        return crudOperations.update(id,meter);
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
        return crudOperations.findAll();
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
        return crudOperations.find(id);
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
        crudOperations.delete(id);
    }

    /**
     * Read Data Fro CSV file from the given Location
     *
     * @param filePath
     */
    @RequestMapping(
            value = "/file",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<L> delete(@RequestParam("filePath")String filePath) {
       return crudOperations.readFromCsv(filePath);
    }

}
