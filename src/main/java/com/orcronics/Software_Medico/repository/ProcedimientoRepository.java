package com.orcronics.Software_Medico.repository;

import java.util.List;

import com.orcronics.Software_Medico.entity.Procedimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProcedimientoRepository extends JpaRepository<Procedimiento, Integer>{

	Procedimiento findByDescripcion(String descripcion);
	Procedimiento findByDescripcionAndLocalId(String descripcion, int localid);
	List<Procedimiento> findByActivo(boolean activo);
	List<Procedimiento> findByLocalId(int localid);
	List<Procedimiento> findByActivoAndLocalId(boolean activo, int localid);
	List<Procedimiento> findByTipoexistenciaId(int idexistencia);
	
	
}
