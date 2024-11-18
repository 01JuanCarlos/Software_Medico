package com.orcronics.Software_Medico.repository;


import com.orcronics.Software_Medico.entity.Historia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HistoriaRepository extends JpaRepository<Historia, Integer> {

	List<Historia> findAllByPacienteIdOrderByFechaDesc(int pacienteid);
	
}
