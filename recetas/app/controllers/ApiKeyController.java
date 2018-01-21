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
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("repeated-apikey")).toJson());
		}

	}
	
	
	public Result deleteApiKey(String key) {
		
		String apikey = request().getQueryString("APIKey");
		if (apikey == null) {
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("no-apikey-sent")).toJson());
		}else if (ApiKey.findByKey(apikey) == null){
			Messages messages = Http.Context.current().messages();
			return Results.status(409, new ErrorObject("2",messages.at("\n" + "apikey-is-incorrect")).toJson());		
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
