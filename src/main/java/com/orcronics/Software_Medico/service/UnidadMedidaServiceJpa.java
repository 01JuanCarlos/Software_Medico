package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.UnidadMedida;
import com.orcronics.Software_Medico.repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaServiceJpa implements UnidadMedidaService {

	@Autowired
	private UnidadMedidaRepository unidadRepository;

	@Override
	public UnidadMedida guardar(UnidadMedida unidad) {
		return unidadRepository.save(unidad);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UnidadMedida buscar(int id) {
		Optional<UnidadMedida> optional = unidadRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<UnidadMedida> buscarTodos() {
		return unidadRepository.findAll();
	}
	
	
	
}
