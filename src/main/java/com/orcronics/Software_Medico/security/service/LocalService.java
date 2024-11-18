package com.orcronics.Software_Medico.security.service;

import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;


@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    // Método para crear o actualizar un Local
    public Local guardar(Local local) {
        return localRepository.save(local);
    }


    // Método para obtener un Local por ID
    public Optional<Local> buscarporId(Integer id) {
        return localRepository.findById(id);
    }


    public List<Local> buscarTodos() {
        return localRepository.findAllByOrderByIdAsc();
    }
    // Método para eliminar un Local por ID
    public void eliminar(Integer id) {
        localRepository.deleteById(id);
    }





}
