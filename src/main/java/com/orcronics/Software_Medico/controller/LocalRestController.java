package com.orcronics.Software_Medico.controller;

import java.util.List;

import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") //por defecto acepta orígenes sólo de localhost:8080
@RestController //@RestController reemplaza a @Controller.
@RequestMapping(value = "/api/app")
public class LocalRestController {
	
	@Autowired
	private LocalService localService;
	
	@GetMapping(value = "/locales") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Local>> obtenerTodos() {
		try {
			List<Local> list = localService.buscarTodos();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/locales/{id}")
	public ResponseEntity<Local> obtenerPorId(@PathVariable int id) {
		Local local = localService.buscarporId(id)
				.orElseThrow(() -> new RuntimeException("Local con ID " + id + " no encontrado"));

		if (local==null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(local, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/locales")
	public ResponseEntity<Local> crear(@RequestBody Local local) {
		try {
			local.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			Local loc = localService.guardar(local);
			return new ResponseEntity<>(loc, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
