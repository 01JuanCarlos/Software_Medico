package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.Incidencia;

import java.util.List;

public interface IncidenciaService {

	List<Incidencia> listarPorLocalYUsuario(int localid, int usuarioid);
	List<Incidencia> listarPorLocal(int localid); //para usuario admin (aun no usado).
	List<Incidencia> listarTodo(); //para usuario admin.
	void guardar(Incidencia incidencia);
	Incidencia buscar(int id);
	
}
