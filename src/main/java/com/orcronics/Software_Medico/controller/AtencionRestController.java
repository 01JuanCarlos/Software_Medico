package com.orcronics.Software_Medico.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.orcronics.Software_Medico.entity.Atencion;
import com.orcronics.Software_Medico.entity.AtencionDetalle;
import com.orcronics.Software_Medico.interfaces.IConteoPodologoProcedimiento;
import com.orcronics.Software_Medico.interfaces.IConteoProcedimiento;
import com.orcronics.Software_Medico.service.AtencionService;
import com.orcronics.Software_Medico.utility.Utilities;
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
public class AtencionRestController {

	@Autowired
	private AtencionService atencionService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
	
	
	@PostMapping(value = "/atenciones")
	public ResponseEntity<Atencion> crear(@RequestBody Atencion atencion) {
		System.out.println(atencion.toString());
		try {
			Set<AtencionDetalle> list = new HashSet<AtencionDetalle>();
			for (AtencionDetalle det : atencion.getDetalle()) {
				det.setAtencion(atencion);
				list.add(det);
			}
			atencion.setDetalle(list);
			atencion.setFecha(new Date());
			Atencion aten = atencionService.guardar(atencion);	
			return new ResponseEntity<>(aten, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/atenciones") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Atencion>> obtenerTodos() {
		try {
			List<Atencion> list = atencionService.buscarTodos();
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/atenciones/general/{fechaInicio}/{fechaFin}/{local}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Atencion>> obtenerPorRangoFechasYLocal(@PathVariable String fechaInicio,
			@PathVariable String fechaFin, @PathVariable int local) {
		try {
			//Para obtener un día despues, y esto ayudara al beetween para que la fecha final esté dentro del rango.
			String end = Utilities.sumarDiasAFecha(dateFormat.parse(fechaFin), 1);
			List<Atencion> list = atencionService.buscarPorRangoFechaYLocal(
					dateFormat.parse(fechaInicio), dateFormat.parse(end), local);
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/atenciones/general-procedimiento/{fechaInicio}/{fechaFin}/{local}")
	public ResponseEntity<List<IConteoProcedimiento>> obtenerConteoProcedimientoPorRangoFechasYLocal(@PathVariable String fechaInicio,
																									 @PathVariable String fechaFin, @PathVariable int local) {
		try {
			//Para obtener un día despues, y esto ayudara al beetween para que la fecha final esté dentro del rango.
			String end = Utilities.sumarDiasAFecha(dateFormat.parse(fechaFin), 1);
			List<IConteoProcedimiento> list = atencionService.conteoPorProcedimientoInterface(
					dateFormat. parse(fechaInicio), dateFormat.parse(end), local);
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/atenciones/general-podologo-procedimiento/{fechaInicio}/{fechaFin}/{local}")
	public ResponseEntity<List<IConteoPodologoProcedimiento>> obtenerConteoPodologoProcedimientoPorRangoFechasYLocal(@PathVariable String fechaInicio,
																													 @PathVariable String fechaFin, @PathVariable int local) {
		try {
			//Para obtener un día despues, y esto ayudara al beetween para que la fecha final esté dentro del rango.
			String end = Utilities.sumarDiasAFecha(dateFormat.parse(fechaFin), 1);
			List<IConteoPodologoProcedimiento> list = atencionService.conteoPorPodologoProcedimientoInterface(
					dateFormat.parse(fechaInicio), dateFormat.parse(end), local);
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
