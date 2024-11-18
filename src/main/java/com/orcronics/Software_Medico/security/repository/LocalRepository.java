package com.orcronics.Software_Medico.security.repository;

import com.orcronics.Software_Medico.security.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Integer> {
    List<Local> findAllByOrderByIdAsc();



}
