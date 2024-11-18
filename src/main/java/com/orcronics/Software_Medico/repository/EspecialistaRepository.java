package com.orcronics.Software_Medico.repository;


import com.orcronics.Software_Medico.entity.Especialista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EspecialistaRepository extends JpaRepository<Especialista, Integer> {

	List<Especialista> findAllByEstado(String estado);
	List<Especialista> findByEstadoAndLocalesId(String estado, int localid);
	List<Especialista> findByEstadoAndLocalesIdAndEspecialidad(String estado, int localid, String especialidad);
	
	@Query( value = " select * from especialista "
			+ "	inner join especialista_local on especialista.id = especialista_local.especialista_id "
			+ " where estado = ?1 and local_id = ?2 and "
			+ "		especialidad  not like ( 'PODOLOGO' and 'BIOMECANICA' and 'TERAPIA') ", nativeQuery = true)
	List<Especialista> findByEstadoAndLocalesIdAndEspecialidadDental(String estado, int localid);
	
	Especialista findByNombres(String param);
	
}
