package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.PacienteMultimedia;

import java.util.List;

public interface PacienteMultimediaService {

	PacienteMultimedia guardar(PacienteMultimedia multimedia);
	void eliminar(int id);
	PacienteMultimedia buscar(int id);
	List<PacienteMultimedia> buscarTodos(int pacienteid);
	
}
