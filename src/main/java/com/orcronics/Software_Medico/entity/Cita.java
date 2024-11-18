package com.orcronics.Software_Medico.entity;

import java.util.Date;

import com.orcronics.Software_Medico.security.entity.Local;
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
@Table(name = "cita")
public class Cita {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull @ManyToOne @JoinColumn(name = "localid")
	private Local local;
	
	@NotBlank @Size(max = 25)
	private String estatus; //enum EstatusCita: pendiente, atendido, anulado.
	
	@NotNull
	private boolean cerrado; //true o false
	
	@NotNull
	private boolean solicitopodologo; //true o false
	
	@NotNull @ManyToOne @JoinColumn(name = "especialistaid")
	private Especialista especialista;
	
	//@NotNull puede ser null para crear citas por lotes sin paciente asignado. 
	@ManyToOne @JoinColumn(name = "pacienteid")
	private Paciente paciente;
	
	@NotBlank @Size(max = 500)
	@Column(length = 500)
	private String procedimiento;
	
	@NotNull
	private Date inicio;
	
	@NotNull
	private Date termino;
	
	@NotNull
	private int usuarioreg;
	
	@NotNull
	private Date fechareg;
	
	private int usuarioact;
	
	private Date fechaact;

	
	public Cita() {
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


	public String getEstatus() {
		return estatus;
	}


	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}


	public boolean isSolicitopodologo() {
		return solicitopodologo;
	}


	public void setSolicitopodologo(boolean solicitopodologo) {
		this.solicitopodologo = solicitopodologo;
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


	public String getProcedimiento() {
		return procedimiento;
	}


	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}


	public Date getInicio() {
		return inicio;
	}


	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}


	public Date getTermino() {
		return termino;
	}


	public void setTermino(Date termino) {
		this.termino = termino;
	}


	public int getUsuarioreg() {
		return usuarioreg;
	}


	public void setUsuarioreg(int usuarioreg) {
		this.usuarioreg = usuarioreg;
	}


	public Date getFechareg() {
		return fechareg;
	}


	public void setFechareg(Date fechareg) {
		this.fechareg = fechareg;
	}


	public int getUsuarioact() {
		return usuarioact;
	}


	public void setUsuarioact(int usuarioact) {
		this.usuarioact = usuarioact;
	}


	public Date getFechaact() {
		return fechaact;
	}


	public void setFechaact(Date fechaact) {
		this.fechaact = fechaact;
	}
	
	
	public boolean isCerrado() {
		return cerrado;
	}


	public void setCerrado(boolean cerrado) {
		this.cerrado = cerrado;
	}


	@Override
	public String toString() {
		return "Cita [id=" + id + ", local=" + local + ", estatus=" + estatus + ", cerrado=" + cerrado
				+ ", solicitopodologo=" + solicitopodologo + ", especialista=" + especialista + ", paciente=" + paciente
				+ ", procedimiento=" + procedimiento + ", inicio=" + inicio + ", termino=" + termino + ", usuarioreg="
				+ usuarioreg + ", fechareg=" + fechareg + ", usuarioact=" + usuarioact + ", fechaact=" + fechaact + "]";
	}

	


}
