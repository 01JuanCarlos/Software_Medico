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
@RequestMapping(value = "/api/app_backup")
public class CitaRestControllerMejoradoBackup {

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
	
	
	@PostMapping(value = "/citas_mejorado/{inicio}/{termino}/{pacienteid}")
	public ResponseEntity<Cita> crear(@RequestBody Cita cita, @PathVariable String inicio, @PathVariable String termino,
									  @PathVariable int pacienteid) {
		try {
			Cita ci = null;
			switch ( cita.getEstatus().toUpperCase() ) {
			case "DISPONIBLE":System.out.println("save as disponible");
				ci = saveAsDisponible(cita, inicio, termino);
				break;
			case "PENDIENTE":System.out.println("save as pendiente");
				ci = saveAsPendiente(cita, inicio, termino, pacienteid);
				break;
			case "CONFIRMADO":System.out.println("save as confirmado");
				ci = saveAsConfirmado(cita, inicio, termino, pacienteid);
				break;
			case "EN ATENCIÓN":System.out.println("save as en atención");
				ci = saveAsEnAtencion(cita, inicio, termino, pacienteid);
				break;
			case "ATENDIDO":System.out.println("save as atendido");
				ci = saveAsAtendido(cita, inicio, termino, pacienteid);
				break;
			default:
				break;
			}
			return new ResponseEntity<>(ci, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//guardar cita como "disponible". Se guarda por 1era vez la cita.
	public Cita saveAsDisponible(Cita cita, String inicio, String termino) {
		try {
			Date ahora = new Date();
			cita.setEstatus("DISPONIBLE");
			cita.setSolicitopodologo(false);
			cita.setPaciente(null);
			cita.setProcedimiento("-");
			cita.setFechareg(ahora);
			cita.setUsuarioreg(0);
			cita.setInicio( fullFormatEn.parse(inicio) );
			cita.setTermino( fullFormatEn.parse(termino) );
			
			return citaService.guardar(cita);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//guardar cita como "pendiente".
	public Cita saveAsPendiente(Cita cita, String inicio, String termino, int pacienteid) {
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(cita.getId());
			//validar si el especialista sigue siendo el mismo.
			if ( obj.getEspecialista().getId() == cita.getEspecialista().getId() )
			{
				//seteo con los nuevos valores.
				obj.setEstatus("PENDIENTE");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				
			} else { //si son diferentes.

				//seteo la cita del antiguo especialista como "disponible".
				Cita obj2 = new Cita();
				obj2.setEstatus("DISPONIBLE");
				obj2.setLocal(obj.getLocal());
				obj2.setEspecialista(obj.getEspecialista());
				obj2.setSolicitopodologo(false);
				obj2.setPaciente(null);
				obj2.setProcedimiento("-");
				obj2.setFechareg(ahora);
				obj2.setUsuarioreg(0);
				obj2.setInicio( fullFormatEn.parse(inicio) );
				obj2.setTermino( fullFormatEn.parse(termino) );
				citaService.guardar(obj2);
				
				//seteo con los nuevos valores para el nuevo especialista.
				obj.setEstatus("PENDIENTE");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				
			}
			return citaService.guardar(obj);
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//guardar cita como "confirmado".
	public Cita saveAsConfirmado(Cita cita, String inicio, String termino, int pacienteid) {
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(cita.getId());
			//validar si el especialista sigue siendo el mismo.
			if ( obj.getEspecialista().getId() == cita.getEspecialista().getId() )
			{
				//seteo con los nuevos valores.
				obj.setEstatus("CONFIRMADO");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				
			} else { //si son diferentes.
				
				//verifico que la cita del antiguo especialista (desde la bd), no esté aún registrado.
				List<Cita> registrados = citaService.buscarCitaCambiadaEnOtraCitaYaRegistrada(obj.getLocal().getId(), obj.getEspecialista().getId(), obj.getInicio(), obj.getTermino(), obj.getId());
				System.out.println("size registrados: "+registrados.size());
				//si en la búsqueda no se encontró ningún registrado, se procede a registrar como disponible.
				if ( registrados.size()==0 )
				{System.out.println("guardando como disponible a: "+obj.getEspecialista().getNombres());
					//seteo la cita del antiguo especialista como "disponible".
					Cita obj2 = new Cita();
					obj2.setEstatus("DISPONIBLE");
					obj2.setLocal(obj.getLocal());
					obj2.setEspecialista(obj.getEspecialista());
					obj2.setSolicitopodologo(false);
					obj2.setPaciente(null);
					obj2.setProcedimiento("-");
					obj2.setFechareg(ahora);
					obj2.setUsuarioreg(0);
					obj2.setInicio( obj.getInicio() );
					obj2.setTermino( obj.getTermino() );
					citaService.guardar(obj2);
				}
				
				//verifico que la cita con el nuevo especialista, no tengo una cita ya registrada como disponible.
				List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), cita.getEspecialista().getId(), fullFormat.parse(inicio) , fullFormat.parse(termino) );
				System.out.println(cita.toString());
				System.out.println("size buscados: "+buscados.size());
				//si existe 1 cita como disponible, entonces lo eliminamos.
				if ( buscados.size()==1 ) {
					Cita c = buscados.get(0);//objeto a eliminar y será reemplazado luego por el confirmado.
					citaService.eliminar( c.getId() ); System.out.println("eliminando id: "+c.getId());
				}
				
				//seteo con los nuevos valores.
				obj.setEstatus("CONFIRMADO");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				System.out.println("guardando como confirmado a: "+obj.getEspecialista().getNombres());
			}
			
			return citaService.guardar(obj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//guardar cita como "en atención".
	public Cita saveAsEnAtencion(Cita cita, String inicio, String termino, int pacienteid) {
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(cita.getId());
			//validar si el especialista sigue siendo el mismo.
			if ( obj.getEspecialista().getId() == cita.getEspecialista().getId() )
			{	
				//seteo con los nuevos valores.
				obj.setEstatus("EN ATENCIÓN");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				
			} else { //si son diferentes.
				
				//verifico que la cita del antiguo especialista (desde la bd), no esté aún registrado.
				List<Cita> registrados = citaService.buscarCitaCambiadaEnOtraCitaYaRegistrada(obj.getLocal().getId(), obj.getEspecialista().getId(), obj.getInicio(), obj.getTermino(), obj.getId());
				System.out.println("size registrados: "+registrados.size());
				//si en la búsqueda no se encontró ningún registrado, se procede a registrar como disponible.
				if ( registrados.size()==0 )
				{System.out.println("guardando como disponible a: "+obj.getEspecialista().getNombres());
					//seteo la cita del antiguo especialista como "disponible".
					Cita obj2 = new Cita();
					obj2.setEstatus("DISPONIBLE");
					obj2.setLocal(obj.getLocal());
					obj2.setEspecialista(obj.getEspecialista());
					obj2.setSolicitopodologo(false);
					obj2.setPaciente(null);
					obj2.setProcedimiento("-");
					obj2.setFechareg(ahora);
					obj2.setUsuarioreg(0);
					obj2.setInicio( obj.getInicio() );
					obj2.setTermino( obj.getTermino() );
					citaService.guardar(obj2);
				}
				
				//verifico que la cita con el nuevo especialista, no tengo una cita ya registrada como disponible.
				List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), cita.getEspecialista().getId(), fullFormat.parse(inicio) , fullFormat.parse(termino) );
				System.out.println(cita.toString());
				System.out.println("size buscados: "+buscados.size());
				//si existe 1 cita como disponible, entonces lo eliminamos.
				if ( buscados.size()==1 ) {
					Cita c = buscados.get(0);//objeto a eliminar y será reemplazado luego por el confirmado.
					citaService.eliminar( c.getId() ); System.out.println("eliminando id: "+c.getId());
				}
				
				//seteo con los nuevos valores.
				obj.setEstatus("EN ATENCIÓN");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente( pacienteService.buscar(pacienteid) );
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio( fullFormatEn.parse(inicio) );
				obj.setTermino( fullFormatEn.parse(termino) );
				
				System.out.println("guardando como confirmado a: "+obj.getEspecialista().getNombres());
			}
			
			return citaService.guardar(obj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
	
	//guardar cita como "atendido".
	public Cita saveAsAtendido(Cita cita, String inicio, String termino, int pacienteid) {		
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(cita.getId());
			//validar si el especialista sigue siendo el mismo.
			if ( obj.getEspecialista().getId() == cita.getEspecialista().getId() )
			{	
				// seteo con los nuevos valores.
				obj.setEstatus("ATENDIDO");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente(pacienteService.buscar(pacienteid));
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio(fullFormatEn.parse(inicio));
				obj.setTermino(fullFormatEn.parse(termino));
				
			} else { //si son diferentes.
				
				//verifico que la cita del antiguo especialista (desde la bd), no esté aún registrado.
				List<Cita> registrados = citaService.buscarCitaCambiadaEnOtraCitaYaRegistrada(obj.getLocal().getId(), obj.getEspecialista().getId(), obj.getInicio(), obj.getTermino(), obj.getId());
				System.out.println("size registrados: "+registrados.size());
				//si en la búsqueda no se encontró ningún registrado, se procede a registrar como disponible.
				if ( registrados.size()==0 )
				{System.out.println("guardando como disponible a: "+obj.getEspecialista().getNombres());
					//seteo la cita del antiguo especialista como "disponible".
					Cita obj2 = new Cita();
					obj2.setEstatus("DISPONIBLE");
					obj2.setLocal(obj.getLocal());
					obj2.setEspecialista(obj.getEspecialista());
					obj2.setSolicitopodologo(false);
					obj2.setPaciente(null);
					obj2.setProcedimiento("-");
					obj2.setFechareg(ahora);
					obj2.setUsuarioreg(0);
					obj2.setInicio( obj.getInicio() );
					obj2.setTermino( obj.getTermino() );
					citaService.guardar(obj2);
				}
				
				//verifico que la cita con el nuevo especialista, no tengo una cita ya registrada como disponible.
				List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(cita.getLocal().getId(), cita.getEspecialista().getId(), fullFormat.parse(inicio) , fullFormat.parse(termino) );
				System.out.println(cita.toString());
				System.out.println("size buscados: "+buscados.size());
				//si existe 1 cita como disponible, entonces lo eliminamos.
				if ( buscados.size()==1 ) {
					Cita c = buscados.get(0);//objeto a eliminar y será reemplazado luego por el confirmado.
					citaService.eliminar( c.getId() ); System.out.println("eliminando id: "+c.getId());
				}
				
				// seteo con los nuevos valores.
				obj.setEstatus("ATENDIDO");
				obj.setSolicitopodologo(cita.isSolicitopodologo());
				obj.setPaciente(pacienteService.buscar(pacienteid));
				obj.setEspecialista(cita.getEspecialista());
				obj.setProcedimiento(cita.getProcedimiento());
				obj.setFechaact(ahora);
				obj.setUsuarioact(0);
				obj.setInicio(fullFormatEn.parse(inicio));
				obj.setTermino(fullFormatEn.parse(termino));
				
				System.out.println("guardando como confirmado a: "+obj.getEspecialista().getNombres());
			}
			
			return citaService.guardar(obj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//guardar cita como "re-agendado".
	@GetMapping(value = "/citas_mejorado/reagendar/{id}/{inicio}/{termino}")
	public Cita saveAsReAgendado(@PathVariable int id, @PathVariable String inicio, @PathVariable String termino) {		
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(id);
			
			// seteo los valores para que quede como registro.
			Cita reagendado = new Cita();
			reagendado.setEstatus("RE-AGENDADO");
			reagendado.setLocal(obj.getLocal());
			reagendado.setEspecialista(obj.getEspecialista());
			reagendado.setSolicitopodologo(false);
			reagendado.setPaciente(null);
			reagendado.setProcedimiento("-");
			reagendado.setFechareg(ahora);
			reagendado.setUsuarioreg(0);
			reagendado.setInicio(obj.getInicio());
			reagendado.setTermino(obj.getTermino());
			citaService.guardar(reagendado);
			
			/*
			// verifico que la cita del antiguo especialista (desde la bd), no esté aún
			// registrado.
			List<Cita> registrados = citaService.buscarCitaCambiadaEnOtraCitaYaRegistrada(obj.getLocal().getId(),
					obj.getEspecialista().getId(), obj.getInicio(), obj.getTermino(), obj.getId());
			System.out.println("size registrados: " + registrados.size());
			// si en la búsqueda no se encontró ningún registrado, se procede a registrar
			// como disponible.
			if (registrados.size() == 0) {
				System.out.println("guardando como disponible a: " + obj.getEspecialista().getNombres());
				// seteo la cita del antiguo especialista como "disponible".
				Cita obj2 = new Cita();
				obj2.setEstatus("DISPONIBLE");
				obj2.setLocal(obj.getLocal());
				obj2.setEspecialista(obj.getEspecialista());
				obj2.setSolicitopodologo(false);
				obj2.setPaciente(null);
				obj2.setProcedimiento("-");
				obj2.setFechareg(ahora);
				obj2.setUsuarioreg(0);
				obj2.setInicio(obj.getInicio());
				obj2.setTermino(obj.getTermino());
				citaService.guardar(obj2);
			}*/

			// verifico que la cita con el nuevo especialista, no tengo una cita ya
			// registrada como disponible.
			List<Cita> buscados = citaService.buscarCitaCambiadaEnOtraCitaLibre(obj.getLocal().getId(),
					obj.getEspecialista().getId(), fullFormat.parse(inicio), fullFormat.parse(termino));
			System.out.println(obj.toString());
			System.out.println("size buscados: " + buscados.size());
			// si existe 1 cita como disponible, entonces lo eliminamos.
			if (buscados.size() == 1) {
				Cita c = buscados.get(0);// objeto a eliminar y será reemplazado luego por el confirmado.
				citaService.eliminar(c.getId());
				System.out.println("eliminando id: " + c.getId());
			}

			// seteo con los nuevos valores.
			Cita nuevo = new Cita();
			nuevo.setEstatus(obj.getEstatus());
			nuevo.setLocal(obj.getLocal());
			nuevo.setSolicitopodologo(obj.isSolicitopodologo());
			nuevo.setPaciente(obj.getPaciente());
			nuevo.setEspecialista(obj.getEspecialista());
			nuevo.setProcedimiento(obj.getProcedimiento());
			nuevo.setFechareg(ahora);
			nuevo.setUsuarioreg(0);
			nuevo.setInicio(fullFormatEn.parse(inicio));
			nuevo.setTermino(fullFormatEn.parse(termino));
			citaService.guardar(nuevo);
			
			
			obj.setEstatus("DISPONIBLE");
			obj.setPaciente(null);
			obj.setSolicitopodologo(false);
			obj.setProcedimiento("-");
			
			return citaService.guardar(obj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//guardar cita como "anulado".
	@GetMapping(value = "/citas_mejorado/anular/{id}")
	public Cita saveAsAnulado(@PathVariable int id) {		
		try {
			Date ahora = new Date();
			//obtengo el objeto desde la bd,
			Cita obj = citaService.buscar(id);

			// seteo para anulado.
			obj.setEstatus("ANULADO");
			obj.setFechaact(ahora);
			obj.setUsuarioact(0);

			// seteo la cita del antiguo cita como "disponible".
			Cita obj2 = new Cita();
			obj2.setEstatus("DISPONIBLE");
			obj2.setLocal(obj.getLocal());
			obj2.setEspecialista(obj.getEspecialista());
			obj2.setSolicitopodologo(false);
			obj2.setPaciente(null);
			obj2.setProcedimiento("-");
			obj2.setFechareg(ahora);
			obj2.setUsuarioreg(0);
			obj2.setInicio(obj.getInicio());
			obj2.setTermino(obj.getTermino());
			citaService.guardar(obj2);

			return citaService.guardar(obj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
		
	
	
	//OK
	@GetMapping(value = "/citas_mejorado/bloquear/{especialistaid}/{inicio}/{localid}/{horainicio}/{horatermino}")
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
	
	
	//OK: Busqueda de cita por local y fecha.
	@GetMapping(value = "/citas_mejorado/busqueda/{local}/{fecha}")
	public ResponseEntity<?> obtenerPorLocalYFecha(@PathVariable int local, @PathVariable String fecha) {
		try {
			List<Cita> list = citaService.buscarTodosByLocalYFecha(local, dateFormat.parse(fecha));
			System.out.println(list.size());
			if (list.isEmpty())
				return new ResponseEntity<>("No existen registros.", HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	//OK: Busqueda de cita por local y fecha y especialidad de especialista.
	@GetMapping(value = "/citas_mejorado/busqueda/{local}/{fecha}/{especialidad}") //también @GetMapping("/usuarios")
	public ResponseEntity<?> obtenerPorLocalYFechaYEspecialidad(@PathVariable int local, @PathVariable String fecha, 
			@PathVariable String especialidad) {
		try {
			List<Cita> list = citaService
					.buscarTodosByLocalYFechaYEspecialidad(local, dateFormat.parse(fecha), especialidad);
			
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT); //return 204
			else
				return new ResponseEntity<>(list, HttpStatus.OK); //return 200
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //return 500
		}
	}
	
	//Generdor de cita: fecha inicio y termino, local, tipo programador y lista de especialistas.
	@GetMapping(value = "/citas_mejorado/generar_citas_dia/{fechainicio}/{fechatermino}/{sucursalid}/{programador}/{listaespecialistas}")
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
						cita.setEstatus("DISPONIBLE");
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
	
	
	@GetMapping(value = "/citas_mejorado/generar_citas_dia_backup/{fecha}/{sucursal}")
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
	
	
	//OK
	@DeleteMapping(value = "/citas_mejorado/{id}")
	public ResponseEntity<Cita> eliminarCita(@PathVariable int id) {
		Cita cita = citaService.buscar(id);
		if ( cita==null )
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else {
			citaService.eliminar(id);
			return new ResponseEntity<>(cita, HttpStatus.OK);
		}
		
	}
	
	
	@GetMapping(value = "/citas_mejorado/validar_duplicidad/{localid}/{especialistaid}/{inicio}/{termino}/{citaid}")
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
