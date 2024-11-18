package com.orcronics.Software_Medico.security.controller;

import com.orcronics.Software_Medico.dto.Mensaje;
import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.dto.JwtDto;
import com.orcronics.Software_Medico.security.dto.LoginUsuario;
import com.orcronics.Software_Medico.security.dto.NuevoUsuario;
import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.entity.Usuario;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import com.orcronics.Software_Medico.security.jwt.JwtProvider;
import com.orcronics.Software_Medico.security.service.LocalService;
import com.orcronics.Software_Medico.security.service.RolService;
import com.orcronics.Software_Medico.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private LocalService localService;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        }

        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getUserName())) {
            return new ResponseEntity<>(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        }

        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getUserName())) {
            return new ResponseEntity<>(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        }

        // Crear usuario
        Usuario usuario = new Usuario(
                nuevoUsuario.getUserName(),
                passwordEncoder.encode(nuevoUsuario.getPassword()),
                nuevoUsuario.isActivo(),
                nuevoUsuario.getNombres(),
                nuevoUsuario.getApellidoPaterno(),
                nuevoUsuario.getApellidoMaterno(),
                nuevoUsuario.getDni(),
                nuevoUsuario.getCelular(),
                nuevoUsuario.getDireccion(),
                nuevoUsuario.getNotas(),
                nuevoUsuario.getUsuarioReg(),
                nuevoUsuario.getFechaReg(),
                nuevoUsuario.getUsuarioAct(),
                nuevoUsuario.getFechaAct()
        );

        // Asignar roles
        Set<Rol> rolesSet = new HashSet<>();
        for (String rolNombre : nuevoUsuario.getRoles()) {
            RolNombre rolEnum;

            try {
                rolEnum = RolNombre.valueOf(rolNombre.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol " + rolNombre + " no es válido");
            }

            Rol rol = rolService.buscarporNombre(rolEnum)
                    .orElseThrow(() -> new RuntimeException("Rol " + rolEnum.name() + " no encontrado"));
            rolesSet.add(rol);
        }
        usuario.setRoles(rolesSet);

        // Asignar locales
        if (nuevoUsuario.getLocales() != null && !nuevoUsuario.getLocales().isEmpty()) {
            Set<Local> localesSet = new HashSet<>();
            for (Integer localId : nuevoUsuario.getLocales()) {
                Local local = localService.buscarporId(localId)
                        .orElseThrow(() -> new RuntimeException("Local con ID " + localId + " no encontrado"));
                localesSet.add(local);
            }
            usuario.setLocales(localesSet);
        }

        // Guardar usuario
        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    /*
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("campos mal puestos o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())){
            return  new ResponseEntity(new Mensaje("ese nombre ya existe"),HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail())){
            return  new ResponseEntity(new Mensaje("ese email ya existe"),HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = new Usuario(nuevoUsuario.getNombre(),nuevoUsuario.getNombreUsuario(),nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        if(nuevoUsuario.getRoles().contains("admin")){
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        }
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("usuario guardado"),HttpStatus.CREATED);
    }
*/



    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        // Verificar errores de validación
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        }

        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword())
        );

        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar el token JWT
        String jwt = jwtProvider.generateToken(authentication);

        // Obtener detalles del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Crear un objeto JwtDto para devolver el token y los detalles del usuario
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }



}
