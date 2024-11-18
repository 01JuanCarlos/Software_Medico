package com.orcronics.Software_Medico.service;

import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.Especialista;
import com.orcronics.Software_Medico.repository.EspecialistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecialistaServiceJPA implements EspecialistaService{

	@Autowired
	private EspecialistaRepository especialistaRepository;

	@Override
	public Especialista guardar(Especialista especialista) {
		return especialistaRepository.save(especialista);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Especialista buscar(int id) {
		Optional<Especialista> optional = especialistaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Especialista> buscarPorEstatus(String estatus) {
		//return especialistaRepository.findAll();
		return especialistaRepository.findAllByEstado(estatus);
	}
	
	@Override
	public List<Especialista> buscarTodos() {
		//return especialistaRepository.findAll();
		return especialistaRepository.findAll();
	}
	

	@Override
	public List<Especialista> buscarPorLocal(int localid) {
		return especialistaRepository.findByEstadoAndLocalesId("ACTIVO", localid);
	}
	
	@Override
	public List<Especialista> buscarPorLocalYEspecialidad(int localid, String especialidad) {
		return especialistaRepository.findByEstadoAndLocalesIdAndEspecialidad("ACTIVO", localid, especialidad);
	}

	@Override
	public List<Especialista> buscarPorLocalYEspecialidadDental(int localid) {
		return especialistaRepository.findByEstadoAndLocalesIdAndEspecialidadDental("ACTIVO", localid);
	}
	
	@Override
	public Especialista buscarPorNombres(String param) {
		return especialistaRepository.findByNombres(param);
	}
	
	
}
