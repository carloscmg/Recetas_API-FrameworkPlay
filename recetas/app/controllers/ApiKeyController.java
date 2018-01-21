package controllers;

import javax.inject.Inject;

import models.ApiKey;
import models.Etiqueta;
import models.Receta;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;




public class ApiKeyController extends Controller {
	
	@Inject 
	private FormFactory formFactory;
	
	
	public Result createApiKey() {
		//FORM

		Form<ApiKey> form = formFactory
								.form(ApiKey.class)
								.bindFromRequest();
		
		if(form.hasErrors()){
			return Results.status(409, form.errorsAsJson());
		}
		
		ApiKey apikey = form.get();
		
		if(apikey.checkAndCreate()) {
			return Results.created();
		}else {
			return Results.status(409, new ErrorObject("2","ApiKey repetida").toJson());
		}

	}
	
	
	public Result deleteApiKey(String key) {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			return Results.status(409, new ErrorObject("2","No se ha enviado ninguna APIKey").toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			return Results.status(409, new ErrorObject("2","La APIKey es incorrecta").toJson());
		}else {
		
			ApiKey ak = ApiKey.findByKey(key);
			if(ak == null) {
					return ok(); // Por la idempotencia 
			}
			if(ak.delete()) {
				return ok();
			}else {
				return internalServerError();
			}
		}
	}

}
