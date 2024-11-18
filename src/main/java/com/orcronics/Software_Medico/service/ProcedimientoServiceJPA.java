package com.orcronics.Software_Medico.service;

import java.util.List;
import java.util.Optional;

import com.orcronics.Software_Medico.entity.Procedimiento;
import com.orcronics.Software_Medico.repository.ProcedimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProcedimientoServiceJPA implements ProcedimientoService {

	@Autowired
	private ProcedimientoRepository productoRepository;

	@Override
	public Procedimiento guardar(Procedimiento producto) {
		return productoRepository.save(producto);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Procedimiento buscar(int id) {
		Optional<Procedimiento> optional = productoRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Procedimiento> buscarTodos() {
		return productoRepository.findAll();
	}

	@Override
	public Procedimiento buscarPorDescripcion(String descripcion) {
		return productoRepository.findByDescripcion(descripcion);
	}
	
	@Override
	public Procedimiento buscarPorDescripcionYLocal(String descripcion, int localid) {
		return productoRepository.findByDescripcionAndLocalId(descripcion, localid);
	}

	@Override
	public List<Procedimiento> buscarPorActivo(boolean activo) {
		return productoRepository.findByActivo(activo);
	}
	
	@Override
	public List<Procedimiento> buscarPorLocal(int localid) {
		return productoRepository.findByLocalId(localid);
	}
	
	@Override
	public List<Procedimiento> buscarPorActivoYLocal(boolean activo, int localid) {
		return productoRepository.findByActivoAndLocalId(activo, localid);
	}

	@Override
	public List<Procedimiento> buscarPorTipoExistencia(int idexistencia) {
		return productoRepository.findByTipoexistenciaId(idexistencia);
	}
	
	
}
