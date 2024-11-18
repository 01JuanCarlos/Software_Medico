package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.AtencionDetalle;

import java.util.List;

public interface AtencionDetalleService {

	AtencionDetalle guardar(AtencionDetalle detalle);
	void eliminar(int id);
	AtencionDetalle buscar(int id);
	List<AtencionDetalle> buscarTodos(int atencionid);
	
}
