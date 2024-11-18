package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.TipoExistencia;

import java.util.List;

public interface TipoExistenciaService {

	TipoExistencia guardar(TipoExistencia existencia);
	void eliminar(int id);
	TipoExistencia buscar(int id);
	List<TipoExistencia> buscarTodos();
	
}
