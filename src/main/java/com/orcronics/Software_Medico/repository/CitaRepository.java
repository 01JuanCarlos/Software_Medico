
package com.orcronics.Software_Medico.repository;

import java.util.Date;
import java.util.List;

import com.orcronics.Software_Medico.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

	//Cerrar todas las citas a cerrado como true, dada la fecha del día.
	@Modifying //uso el modify porque es un update.
	@Query( value = " update cita "
			+ " set cerrado = 1 "
			+ " where date(inicio) = date(?1) and estatus not like 'ANULADO' ", nativeQuery = true)
	void cerrarModificacionDeCitas(Date inicio); //date inicio tendrá en sql formato: yyyy-mm-dd
		
	
	//Busqueda de lista de citas por local y fecha.
	@Query( value = " select * from cita "
			+ " where localid = ?1 and date(inicio) = date(?2) and estatus not like 'ANULADO' "
			+ " order by inicio, termino ", nativeQuery = true)
	List<Cita> findAllByLocalIdAndInicio(int localid, Date inicio); //date inicio tendrá en sql formato: yyyy-mm-dd
	
	
	//Busqueda de lista de citas por local, fecha y especialidad del podólogo.
	@Query( value = " select cita.* from cita "
			+ " inner join especialista on cita.especialistaid = especialista.id "
			+ " where cita.localid = ?1 and date(cita.inicio) = date(?2) and especialista.especialidad like ?3 and estatus not like 'ANULADO' "
			+ " order by cita.inicio, cita.termino ", nativeQuery = true)
	List<Cita> findAllByLocalIdAndInicioAndEspecialidad(int localid, Date inicio, String especialidad); //date inicio tendrá en sql formato: yyyy-mm-dd
	
	
	//Busqueda de lista de citas por local, fecha y especialidad OTROS.
	@Query( value = " select cita.* from cita "
			+ " inner join especialista on cita.especialistaid = especialista.id "
			+ " where cita.localid = ?1 and date(cita.inicio) = date(?2) "
			+ " and (especialista.especialidad not like 'PODOLOGO' and especialista.especialidad not like 'BIOMECANICA' and especialista.especialidad not like 'TERAPIA' ) "
			+ " and estatus not like 'ANULADO' "
			+ " order by cita.inicio, cita.termino ", nativeQuery = true)
	List<Cita> findAllByLocalIdAndInicioAndEspecialidadOtros(int localid, Date inicio); //date inicio tendrá en sql formato: yyyy-mm-dd
		

	//Verificamos desde una cita nueva por registrar.
	@Query( value = " select * from cita "
			+ " where localid = ?1 and especialistaid = ?2 "
			+ " and (estatus like 'DISPONIBLE' or estatus like 'PENDIENTE' or estatus like 'CONFIRMADO' or estatus like 'EN ATENCIÓN' or estatus like 'ATENDIDO') "
			/*+ " and (inicio between ?3 and ?4 "
			+ " or termino between ?3 and ?4 "
			+ " or ?3 between inicio and termino "
			+ " or ?4 between inicio and termino) "
			+ " order by inicio, termino "*/
			//+ " and pacienteid > 0 " //quité estatus disponible ya que me aseguro que tenga paciente asignado.
			+ " and ( "
			+ "    (inicio >= ?3 and inicio < ?4) "
			+ "    or (termino > ?3 and termino <= ?4) "
			+ "    or (inicio <= ?3 and termino >= ?4) "
			+ "  ) "
			+ " order by inicio, termino ", nativeQuery = true)
	List<Cita> verificarSiExistenCitasAgregadas(int localid, int especialistaid, Date inicio, Date termino);
	
	
	
	//Verificamos desde una cita nueva por registrar.
	@Query( value = " select * from cita "
			+ " where localid = ?1 and especialistaid = ?2 "
			+ " and (estatus like 'PENDIENTE' or estatus like 'CONFIRMADO' or estatus like 'EN ATENCIÓN') "
			/*+ " and (inicio between ?3 and ?4 "
			+ " or termino between ?3 and ?4 "
			+ " or ?3 between inicio and termino "
			+ " or ?4 between inicio and termino) "
			+ " order by inicio, termino "*/
			+ " and pacienteid > 0 " //quité estatus disponible ya que me aseguro que tenga paciente asignado.
			+ " and ( "
			+ "    (inicio >= ?3 and inicio < ?4) "
			+ "    or (termino > ?3 and termino <= ?4) "
			+ "    or (inicio <= ?3 and termino >= ?4) "
			+ "  ) "
			+ " order by inicio, termino ", nativeQuery = true)
	List<Cita> verificarDisponibilidadDePodologoEnCita(int localid, int especialistaid, Date inicio, Date termino);
	
	//Verificamos desde una cita ya registrada, es decir sin incluir ésta cita en la búsqueda.
	@Query( value = " select * from cita "
			+ " where id != ?5 and localid = ?1 and especialistaid = ?2 "
			+ " and (estatus like 'PENDIENTE' or estatus like 'CONFIRMADO' or estatus like 'EN ATENCIÓN') "
			/*+ " and (inicio between ?3 and ?4 "
			+ " or termino between ?3 and ?4 "
			+ " or ?3 between inicio and termino "
			+ " or ?4 between inicio and termino) "
			+ " order by inicio, termino "*/
			+ " and pacienteid > 0 " //quité estatus disponible ya que me aseguro que tenga paciente asignado.
			+ " and ( "
			+ "		(inicio >= ?3 and inicio < ?4) "
			+ "		or (termino > ?3 and termino <= ?4) "
			+ "		or (inicio <= ?3 and termino >= ?4) "
			+ "	 ) "
			+ " order by inicio, termino ", nativeQuery = true)
	List<Cita> verificarDisponibilidadDePodologoEnCita(int localid, int especialistaid, Date inicio, Date termino, int citaid);

	
	//Buscamos si una cita ya registrada a la cual se le está reprogramando de fecha o cambiando de podologo,
	// entonces si esa cita ya existe en una cita que esté LIBRE con dicho podologo y fechas (sin paciente asignado).
	// ya luego en el controller la cita cambiada reemplazará a la cita libre, y a la ves se eliminará la 1era cita.
	// Method OK.
	@Query( value = " select * from cita "
			+ " where localid = ?1 and especialistaid = ?2 "
			+ " and estatus like 'DISPONIBLE' "
			// lo comento porque al estar en disponible se supone que no tiene paciente asignado.
			//+ " and pacienteid == null "
			+ " and (inicio = ?3 and termino = ?4 ) ", nativeQuery = true)
			//+ " and ( "
			//+ "		(inicio >= ?3 and inicio < ?4) "
			//+ "		or (termino > ?3 and termino <= ?4) "
			//+ "		or (inicio <= ?3 and termino >= ?4) "
			//+ "	 )  ", nativeQuery = true)
	List<Cita> buscarCitaCambiadaEnOtraCitaLibre(int localid, int especialistaid, Date inicio, Date termino);
	
	
	// Method OK.
	@Query( value = " select * from cita "
			+ " where id != ?5 and localid = ?1 and especialistaid = ?2 "
			+ " and (estatus like 'DISPONIBLE' or estatus like 'PENDIENTE' or estatus like 'CONFIRMADO' "
			+ "		or estatus like 'EN ATENCIÓN'  or estatus like 'ATENDIDO') "
			// lo comento porque al estar en disponible se supone que no tiene paciente asignado.
			//+ " and pacienteid == null "
			+ " and ( inicio=?3 and termino=?4 ) ", nativeQuery = true)
			//+ " and ( "
			//+ "		(inicio >= ?3 and inicio < ?4) "
			//+ "		or (termino > ?3 and termino <= ?4) "
			//+ "		or (inicio <= ?3 and termino >= ?4) "
			//+ "	 )  ", nativeQuery = true)
	List<Cita> buscarCitaCambiadaEnOtraCitaYaRegistrada(int localid, int especialistaid, Date inicio, Date termino, int citaid);
	

	
	@Query( value = " select * from cita "
			+ " where localid = ?1 and especialistaid = ?2 "
			//+ "		and date(inicio) = date(?3) "
			+ "		and (inicio >= ?3 and termino<= ?4) "
			+ "		and estatus like 'DISPONIBLE' ", nativeQuery = true)
	List<Cita> buscarCitasPorLocalYEspecialistaYIntervaloDeHoras(int localid, int especialistaid, Date inicio, Date termino); //date inicio y terminao tendrás en sql formato: yyyy-mm-dd HH:ss
	
	
	
}
