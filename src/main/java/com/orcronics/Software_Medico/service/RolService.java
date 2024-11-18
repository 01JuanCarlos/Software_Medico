package com.orcronics.Software_Medico.service;


import com.orcronics.Software_Medico.security.entity.Rol;

import java.util.List;

public interface RolService {

	Rol guardar(Rol rol);
	void eliminar(int id);
	Rol buscar(int id);
	List<Rol> buscarTodos();

	Rol buscarPorRol(String rolName);
	
}
