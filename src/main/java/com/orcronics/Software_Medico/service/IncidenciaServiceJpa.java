package com.orcronics.Software_Medico.service;



import com.orcronics.Software_Medico.entity.Incidencia;
import com.orcronics.Software_Medico.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class IncidenciaServiceJpa implements IncidenciaService {

	@Autowired
	private IncidenciaRepository incidenciaRepository;

	@Override
	public List<Incidencia> listarPorLocalYUsuario(int localid, int usuarioid) {
		return incidenciaRepository.findByLocalIdAndUsuarioIdOrderByFechaDesc(localid, usuarioid);
	}

	@Override
	public List<Incidencia> listarPorLocal(int localid) {
		return incidenciaRepository.findByLocalIdOrderByFechaDesc(localid);
	}

	@Override
	public List<Incidencia> listarTodo() {
		return incidenciaRepository.findAll();
	}

	@Override
	public void guardar(Incidencia incidencia) {
		incidenciaRepository.save(incidencia);
	}

	@Override
	public Incidencia buscar(int id) {
		Optional<Incidencia> optional = incidenciaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}
	
}
