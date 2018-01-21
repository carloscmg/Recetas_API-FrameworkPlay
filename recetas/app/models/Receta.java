package models;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.PagedList;
import play.data.validation.Constraints.Required;
import validators.Difficulty;
import validators.LowerCase;
import validators.OneWord;
import validators.TypeOfKitchen;


@Entity
public class Receta  extends ModeloBase {
		
	
	@Required(message="Titulo de la receta requerido")
	private String titulo;
	
	@Required(message="tipoCocina requerido")
	@OneWord
	@LowerCase
	@TypeOfKitchen
	private String tipoCocina;
	
	@Required(message="dificultad requerida")
	@OneWord
	@LowerCase
	@Difficulty
	private String dificultad;
	
	private Integer tiempo; //tiempo de cocinado cooking time
	
	private String elaboracion;
	
	private Integer numeroPersonas;
	
	@URL
	private String imagen;
	
	//Relation with Cocinero (N:1)
	@ManyToOne
	@JsonBackReference(value = "cocinero")
	private Cocinero cocinero;
	
	//Relation with ingrediente (1:N)
	@OneToMany(cascade=CascadeType.ALL, mappedBy="receta")
	@Required
	@Valid
	@JsonManagedReference
	private List<Ingrediente> ingredientes = new ArrayList<>();
		
	//Relation with Etiqueta (N:N)
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Etiqueta> etiquetas = new ArrayList<>();
	
		
		
		
		
		//FINDER
		
	



		public static final Finder<Long,Receta> find = new Finder<>(Receta.class);
		//METODOS FACTORIAS te devuelven instancias
		public static Receta findById(Long id) {
			if (id == null) {
				throw new IllegalArgumentException("Se requiere el id"); 
			}
			return find.byId(id);
		} 
		
		public static Receta equalsTitulo(String titulo) {
			return find
					.query()
					.where()
					    .eq("titulo", titulo)
					.findOne();
		} 
		
		public static PagedList<Receta> findByTitulo(String palabra, Integer page) {
			if (palabra == null) {
				throw new IllegalArgumentException(); 
			}
			return find.query()
					.where()
					.contains("titulo", palabra)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		public static PagedList<Receta> findByDificultad(String dificultad, Integer page) {
			if (dificultad == null) {
				throw new IllegalArgumentException(); 
			}
			return find.query()
					.where()
					.eq("dificultad", dificultad)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		public static PagedList<Receta> findByTipoCocina(String tipo, Integer page) {
			if (tipo == null) {
				throw new IllegalArgumentException(); 
			}
			return find.query()
					.where()
					.eq("tipoCocina", tipo)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		public static PagedList<Receta> findPage(Integer page) {
			if (page == null) {
				throw new IllegalArgumentException(); 
			}
			return find.query()
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		
		public static PagedList<Receta> findByDificultadTipo(String tipo, String dificultad, Integer page) {
			return find.query()
					.where()
					.eq("dificultad", dificultad)
					.eq("tipoCocina", tipo)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		public static PagedList<Receta> findByDificultadTitulo(String titulo, String dificultad, Integer page) {
			return find.query()
					.where()
					.eq("dificultad", dificultad)
					.contains("titulo", titulo)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		public static PagedList<Receta> findByTipoTitulo(String titulo, String tipo, Integer page) {
			return find.query()
					.where()
					.eq("tipoCocina", tipo)
					.contains("titulo", titulo)
					.setMaxRows(25)
					.setFirstRow(25*page)
					.findPagedList();
		}
		
		
		public boolean checkAndCreate() {
			if (Receta.equalsTitulo(this.getTitulo()) == null ) {
				
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
		
		public boolean actualizar(Long id) {
			
			Receta r = Receta.findById(id);
			
			if (r != null) {
			r.setTitulo(this.getTitulo());
			r.setTipoCocina(this.getTipoCocina());
			r.setDificultad(this.getDificultad());
			r.setTiempo(this.getTiempo());
			r.setElaboracion(this.getElaboracion());
			r.setNumeroPersonas(this.getNumeroPersonas());
			r.setImagen(this.getImagen());
			r.setIngredientes(this.getIngredientes());
			
				
				Ebean.beginTransaction();
				try {
					r.update();
					Ebean.commitTransaction();
				}finally {
					Ebean.endTransaction();
				}

				return true;
			}else {
				return false;
			}
			
			
		}
		
		public boolean addEtiquetaAndSave(String etiquetaName) {
			
			Etiqueta etiqueta = Etiqueta.findByName(etiquetaName);
			
			for (Etiqueta etiquet : etiquetas) {
	            if (etiquet.getNombre().equals(etiquetaName)){
	                 //FALLO 
	            		return false; 
	            }
	        }
			
			if(etiqueta == null) {
				etiqueta = new Etiqueta();
				etiqueta.setNombre(etiquetaName);
				etiqueta.save();
			}
			
			etiqueta.getRecetas().add(this);
			this.etiquetas.add(etiqueta);
			
			this.save();
			return true;
			
		}
		
	public void deleteEtiqueta(String etiquetaName) {
			
			Etiqueta etiqueta = Etiqueta.findByName(etiquetaName);
			
			etiqueta.getRecetas().remove(this);
			this.etiquetas.remove(etiqueta);

			etiqueta.save();
			this.save();
			
		}
		
		
		
		//JavaBean
		public Receta() {
			super();
		}
		
		//Constructor
		public Receta(String titulo, String tipoCocina, String dificultad, Integer tiempo, String elaboracion,
				Integer numeroPersonas, String imagen) {
			super();
			this.titulo = titulo;
			this.tipoCocina = tipoCocina;
			this.dificultad = dificultad;
			this.tiempo = tiempo;
			this.elaboracion = elaboracion;
			this.numeroPersonas = numeroPersonas;
			this.imagen = imagen;
		}


		//Getters and Setters...
		public String getTitulo() {
			return titulo;
		}


		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}


		public String getTipoCocina() {
			return tipoCocina;
		}


		public void setTipoCocina(String tipoCocina) {
			this.tipoCocina = tipoCocina;
		}


		public String getDificultad() {
			return dificultad;
		}


		public void setDificultad(String dificultad) {
			this.dificultad = dificultad;
		}


		public Integer getTiempo() {
			return tiempo;
		}


		public void setTiempo(Integer tiempo) {
			this.tiempo = tiempo;
		}


		public String getElaboracion() {
			return elaboracion;
		}


		public void setElaboracion(String elaboracion) {
			this.elaboracion = elaboracion;
		}


		public Integer getNumeroPersonas() {
			return numeroPersonas;
		}


		public void setNumeroPersonas(Integer numeroPersonas) {
			this.numeroPersonas = numeroPersonas;
		}


		public String getImagen() {
			return imagen;
		}


		public void setImagen(String imagen) {
			this.imagen = imagen;
		}
		
		public Cocinero getCocinero() {
			return cocinero;
		}


		public void setCocinero(Cocinero cocinero) {
			this.cocinero = cocinero;
		}

		public List<Ingrediente> getIngredientes() {
			return ingredientes;
		}


		public void setIngredientes(List<Ingrediente> ingredientes) {
			this.ingredientes = ingredientes;
		}

		public List<Etiqueta> getEtiquetas() {
			return etiquetas;
		}

		public void setEtiquetas(List<Etiqueta> etiquetas) {
			this.etiquetas = etiquetas;
		}

		//Consideramos que dos recetas son iguales cuando tengan el mismo Titulo
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Receta){
				Receta otherReceta = (Receta)obj;
				return this.getTitulo().equals(otherReceta.getTitulo());
			}
			return false;
		}


		


	

		

}
