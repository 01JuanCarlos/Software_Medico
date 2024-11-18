package com.orcronics.Software_Medico.controller;

import java.util.ArrayList;
import java.util.List;

import com.orcronics.Software_Medico.entity.Especialista;
import com.orcronics.Software_Medico.service.EspecialistaService;
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
public class EspecialistaRestController {

	@Autowired
	private EspecialistaService especialistaService;
	
	
	@PostMapping(value = "/especialistas")
	public ResponseEntity<Especialista> crear(@RequestBody Especialista especialista) {
		try {System.out.println(especialista.toString());
			especialista.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			Especialista esp = especialistaService.guardar(especialista);
			return new ResponseEntity<>(esp, HttpStatus.OK);
		} catch (Exception e) {System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/especialistas") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Especialista>> obtenerTodos() {
		try {
			List<Especialista> list = especialistaService.buscarTodos();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/especialistas/activos") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Especialista>> obtenerTodosActivo() {
		try {
			List<Especialista> list = especialistaService.buscarPorEstatus("ACTIVO");
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/especialistas/por_estatus/{estatus}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Especialista>> obtenerTodosPorEstatus(@PathVariable String estatus) {
		try {
			List<Especialista> list = especialistaService.buscarPorEstatus(estatus);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/especialistas/activos_por_local/{localid}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Especialista>> obtenerTodosActivoPorLocal(@PathVariable int localid) {
		try {
			List<Especialista> list = especialistaService.buscarPorLocal(localid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/especialistas/activos_por_local_y_especialidad/{localid}/{especialidad}")
	public ResponseEntity<List<Especialista>> obtenerTodosActivoPorLocal(@PathVariable int localid, 
			@PathVariable String especialidad) {
		try {
			List<Especialista> list = new ArrayList<>();
			if (especialidad.equals("dental"))
				list = especialistaService.buscarPorLocalYEspecialidadDental(localid);
			else
				list = especialistaService.buscarPorLocalYEspecialidad(localid, especialidad);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/especialistas/nombres/{search}") //también @GetMapping("/usuarios")
	public ResponseEntity<Especialista> obtenerPorNombres(@PathVariable String search) {
		try {
			Especialista especialista = especialistaService.buscarPorNombres(search);
			if (especialista == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(especialista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
