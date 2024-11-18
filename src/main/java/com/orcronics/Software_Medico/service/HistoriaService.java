package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.Historia;

import java.util.List;

public interface HistoriaService {
	
	Historia guardar(Historia historia);
	void eliminar(int id);
	Historia buscar(int id);
	List<Historia> buscarTodosPorPaciente(int pacienteid);

}
