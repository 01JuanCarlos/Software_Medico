package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.Paciente;
import com.orcronics.Software_Medico.interfaces.IDataTablePaciente;

import java.util.List;

public interface PacienteService {

	Paciente guardar(Paciente paciente);
	void eliminar (int id);
	Paciente buscar(int id);
	List<Paciente> buscarTodos();
	List<IDataTablePaciente> buscarPorParametro(String parametro);
	List<IDataTablePaciente> buscarPorParametroYLocal(int localid, String parametro);
	List<IDataTablePaciente> buscarTodosPorLocalId(int localid);
	List<IDataTablePaciente> buscarTodos2();
	int getMaximoCodigoNroPorCodigoLetraPorLocal(String codigoletra, int localid);
}
