package io.vertx.demo.web.webclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class WebClientVerticle extends AbstractVerticle {
	private String token = "";
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new WebClientVerticle());
	}
	
	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);

		MultiMap form = MultiMap.caseInsensitiveMultiMap();
		form.set("username", "po");
		form.set("password", "123");

		client.post(80, "localhost", "/mine/restful_ttgt/api/v1/auth/login").sendForm(form, req -> {
			if (req.succeeded()) {
				HttpResponse<Buffer> response = req.result();

				JsonObject json = response.body().toJsonObject();
				this.token = ((JsonObject) json.getValue("data")).getValue("token").toString();
				System.out.println("Token: " + this.token);
			} else {
				System.out.println("Something went wrong " + req.cause().getMessage());
			}
		});

		client.get(80, "localhost",
				"/mine/restful_ttgt/api/v1/places/suggest?token=" + this.token + "&key=ly thuong kiet").send(ar -> {
					if (ar.succeeded()) {
						// Obtain response
						HttpResponse<Buffer> responseGet = ar.result();

						JsonObject jsonGet = responseGet.body().toJsonObject();
						System.out.println(jsonGet);
					} else {
						System.out.println("Something went wrong " + ar.cause().getMessage());
					}
				});
	}
}
