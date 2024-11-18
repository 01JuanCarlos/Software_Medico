package com.orcronics.Software_Medico.service;

import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.PacienteMultimedia;
import com.orcronics.Software_Medico.repository.PacienteMultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PacienteMultimediaServiceJpa implements PacienteMultimediaService {

	@Autowired
	private PacienteMultimediaRepository multimediaRepository;

	@Override
	public PacienteMultimedia guardar(PacienteMultimedia multimedia) {
		return multimediaRepository.save(multimedia);
	}

	@Override
	public void eliminar(int id) {
		multimediaRepository.deleteById(id);
	}

	@Override
	public PacienteMultimedia buscar(int id) {
		Optional<PacienteMultimedia> optional = multimediaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<PacienteMultimedia> buscarTodos(int pacienteid) {
		return multimediaRepository.findByPacienteId(pacienteid);
	}
	
}
