package com.orcronics.Software_Medico.service;



import com.orcronics.Software_Medico.entity.HistoriaDetalle;
import com.orcronics.Software_Medico.repository.HistoriaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HistoriaDetalleServiceJpa implements HistoriaDetalleService {

	@Autowired
	private HistoriaDetalleRepository detalleRepository;

	@Override
	public HistoriaDetalle guardar(HistoriaDetalle detalle) {
		return detalleRepository.save(detalle);
	}

	@Override
	public void eliminar(int id) {
		detalleRepository.deleteById(id);
	}

	@Override
	public HistoriaDetalle buscar(int id) {
		Optional<HistoriaDetalle> optional = detalleRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<HistoriaDetalle> buscarTodos(int historiaid) {
		return detalleRepository.findByHistoriaId(historiaid);
	}

	
}
