package com.orcronics.Software_Medico.repository;


import com.orcronics.Software_Medico.entity.AtencionDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AtencionDetalleRepository extends JpaRepository<AtencionDetalle, Integer> {

	List<AtencionDetalle> findByAtencionId(int atencionid);
	
}
