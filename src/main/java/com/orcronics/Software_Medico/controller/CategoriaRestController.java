package com.orcronics.Software_Medico.controller;

import java.util.List;

import com.orcronics.Software_Medico.entity.Categoria;
import com.orcronics.Software_Medico.service.CategoriaService;
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


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/app")
public class CategoriaRestController {

	
	@Autowired
	private CategoriaService categoriaService;
	
	
	@PostMapping(value = "/categorias")
	public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
		try {
			Categoria categ = categoriaService.guardar(categoria);
			return new ResponseEntity<>(categ, HttpStatus.OK);
		} catch (Exception e) {
		 	return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/categorias/{id}")
	public ResponseEntity<Categoria> buscarPorId(@PathVariable int id) {
		try {
			Categoria categ = categoriaService.buscar(id);
			if (categ==null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(categ, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/categorias")
	public ResponseEntity<List<Categoria>> obtenerTodos() {
		try {
			List<Categoria> list = categoriaService.buscarTodos();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
