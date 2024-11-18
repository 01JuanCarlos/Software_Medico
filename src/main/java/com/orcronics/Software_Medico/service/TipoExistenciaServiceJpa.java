package com.orcronics.Software_Medico.service;

import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.TipoExistencia;
import com.orcronics.Software_Medico.repository.TipoExistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TipoExistenciaServiceJpa implements TipoExistenciaService {

	@Autowired
	private TipoExistenciaRepository existenciaRepository;

	@Override
	public TipoExistencia guardar(TipoExistencia existencia) {
		return existenciaRepository.save(existencia);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TipoExistencia buscar(int id) {
		Optional<TipoExistencia> optional = existenciaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<TipoExistencia> buscarTodos() {
		return existenciaRepository.findAll();
	}
	
	
	
}
