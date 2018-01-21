package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.PagedList;
import play.data.validation.Constraints.Required;
import validators.LowerCase;
import validators.OneWord;

@Entity
public class Etiqueta extends ModeloBase {
	
	@Required(message = "Nombre de la etiqueta requerido")
	@OneWord
	@LowerCase
	private String nombre;
	
	//Relation with Receta (N:N)
	@ManyToMany(mappedBy = "etiquetas")
	@JsonBackReference
	private List<Receta> recetas = new ArrayList<>();
	
	
	public static final Finder<Long,Etiqueta> find = new Finder<>(Etiqueta.class);
	
	public static Etiqueta findByName(String palabra) {
		if (palabra == null) {
			throw new IllegalArgumentException(); 
		}
		return find.query()
				.where()
				.eq("nombre", palabra)
				.findOne();
	}
	
	
	
	public static PagedList<Etiqueta> findPage(Integer page) {
		if (page == null) {
			throw new IllegalArgumentException(); 
		}
		return find.query()
				.setMaxRows(25)
				.setFirstRow(25*page)
				.findPagedList();
	}
	
	public boolean actualizar(String name) {
		
		Etiqueta e = Etiqueta.findByName(name);
		
		if (e != null) {
		e.setNombre(this.getNombre());
		
			Ebean.beginTransaction();
			try {
				e.update();
				Ebean.commitTransaction();
			}finally {
				Ebean.endTransaction();
			}

			return true;
		}else {
			return false;
		}
		
		
	}
	
	public boolean borrar() {
		
		
		for(Receta r: this.recetas) {
			r.getEtiquetas().remove(this);
			r.save();
		}
		
		while( !this.recetas.isEmpty() ) {  
		    this.recetas.remove(0);  
		}  
		
		
		this.save();  
		this.delete();
		return true;
	}
	

	

	//JavaBean
			public Etiqueta() {
				super();
			}

	public Etiqueta(String nombre) {
		super();
		this.nombre = nombre;
	}

	
	//Getters and Setters...

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Receta> getRecetas() {
		return recetas;
	}

	public void setRecetas(List<Receta> recetas) {
		this.recetas = recetas;
	}

}
