package com.orcronics.Software_Medico.service;



import com.orcronics.Software_Medico.entity.Categoria;
import com.orcronics.Software_Medico.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriaServiceJpa implements CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public Categoria guardar(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Categoria buscar(int id) {
		Optional<Categoria> optional = categoriaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll();
	}
	
	
}
