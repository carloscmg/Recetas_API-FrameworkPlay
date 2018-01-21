package controllers;

import java.util.List;

import javax.inject.Inject;

import io.ebean.PagedList;
import models.Etiqueta;
import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;






public class EtiquetaController extends Controller {

	@Inject 
	private FormFactory formFactory;
	

		//No hay metodo de creacion de etiquetas por que se crean en el RecetaController 
	
	

		@With(CheckAPIKeyAction.class)
		public Result updateEtiqueta(String nombre) {//se actualiza una etiqueta dandole su nombre
			
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
				Messages messages = Http.Context.current().messages();
				return Results.status(409, new ErrorObject("2",messages.at("error-in-the-UPDATE")).toJson());
			}

		}
		
		@With(CheckAPIKeyAction.class)
		public Result deleteEtiqueta(String nombre) {//Se borra una etiqueta dandole su nombre
			
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
		
		@With(CheckAPIKeyAction.class)
		public Result retrieveEtiquetasCollection(Integer page) {// Devuelve de panera paginada la coleccion de etiquetas
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
		
		@With(CheckAPIKeyAction.class)
		public Result retrieveByEtiqueta(String etiquetaName) { // Devuelve todas las recetas que pertenece a determinada etiqueta
			
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