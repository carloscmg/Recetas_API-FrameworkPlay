package controllers;

import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;

import java.util.List;

import javax.inject.Inject;

import io.ebean.PagedList;
import models.Cocinero;

import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

public class RecetaController extends Controller {

@Inject 
private FormFactory formFactory;


	
	@With(CheckAPIKeyAction.class)
	public Result createReceta() {// Creacion de la Receta
		
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
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("repeated-recipe")).toJson());
		}
		

	}
	

	@With(CheckAPIKeyAction.class)
	public Result retrieveReceta(Long id) {// Devuelve una receta dandole su id
		
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
	
	@With(CheckAPIKeyAction.class)
	public Result updateReceta(Long id) {// actualiza una receta dandole su id
		
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
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("error-in-the-UPDATE")).toJson());
		}
	
	}
	
	@With(CheckAPIKeyAction.class)
	public Result deleteReceta(Long idReceta) {// borra una receta dandole su id
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
	
	@With(CheckAPIKeyAction.class)
	public Result retrieveRecetasCollection(Integer page) {// Saca de manera paginada la collecion de recetas, y permite
		//a traves de query string la busqueda por dificultad, titulo y tipoCocina
		
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
			}else if ((dificultad != null) && (titulo != null) && (tipo != null)) {//DificultadTituloTipo
				PagedList<Receta> list = Receta.findByDificultadTipoTitulo(dificultad, titulo, tipo, page);
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
	
	@With(CheckAPIKeyAction.class)
	public Result haveCocinero(Long id) {//Devuelve el cocinero al que pertenece una receta
		
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
	
	@With(CheckAPIKeyAction.class)
	public Result assignEtiqueta(Long idReceta, String etiquetaName) { //Asigna una etiqueta y si no exite la crea
		
		Receta r = Receta.findById(idReceta);
		
		if(r == null) {
			return Results.notFound();
		}
		
		if(r.addEtiquetaAndSave(etiquetaName)) {
			return  created();
		}else {
			
			r.deleteEtiqueta(etiquetaName);
			
			Messages messages = Http.Context.current().messages();
			return ok(messages.at("unassigned-tag"));
		}
		
	}
	

	
}