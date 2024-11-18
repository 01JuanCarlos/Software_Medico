package com.orcronics.Software_Medico.repository;

import java.util.Date;
import java.util.List;

import com.orcronics.Software_Medico.entity.Atencion;
import com.orcronics.Software_Medico.interfaces.IConteoPodologoProcedimiento;
import com.orcronics.Software_Medico.interfaces.IConteoProcedimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer> {

	List<Atencion> findByFechaBetweenAndLocalId(Date fechaInicio, Date fechaFin, int idlocal);
	List<Atencion> findByFechaLessThanEqualAndFechaGreaterThanEqualAndLocalId(Date fechaFin, Date fechaInicio, int idlocal);
	
	//Proyecciones de Spring Data.
	
	//Consulta de JPA con JPQL.
	@Query( value = " select d.procedimiento as procedimiento, count(d.procedimiento) as totalProcedimiento "
			+ " from atenciondetalle d inner join atencion a on d.atencionid = a.id "
			+ " where a.localid = ?3 and fecha between ?1 and ?2 "
			+ " group by d.procedimiento "
			+ " order by totalProcedimiento desc ", nativeQuery = true)
	List<IConteoProcedimiento> conteoPorProcedimientoInterface(Date fechaInicio, Date fechaFin, int idlocal);
	
	//Algunas veces las consultas JPA pueden sen no tan rápidas como las SQL nativas o no pueden usar las funciones específicas
	//del motor de base de datos.
	//Ejemplo de estructura de JPQL con JPA:
	//@Query("SELECT ..... FROM ...... ")
	//Ejemplo de estructura de SQL Nativo:
	//@Query(value = "SELECT ..... FROM ...... ", nativeQuery = true)
	

	@Query(value = " select e.nombres as podologo, d.procedimiento as procedimiento, count(d.procedimiento) as totalProcedimiento "
			+ " from atenciondetalle d inner join atencion a on d.atencionid = a.id "
			+ " inner join especialista e on a.especialistaid = e.id "
			+ " where a.localid = ?3 and fecha between ?1 and ?2 "
			+ " group by e.nombres, d.procedimiento "
			+ " order by totalProcedimiento desc, podologo ", nativeQuery = true)
	List<IConteoPodologoProcedimiento> conteoPorPodologoProcedimientoInterface(Date fechaInicio, Date fechaFin, int idlocal);
	
}
