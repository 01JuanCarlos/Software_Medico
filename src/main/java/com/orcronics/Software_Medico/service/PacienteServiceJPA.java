package com.orcronics.Software_Medico.service;

import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.Paciente;
import com.orcronics.Software_Medico.interfaces.IDataTablePaciente;
import com.orcronics.Software_Medico.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PacienteServiceJPA implements PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;

	@Override
	public Paciente guardar(Paciente paciente) {
		return pacienteRepository.save(paciente);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Paciente buscar(int id) {
		Optional<Paciente> optional = pacienteRepository.findById(id);
		if (optional.isPresent() )
			return optional.get();
		return null;
	}

	@Override
	public List<Paciente> buscarTodos() {
		return pacienteRepository.findAll();
	}

	@Override
	public List<IDataTablePaciente> buscarPorParametro(String parametro) {
		return pacienteRepository.findByParameter(parametro);
	}
	
	@Override
	public List<IDataTablePaciente> buscarPorParametroYLocal(int localid, String parametro) {
		return pacienteRepository.findByParameterYLocal(localid, parametro);
	}
	
	@Override
	public List<IDataTablePaciente> buscarTodosPorLocalId(int localid) {
		return pacienteRepository.findByLocal(localid);
	}

	@Override
	public List<IDataTablePaciente> buscarTodos2() {
		return pacienteRepository.findTodos();
	}
	
	@Override
	public int getMaximoCodigoNroPorCodigoLetraPorLocal(String codigoletra, int localid) {
		return pacienteRepository.getMaximoCodigoNroByCodigoLetraPorLocal(codigoletra, localid);
	}
	
	
}
