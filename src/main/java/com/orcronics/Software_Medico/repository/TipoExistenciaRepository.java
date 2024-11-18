package com.orcronics.Software_Medico.repository;

import com.orcronics.Software_Medico.entity.TipoExistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TipoExistenciaRepository extends JpaRepository<TipoExistencia, Integer>{

}
