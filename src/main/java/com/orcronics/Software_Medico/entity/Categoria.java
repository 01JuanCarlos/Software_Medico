package com.orcronics.Software_Medico.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categoria")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Size(max = 6, message = "debe contener máximo 6 caracteres")
	@Column(length = 6, unique = true, nullable = false)
	private String codigo;
	
	@NotBlank
	@Size(max = 50, message = "debe contener máximo 50 caracteres")
	@Column(length = 50, unique = true, nullable = false)
	private String nombre;

	
	public Categoria() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Override
	public String toString() {
		return "Categoria [id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + "]";
	}
	
}
