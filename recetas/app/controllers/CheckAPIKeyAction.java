package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import models.ApiKey;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;

public class CheckAPIKeyAction extends Simple {  //Se encarga de comprobar la APIKey

	@Override
	public CompletionStage<Result> call(Context ctx) {
		String apikey = ctx.request().getQueryString("APIKey");
		Messages messages = Http.Context.current().messages();
		if (apikey == null){
			return CompletableFuture.supplyAsync(
				() -> {
					return status(409, new ErrorObject("2",messages.at("no-apikey-sent")).toJson());
				});
		} else if (ApiKey.findByKey(apikey) == null){
			return CompletableFuture.supplyAsync(
					() -> {
						return status(409, new ErrorObject("2",messages.at("apikey-is-incorrect")).toJson());
					});
		}
		return this.delegate.call(ctx);
	
	}
}
