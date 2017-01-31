package com.shaik.service.operations;

import com.shaik.model.FileDetails;

import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
public interface BaseOperations<M,ID> {

    M create(M request);
    M update(ID id, M request);

    List<M> findAll();
    M find(ID id);

    void delete(ID id);

    List<M> readFromCsv(FileDetails filePath);
}
