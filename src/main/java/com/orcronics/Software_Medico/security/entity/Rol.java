package com.orcronics.Software_Medico.security.entity;

import com.orcronics.Software_Medico.security.enums.RolNombre;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "role")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol() {
    }

    public Rol(@NotNull RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RolNombre getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }


}