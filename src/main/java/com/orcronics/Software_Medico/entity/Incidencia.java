package com.orcronics.Software_Medico.entity;

import java.util.Date;

import com.orcronics.Software_Medico.security.entity.Local;
import com.orcronics.Software_Medico.security.entity.Usuario;
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

@Entity
@Table(name = "incidencia")
public class Incidencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull @ManyToOne @JoinColumn(name = "usuarioid")
	private Usuario usuario;
	
	@NotNull @ManyToOne @JoinColumn(name = "localid")
	private Local local;
	
	@NotNull
	private Date fecha;
	
	@NotBlank @Size(max = 500) @Column(length = 500)
	private String comentario;

	
	public Incidencia() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", usuario=" + usuario + ", local=" + local + ", fecha=" + fecha
				+ ", comentario=" + comentario + "]";
	}
	

}
