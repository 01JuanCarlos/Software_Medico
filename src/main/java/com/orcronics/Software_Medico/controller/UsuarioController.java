package com.orcronics.Software_Medico.controller;


import com.orcronics.Software_Medico.dto.Mensaje;
import com.orcronics.Software_Medico.security.dto.NuevoUsuario;
import com.orcronics.Software_Medico.security.dto.UsuarioDTO;
import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.entity.Usuario;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import com.orcronics.Software_Medico.security.repository.UsuarioRepository;
import com.orcronics.Software_Medico.security.service.LocalService;
import com.orcronics.Software_Medico.security.service.RolService;
import com.orcronics.Software_Medico.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolService rolService;
    @Autowired
    private LocalService localService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }



    // Endpoint para eliminar un usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();  // Retorna un 204 No Content si la eliminación es exitosa
        } catch (Exception e) {
            // Puedes manejar la excepción y devolver un mensaje adecuado
            return ResponseEntity.status(500).build();  // Retorna un 500 si ocurre algún error
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
