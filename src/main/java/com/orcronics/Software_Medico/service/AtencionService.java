package com.orcronics.Software_Medico.service;

import com.orcronics.Software_Medico.entity.Atencion;
import com.orcronics.Software_Medico.interfaces.IConteoPodologoProcedimiento;
import com.orcronics.Software_Medico.interfaces.IConteoProcedimiento;

import java.util.Date;
import java.util.List;


public interface AtencionService {

	Atencion guardar(Atencion atencion);
	void eliminar(int id);
	Atencion buscar(int id);
	List<Atencion> buscarTodos();
	

	List<Atencion> buscarPorRangoFechaYLocal(Date fechaInicio, Date fechaFin, int idlocal);
	List<Atencion> buscarPorRangoFechaMenorIgualQueYMayorIgualQueYLocal(Date fechaFin, Date fechaInicio, int idlocal);
	
	List<IConteoProcedimiento> conteoPorProcedimientoInterface(Date fechaInicio, Date fechaFin, int idlocal);
	List<IConteoPodologoProcedimiento> conteoPorPodologoProcedimientoInterface(Date fechaInicio, Date fechaFin, int idlocal);
	
}
