package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.ebean.Finder;
import play.data.validation.Constraints.Required;


@Entity
public class fichaContacto extends ModeloBase {


	
	@Required(message="phone-number-required")
	private Integer telefono;
	
	@Required(message="email-required")
	@Email
	private String email;
	
	@URL
	private String web;
	
	
	//Relation with Cocinero (1:1)
	@OneToOne(mappedBy="fichaContacto")
	@JsonBackReference
	private Cocinero cocinero;
	
	
	public static final Finder<Long,fichaContacto> find = new Finder<>(fichaContacto.class);
	//METODOS FACTORIAS te devuelven instancias
	public static fichaContacto findById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Se requiere el id"); 
		}
		return find.byId(id);
	} 
	
	// Getters and Setters...
	public Integer getTelefono() {
		return telefono;
	}
	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}

	
}
