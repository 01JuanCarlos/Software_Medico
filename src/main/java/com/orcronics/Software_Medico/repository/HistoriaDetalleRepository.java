package com.orcronics.Software_Medico.repository;


import com.orcronics.Software_Medico.entity.HistoriaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriaDetalleRepository  extends JpaRepository<HistoriaDetalle, Integer> {

	List<HistoriaDetalle> findByHistoriaId(int historiaid);
	
}
