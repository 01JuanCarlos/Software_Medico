package com.orcronics.Software_Medico.entity;

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
@Table(name = "procedimiento")
public class Procedimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Size(min = 1, max = 100, message = "debe contener entre 1 y 100 caracteres.")
	@Column(length = 100, unique = false, nullable = false)
	private String descripcion;
	
	@NotNull
	private boolean activo;
	
	/**
	@NotBlank
	@Size(min = 1, max = 20, message = "debe contener entre 1 y 20 caracteres.")
	@Column(length = 20, unique = true)
	private String barcode;
	*/
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "unidadmedidaid")
	private UnidadMedida unidadmedida;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "tipoexistenciaid")
	private TipoExistencia tipoexistencia;
	
	@NotNull
	private float precio; //precio unitario de venta.
	
	@NotNull
	private float costo; //precio unitario de compra.
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoriaid")
	private Categoria categoria;
	
	@NotNull @ManyToOne @JoinColumn(name = "localid")
	private Local local;
	
	@Size(max = 100, message = "debe contener máximo 100 caracteres.")
	@Column(length = 100)
	private String imagen;
	
	
	public Procedimiento() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public UnidadMedida getUnidadmedida() {
		return unidadmedida;
	}

	public void setUnidadmedida(UnidadMedida unidadmedida) {
		this.unidadmedida = unidadmedida;
	}

	public TipoExistencia getTipoexistencia() {
		return tipoexistencia;
	}

	public void setTipoexistencia(TipoExistencia tipoexistencia) {
		this.tipoexistencia = tipoexistencia;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public String toString() {
		return "Procedimiento [id=" + id + ", descripcion=" + descripcion + ", activo=" + activo + ", unidadmedida="
				+ unidadmedida + ", tipoexistencia=" + tipoexistencia + ", precio=" + precio + ", costo=" + costo
				+ ", categoria=" + categoria + ", local=" + local + ", imagen=" + imagen + "]";
	}

}
