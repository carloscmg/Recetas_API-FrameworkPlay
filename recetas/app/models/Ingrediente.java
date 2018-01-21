package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.data.validation.Constraints.Required;

@Entity
public class Ingrediente extends ModeloBase {
	
	@Required(message = "Nombre del ingrediente requerido")
	private String nombre;
	
	private String cantidad;
	
	//Relation with Receta (N:1)
	@ManyToOne
	@JsonBackReference
	private Receta receta;
	

	//Getters and Setters...
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}






}
