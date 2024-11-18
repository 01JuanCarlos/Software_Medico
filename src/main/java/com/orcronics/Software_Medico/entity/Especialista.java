package com.orcronics.Software_Medico.entity;


import com.orcronics.Software_Medico.security.entity.Local;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;


@Entity
@Table(name = "especialista")
public class Especialista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nombres;
	private String estado;
	private String username;
	private String especialidad; //Puede ser: ['PODOLOGO', 'BIOMECANICA', 'TERAPIA'].
	
	//@NotNull
	//@OneToMany(mappedBy = "especialista", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	//@JsonProperty(value = "locales")
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "especialista_local", 
    			joinColumns = @JoinColumn(name = "especialista_id"), // id de entidad Especialista.
    			inverseJoinColumns = @JoinColumn(name = "local_id")) // id de entidad Local.
	@JsonProperty(value = "locales")
	private Set<Local> locales; //locales de trabajo.
	
	
	public Especialista() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Set<Local> getLocales() {
		return locales;
	}

	public void setLocales(Set<Local> locales) {
		this.locales = locales;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	@Override
	public String toString() {
		return "Especialista [id=" + id + ", nombres=" + nombres + ", estado=" + estado + ", username=" + username
				+ ", especialidad=" + especialidad + ", locales=" + locales + "]";
	}

	
}
