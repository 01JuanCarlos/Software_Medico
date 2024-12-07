package com.orcronics.Software_Medico.security.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RolNombre {
    ADMIN,SUPERVISOR,RECEPCION,PODOLOGO;

    @JsonCreator
    public static RolNombre fromString(String rol) {
        return RolNombre.valueOf(rol.toUpperCase());  // Convierte la cadena a mayúsculas antes de buscarla
    }
}
