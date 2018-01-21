package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.PagedList;
import play.data.validation.Constraints.Required;

@Entity
public class Cocinero extends ModeloBase {

	@Required(message="chef-name-required")
	private String nombre;
	
	@Required(message="chef-surname-required")
	private String apellidos;
	
	@Required(message="chef-country-required")
	private String pais;
	
	//Relation with fichaContacto (1:1)
	@OneToOne(cascade=CascadeType.ALL)
	@Required(message="chef-information-required")
	@Valid
	@JsonManagedReference
	private fichaContacto fichaContacto;
	
	//Relation with Receta(1:N)
	@OneToMany(mappedBy="cocinero")
	@Valid
	@JsonManagedReference
	private List<Receta> recetas = new ArrayList<>();
	

	//FINDER
	
	



			public static final Finder<Long,Cocinero> find = new Finder<>(Cocinero.class);
			//METODOS FACTORIAS te devuelven instancias
			public static Cocinero findById(Long id) {
				if (id == null) {
					throw new IllegalArgumentException("Se requiere el id");
				}
				return find.byId(id);
			} 
			
			public static Cocinero equalsCocinero(String nombre, String apellidos) {
				return find
						.query()
						.where()
						    .eq("nombre", nombre)
						    .eq("apellidos", apellidos)
						.findOne();
			} 
			
			public static PagedList<Cocinero> findByNombre(String nombre, Integer page) {
				if (nombre == null) {
					throw new IllegalArgumentException(); 
				}
				return find.query()
						.where()
						.contains("nombre", nombre)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public static PagedList<Cocinero> findByApellidos(String apellidos, Integer page) {
				if (apellidos == null) {
					throw new IllegalArgumentException(); 
				}
				return find.query()
						.where()
						.contains("apellidos", apellidos)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public static PagedList<Cocinero> findByPais(String pais, Integer page) {
				if (pais == null) {
					throw new IllegalArgumentException(); 
				}
				return find.query()
						.where()
						.eq("pais", pais)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			
			public static PagedList<Cocinero> findByNombreApellidos(String nombre, String apellidos, Integer page) {
				return find.query()
						.where()
						.contains("nombre", nombre)
						.contains("apellidos", apellidos)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public static PagedList<Cocinero> findByNombrePais(String nombre, String pais, Integer page) {
				return find.query()
						.where()
						.contains("nombre", nombre)
						.eq("pais", pais)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public static PagedList<Cocinero> findByApellidosPais(String apellidos, String pais, Integer page) {
				return find.query()
						.where()
						.contains("apellidos", apellidos)
						.eq("pais", pais)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public static PagedList<Cocinero> findByNombreApellidosPais(String nombre, String apellidos, String pais, Integer page) {
				return find.query()
						.where()
						.contains("nombre", nombre)
						.contains("apellidos", apellidos)
						.eq("pais", pais)
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
		
	
			public static PagedList<Cocinero> findPage(Integer page) {
				if (page == null) {
					throw new IllegalArgumentException(); //Le parece un mal invento
				}
				return find.query()
						.setMaxRows(25)
						.setFirstRow(25*page)
						.findPagedList();
			}
			
			public boolean checkAndCreate() {
				if (Cocinero.equalsCocinero(this.getNombre(), this.getApellidos()) == null ) {
					
					Ebean.beginTransaction();
					try {
						this.save();
						Ebean.commitTransaction();
					}finally {
						Ebean.endTransaction();
					}

					return true;
				}
				
				return false;
			}
			
			public void addReceta(Long idReceta) {
				
				Ebean.beginTransaction();
				try {
					
					Receta r = Receta.findById(idReceta);
					r.setCocinero(this);
					this.recetas.add(r);
					this.save();
					
					r.update();
					this.update();
					
					Ebean.commitTransaction();
				}finally {
					Ebean.endTransaction();
				}
				
			}
			
			public boolean actualizar(Long id) {
				
				Cocinero c = Cocinero.findById(id);
				
				if (c != null) {
				c.setNombre(this.getNombre());
				c.setApellidos(this.getApellidos());
				c.setPais(this.getPais());
				Long idBorrar = c.fichaContacto.getId();
				c.setFichaContacto(this.getFichaContacto());

					
					Ebean.beginTransaction();
					try {
						c.update();
						fichaContacto fc = models.fichaContacto.findById(idBorrar);
						fc.delete();
						Ebean.commitTransaction();
					}finally {
						Ebean.endTransaction();
					}

					return true;
				}else {
					return false;
				}
						
			}
			
			
			//JavaBean
			public Cocinero() {
				super();
			}
	public Cocinero(String nombre, String apellidos,	String pais) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.pais = pais;
	}
	// Getters and Setters...
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public fichaContacto getFichaContacto() {
		return fichaContacto;
	}
	public void setFichaContacto(fichaContacto fichaContacto) {
		this.fichaContacto = fichaContacto;
	}
	public List<Receta> getRecetas() {
		return recetas;
	}

	public void setRecetas(List<Receta> recetas) {
		this.recetas = recetas;
	}



	
	
	
}
