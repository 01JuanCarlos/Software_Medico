package com.orcronics.Software_Medico.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pacientemultimedia")
public class PacienteMultimedia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Size(max = 255, message = "debe contener máximo 255 caracteres.")
	@Column(length = 255)
	private String fichero;
	
	@NotNull
	@Size(max = 255, message = "debe contener máximo 255 caracteres.")
	@Column(length = 255)
	private String tipo;
	
	@NotNull
	@Size(max = 255, message = "debe contener máximo 255 caracteres.")
	@Column(length = 255)
	private String size;
	
	@NotNull
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "pacienteid", nullable = false)
	@JsonIgnore
	private Paciente paciente;

	
	public String getFichero() {
		return fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "PacienteMultimedia [id=" + id + ", fichero=" + fichero + ", tipo=" + tipo + ", size=" + size
				+ ", fecha=" + fecha + "]";
	}

	
	
}
