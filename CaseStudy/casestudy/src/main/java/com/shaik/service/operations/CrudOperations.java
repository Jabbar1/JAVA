package com.shaik.service.operations;

import java.util.List;

/**
 * Created by jabbars on 1/23/2017.
 */
public interface CrudOperations<T,ID> {

    String FILENAME = "D:\\WorkSpace\\jdc\\log.txt";

    T create(T request);
    T update(ID id, T request);

    List<T> findAll();
    T find(ID id);

    void delete(ID id);

    List<T> readFromCsv(String filePath);
}
