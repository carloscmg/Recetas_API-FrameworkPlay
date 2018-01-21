package controllers;

import play.mvc.Result;
import play.mvc.Results;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.PagedList;
import models.ApiKey;
import models.Cocinero;
import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;

public class RecetaController extends Controller {

@Inject 
private FormFactory formFactory;

	

	public Result createReceta() {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
		
	
			//FORM
	
			Form<Receta> form = formFactory
									.form(Receta.class)
									.bindFromRequest();
			
			if(form.hasErrors()){
				return Results.status(409, form.errorsAsJson());
			}
			
			Receta receta = form.get();
			
			if(receta.checkAndCreate()) {
				return Results.created();
			}else {
				return Results.status(409, new ErrorObject("2","Receta repetida").toJson());
			}
		}

	}
	

	public Result retrieveReceta(Long id) {
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
			Receta r = Receta.findById(id);
			if(r == null) {
					return Results.notFound();
			}else {
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(r));
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.receta.render(r));
				} else {
					return Results
							.status(415);
				}
			}
		}
	}
	
	public Result updateReceta(Long id) {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
			Form<Receta> form = formFactory
									.form(Receta.class)
									.bindFromRequest();
			
			if(form.hasErrors()){
				return Results.status(409, form.errorsAsJson());
			}
			
			
			Receta r = form.get();
			
			if (r.actualizar(id)) {
				return Results.ok();
			}else {
				return Results.status(409, new ErrorObject("2","Error en el UPDATE").toJson());
			}
				
		}	
	}
	

	public Result deleteReceta(Long idReceta) {
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
			Receta r = Receta.findById(idReceta);
			if(r == null) {
					return ok(); // Por la idempotencia 
			}
			if(r.delete()) {
				return ok();
			}else {
				return internalServerError();
			}
		}
	}
	

	public Result retrieveRecetasCollection(Integer page) {
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
		
			String dificultad = request().getQueryString("dificultad");
			String titulo = request().getQueryString("titulo");
			String tipo = request().getQueryString("tipoCocina");
			
	
			
			if ((dificultad == null) && (titulo == null) && (tipo == null)) {//Sin busqueda
				PagedList<Receta> list = Receta.findPage(page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				} else {
					return Results
							.status(415);
				}
			}else if ((dificultad != null) && (titulo == null) && (tipo == null)) {//Dificultad
				PagedList<Receta> list = Receta.findByDificultad(dificultad, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				} else {
					return Results
							.status(415);
				}
			}else if ((dificultad == null) && (titulo != null) && (tipo == null)) {//Titulo
				PagedList<Receta> list = Receta.findByTitulo(titulo, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				}else {
					return Results.status(415);
				}
			}else if ((dificultad == null) && (titulo == null) && (tipo != null)) {//Tipo
				PagedList<Receta> list = Receta.findByTipoCocina(tipo, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				}else {
					return Results.status(415);
				}
			}else if ((dificultad != null) && (titulo == null) && (tipo != null)) {//Dificultad,Tipo
				PagedList<Receta> list = Receta.findByDificultadTipo(tipo, dificultad, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				}else {
					return Results.status(415);
				}
			}else if ((dificultad != null) && (titulo != null) && (tipo == null)) {//Dificultad,Titulo
				PagedList<Receta> list = Receta.findByDificultadTitulo(titulo, dificultad, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				}else {
					return Results.status(415);
				}
			}else if ((dificultad == null) && (titulo != null) && (tipo != null)) {//TituloTipo
				PagedList<Receta> list = Receta.findByDificultadTitulo(titulo, dificultad, page);
				List<Receta> recetas = list.getList();
				Integer count = list.getTotalPageCount();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas))
						    .withHeader("X-Recetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas))
							.withHeader("X-Recetas-Count", count.toString());
				}else {
					return Results.status(415);
				}
			}
				
			
			return Results.notFound();
		}
		
	}
	
	public Result haveCocinero(Long id) {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
		
			Receta r = Receta.findById(id);
			Cocinero c = r.getCocinero();
			
			if (c == null) {
				return Results.notFound();
			} else {
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(c));
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.cocinero.render(c));
				} else {
					return Results
							.status(415);
				}
			}
		}
	}
	
	
	public Result assignEtiqueta(Long idReceta, String etiquetaName) {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
		
			Receta r = Receta.findById(idReceta);
			
			if(r == null) {
				return Results.notFound();
			}
			
			if(r.addEtiquetaAndSave(etiquetaName)) {
				return  created();
			}else {
				
				r.deleteEtiqueta(etiquetaName);
				
				return ok("Etiqueta desasignada\n");
			}
		
		}
		
	}
	

	
}
