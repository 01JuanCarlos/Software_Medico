package com.orcronics.Software_Medico.security.service;


import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import com.orcronics.Software_Medico.security.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> buscarporNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void guardar(Rol rol){
        rolRepository.save(rol);
    }

    public void eliminar(int id) {
        // TODO Auto-generated method stub
    }

    public Rol buscar(int id) {
        return null;
    }

    public List<Rol> buscarTodos() {
        return rolRepository.findAll();
    }




}
