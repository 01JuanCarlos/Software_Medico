package com.orcronics.Software_Medico;

import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import com.orcronics.Software_Medico.security.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class crearRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
      /*   Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
         Rol rolUser = new Rol(RolNombre.ROLE_USER);
         rolService.save(rolAdmin);
         rolService.save(rolUser);
*/
    }
}