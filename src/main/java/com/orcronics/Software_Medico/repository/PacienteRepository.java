package com.orcronics.Software_Medico.repository;

import java.util.List;

import com.orcronics.Software_Medico.entity.Paciente;
import com.orcronics.Software_Medico.interfaces.IDataTablePaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

	@Query( value = " SELECT * "
			+ "		FROM ( "
			+ "			select p.id as id, concat(p.codigoletra, '-', p.codigonro) as codigo, concat(p.nombres, ' ', p.apellidos) as fullname,"
			+ "			p.dni as dni, concat( ifnull(p.telefono,''), ' / ', ifnull(p.celular, '') ) as telefono, "
			+ "			l.nombre as local, p.id as botones "
			+ " 		from paciente p inner join local l on p.localid = l.id "
			//+ " 		where p.localid = ?1"
			+ "		) as subconsulta "
			+ "	where fullname like %?1% or dni like %?1% or codigo like %?1% "
			+ " order by fullname ", nativeQuery = true)
	List<IDataTablePaciente> findByParameter(String parametro);
	
	
	@Query( value = " SELECT * "
			+ "		FROM ( "
			+ "			select p.id as id, concat(p.codigoletra, '-', p.codigonro) as codigo, concat(p.nombres, ' ', p.apellidos) as fullname,"
			+ "			p.dni as dni, concat( ifnull(p.telefono,''), ' / ', ifnull(p.celular, '') ) as telefono, "
			+ "			l.nombre as local, p.id as botones "
			+ " 		from paciente p inner join local l on p.localid = l.id "
			+ " 		where p.localid = ?1"
			+ "		) as subconsulta "
			+ "	where fullname like %?2% or dni like %?2% or codigo like %?2% "
			+ " order by fullname ", nativeQuery = true)
	List<IDataTablePaciente> findByParameterYLocal(int localid, String parametro);
	
	
	//Consulta de JPA con JPQL.
	/*@Query( value = " select p.id as id, concat(p.codigoletra, '-', p.codigonro) as codigo, concat(p.nombres, ' ', p.apellidos) as fullname,"
			+ "		p.dni as dni, p.telefono as telefono, p.celular as celular, l.nombre as local, "
			+ "		concat(\"<a href='#' v-on:click='editPaciente(\", p.id, \")' data-bs-toggle='tooltip' data-bs-placement='top' title='Editar'> "
			+ "					<i class='mdi mdi-square-edit-outline mdi-24px'></i> "
			+ "				</a>\") as botones "
			+ " from paciente p inner join local l on p.localid = l.id "
			+ " where p.localid = ?1 "
			+ " order by p.id desc ", nativeQuery = true)*/
	@Query( value = " select p.id as id, concat(p.codigoletra, '-', p.codigonro) as codigo, concat(p.nombres, ' ', p.apellidos) as fullname,"
			+ "		p.dni as dni, concat( ifnull(p.telefono,''), ' / ', ifnull(p.celular, '') ) as telefono, "
			+ "		l.nombre as local, p.id as botones "
			+ " from paciente p inner join local l on p.localid = l.id "
			+ " where p.localid = ?1 "
			+ " order by p.id desc ", nativeQuery = true)
	List<IDataTablePaciente> findByLocal(int localid);
	
	
	@Query( value = " select p.id as id, concat(p.codigoletra, '-', p.codigonro) as codigo, concat(p.nombres, ' ', p.apellidos) as fullname,"
				+ "		p.dni as dni, concat( ifnull(p.telefono,''), ' / ', ifnull(p.celular, '') ) as telefono, "
				+ "		l.nombre as local, p.id as botones "
				+ " from paciente p inner join local l on p.localid = l.id "
				+ " order by p.id desc ", nativeQuery = true)
	List<IDataTablePaciente> findTodos();
	
	
	@Query( value = " select ifnull( max(codigonro), 0) "
			+ "		from paciente "
			+ "		where codigoletra = ?1 and localid =  ?2 ", nativeQuery = true)
	int getMaximoCodigoNroByCodigoLetraPorLocal(String codigoletra, int localid);
	
	
}
