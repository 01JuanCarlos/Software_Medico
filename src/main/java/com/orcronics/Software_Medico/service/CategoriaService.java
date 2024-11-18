package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.entity.Categoria;

import java.util.List;

public interface CategoriaService {
	
	Categoria guardar(Categoria categoria);
	void eliminar(int id);
	Categoria buscar(int id);
	List<Categoria> buscarTodos();

}
