package com.orcronics.Software_Medico.controller;

import java.util.List;

import com.orcronics.Software_Medico.entity.UnidadMedida;
import com.orcronics.Software_Medico.service.UnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping(value = "/api/app")
public class UnidadMedidadRestController {

	@Autowired
	private UnidadMedidaService unidadService;
	
	@PostMapping(value = "/unidades")
	public ResponseEntity<UnidadMedida> guardar(@RequestBody UnidadMedida unidad) {
		try {
			UnidadMedida um = unidadService.guardar(unidad);
			return new ResponseEntity<>(um, HttpStatus.OK);
		} catch (Exception e) {
		 	return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/unidades/{id}")
	public ResponseEntity<UnidadMedida> buscarPorId(@PathVariable int id) {
		try {
			UnidadMedida um= unidadService.buscar(id);
			if (um==null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(um, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/unidades")
	public ResponseEntity<List<UnidadMedida>> obtenerTodos() {
		try {
			List<UnidadMedida> list = unidadService.buscarTodos();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
