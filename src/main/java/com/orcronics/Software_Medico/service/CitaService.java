package com.orcronics.Software_Medico.service;

import com.orcronics.Software_Medico.entity.Cita;

import java.util.Date;
import java.util.List;


public interface CitaService {

	Cita guardar(Cita cita);
	void eliminar(int id);
	Cita buscar(int id);
	void cerrarModificacionDeCitas(Date inicio);
	List<Cita> buscarTodosByLocalYFecha(int id, Date inicio);
	List<Cita> buscarTodosByLocalYFechaYEspecialidad(int id, Date inicio, String especialidad);
	List<Cita> buscarTodosByLocalYFechaYEspecialidadOtros(int id, Date inicio);
	List<Cita> verificarSiExisteCitasAgregadas(int localid, int especialistaid, Date inicio, Date termino);
	List<Cita> validarDuplicidad(int localid, int especialistaid, Date inicio, Date termino);
	List<Cita> validarDuplicidadSinIncluirCitaActual(int localid, int especialistaid, Date inicio, Date termino, int citaid);
	List<Cita> buscarCitaCambiadaEnOtraCitaLibre(int localid, int especialistaid, Date inicio, Date termino);
	List<Cita> buscarCitaCambiadaEnOtraCitaYaRegistrada(int localid, int especialistaid, Date inicio, Date termino, int citaid);
	List<Cita> buscarCitasPorLocalYEspecialistaYIntervaloDeHoras(int localid, int especialistaid, Date inicio, Date termino);
}
