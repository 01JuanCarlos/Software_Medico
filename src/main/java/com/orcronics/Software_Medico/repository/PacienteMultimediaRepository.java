package com.orcronics.Software_Medico.repository;

import java.util.List;

import com.orcronics.Software_Medico.entity.PacienteMultimedia;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PacienteMultimediaRepository extends JpaRepository<PacienteMultimedia, Integer> {

	List<PacienteMultimedia> findByPacienteId(int pacienteid);
	
}
