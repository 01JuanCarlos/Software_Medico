package com.orcronics.Software_Medico.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.orcronics.Software_Medico.entity.Paciente;
import com.orcronics.Software_Medico.entity.PacienteMultimedia;
import com.orcronics.Software_Medico.interfaces.IDataTablePaciente;
import com.orcronics.Software_Medico.service.PacienteMultimediaService;
import com.orcronics.Software_Medico.service.PacienteService;
import com.orcronics.Software_Medico.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*") //por defecto acepta orígenes sólo de localhost:8080
@RestController //@RestController reemplaza a @Controller.
@RequestMapping(value = "/api/app")
public class PacienteRestController {

	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private PacienteMultimediaService multimediaService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private ResourceLoader resourceLoader; //permite cargar los recursos de diferente fuentes.
	@Autowired
	private Environment env; //para acceder a las propiedades definidas el application.properties.
	
	
	@PostMapping(value = "/pacientes/{fechanacimiento}")
	public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente, @PathVariable String fechanacimiento) {
		try {
			String codigoletra = "";
			int codigonro = 0;
			if (paciente.getId()==0) {
				codigoletra = paciente.getApellidos().trim().substring(0, 2);
				switch (codigoletra) {
				case "CH":
					break;
				case "LL":
					break;
				default:
					codigoletra = String.valueOf(paciente.getApellidos().trim().charAt(0)); 
					break;
				}
				//trim elimina espacios delante y detrás.
				codigonro = pacienteService
						.getMaximoCodigoNroPorCodigoLetraPorLocal(codigoletra, paciente.getLocal().getId()) + 1;
				paciente.setCodigoLetra(codigoletra);
				paciente.setCodigoNro(codigonro);
			}
			if (fechanacimiento.equals("null"))
				paciente.setFechaNacimiento(null);
			else
				paciente.setFechaNacimiento( dateFormat.parse(fechanacimiento) );
			paciente.setNombres(paciente.getNombres().trim()); //quitamos espacios.
			paciente.setApellidos(paciente.getApellidos().trim()); //quitamos espacios.
			
			Paciente pac = pacienteService.guardar(paciente);	
			return new ResponseEntity<>(pac, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Creamos al paciente, sin asignarlo codigo de letra y numero, sólo lo registramos y en codigo letra le ponemos "NUEVO".
	 * @param paciente
	 * @return
	 */
	@PostMapping(value = "/pacientes/desde_cita")
	public ResponseEntity<Paciente> crearDesdeCita(@RequestBody Paciente paciente) {
		try {
			paciente.setCodigoLetra("NUEVO");
			paciente.setCodigoNro(0);
			paciente.setFechaNacimiento(null);
			paciente.setNombres(paciente.getNombres().trim()); //quitamos espacios.
			paciente.setApellidos(paciente.getApellidos().trim()); //quitamos espacios.
			
			Paciente pac = pacienteService.guardar(paciente);	
			return new ResponseEntity<>(pac, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/updateTelCel/{id}/{telefono}/{celular}")
	public ResponseEntity<String> updateTelCel(@PathVariable int id, @PathVariable String telefono, 
			@PathVariable String celular) {
		try {
			Paciente paciente = pacienteService.buscar(id);
			paciente.setTelefono(telefono);
			paciente.setCelular(celular);
			pacienteService.guardar(paciente);
			return new ResponseEntity<>("!Teléfono y celular fueron actualizados¡", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes") //también @GetMapping("/usuarios")
	public ResponseEntity<List<Paciente>> obtenerTodos() {
		try {
			List<Paciente> list = pacienteService.buscarTodos(); System.out.println("Tamaña: "+list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/porlocal/{localid}")
	public ResponseEntity<List<IDataTablePaciente>> obtenerTodosPorLocal(@PathVariable int localid) {
		try {
			List<IDataTablePaciente> list = pacienteService.buscarTodosPorLocalId(localid); 
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/todos")
	public ResponseEntity<List<IDataTablePaciente>> obtenerTodos2() {
		try {
			List<IDataTablePaciente> list = pacienteService.buscarTodos2(); 
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/buscarPorParametroYLocal")
	public ResponseEntity<List<IDataTablePaciente>> buscarPacientePorLocal(@RequestParam int localid, 
			@RequestParam String search, @RequestParam boolean otros) {
		//System.out.println("local y param: "+localid+" / "+search);
		try {
			List<IDataTablePaciente> list = null;
			if (otros) //buscamos los pacientes de todos los locales.
				list = pacienteService.buscarPorParametro(search);
			else //buscamos pacientes de un solo local.
				list = pacienteService.buscarPorParametroYLocal(localid, search);
			//System.out.println("Size bsuqueda: "+list.size());
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/{id}")
	public ResponseEntity<Paciente> obtenerPorId(@PathVariable int id) {
		try {
			Paciente paciente = pacienteService.buscar(id);
			if (paciente == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(paciente, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// ***********
	// MULTIMEDIA:
	// ***********
	
	
	@PostMapping(value = "/pacientes/multimedia/upload")
	public ResponseEntity<PacienteMultimedia> guardarVariosMultimedia(int pacienteid, List<MultipartFile> archivos) {
		try {
			String rutaMultimedia = resourceLoader.getResource(env.getProperty("multimedia.paciente")).getFile().getAbsolutePath();
			Paciente paciente = pacienteService.buscar(pacienteid);
			PacienteMultimedia multimedia;
			Date fecha = new Date();
			
			for (MultipartFile file : archivos) {
				if (!file.isEmpty()) {
					String nombre = Utilities.subirArchivo(file, paciente.getLocal().getId()+"_"+paciente.getId(), rutaMultimedia);
					if (nombre!=null) { 
						multimedia = new PacienteMultimedia();
						multimedia.setPaciente(paciente);
						multimedia.setFichero(nombre);
						File fichero = new File(rutaMultimedia+"/"+nombre);
						String tipo = Files.probeContentType(fichero.toPath());
						multimedia.setTipo(tipo);
						multimedia.setSize(Utilities.convertirBytes(file.getSize()));
						multimedia.setFecha(fecha);
						
						multimediaService.guardar(multimedia);
					}
				}
			}
			return new ResponseEntity<>(null, HttpStatus.OK);
		
		} catch (Exception e) {
			System.out.println("Error upload: "+e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/pacientes/multimedia/upload-base64")
	public ResponseEntity<PacienteMultimedia> guardarPhotoMultimedia(int pacienteid, MultipartFile photo) {
		try {
			String rutaMultimedia = resourceLoader.getResource(env.getProperty("multimedia.paciente")).getFile().getAbsolutePath();
			Paciente paciente = pacienteService.buscar(pacienteid);
			PacienteMultimedia multimedia;
			Date fecha = new Date();
			
			//for (MultipartFile file : archivos) {
				if (!photo.isEmpty()) {
					String nombre = Utilities.subirArchivoConExtensionPNG(photo, paciente.getLocal().getId()+"_"+paciente.getId(), rutaMultimedia);
					if (nombre!=null) { 
						multimedia = new PacienteMultimedia();
						multimedia.setPaciente(paciente);
						multimedia.setFichero(nombre);
						File fichero = new File(rutaMultimedia+"/"+nombre);
						String tipo = Files.probeContentType(fichero.toPath());
						multimedia.setTipo(tipo);
						multimedia.setSize(Utilities.convertirBytes(photo.getSize()));
						multimedia.setFecha(fecha);
						
						multimediaService.guardar(multimedia);
					}
				}
			//}
			return new ResponseEntity<>(null, HttpStatus.OK);
		
		} catch (Exception e) {
			System.out.println("Error upload: "+e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/pacientes/multimedia/get_all/{pacienteid}")
	public ResponseEntity<List<PacienteMultimedia>> obtenerTodosMultimediaPorPaciente(@PathVariable int pacienteid) {
		try {
			List<PacienteMultimedia> list = multimediaService.buscarTodos(pacienteid);
			if (list.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/pacientes/autocodificar/{id}")
	public ResponseEntity<Paciente> autocodificarPaciente(@PathVariable int id) {
		try {
			Paciente paciente = pacienteService.buscar(id);
			String codigoletra = "";
			int codigonro = 0;
			if (paciente.getCodigoLetra().equals("NUEVO")) 
			{
				codigoletra = String.valueOf(paciente.getApellidos().trim().charAt(0)); //trim elimina espacios delante y detrás.
				codigonro = pacienteService
						.getMaximoCodigoNroPorCodigoLetraPorLocal(codigoletra, paciente.getLocal().getId()) + 1;
				paciente.setCodigoLetra(codigoletra);
				paciente.setCodigoNro(codigonro);
				//guardamos los cambios del codigo de historia.
				Paciente pac = pacienteService.guardar(paciente);	
				return new ResponseEntity<>(pac, HttpStatus.OK);
			} else
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
