package models;

import javax.persistence.Entity;

import io.ebean.Ebean;
import io.ebean.Finder;
import play.data.validation.Constraints.Required;
import validators.OneWord;

@Entity
public class ApiKey extends ModeloBase {
	
	@Required(message = "key-is-required")
	@OneWord
	private String key;
	
	public static final Finder<Long,ApiKey> find = new Finder<>(ApiKey.class);
	//METODOS FACTORIAS te devuelven instancias
	
	public static ApiKey findByKey(String apikey) {
		if (apikey == null) {
			throw new IllegalArgumentException("Se requiere el id"); 
		}
		return find
				.query()
				.where()
				    .eq("key", apikey)
				.findOne();
	} 
	
	public boolean checkAndCreate() {
		if (ApiKey.equalsKey(this.getKey()) == null ) {
			
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
	
	public static ApiKey equalsKey(String key) {
		return find
				.query()
				.where()
				    .eq("key", key)
				.findOne();
	} 
	
	//Getters and Setters...
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
	

