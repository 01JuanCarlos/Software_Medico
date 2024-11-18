package com.orcronics.Software_Medico.repository;

import java.util.List;

import com.orcronics.Software_Medico.entity.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer>{

	List<Incidencia> findByLocalIdAndUsuarioIdOrderByFechaDesc(int localid, int usuarioid);
	List<Incidencia> findByLocalIdOrderByFechaDesc(int localid);
	
    @Query(value = "SELECT i.id, i.comentario, i.fecha, i.localid, i.usuarioid  FROM incidencia i "
    		+ " INNER JOIN local l on i.localid=l.id"
    		+ " INNER JOIN user u on i.usuarioid=u.id "
    		+ " ORDER BY i.localid, i.usuarioid, i.fecha DESC " , nativeQuery = true)
	List<Incidencia> findAll();
	
}
