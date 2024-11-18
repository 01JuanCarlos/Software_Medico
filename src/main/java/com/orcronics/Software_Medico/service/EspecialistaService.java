package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.Especialista;

import java.util.List;

public interface EspecialistaService {

	Especialista guardar(Especialista especialista);
	void eliminar(int id);
	Especialista buscar(int id);
	List<Especialista> buscarPorEstatus(String estatus);
	List<Especialista> buscarTodos();
	List<Especialista> buscarPorLocal(int localid);
	List<Especialista> buscarPorLocalYEspecialidad(int localid, String especialidad);
	List<Especialista> buscarPorLocalYEspecialidadDental(int localid);
	
	Especialista buscarPorNombres(String param);
	
}
