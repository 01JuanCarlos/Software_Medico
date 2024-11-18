package com.orcronics.Software_Medico.entity;

import java.util.Date;
import java.util.List;

import com.orcronics.Software_Medico.security.entity.Local;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "paciente")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nombres;
	private String apellidos;
	private String dni;
	private String telefono;
	
	@Column(name = "codigoletra")
	private String codigoLetra;
	@Column(name = "codigonro")
	private int codigoNro;
	
	@Column(name = "fechanacimiento")
	private Date fechaNacimiento;
	
	@NotNull @ManyToOne @JoinColumn(name = "localid")
	private Local local;
	
	private String email;
	private String celular;
	private String domicilio;
	private float peso;
	private float estatura;
	private float calzado;
	private boolean cardiovascular; //presenta enfermedad.
	private boolean diabetes; //presenta enfermedad.
	private boolean hemofilia; //presenta enfermedad.
	private String otros;
	private String derivado;
	private String plantillas;
	private String ortesicos;
	private boolean onicomicosis; //presenta afectación en el pie.
	private boolean onicocriptosis; //presenta afectación en el pie.
	private boolean helomas; //presenta afectación en el pie.
	private boolean onicogrifosis; //presenta afectación en el pie.
	private boolean halluxvalgus; //presenta afectación en el pie.
	private boolean vph; //presenta afectación en el pie.
	private boolean dermatomicosis; //presenta afectación en el pie.
	private String otrasafectaciones; //presenta afectación en el pie.
	
	@OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonProperty(value = "multimedias")
	private List<PacienteMultimedia> multimedias; //representa los archivos multimedia del paciente: foto, pdf u otro archivo.
	
	public Paciente() {
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getCodigoLetra() {
		return codigoLetra;
	}

	public void setCodigoLetra(String codigoLetra) {
		this.codigoLetra = codigoLetra;
	}

	public int getCodigoNro() {
		return codigoNro;
	}

	public void setCodigoNro(int codigoNro) {
		this.codigoNro = codigoNro;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getEstatura() {
		return estatura;
	}

	public void setEstatura(float estatura) {
		this.estatura = estatura;
	}

	public float getCalzado() {
		return calzado;
	}

	public void setCalzado(float calzado) {
		this.calzado = calzado;
	}

	public boolean isCardiovascular() {
		return cardiovascular;
	}

	public void setCardiovascular(boolean cardiovascular) {
		this.cardiovascular = cardiovascular;
	}

	public boolean isDiabetes() {
		return diabetes;
	}

	public void setDiabetes(boolean diabetes) {
		this.diabetes = diabetes;
	}

	public boolean isHemofilia() {
		return hemofilia;
	}

	public void setHemofilia(boolean hemofilia) {
		this.hemofilia = hemofilia;
	}

	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

	public String getDerivado() {
		return derivado;
	}

	public void setDerivado(String derivado) {
		this.derivado = derivado;
	}

	public String getPlantillas() {
		return plantillas;
	}

	public void setPlantillas(String plantillas) {
		this.plantillas = plantillas;
	}

	public String getOrtesicos() {
		return ortesicos;
	}

	public void setOrtesicos(String ortesicos) {
		this.ortesicos = ortesicos;
	}

	public boolean isOnicomicosis() {
		return onicomicosis;
	}

	public void setOnicomicosis(boolean onicomicosis) {
		this.onicomicosis = onicomicosis;
	}

	public boolean isOnicocriptosis() {
		return onicocriptosis;
	}

	public void setOnicocriptosis(boolean onicocriptosis) {
		this.onicocriptosis = onicocriptosis;
	}

	public boolean isHelomas() {
		return helomas;
	}

	public void setHelomas(boolean helomas) {
		this.helomas = helomas;
	}

	public boolean isOnicogrifosis() {
		return onicogrifosis;
	}

	public void setOnicogrifosis(boolean onicogrifosis) {
		this.onicogrifosis = onicogrifosis;
	}

	public boolean isHalluxvalgus() {
		return halluxvalgus;
	}

	public void setHalluxvalgus(boolean halluxvalgus) {
		this.halluxvalgus = halluxvalgus;
	}

	public boolean isVph() {
		return vph;
	}

	public void setVph(boolean vph) {
		this.vph = vph;
	}

	public boolean isDermatomicosis() {
		return dermatomicosis;
	}

	public void setDermatomicosis(boolean dermatomicosis) {
		this.dermatomicosis = dermatomicosis;
	}

	public String getOtrasafectaciones() {
		return otrasafectaciones;
	}

	public void setOtrasafectaciones(String otrasafectaciones) {
		this.otrasafectaciones = otrasafectaciones;
	}
	
	public List<PacienteMultimedia> getMultimedias() {
		return multimedias;
	}

	public void setMultimedias(List<PacienteMultimedia> multimedias) {
		this.multimedias = multimedias;
	}

	@Override
	public String toString() {
		return "Paciente [id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", dni=" + dni
				+ ", telefono=" + telefono + ", codigoLetra=" + codigoLetra + ", codigoNro=" + codigoNro
				+ ", fechaNacimiento=" + fechaNacimiento + ", local=" + local + ", email=" + email + ", celular="
				+ celular + ", domicilio=" + domicilio + ", peso=" + peso + ", estatura=" + estatura + ", calzado="
				+ calzado + ", cardiovascular=" + cardiovascular + ", diabetes=" + diabetes + ", hemofilia=" + hemofilia
				+ ", otros=" + otros + ", derivado=" + derivado + ", plantillas=" + plantillas + ", ortesicos="
				+ ortesicos + ", onicomicosis=" + onicomicosis + ", onicocriptosis=" + onicocriptosis + ", helomas="
				+ helomas + ", onicogrifosis=" + onicogrifosis + ", halluxvalgus=" + halluxvalgus + ", vph=" + vph
				+ ", dermatomicosis=" + dermatomicosis + ", otrasafectaciones=" + otrasafectaciones + "]";
	}
	

}
