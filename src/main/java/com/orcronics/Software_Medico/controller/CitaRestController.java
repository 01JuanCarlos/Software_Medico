package com.orcronics.Software_Medico.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orcronics.Software_Medico.entity.Cita;
import com.orcronics.Software_Medico.entity.Especialista;
import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.service.LocalService;
import com.orcronics.Software_Medico.service.CitaService;
import com.orcronics.Software_Medico.service.EspecialistaService;
import com.orcronics.Software_Medico.service.PacienteService;
import com.orcronics.Software_Medico.utility.UtilitiesDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import ch.qos.logback.core.util.COWArrayList;

@CrossOrigin(origins = "*") //por defecto acepta orígenes sólo de localhost:8080
@RestController //@RestController reemplaza a @Controller.
@RequestMapping(value = "/api/app")
public class CitaRestController {

	@Autowired
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private EspecialistaService especialistaService;
	@Autowired
	private LocalService localService;
	
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat shortFormat = new SimpleDateFormat("hh:mm a"); //para mostrar hora con am/pm la h minuscula y a
	private SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	private SimpleDateFormat fullFormatEn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	private SimpleDateFormat fullFormatEn2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	@PostMapping(value = "/citas/{inicio}/{termino}/{pacienteid}")
	public ResponseEntity<Cita> crear(@RequestBody Cita cita, @PathVariable String inicio, @PathVariable String termino,
									  @PathVariable int pacienteid) {
		try {
			Date ahora = new Date();
			cita.setFechareg(ahora);
			cita.setPaciente( pacienteService.buscar(pacienteid) );
			if (cita.getId()==0)
			{
				cita.setFechareg(ahora);
				cita.setUsuarioreg(0);
			} else {
				cita.setFechaact(ahora);
				cita.setUsuarioact(0);
			}
			if (cita.getPaciente()==null)
				cita.setEstatus("Disponible");
			else
				cita.setEstatus("Pendiente");
			cita.setInicio( fullFormatEn.parse(inicio) );
			cita.setTermino( fullFormatEn.parse(termino) );
			
			Cita ci = citaService.guardar(cita);
			return new ResponseEntity<>(ci, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/** 
	Sólo guardar cambios, no alterar los estatus.
	**/
	@GetMapping(value = "/citas/update/{id}/{especialistaid}/{procedimiento}/{pacienteid}")
	public ResponseEntity<Cita> guardarCambiosCita(@PathVariable int id, @PathVariable int especialistaid, 
			@PathVariable String procedimiento, @PathVariable int pacienteid) {
		try {
			Cita cita = citaService.buscar(id);
			
			//Si se cambió de polódogo, entonces el podologo inicial queda en una cita nueva como disponible.
			if (cita.getEspecialista().getId()!=especialistaid) {
				//Antes de hacer una cita nueva, verificamos si hay una cita registrada con esos datos para no registrarlo.
				//Caso contrario se procede a registrarlo con estatus disponible.
				List<Cita> registrados = citaService
						.buscarCitaCambiadaEnOtraCitaYaRegistrada(cita.getLocal().getId(), 
								cita.getEspecialista().getId(), cita.getInicio(), cita.getTermino(), cita.getId());
				if (registrados.size()==0) {
					Cita cita2 = new Cita();
					cita2.setEstatus("Disponible");
					cita2.setEspecialista(cita.getEspecialista());
					cita2.setPaciente(null);
					cita2.setInicio(cita.getInicio() );
					cita2.setTermino(cita.getTermino());
					cita2.setProcedimiento("-");
					cita2.setSolicitopodologo(false);
					cita2.setLocal(cita.getLocal());
					cita2.setUsuarioreg(cita.getUsuarioreg());
					cita2.setUsuarioact(cita.getUsuarioact());
					cita2.setFechareg(cita.getFechareg());
					cita2.setFechaact(cita.getFechaact());
					cita2 = citaService.guardar(cita2);
				}
			}
			
			//Continuamos con la cita inicial.
			//Antes verificamos si el especialista cambiado (nuevo) ya tiene cita libre (Disponible).
			List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), especialistaid, cita.getInicio(), cita.getTermino());
			//Si la cantidad encontrada es igual 1, entonces ahí será reemplazado, caso contrario se guarda.
			if (buscados.size()==1) {
				Cita c = buscados.get(0); // aquí reemplazamos la cita cambiada.
				//c.setEstatus("En Atención");
				c.setPaciente(cita.getPaciente());
				c.setProcedimiento(procedimiento);
				c.setSolicitopodologo(false);
				c.setEspecialista( especialistaService.buscar(especialistaid) );
				//actualizamos: reemplazamos.
				c = citaService.guardar(c);
				//y el objeto "cita" se elimina".
				citaService.eliminar(cita.getId());
			} else {
				//sino se encontró sólo lo actualizamos el objeto "cita".
				//cita.setEstatus("En Atención");
				cita.setProcedimiento(procedimiento);
				cita.setPaciente( pacienteService.buscar(pacienteid) );
				//cita.setSolicitopodologo(false);
				cita.setEspecialista( especialistaService.buscar(especialistaid) );
				cita = citaService.guardar(cita);
			}
			/*cita.setEstatus("En Atención");
			cita.setProcedimiento(procedimiento);
			cita.setSolicitopodologo(false);
			cita.setEspecialista( especialistaService.buscar(especialistaid) );
			cita = citaService.guardar(cita);*/
			
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	/*
	 * 
	@PostMapping(value = "/citasbackup/{pacienteid}")
	public ResponseEntity<Cita> crearBackup(@RequestBody Cita cita, @PathVariable int pacienteid) {
		System.out.println(cita.toString());
		System.out.println("Id: "+pacienteid);
		try {
			Paciente p = null;
			if (pacienteid==0)
			{
				p = new Paciente();
				p.setNombres(cita.getNombres());
				p.setApellidos("");
				p.setDni(cita.getDni());
				p.setTelefono(cita.getTelefono());
				if (cita.getCodigohistoria()!="" || cita.getCodigohistoria()!=null)
				{
					p.setCodigoLetra("");
					p.setCodigoNro(0);
				} else {
					p.setCodigoLetra("");
					p.setCodigoNro(0);
				}
				p = pacienteService.guardar(p);
				cita.setPaciente(p); System.out.println("paciente insert ok"+cita.toString());
			} else {
				p = pacienteService.buscar(pacienteid);
				cita.setPaciente(p);
			}
			Date ahora = new Date();
			if (cita.getId()==0)
			{
				cita.setFechareg(ahora);
				cita.setUsuarioreg(0);
			} else {
				cita.setFechaact(ahora);
				cita.setUsuarioact(0);
			}
			Cita ci = citaService.guardar(cita);
			return new ResponseEntity<>(ci, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/
	
	//Confirmar cita: sólo cambia el estatus.
	@GetMapping(value = "/citas/confirmar/{id}")
	public ResponseEntity<Cita> confirmarCita(@PathVariable int id) {
		try {
			Cita cita = citaService.buscar(id);
			cita.setEstatus("Confirmado");
			cita = citaService.guardar(cita);
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Pasar a estatus "En Atención".
	// - Primero se verifica si en la cita agendada se ha cambiado al especialista, entonces a la cita agendada 
	// se le quita al paciente y queda como Disponible para dicho especialista.
	// - Segundo basado en el especialista nuevo asignado y con los datos anteriores del paciente, fechas y procedimiento,
	// se procede a buscar en base a la fecha/hora de inicio y termino si esque ese especialista ya está 
	// en una cita Disponible para reemplazarlo simplemente en esa cita libre; caso contrario de no haber encontrado
	// algo se procede a registrar (insert) como nuevo registro de cita con sus valores.
	@GetMapping(value = "/citas/en_atencion/{id}/{especialistaid}/{procedimiento}")
	public ResponseEntity<Cita> enAtencionCita(@PathVariable int id, @PathVariable int especialistaid, 
			@PathVariable String procedimiento) {
		try {
			Cita cita = citaService.buscar(id);
			
			//Si se cambió de polódogo, entonces el podologo inicial queda en una cita nueva como disponible.
			if (cita.getEspecialista().getId()!=especialistaid) {
				//Antes de hacer una cita nueva, verificamos si hay una cita registrada con esos datos para no registrarlo.
				//Caso contrario se procede a registrarlo con estatus disponible.
				List<Cita> registrados = citaService
						.buscarCitaCambiadaEnOtraCitaYaRegistrada(cita.getLocal().getId(), 
								cita.getEspecialista().getId(), cita.getInicio(), cita.getTermino(), cita.getId());
				if (registrados.size()==0) {
					Cita cita2 = new Cita();
					cita2.setEstatus("Disponible");
					cita2.setEspecialista(cita.getEspecialista());
					cita2.setPaciente(null);
					cita2.setInicio(cita.getInicio() );
					cita2.setTermino(cita.getTermino());
					cita2.setProcedimiento("-");
					cita2.setSolicitopodologo(false);
					cita2.setLocal(cita.getLocal());
					cita2.setUsuarioreg(cita.getUsuarioreg());
					cita2.setUsuarioact(cita.getUsuarioact());
					cita2.setFechareg(cita.getFechareg());
					cita2.setFechaact(cita.getFechaact());
					cita2 = citaService.guardar(cita2);
				}
			}
			
			//Continuamos con la cita inicial.
			//Antes verificamos si el especialista cambiado (nuevo) ya tiene cita libre (Disponible).
			List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), especialistaid, cita.getInicio(), cita.getTermino());
			//Si la cantidad encontrada es igual 1, entonces ahí será reemplazado, caso contrario se guarda.
			if (buscados.size()==1) {
				Cita c = buscados.get(0); // aquí reemplazamos la cita cambiada.
				c.setEstatus("En Atención");
				c.setPaciente(cita.getPaciente());
				c.setProcedimiento(procedimiento);
				c.setSolicitopodologo(false);
				c.setEspecialista( especialistaService.buscar(especialistaid) );
				//actualizamos: reemplazamos.
				c = citaService.guardar(c);
				//y el objeto "cita" se elimina".
				citaService.eliminar(cita.getId());
			} else {
				//sino se encontró sólo lo actualizamos el objeto "cita".
				cita.setEstatus("En Atención");
				cita.setProcedimiento(procedimiento);
				cita.setSolicitopodologo(false);
				cita.setEspecialista( especialistaService.buscar(especialistaid) );
				cita = citaService.guardar(cita);
			}
			/*cita.setEstatus("En Atención");
			cita.setProcedimiento(procedimiento);
			cita.setSolicitopodologo(false);
			cita.setEspecialista( especialistaService.buscar(especialistaid) );
			cita = citaService.guardar(cita);*/
			
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/citas/atender/{id}/{especialistaid}/{procedimiento}")
	public ResponseEntity<Cita> atenderCita(@PathVariable int id, @PathVariable int especialistaid, @PathVariable String procedimiento) {
		try {
			Cita cita = citaService.buscar(id);
			
			//Si se cambió de polódogo, entonces el podologo inicial queda en una cita nueva como disponible.
			if (cita.getEspecialista().getId()!=especialistaid) {
				//Antes de hacer una cita nueva, verificamos si hay una cita registrada con esos datos para no registrarlo.
				//Caso contrario se procede a registrarlo con estatus disponible.
				List<Cita> registrados = citaService
						.buscarCitaCambiadaEnOtraCitaYaRegistrada(cita.getLocal().getId(), 
								cita.getEspecialista().getId(), cita.getInicio(), cita.getTermino(), cita.getId());
				if (registrados.size()==0) {
					Cita cita2 = new Cita();
					cita2.setEstatus("Disponible");
					cita2.setEspecialista(cita.getEspecialista());
					cita2.setPaciente(null);
					cita2.setInicio(cita.getInicio() );
					cita2.setTermino(cita.getTermino());
					cita2.setProcedimiento("-");
					cita2.setSolicitopodologo(false);
					cita2.setLocal(cita.getLocal());
					cita2.setUsuarioreg(cita.getUsuarioreg());
					cita2.setUsuarioact(cita.getUsuarioact());
					cita2.setFechareg(cita.getFechareg());
					cita2.setFechaact(cita.getFechaact());
					cita2 = citaService.guardar(cita2);
				}
			}
			
			
			//Continuamos con la cita inicial.
			//Antes verificamos si el especialista cambiado (nuevo) ya tiene cita libre (Disponible).
			List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), especialistaid, cita.getInicio(), cita.getTermino());
			//Si la cantidad encontrada es igual 1, entonces ahí será reemplazado, caso contrario se guarda.
			if (buscados.size()==1) {
				Cita c = buscados.get(0); // aquí reemplazamos la cita cambiada.
				c.setEstatus("Atendido");
				c.setPaciente(cita.getPaciente());
				c.setProcedimiento(procedimiento);
				c.setSolicitopodologo(false);
				c.setEspecialista( especialistaService.buscar(especialistaid) );
				//actualizamos: reemplazamos.
				c = citaService.guardar(c);
				//y el objeto "cita" se elimina.
				citaService.eliminar(cita.getId());
			} else {
				//sino se encontró sólo lo actualizamos el objeto "cita".
				cita.setEstatus("Atendido");
				cita.setProcedimiento(procedimiento);
				cita.setSolicitopodologo(false);
				cita.setEspecialista( especialistaService.buscar(especialistaid) );
				cita = citaService.guardar(cita);
			}
			//Continuamos con la cita inicial.
			/*cita.setEstatus("Atendido");
			cita.setProcedimiento(procedimiento);
			cita.setSolicitopodologo(false);
			cita.setEspecialista( especialistaService.buscar(especialistaid) );
			cita = citaService.guardar(cita);*/
			
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/citas/reagendar/{id}/{inicio}/{termino}")
	public ResponseEntity<Cita> reagendarCita(@PathVariable int id, @PathVariable String inicio, @PathVariable String termino) {
		try {
			//Obtenemos cita inicial.
			Cita cita = citaService.buscar(id);
			
			//Cita reagendada quedará con los mismos datos, pero como re-agendado sólo como registro.
			Cita reagendado = new Cita();
			reagendado.setEstatus("Re-agendado");
			reagendado.setEspecialista(cita.getEspecialista());
			reagendado.setPaciente(cita.getPaciente());
			reagendado.setInicio(cita.getInicio() );
			reagendado.setTermino(cita.getTermino());
			reagendado.setProcedimiento(cita.getProcedimiento());
			reagendado.setSolicitopodologo(cita.isSolicitopodologo());
			reagendado.setLocal(cita.getLocal());
			reagendado.setUsuarioreg(cita.getUsuarioreg());
			reagendado.setUsuarioact(cita.getUsuarioact());
			reagendado.setFechareg(cita.getFechareg());
			reagendado.setFechaact(cita.getFechaact());
			citaService.guardar(reagendado);
			
			//Cita nueva, pasará con los mismos datos pero con nueva fecha inicio y termino y como Pendiente.
			// Antes debo verificar si en la nueva fecha ya está registrada alguna cita libre con ese especialista, 
			// para así reemplazarla (actualizarla en esa cita), caso contrario insertar la cita.
			List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre
					(cita.getLocal().getId(), cita.getEspecialista().getId(), 
							fullFormatEn.parse(inicio), fullFormatEn.parse(termino));
			//Si la cantidad encontrada es igual 1, entonces ahí será reemplazado, caso contrario se guarda.
			if (buscados.size()==1) {
				Cita nuevo = buscados.get(0); // aquí reemplazamos la cita cambiada.
				nuevo.setEstatus("Pendiente");
				nuevo.setEspecialista(cita.getEspecialista());
				nuevo.setPaciente(cita.getPaciente());
				nuevo.setInicio( fullFormatEn.parse(inicio) );
				nuevo.setTermino( fullFormatEn.parse(termino) );
				nuevo.setProcedimiento(cita.getProcedimiento());
				nuevo.setSolicitopodologo(cita.isSolicitopodologo());
				nuevo.setLocal(cita.getLocal());
				nuevo.setUsuarioreg(cita.getUsuarioreg());
				nuevo.setUsuarioact(cita.getUsuarioact());
				nuevo.setFechareg(cita.getFechareg());
				nuevo.setFechaact(cita.getFechaact());
				//actualizamos: reemplazamos.
				citaService.guardar(nuevo);
			} else {
				//sino se encontró insertamos la cita.
				Cita nuevo = new Cita();
				nuevo.setEstatus("Pendiente");
				nuevo.setEspecialista(cita.getEspecialista());
				nuevo.setPaciente(cita.getPaciente());
				nuevo.setInicio( fullFormatEn.parse(inicio) );
				nuevo.setTermino( fullFormatEn.parse(termino) );
				nuevo.setProcedimiento(cita.getProcedimiento());
				nuevo.setSolicitopodologo(cita.isSolicitopodologo());
				nuevo.setLocal(cita.getLocal());
				nuevo.setUsuarioreg(cita.getUsuarioreg());
				nuevo.setUsuarioact(cita.getUsuarioact());
				nuevo.setFechareg(cita.getFechareg());
				nuevo.setFechaact(cita.getFechaact());
				//insert.
				citaService.guardar(nuevo);
			}
			/*Cita nuevo = new Cita();
			nuevo.setEstatus("Pendiente");
			nuevo.setEspecialista(cita.getEspecialista());
			nuevo.setPaciente(cita.getPaciente());
			nuevo.setInicio( fullFormatEn.parse(inicio) );
			nuevo.setTermino( fullFormatEn.parse(termino) );
			nuevo.setProcedimiento(cita.getProcedimiento());
			nuevo.setSolicitopodologo(cita.isSolicitopodologo());
			nuevo.setLocal(cita.getLocal());
			nuevo.setUsuarioreg(cita.getUsuarioreg());
			nuevo.setUsuarioact(cita.getUsuarioact());
			nuevo.setFechareg(cita.getFechareg());
			nuevo.setFechaact(cita.getFechaact());
			citaService.guardar(nuevo);*/
			
			//Cita inicial pasará a disponible sin paciente.
			cita.setEstatus("Disponible");
			cita.setPaciente(null);
			cita.setSolicitopodologo(false);
			cita.setProcedimiento("-");
			cita = citaService.guardar(cita);
			
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/citas/anular/{id}")
	public ResponseEntity<Cita> anularCita(@PathVariable int id) {
		try {
			//Obtenemos la cita.
			Cita cita = citaService.buscar(id);
			
			//Duplicamos y lo ponemos como Disponible sin paciente.
			Cita nuevo = new Cita();
			nuevo.setEstatus("Disponible");
			nuevo.setEspecialista(cita.getEspecialista());
			nuevo.setPaciente(null);
			nuevo.setInicio(cita.getInicio());
			nuevo.setTermino(cita.getTermino());
			nuevo.setProcedimiento("-");
			nuevo.setSolicitopodologo(false);
			nuevo.setLocal(cita.getLocal());
			nuevo.setUsuarioreg(cita.getUsuarioreg());
			nuevo.setUsuarioact(cita.getUsuarioact());
			nuevo.setFechareg(cita.getFechareg());
			nuevo.setFechaact(cita.getFechaact());
			citaService.guardar(nuevo);
			
			//La cita inicial lo Anulamos.
			cita.setEstatus("Anulado");
			cita = citaService.guardar(cita);
			
			return new ResponseEntity<>(cita, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/citas/bloquear/{especialistaid}/{inicio}/{localid}/{horainicio}/{horatermino}")
	public ResponseEntity<String> bloquearCitasDisponibleDeUnEspecialistaYFecha(@PathVariable int especialistaid, 
			@PathVariable String inicio, @PathVariable int localid, 
			@PathVariable String horainicio, @PathVariable String horatermino ) {
		try {
			Date dia = dateFormat.parse(inicio);
			Date ini = fullFormatEn2.parse( dateFormat.format(dia)+" "+horainicio);
			Date ter = fullFormatEn2.parse( dateFormat.format(dia)+" "+horatermino);
			//Obtenemos las citas sólo disponibles.
			List<Cita> citas = citaService
					.buscarCitasPorLocalYEspecialistaYIntervaloDeHoras(localid, especialistaid, ini, ter);
			
			for (Cita cita : citas) {
				cita.setEstatus("Bloqueado");
				citaService.guardar(cita);
			}
			
			String msj = "Fueron bloqueados "+citas.size()+" citas";
			return new ResponseEntity<>(msj, HttpStatus.OK);
		} catch (Exception e) { System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Busqueda de cita por local y fecha.
	@GetMapping(value = "/citas/busqueda/{local}/{fecha}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Cita>> obtenerPorLocalYFecha(@PathVariable int local, @PathVariable String fecha) {
		try {
			List<Cita> list = citaService.buscarTodosByLocalYFecha(local, dateFormat.parse(fecha));
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	//Busqueda de cita por local y fecha y especialidad de especialista.
	@GetMapping(value = "/citas/busqueda/{local}/{fecha}/{especialidad}") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Cita>> obtenerPorLocalYFechaYEspecialidad(@PathVariable int local, @PathVariable String fecha, 
			@PathVariable String especialidad) {
		try {
			List<Cita> list = citaService
					.buscarTodosByLocalYFechaYEspecialidad(local, dateFormat.parse(fecha), especialidad);
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Generdor de cita: fecha inicio y termino, local, tipo programador y lista de especialistas.
	@GetMapping(value = "/citas/generar_citas_dia/{fechainicio}/{fechatermino}/{sucursalid}/{programador}/{listaespecialistas}")
	public ResponseEntity<String> generarCitasPorDia(@PathVariable String fechainicio, @PathVariable String fechatermino, 
			@PathVariable int sucursalid, @PathVariable String programador, @PathVariable List<Integer> listaespecialistas) {

		try {
			//primer y ultimo día del mes a utilizar: yyyy-MM-dd.
			Date primer = fullFormat.parse(fechainicio); //dateFormat.parse(fechainicio);
			Date ultimo = fullFormat.parse(fechatermino); //dateFormat.parse(fechatermino);
			//intervalo de hora inicio y termino para cada día: HH:mm
			Date horainicio = hourFormat.parse( hourFormat.format(primer) );
			Date horatermino = hourFormat.parse( hourFormat.format(ultimo) );
			//Fecha actual.
			Date ahora = new Date();
			//Local
			Local local = localService.buscarporId(sucursalid)
					.orElseThrow(() -> new RuntimeException("Local con ID " + sucursalid + " no encontrado"));

			//recorrido desde el primer día hasta el último dia del rango de fechas seleccionados.
			while (primer.getTime() <= ultimo.getTime()) 
			{
				//Obtenemos el horario de un día en específico y definimos el procedimiento genérico.
				List<Date> horarios = null;
				String procedimiento = "";
				int minutos = 0;
				switch (programador) {
				case "podologo":
					minutos = 60;
					horarios = obtenerHorariosIntervaloIndicado(primer, horainicio, horatermino, minutos);
					//horarios = obtenerHorariosIntervalo1(dateFormat.format(primer));
					procedimiento = "-";
					break;
				/*case "adicionales":
					horarios = obtenerHorariosIntervalo1(dateFormat.format(primer));
					procedimiento = "-";
					minutos = 60;
					break;*/
				case "biomecanica":
					minutos = 40;
					horarios = obtenerHorariosIntervaloIndicado(primer, horainicio, horatermino, minutos);
					//horarios = obtenerHorariosIntervalo40(dateFormat.format(primer));
					procedimiento = programador.toUpperCase();
					break;
				case "terapia":
					minutos = 20;
					horarios = obtenerHorariosIntervaloIndicado(primer, horainicio, horatermino, minutos);
					//horarios = obtenerHorariosIntervalo20(dateFormat.format(primer));
					procedimiento = programador.toUpperCase();
					break;
				case "dental":
					minutos = 60;
					horarios = obtenerHorariosIntervaloIndicado(primer, horainicio, horatermino, minutos);
					//horarios = obtenerHorariosIntervalo1(dateFormat.format(primer));
					procedimiento = "-";
					break;
				}
				
				//recorrer la lista de id de especialista, y obtenemos el objeto.
				for (int id : listaespecialistas)
				{
					int validar = 0;
					Especialista e = especialistaService.buscar(id);
					//Creamos horarios desde inicio hasta el fin del dia laboral.
					for (Date hora : horarios)
					{
						//crear objeto de cita para cada especialista.
						Cita cita = new Cita();
						cita.setLocal(local);
						cita.setEstatus("Disponible");
						cita.setSolicitopodologo(false);
						cita.setEspecialista(e);
						cita.setPaciente(null);
						cita.setProcedimiento(procedimiento);
						cita.setFechareg(ahora);
						cita.setUsuarioact(0);
						cita.setInicio(hora);
						cita.setTermino( fullFormatEn2.parse(UtilitiesDate.sumarRestarMinutosAFecha(hora, minutos)) );
						//validamos que no haya cita con este horario ya registrado a un podologo.
						//validar = citaService.validarDuplicidad(local.getId(), e.getId(), cita.getInicio(), cita.getTermino()).size();
						validar = citaService.verificarSiExisteCitasAgregadas(local.getId(), e.getId(), cita.getInicio(), cita.getTermino()).size();
						if (validar==0)
							citaService.guardar(cita);
					}
				}
				//Pasamos a la siguiente fecha: Añadiendo un día.
				String f = UtilitiesDate.sumarRestarDiasAFecha(primer, 1);
				//System.out.println("Fecha String: "+f);
				primer = dateFormat.parse(f);
				//System.out.println("Fecha: "+primer);
				
			} //end while.
			
			
			return new ResponseEntity<>("Se generaron las citas para las fechas ingresadas.", HttpStatus.OK);
		
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/citas/generar_citas_dia_backup/{fecha}/{sucursal}")
	public ResponseEntity<String> generarCitasPorDia_backup(@PathVariable String fecha, @PathVariable int sucursal) {
		try {
			Date dia = dateFormat.parse(fecha);
			System.out.println("Fecha: "+dia);
			String f1 = UtilitiesDate.obtenerPrimerDia(dia);
			String f2 = UtilitiesDate.obtenerUltimoDia(dia);
			System.out.println("Primer día: "+f1);
			System.out.println("Ultimo día: "+f2);
			Date primer = dateFormat.parse(f1);
			Date ultimo = dateFormat.parse(f2);
			
			
			Date ahora = new Date();
			Local local = localService.buscarporId(sucursal)
					.orElseThrow(() -> new RuntimeException("Local con ID " + sucursal + " no encontrado"));

			System.out.println(local.toString());
			
			while (primer.getTime() <= ultimo.getTime()) 
			{
				//obtener lista de horas.
				List<Date> horarios = obtenerHorariosIntervalo1(dateFormat.format(primer));
				//recorrer por especialista pero aquellos que trabajen en dicha sucursal.
				List<Especialista> especialistas = especialistaService.buscarPorLocal(local.getId());
				System.out.println("Cantidad de especialistas: "+especialistas.size());
				for (Especialista e : especialistas)
				{
					//Creamos horarios desde inicio hasta el fin del dia laboral.
					for (Date hora : horarios)
					{
						//crear objeto de cita para cada especialista.
						Cita cita = new Cita();
						cita.setLocal(local);
						cita.setEstatus("Disponible");
						cita.setSolicitopodologo(false);
						cita.setEspecialista(e);
						cita.setPaciente(null);
						cita.setProcedimiento("-");
						cita.setFechareg(ahora);
						cita.setUsuarioact(0);
						cita.setInicio(hora);
						cita.setTermino( fullFormatEn.parse(UtilitiesDate.sumarRestarHorasAFecha(hora, 1)) );
						citaService.guardar(cita);
					}
				}
				//Pasamos a la siguiente fecha: Añadiendo un día.
				String f = UtilitiesDate.sumarRestarDiasAFecha(primer, 1);
				System.out.println("Fecha String: "+f);
				primer = dateFormat.parse(f);
				System.out.println("Fecha: "+primer);
			}
			
			return new ResponseEntity<>("Se generaron las citas para todo el mes.", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	public List<Date> obtenerHorariosIntervaloIndicado(Date diainicio, Date horainicio, Date horatermino, 
			int minutos) throws ParseException {
		
		List<Date> horarios = new COWArrayList<>(null);
		
		while (horainicio.getTime() < horatermino.getTime()) {
			//componemos fecha + hora.
			String componer = dateFormat.format(diainicio) +" "+ hourFormat.format(horainicio);
			horarios.add( fullFormatEn2.parse(componer) ); //formato: yyyy-MM-dd HH:mm
			horainicio = fullFormatEn2.parse( UtilitiesDate.sumarRestarMinutosAFecha(horainicio, minutos) );
		}
		
		return horarios;
	}
	
	/**
	 * Generar horarios con intervalos de 1 hora.
	 * @param dia
	 * @return
	 * @throws ParseException
	 */
	public List<Date> obtenerHorariosIntervalo1(String dia) throws ParseException {
		List<String> horas = new COWArrayList<>(null);
		//horas.add("08:00");
		horas.add("09:00");
		horas.add("10:00");
		horas.add("11:00");
		horas.add("12:00");
		horas.add("13:00");
		horas.add("14:00");
		horas.add("15:00");
		horas.add("16:00");
		horas.add("17:00");
		horas.add("18:00");
		horas.add("19:00");
		horas.add("20:00");
		horas.add("21:00");
		
		List<Date> horarios = new COWArrayList<>(null);
		
		for (String h : horas)
			horarios.add( fullFormatEn2.parse(dia+" "+h) ); //yyyy-MM-dd HH:mm
		return horarios;
	}
	
	/**
	 * Generar horarios con intervalos de 40 minutos.
	 * @param dia
	 * @return
	 * @throws ParseException
	 */
	public List<Date> obtenerHorariosIntervalo40(String dia) throws ParseException {
		List<String> horas = new COWArrayList<>(null);
		//horas.add("08:00");
		//horas.add("08:40");
		horas.add("09:00");
		horas.add("09:40");
		horas.add("10:20");
		horas.add("11:00");
		horas.add("11:40");
		horas.add("12:20");
		horas.add("13:00");
		horas.add("13:40");
		horas.add("14:20");
		horas.add("15:00");
		horas.add("15:40");
		horas.add("16:20");
		horas.add("17:00");
		horas.add("17:40");
		horas.add("18:20");
		horas.add("19:00");
		horas.add("19:40");
		horas.add("20:20");
		horas.add("21:00");
		//horas.add("21:20");
		
		List<Date> horarios = new COWArrayList<>(null);
		
		for (String h : horas)
			horarios.add( fullFormatEn2.parse(dia+" "+h) ); //yyyy-MM-dd HH:mm
		return horarios;
	}
	
	/**
	 * Generar horarios con intervalos de 20 minutos.
	 * @param dia
	 * @return
	 * @throws ParseException
	 */
	public List<Date> obtenerHorariosIntervalo20(String dia) throws ParseException {
		List<String> horas = new COWArrayList<>(null);
		//horas.add("08:00");
		//horas.add("08:20");
		//horas.add("08:40");
		horas.add("09:00");
		horas.add("09:20");
		horas.add("09:40");
		horas.add("10:00");
		horas.add("10:20");
		horas.add("10:40");
		horas.add("11:00");
		horas.add("11:20");
		horas.add("11:40");
		horas.add("12:00");
		horas.add("12:20");
		horas.add("12:40");
		horas.add("13:00");
		horas.add("13:20");
		horas.add("13:40");
		horas.add("14:00");
		horas.add("14:20");
		horas.add("14:40");
		horas.add("15:00");
		horas.add("15:20");
		horas.add("15:40");
		horas.add("16:00");
		horas.add("16:20");
		horas.add("16:40");
		horas.add("17:00");
		horas.add("17:20");
		horas.add("17:40");
		horas.add("18:00");
		horas.add("18:20");
		horas.add("18:40");
		horas.add("19:00");
		horas.add("19:20");
		horas.add("19:40");
		horas.add("20:00");
		horas.add("20:20");
		horas.add("20:40");
		horas.add("21:00");
		
		List<Date> horarios = new COWArrayList<>(null);
		
		for (String h : horas)
			horarios.add( fullFormatEn2.parse(dia+" "+h) ); //yyyy-MM-dd HH:mm
		return horarios;
	}
	
	
	@DeleteMapping(value = "/citas/{id}")
	public ResponseEntity<Cita> eliminarCita(@PathVariable int id) {
		Cita cita = citaService.buscar(id);
		if ( cita==null )
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else {
			citaService.eliminar(id);
			return new ResponseEntity<>(cita, HttpStatus.OK);
		}
		
	}
	
	
	@GetMapping(value = "/citas/validar_duplicidad/{localid}/{especialistaid}/{inicio}/{termino}/{citaid}")
	public ResponseEntity<String> validadDuplicidad(@PathVariable int localid, @PathVariable int especialistaid,
			@PathVariable String inicio, @PathVariable String termino, @PathVariable int citaid) {
		try {
			Date finicio = fullFormat.parse(inicio);			
			Date ftermino = fullFormat.parse(termino);
			String msj = "";
			List<Cita> encontradas = new ArrayList<>();
			System.out.println("horas: "+finicio+" , "+ftermino);
			if (citaid>0)
				encontradas = citaService.validarDuplicidadSinIncluirCitaActual(localid, especialistaid, finicio, ftermino, citaid);
			else
				encontradas = citaService.validarDuplicidad(localid, especialistaid, finicio, ftermino);
			
			if (encontradas.size()>0) {System.out.println("size: "+encontradas.size());
				msj = "El podólogo ya tiene citas agendadas. <br> De: <br>";
				for (Cita c : encontradas)
					msj = msj + "-> "+shortFormat.format(c.getInicio())+" - "+shortFormat.format(c.getTermino())+"<br>";
				System.out.println(msj);
			} else {
				msj = "El podólogo está disponible en ese horario.";
			}
			
			return new ResponseEntity<>(msj, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
