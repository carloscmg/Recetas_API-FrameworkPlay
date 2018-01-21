package controllers;

import java.util.List;

import javax.inject.Inject;

import io.ebean.PagedList;
import models.ApiKey;
import models.Cocinero;
import models.Etiqueta;
import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class EtiquetaController extends Controller {

	@Inject 
	private FormFactory formFactory;

		
		public Result updateEtiqueta(String nombre) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
			
				Form<Etiqueta> form = formFactory
										.form(Etiqueta.class)
										.bindFromRequest();
				
				if(form.hasErrors()){
					return Results.status(409, form.errorsAsJson());
				}
				
				
				Etiqueta e = form.get();
				
				if (e.actualizar(nombre)) {
					return Results.ok();
				}else {
					return Results.status(409, new ErrorObject("2","Error en el UPDATE").toJson());
				}
			}
					
					
		}
		

		public Result deleteEtiqueta(String nombre) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				Etiqueta e = Etiqueta.findByName(nombre);
				if(e == null) {
						return ok(); // Por la idempotencia 
				}
				if(e.borrar()) {
					return ok();
				}else {
					return internalServerError();
				}
			}
		}
		

		public Result retrieveEtiquetasCollection(Integer page) {
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				PagedList<Etiqueta> list = Etiqueta.findPage(page);
				List<Etiqueta> etiquetas = list.getList();
				Integer count = list.getTotalPageCount();
				
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(etiquetas))
						    .withHeader("X-Etiquetas-Count", count.toString());
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.etiquetas.render(etiquetas))
							.withHeader("X-Etiquetas-Count", count.toString());
				} else {
					return Results
							.status(415);
				}
			}
		}
		
		public Result retrieveByEtiqueta(String etiquetaName) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
			
				Etiqueta e = Etiqueta.findByName(etiquetaName);
				if(e == null) {
					return notFound();
				}
	
				List<Receta> recetas = e.getRecetas();
				if (request().accepts("application/json")) {
				    return Results
						    .ok(Json.toJson(recetas));
				} else if (request().accepts("application/xml")) {
					return Results
							.ok(views.xml.recetas.render(recetas));
				}else {
					return Results.status(415);
				}
			}

		}
		

		
		
		
	
}
