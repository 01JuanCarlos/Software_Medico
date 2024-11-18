package com.orcronics.Software_Medico.controller;

import java.util.List;

import com.orcronics.Software_Medico.entity.Incidencia;
import com.orcronics.Software_Medico.service.IncidenciaService;
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


@CrossOrigin(origins = "*") //por defecto acepta orígenes sólo de localhost:8080
@RestController
@RequestMapping(value = "/api/app")
public class IncidenciaRestController {

	@Autowired
	private IncidenciaService incidenciaService;
	
	@GetMapping(value = "/incidencias")
	public ResponseEntity<List<Incidencia>> obtenerTodos() {
		try {
			List<Incidencia> list = incidenciaService.listarTodo();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/incidencias/{localid}")
	public ResponseEntity<List<Incidencia>> obtenerPorLocal(@PathVariable int localid) {
		try {
			List<Incidencia> list = incidenciaService.listarPorLocal(localid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/incidencias/{localid}/{usuarioid}")
	public ResponseEntity<List<Incidencia>> obtenerPorLocalYUsuario(@PathVariable int localid, 
			@PathVariable int usuarioid) {
		try {
			List<Incidencia> list = incidenciaService.listarPorLocalYUsuario(localid, usuarioid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/incidencias/porid/{id}")
	public ResponseEntity<Incidencia> obtenerPorId(@PathVariable int id) {
		Incidencia incidencia = incidenciaService.buscar(id);
		if (incidencia==null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(incidencia, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/incidencias")
	public ResponseEntity<String> guardar(@RequestBody Incidencia incidencia) {
		try {
			incidenciaService.guardar(incidencia);
			return new ResponseEntity<>("¡Incidencia guardada!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
