package com.orcronics.Software_Medico.repository;

import com.orcronics.Software_Medico.entity.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer>{

}
