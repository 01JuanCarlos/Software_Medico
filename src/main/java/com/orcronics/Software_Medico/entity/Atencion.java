package com.orcronics.Software_Medico.entity;

import java.util.Date;
import java.util.Set;

import com.orcronics.Software_Medico.security.entity.Local;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "atencion")
public class Atencion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "localid")
	//@JsonProperty(value = "local")
	private Local local;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "especialistaid")
	private Especialista especialista;
	@NotBlank
	@Size(max = 255)
	private String paciente;
	@NotNull
	private Date fecha;
	@NotNull
	@OneToMany(mappedBy = "atencion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty(value = "detalle")
	//@JsonManagedReference
	//@JsonIgnore
	private Set<AtencionDetalle> detalle;
	
	public Atencion() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Especialista getEspecialista() {
		return especialista;
	}

	public void setEspecialista(Especialista especialista) {
		this.especialista = especialista;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Set<AtencionDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(Set<AtencionDetalle> detalle) {
		this.detalle = detalle;
	}

	@Override
	public String toString() {
		return "Atencion [id=" + id + ", local=" + local + ", especialista=" + especialista + ", paciente=" + paciente
				+ ", fecha=" + fecha + "]";
	}
	
}
