package com.orcronics.Software_Medico.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.orcronics.Software_Medico.entity.Atencion;
import com.orcronics.Software_Medico.interfaces.IConteoPodologoProcedimiento;
import com.orcronics.Software_Medico.interfaces.IConteoProcedimiento;
import com.orcronics.Software_Medico.repository.AtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AtencionServiceJPA implements AtencionService {

	@Autowired
	private AtencionRepository atencionRepository;

	@Override
	public Atencion guardar(Atencion atencion) {
		return atencionRepository.save(atencion);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atencion buscar(int id) {
		Optional<Atencion> optional = atencionRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Atencion> buscarTodos() {
		return atencionRepository.findAll();
	}

	@Override
	public List<Atencion> buscarPorRangoFechaYLocal(Date fechaInicio, Date fechaFin, int idlocal) {
		return atencionRepository.findByFechaBetweenAndLocalId(fechaInicio, fechaFin, idlocal);
	}

	@Override
	public List<Atencion> buscarPorRangoFechaMenorIgualQueYMayorIgualQueYLocal(Date fechaFin, Date fechaInicio, int idlocal) {
		return atencionRepository.findByFechaLessThanEqualAndFechaGreaterThanEqualAndLocalId(fechaFin, fechaInicio, idlocal);
	}

	@Override
	public List<IConteoProcedimiento> conteoPorProcedimientoInterface(Date fechaInicio, Date fechaFin, int idlocal) {
		return atencionRepository.conteoPorProcedimientoInterface(fechaInicio, fechaFin, idlocal);
	}

	@Override
	public List<IConteoPodologoProcedimiento> conteoPorPodologoProcedimientoInterface(Date fechaInicio, Date fechaFin,
																					  int idlocal) {
		return atencionRepository.conteoPorPodologoProcedimientoInterface(fechaInicio, fechaFin, idlocal);
	}
	
	
}
