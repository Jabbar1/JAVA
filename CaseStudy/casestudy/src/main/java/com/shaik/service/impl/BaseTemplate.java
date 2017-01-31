package com.shaik.service.impl;

import com.shaik.domain.repository.BaseRepository;
import com.shaik.exception.NotFoundException;
import com.shaik.service.operations.BaseOperations;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Common Operations are performed in this class
 *
 * Created by jabbars on 1/31/2017.
 */
public abstract class BaseTemplate<M,E,ID extends Serializable> implements BaseOperations<M,ID> {

    protected BaseRepository<E,ID> baseRepository;
    protected Function<E,M>  modelMapper;
    protected Function<M,E> entityMapper;
    protected BiFunction<M,E,E> updateMapper;

    public BaseTemplate(BaseRepository<E, ID> baseRepository,
                        Function<E, M> modelMapper,
                        Function<M, E> entityMapper,
                        BiFunction<M, E, E> updateMapper) {
        this.baseRepository = baseRepository;
        this.modelMapper = modelMapper;
        this.entityMapper = entityMapper;
        this.updateMapper = updateMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public M create(M request) {
        E entity = entityMapper.apply(request);
        entity = baseRepository.save(entity);
        return modelMapper.apply(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public M update(ID id, M request) {
        E entity = findOne(id);
        entity = updateMapper.apply(request, entity);
        entity = baseRepository.save(entity);
        return modelMapper.apply(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<M> findAll() {
        List<E> result = baseRepository.findAll();
        return result.stream().map(modelMapper)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public M find(ID id) {
        E entity = findOne(id);
        return modelMapper.apply(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(ID id) {
        findOne(id);
        baseRepository.delete(id);
    }

    protected E findOne(ID id) {
        E entity = baseRepository.findOne(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new NotFoundException("Given Fraction for a Profile Not exist with ID " + id + ", Please Provide Valid data");
        }
        return entity;
    }
}
