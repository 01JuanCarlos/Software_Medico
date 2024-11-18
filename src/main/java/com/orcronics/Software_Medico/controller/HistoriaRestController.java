package com.orcronics.Software_Medico.controller;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.orcronics.Software_Medico.entity.Historia;
import com.orcronics.Software_Medico.entity.HistoriaDetalle;
import com.orcronics.Software_Medico.service.HistoriaService;
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
@RestController //@RestController reemplaza a @Controller.
@RequestMapping(value = "/api/app")
public class HistoriaRestController {
	
	@Autowired
	private HistoriaService historiaService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@PostMapping(value = "/historias/{fecha}")
	public ResponseEntity<Historia> crear(@RequestBody Historia historia, @PathVariable String fecha) {
		System.out.println(historia.toString());
		try {
			Set<HistoriaDetalle> list = new HashSet<HistoriaDetalle>();
			for (HistoriaDetalle det : historia.getDetalle()) {
				det.setHistoria(historia);
				list.add(det);
			}
			historia.setDetalle(list);
			historia.setFecha( dateFormat.parse(fecha) ); System.out.println(historia.toString());
			Historia hist = historiaService.guardar(historia);	
			return new ResponseEntity<>(hist, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/historias/{pacienteid}")
	public ResponseEntity<List<Historia>> obtenerTodosPorPaciente(@PathVariable int pacienteid) {
		try {
			List<Historia> list = historiaService.buscarTodosPorPaciente(pacienteid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
