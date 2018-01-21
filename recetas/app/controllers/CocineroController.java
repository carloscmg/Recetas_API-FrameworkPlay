package controllers;

import java.util.List;

import javax.inject.Inject;

import io.ebean.PagedList;
import models.ApiKey;
import models.Cocinero;
import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class CocineroController extends Controller {
	
	@Inject 
	private FormFactory formFactory;

		
		public Result createCocinero() {

			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				//FORM
	
				Form<Cocinero> form = formFactory
										.form(Cocinero.class)
										.bindFromRequest();
				
				if(form.hasErrors()){
					return Results.status(409, form.errorsAsJson());
				}
				
				Cocinero cocinero = form.get();
				
				if(cocinero.checkAndCreate()) {
					return Results.created();
				}else {
					return Results.status(409, new ErrorObject("2","Cocinero repetido").toJson());
				}
			}

		}
		

		public Result retrieveCocinero(Long id) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
			
				Cocinero c = Cocinero.findById(id);
				if(c == null) {
						return Results.notFound();
				}else {
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
		
		public Result updateCocinero(Long id) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				Form<Cocinero> form = formFactory
						.form(Cocinero.class)
						.bindFromRequest();
	
				if(form.hasErrors()){
				return Results.status(409, form.errorsAsJson());
				}
				
				
				Cocinero c = form.get();
				
				if (c.actualizar(id)) {
				return Results.ok();
				}else {
				return Results.status(409, new ErrorObject("2","Error en el UPDATE").toJson());
				}
			}
		}
		

		public Result deleteCocinero(Long id) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				Cocinero	 c = Cocinero.findById(id);
				if(c == null) {
						return ok(); // Por la idempotencia 
				}
				if(c.delete()) {
					return ok();
				}else {
					return internalServerError();
				}
			}
		}
		

		public Result retrieveCocinerosCollection(Integer page) {
			
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
			
				String nombre = request().getQueryString("nombre");
				String apellidos = request().getQueryString("apellidos");
				String pais = request().getQueryString("pais");
				
				
				if ((nombre == null) && (apellidos == null) && (pais == null)) {//Sin busqueda
					PagedList<Cocinero> list = Cocinero.findPage(page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre != null) && (apellidos == null) && (pais == null)) {//Nombre
					
					PagedList<Cocinero> list = Cocinero.findByNombre(nombre, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre == null) && (apellidos != null) && (pais == null)) {//Apellidos
					
					PagedList<Cocinero> list = Cocinero.findByApellidos(apellidos, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre == null) && (apellidos == null) && (pais != null)) {//Pais
					
					PagedList<Cocinero> list = Cocinero.findByApellidos(apellidos, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre != null) && (apellidos != null) && (pais == null)) {//NombreApellidos
					
					PagedList<Cocinero> list = Cocinero.findByNombreApellidos(nombre, apellidos, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre != null) && (apellidos == null) && (pais != null)) {//NombrePais
					
					PagedList<Cocinero> list = Cocinero.findByNombrePais(nombre, pais, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				} else if ((nombre == null) && (apellidos != null) && (pais != null)) {//ApellidosPais
					
					PagedList<Cocinero> list = Cocinero.findByApellidosPais(apellidos, pais, page);
					List<Cocinero> cocineros = list.getList();
					Integer count = list.getTotalPageCount();
					
					if (request().accepts("application/json")) {
					    return Results
							    .ok(Json.toJson(cocineros))
							    .withHeader("X-Cocineros-Count", count.toString());
					} else if (request().accepts("application/xml")) {
						return Results
								.ok(views.xml.cocineros.render(cocineros))
								.withHeader("X-Cocineros-Count", count.toString());
					} else {
						return Results
								.status(415);
					}
				}
				
				
				return Results.notFound();
				
			
			
			}
			
			
		}
		
		public Result addRecetaCocinero(Long idCocinero, Long idReceta) {
			String apikey = request().getQueryString("APIKey");
			if (apikey == null) {
				return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
			}else if (ApiKey.findByKey(apikey) == null){
				return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
			}else {
				Cocinero c = Cocinero.findById(idCocinero);
				Receta r = Receta.findById(idReceta);
				
				if (c == null) {
					return Results.notFound();
				}
				
				if (r == null) {
					return Results.notFound();
				}
				
				c.addReceta(idReceta);
				
				return created();
			}
				
		}	

}
