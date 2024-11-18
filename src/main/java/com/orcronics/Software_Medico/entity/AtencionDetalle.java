package com.orcronics.Software_Medico.entity;

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
@Table(name = "atenciondetalle")
public class AtencionDetalle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank
	@Size(max = 255)
	private String procedimiento;
	@NotNull
	private float precio;
	@ManyToOne
	@JoinColumn(name = "atencionid", nullable = false)
	//@JsonBackReference
	@JsonIgnore
	private Atencion atencion;
	
	public AtencionDetalle() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getProcedimiento() {
		return procedimiento;
	}
	
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	
	public float getPrecio() {
		return precio;
	}
	
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	public Atencion getAtencion() {
		return atencion;
	}
	
	public void setAtencion(Atencion atencion) {
		this.atencion = atencion;
	}

	@Override
	public String toString() {
		return "AtencionDetalle [id=" + id + ", procedimiento=" + procedimiento + ", precio=" + precio + ", atencion="
				+ atencion + "]";
	}

	
}
