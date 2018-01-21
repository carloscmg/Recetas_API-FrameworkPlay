package controllers;

import javax.inject.Inject;

import models.ApiKey;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;




public class ApiKeyController extends Controller {
	
	@Inject 
	private FormFactory formFactory;
	
	
	public Result createApiKey() {// Se crea el APIKey 
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
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("repeated-apikey")).toJson());
		}

	}
	
	@With(CheckAPIKeyAction.class)
	public Result deleteApiKey(String key) {// Se borra el APIKey
		
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
