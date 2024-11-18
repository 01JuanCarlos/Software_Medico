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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "historia")
public class Historia {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull @ManyToOne @JoinColumn(name = "localid")
	//@JsonProperty(value = "local")
	private Local local;
	
	@NotNull @ManyToOne @JoinColumn(name = "especialistaid")
	private Especialista especialista;
	
	@NotNull @ManyToOne @JoinColumn(name = "pacienteid")
	private Paciente paciente;
	
	@NotNull
	private Date fecha;
	
	private String observaciones;
	
	@NotNull
	private float total;
	
	@NotNull
	@OneToMany(mappedBy = "historia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty(value = "detalle")
	//@JsonManagedReference
	//@JsonIgnore
	private Set<HistoriaDetalle> detalle;
	
	public Historia() {
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

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Set<HistoriaDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(Set<HistoriaDetalle> detalle) {
		this.detalle = detalle;
	}

	@Override
	public String toString() {
		return "Historia [id=" + id + ", local=" + local + ", especialista=" + especialista + ", paciente=" + paciente
				+ ", fecha=" + fecha + ", observaciones=" + observaciones + ", total=" + total + "]";
	}

	

	
}
