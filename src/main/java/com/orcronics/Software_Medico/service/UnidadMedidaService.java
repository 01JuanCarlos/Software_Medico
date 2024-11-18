package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.UnidadMedida;

import java.util.List;

public interface UnidadMedidaService {

	UnidadMedida guardar(UnidadMedida unidad);
	void eliminar(int id);
	UnidadMedida buscar(int id);
	List<UnidadMedida> buscarTodos();
	
}
