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
@Table(name = "historiadetalle")
public class HistoriaDetalle {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull @ManyToOne @JoinColumn(name = "procedimientoid")
	private Procedimiento procedimiento;
	
	@NotNull
	private float precio;
	
	@NotNull
	private float cantidad;
	
	@NotNull
	private float importe;
	
	@NotBlank
	private String motivo;
	
	@ManyToOne
	@JoinColumn(name = "historiaid", nullable = false)
	//@JsonBackReference
	@JsonIgnore
	private Historia historia;
	
	
	public HistoriaDetalle() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Procedimiento getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Historia getHistoria() {
		return historia;
	}

	public void setHistoria(Historia historia) {
		this.historia = historia;
	}

	@Override
	public String toString() {
		return "HistoriaDetalle [id=" + id + ", procedimiento=" + procedimiento + ", precio=" + precio + ", cantidad="
				+ cantidad + ", importe=" + importe + ", motivo=" + motivo + ", historia=" + historia + "]";
	}
	
	
}
