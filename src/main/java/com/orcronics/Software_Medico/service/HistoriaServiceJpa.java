package com.orcronics.Software_Medico.service;



import com.orcronics.Software_Medico.entity.Historia;
import com.orcronics.Software_Medico.repository.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HistoriaServiceJpa implements HistoriaService {

	@Autowired
	private HistoriaRepository historiaRepository;

	@Override
	public Historia guardar(Historia historia) {
		return historiaRepository.save(historia);
	}

	@Override
	public void eliminar(int id) {
		historiaRepository.deleteById(id);
	}

	@Override
	public Historia buscar(int id) {
		Optional<Historia> optional = historiaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Historia> buscarTodosPorPaciente(int pacienteid) {
		return historiaRepository.findAllByPacienteIdOrderByFechaDesc(pacienteid);
	}
	
	
	
}
