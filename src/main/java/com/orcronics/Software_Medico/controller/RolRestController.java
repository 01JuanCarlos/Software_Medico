package com.orcronics.Software_Medico.controller;

import java.util.List;

import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/app")
public class RolRestController {

	@Autowired
	private RolService rolService;
	
	@GetMapping(value = "/roles")
	public List<Rol> obtenerTodos() {
		return rolService.buscarTodos();
	}
	
}
