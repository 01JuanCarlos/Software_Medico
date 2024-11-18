package com.orcronics.Software_Medico.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.Cita;
import com.orcronics.Software_Medico.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CitaServiceJPA implements CitaService {

	@Autowired
	private CitaRepository citaRepository;

	@Override
	public Cita guardar(Cita cita) {
		return citaRepository.save(cita);
	}

	@Override
	public void eliminar(int id) {
		citaRepository.deleteById(id);
	}

	@Override
	public Cita buscar(int id) {
		Optional<Cita> optional = citaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}
	
	@Override
	public void cerrarModificacionDeCitas(Date inicio) {
		citaRepository.cerrarModificacionDeCitas(inicio);	
	}

	@Override
	public List<Cita> buscarTodosByLocalYFecha(int id, Date inicio) {
		return citaRepository.findAllByLocalIdAndInicio(id, inicio);
	}
	
	@Override
	public List<Cita> buscarTodosByLocalYFechaYEspecialidad(int id, Date inicio, String especialidad) {
		return citaRepository.findAllByLocalIdAndInicioAndEspecialidad(id, inicio, especialidad);
	}
	
	@Override
	public List<Cita> buscarTodosByLocalYFechaYEspecialidadOtros(int id, Date inicio) {
		return citaRepository.findAllByLocalIdAndInicioAndEspecialidadOtros(id, inicio);
	}
	
	@Override
	public List<Cita> verificarSiExisteCitasAgregadas(int localid, int especialistaid, Date inicio, Date termino) {
		return citaRepository.verificarSiExistenCitasAgregadas(localid, especialistaid, inicio, termino);
	}

	@Override
	public List<Cita> validarDuplicidad(int localid, int especialistaid, Date inicio, Date termino) {
		return citaRepository.verificarDisponibilidadDePodologoEnCita(localid, especialistaid, inicio, termino);
	}
	
	@Override
	public List<Cita> validarDuplicidadSinIncluirCitaActual(int localid, int especialistaid, Date inicio, Date termino, int citaid) {
		return citaRepository.verificarDisponibilidadDePodologoEnCita(localid, especialistaid, inicio, termino, citaid);
	}

	@Override
	public List<Cita> buscarCitaCambiadaEnOtraCitaLibre(int localid, int especialistaid, Date inicio, Date termino) {
		return citaRepository.buscarCitaCambiadaEnOtraCitaLibre(localid, especialistaid, inicio, termino);
	}
	
	@Override
	public List<Cita> buscarCitaCambiadaEnOtraCitaYaRegistrada(int localid, int especialistaid, Date inicio, Date termino, int citaid) {
		return citaRepository.buscarCitaCambiadaEnOtraCitaYaRegistrada(localid, especialistaid, inicio, termino, citaid);
	}

	@Override
	public List<Cita> buscarCitasPorLocalYEspecialistaYIntervaloDeHoras(int localid, int especialistaid, Date inicio, Date termino) {
		return citaRepository.buscarCitasPorLocalYEspecialistaYIntervaloDeHoras(localid, especialistaid, inicio, termino);
	}
	
	
}
