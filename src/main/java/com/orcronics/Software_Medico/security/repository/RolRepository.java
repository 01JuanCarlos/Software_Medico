package com.orcronics.Software_Medico.security.repository;

import com.orcronics.Software_Medico.security.entity.Rol;
import com.orcronics.Software_Medico.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}
