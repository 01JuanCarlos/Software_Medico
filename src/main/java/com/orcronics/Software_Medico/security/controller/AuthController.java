package com.orcronics.Software_Medico.security.controller;

import com.orcronics.Software_Medico.dto.Mensaje;
import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.dto.JwtDto;
import com.orcronics.Software_Medico.security.dto.LoginUsuario;
import com.orcronics.Software_Medico.security.dto.NuevoUsuario;
import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.entity.Usuario;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import com.orcronics.Software_Medico.security.jwt.JwtEntryPoint;
import com.orcronics.Software_Medico.security.jwt.JwtProvider;
import com.orcronics.Software_Medico.security.repository.UsuarioRepository;
import com.orcronics.Software_Medico.security.service.LocalService;
import com.orcronics.Software_Medico.security.service.RolService;
import com.orcronics.Software_Medico.security.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RolService rolService;
    @Autowired
    private LocalService localService;



    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByUserName(nuevoUsuario.getUserName())) {
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
        try {
             usuarioService.save(usuario);
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de la excepción
        }


        return new ResponseEntity<>(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }


    // Endpoint para verificar si el correo ya está registrado
   /* @GetMapping("/verificar-correo")
    public ResponseEntity<Boolean> verificarCorreo(@RequestParam("email") String email) {
        boolean existe = usuarioService.existsByUserName(email);  // Llama al servicio para verificar el correo
        return ResponseEntity.ok(existe);  // Devuelve un booleano indicando si el correo existe o no
    }*/


    @GetMapping("/existsByDni/{dni}")
    public ResponseEntity<Boolean> existsByDni(@PathVariable String dni) {
        boolean existin = usuarioService.existsByDni(dni);
        return new ResponseEntity<>(existin,HttpStatus.OK); // Devuelve true o false
    }

    @GetMapping("/existsByUserName/{userName}")
    public ResponseEntity<Boolean> existsByUserName(@PathVariable String userName) {
        boolean exists = usuarioService.existsByUserName(userName);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }


  /*  @GetMapping("/validar-dni")
    public ResponseEntity<Boolean> validarDni(
            @RequestParam String dni,
            @RequestParam(required = false) Long excludeUserId) {

        boolean dniEsUnico = usuarioService.validarDniUnico(dni, excludeUserId);
        return ResponseEntity.ok(!dniEsUnico); // Devuelve true si el DNI está disponible
    }*/
  @GetMapping("/validar-dni")
  public ResponseEntity<Boolean> validarDni(
          @RequestParam String dni,
          @RequestParam(required = false) Long excludeUserId) {

      // Validación básica del parámetro
      if (dni == null || dni.trim().isEmpty()) {
          return ResponseEntity.badRequest().body(false); // Respuesta clara en caso de error
      }

      // Validación usando el repositorio
      boolean exists = usuarioRepository.existsByDniExcludingId(dni, excludeUserId != null ? excludeUserId : -1L);
      return ResponseEntity.ok(!exists); // Retorna true si no existe, false si ya está registrado
  }


    @GetMapping("/validar-UserName")
    public ResponseEntity<Boolean> validarUserName(
            @RequestParam String username,
            @RequestParam(required = false) Long excludeUserId) {

        // Validación básica del parámetro
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(false); // Respuesta clara en caso de error
        }

        // Usar el servicio en lugar del repositorio directamente
        boolean exists = usuarioService.existsByUserNameExcludingId(username, excludeUserId);

        // Retornar true si está disponible (es decir, no existe en la base de datos)
        return ResponseEntity.ok(!exists);
    }



/*
    @GetMapping("/check-dni-or-username")
    public boolean existsByDniOrUsername(
            @RequestParam String value,
            @RequestParam(required = false) Long excludeUserId) {
        return usuarioService.checkDniOrUsernameExists(value, excludeUserId);
    }*/


   /* @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) throws Exception {
        // Verificar si hay errores en los campos
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        }

        // Verificar si el nombre de usuario (email) ya existe
        if (usuarioService.existsByUserName(nuevoUsuario.getUserName())) {
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
*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        }

        // Verificar si el usuario existe
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUserName(loginUsuario.getNombreUsuario());
        if (!usuarioOptional.isPresent()) {
            // Si no existe el usuario, enviar un mensaje que indique que el correo es incorrecto
            return new ResponseEntity<>(new Mensaje("Correo incorrecto"), HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = usuarioOptional.get();

        // Verificar si la contraseña es correcta
        if (!passwordEncoder.matches(loginUsuario.getPassword(), usuario.getPassword())) {
            // Si la contraseña no es correcta, enviar un mensaje que indique que la contraseña es incorrecta
            return new ResponseEntity<>(new Mensaje("Contraseña incorrecta"), HttpStatus.BAD_REQUEST);
        }

        // Autenticación exitosa, generar el JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }



    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) {
        try {
            // Refresca el token usando el servicio de JwtProvider
            String token = jwtProvider.refreshToken(jwtDto);
            JwtDto jwt = new JwtDto(token);
            return new ResponseEntity(jwt, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);








}
