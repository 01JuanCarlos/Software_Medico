package com.orcronics.Software_Medico.controller;


import com.orcronics.Software_Medico.entity.Procedimiento;
import com.orcronics.Software_Medico.service.ProcedimientoService;
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

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*") //por defecto acepta orígenes sólo de localhost:8080
@RestController //@RestController reemplaza a @Controller.
@RequestMapping(value = "/api/app")
public class ProductoRestController {

	@Autowired
	private ProcedimientoService productoService;
	
	@GetMapping(value = "/productos/por_local/{localid}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Procedimiento>> obtenerTodos(@PathVariable int localid) {
		try {
			List<Procedimiento> list = productoService.buscarPorLocal(localid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//@GetMapping(value = "/productos/activos")
	@GetMapping(value = "/procedimientos/activos")
	public ResponseEntity<List<Procedimiento>> obtenerTodosActivos() {
		try {
			List<Procedimiento> list = productoService.buscarPorActivo(true);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@GetMapping(value = "/productos/activos_por_local/{localid}")
	@GetMapping(value = "/procedimientos/activos_por_local/{localid}")
	public ResponseEntity<List<Procedimiento>> obtenerTodosActivosPorLoca(@PathVariable int localid) {
		try {
			List<Procedimiento> list = productoService.buscarPorActivoYLocal(true, localid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/productos/tipoexistencia/{idtipoexistencia}")
	public ResponseEntity<List<Procedimiento>> obtenerTodosPorTipoExistencia(@PathVariable int idexistencia) {
		try {
			List<Procedimiento> list = productoService.buscarPorTipoExistencia(0);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/productos/descripcion/{local}/{descripcion}")
	public ResponseEntity<Procedimiento> obtenerPorDescripcion(@PathVariable int local, @PathVariable String descripcion) {
		try {
			Procedimiento producto = productoService.buscarPorDescripcionYLocal(descripcion, local);
			if (producto == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(producto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/productos/descripcionylocal/{descripcion}/{local}")
	public ResponseEntity<Procedimiento> obtenerPorDescripcionYLocal(@PathVariable String descripcion, 
			@PathVariable int local) {
		try {
			Procedimiento producto = productoService.buscarPorDescripcionYLocal(descripcion, local);
			if (producto == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(producto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/productos")
	public ResponseEntity<Procedimiento> guardar(@Valid @RequestBody Procedimiento producto) {
		try {
			Procedimiento prod = null;
			if (producto.getId()==0)
			{
				//inserto data y obtengo objeto.
				prod = productoService.guardar(producto);
				//obtengo id del objeto insertado, luego seteo el codigo con 2 ceros.
				//prod.setCodigo("00"+prod.getId());
				//inserto el objeto seteado.
				//productoService.guardar(prod);
			} else {
				//actualizo data.
				prod = productoService.guardar(producto);
			}
			return new ResponseEntity<>(prod, HttpStatus.OK);
		} catch (Exception e) {
		 	return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/productos/{id}")
	public ResponseEntity<Procedimiento> buscarPorId(@PathVariable int id) {
		try {
			Procedimiento prod = productoService.buscar(id);
			if (prod==null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(prod, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
}
