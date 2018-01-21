package models;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;

@MappedSuperclass
public class ModeloBase extends Model {
	
	@Id
	private Long id;
	
	@Version
	@JsonIgnore
	private Long version;
	
	@CreatedTimestamp
	@JsonIgnore
	private Timestamp whenCreated;
	
	@UpdatedTimestamp
	@JsonIgnore
	private Timestamp whenUpdated;
	
	//Getters and Setters...
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Timestamp getWhenCreated() {
		return whenCreated;
	}

	public void setWhenCreated(Timestamp whenCreated) {
		this.whenCreated = whenCreated;
	}

	public Timestamp getWhenUpdated() {
		return whenUpdated;
	}

	public void setWhenUpdated(Timestamp whenUpdated) {
		this.whenUpdated = whenUpdated;
	}


}
