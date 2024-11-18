package com.orcronics.Software_Medico.security.service;

import com.orcronics.Software_Medico.security.entity.Usuario;
import com.orcronics.Software_Medico.security.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByUserName(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByUserName(nombreUsuario);
    }


    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }

}
