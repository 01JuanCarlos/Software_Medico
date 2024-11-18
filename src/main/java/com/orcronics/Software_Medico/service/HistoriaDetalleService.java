package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.HistoriaDetalle;

import java.util.List;

public interface HistoriaDetalleService {

	HistoriaDetalle guardar(HistoriaDetalle detalle);
	void eliminar(int id);
	HistoriaDetalle buscar(int id);
	List<HistoriaDetalle> buscarTodos(int historiaid);
	
}
