package com.orcronics.Software_Medico.service;

import java.util.List;

import com.orcronics.Software_Medico.entity.AtencionDetalle;
import com.orcronics.Software_Medico.repository.AtencionDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AtencionDetalleServiceJPA implements AtencionDetalleService {

	@Autowired
	private AtencionDetalleRepository atencionDetalleRepository;

	@Override
	public AtencionDetalle guardar(AtencionDetalle detalle) {
		return atencionDetalleRepository.save(detalle);
	}

	@Override
	public void eliminar(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AtencionDetalle buscar(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AtencionDetalle> buscarTodos(int atencionid) {
		return atencionDetalleRepository.findByAtencionId(atencionid);
	}
	
}
