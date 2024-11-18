package com.orcronics.Software_Medico.service;

import com.orcronics.Software_Medico.entity.Procedimiento;

import java.util.List;

public interface ProcedimientoService {

	Procedimiento guardar(Procedimiento producto);
	void eliminar(int id);
	Procedimiento buscar(int id);
	List<Procedimiento> buscarTodos();
	
	Procedimiento buscarPorDescripcion(String descripcion);
	Procedimiento buscarPorDescripcionYLocal(String descripcion, int localid);
	List<Procedimiento> buscarPorActivo(boolean activo);
	List<Procedimiento> buscarPorLocal(int localid);
	List<Procedimiento> buscarPorActivoYLocal(boolean activo, int localid);
	List<Procedimiento> buscarPorTipoExistencia(int idexistencia);
	
}
