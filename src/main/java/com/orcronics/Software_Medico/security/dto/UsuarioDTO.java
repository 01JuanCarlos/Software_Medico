package com.orcronics.Software_Medico.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class UsuarioDTO {

    @NotBlank
    @Size(max = 50)
    private String userName; // Es el correo usado para autenticación.

    @NotBlank
    @Size(min = 4, max = 255)
    private String password;

    private boolean activo;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nombres;

    @NotBlank
    @Size(min = 3, max = 30)
    private String apellidoPaterno;

    @Size(max = 30)
    private String apellidoMaterno;

    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;

    @Size(max = 30)
    private String celular;

    @Size(max = 255)
    private String direccion;

    @Size(max = 255)
    private String notas;

    @NotNull
    private int usuarioReg;

    @NotNull
    private Date fechaReg;

    private int usuarioAct;
    private Date fechaAct;

    private Set<String> roles = new HashSet<>(); // Nombres de roles (en lugar de IDs)

    private List<Integer> locales; // IDs de locales

    // Getters y setters


    public UsuarioDTO() {

    }

    public @NotBlank @Size(max = 50) String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank @Size(max = 50) String userName) {
        this.userName = userName;
    }

    public @NotBlank @Size(min = 4, max = 255) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 4, max = 255) String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public @NotBlank @Size(min = 3, max = 50) String getNombres() {
        return nombres;
    }

    public void setNombres(@NotBlank @Size(min = 3, max = 50) String nombres) {
        this.nombres = nombres;
    }

    public @NotBlank @Size(min = 3, max = 30) String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(@NotBlank @Size(min = 3, max = 30) String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public @Size(max = 30) String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(@Size(max = 30) String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public @NotBlank @Size(min = 8, max = 8) String getDni() {
        return dni;
    }

    public void setDni(@NotBlank @Size(min = 8, max = 8) String dni) {
        this.dni = dni;
    }

    public @Size(max = 30) String getCelular() {
        return celular;
    }

    public void setCelular(@Size(max = 30) String celular) {
        this.celular = celular;
    }

    public @Size(max = 255) String getDireccion() {
        return direccion;
    }

    public void setDireccion(@Size(max = 255) String direccion) {
        this.direccion = direccion;
    }

    public @Size(max = 255) String getNotas() {
        return notas;
    }

    public void setNotas(@Size(max = 255) String notas) {
        this.notas = notas;
    }

    @NotNull
    public int getUsuarioReg() {
        return usuarioReg;
    }

    public void setUsuarioReg(@NotNull int usuarioReg) {
        this.usuarioReg = usuarioReg;
    }

    public @NotNull Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(@NotNull Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public int getUsuarioAct() {
        return usuarioAct;
    }

    public void setUsuarioAct(int usuarioAct) {
        this.usuarioAct = usuarioAct;
    }

    public Date getFechaAct() {
        return fechaAct;
    }

    public void setFechaAct(Date fechaAct) {
        this.fechaAct = fechaAct;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<Integer> getLocales() {
        return locales;
    }

    public void setLocales(List<Integer> locales) {
        this.locales = locales;
    }
}
