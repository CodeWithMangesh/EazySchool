package com.easybytes.easyschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.EazyClass;

@Repository
public interface EazyClassRepository extends JpaRepository<EazyClass, Integer>{

}
