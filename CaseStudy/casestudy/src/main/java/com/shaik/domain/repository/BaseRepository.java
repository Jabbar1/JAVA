package com.shaik.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by jabbars on 1/31/2017.
 */
@NoRepositoryBean
public interface BaseRepository<E,ID extends Serializable> extends JpaRepository<E,ID> {
}
