package io.vertx.demo.data.mongodb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.Vertx;
import io.vertx.core.json.*;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ServerMongo extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(ServerMongo.class.getName());
	}

	@Override
	public void start() throws Exception {

		final MongoClient mongo = MongoClient.createShared(vertx,
				new JsonObject().put("connection_string", "mongodb://127.0.0.1:27017").put("db_name", "mydb"));
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		router.get("/users").handler(ctx -> {
			mongo.find("sinhvien", new JsonObject(), lookup -> {
				final JsonArray json = new JsonArray();
				for (JsonObject o : lookup.result()) {
					json.add(o);
				}
				ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				ctx.response().end(json.encode());
			});
		});

		router.post("/users").handler(ctx -> {
			JsonObject user = new JsonObject().put("dept_id", ctx.request().getFormAttribute("dept_id"))
					.put("dept_no", ctx.request().getFormAttribute("dept_no"))
					.put("dept_name", ctx.request().getFormAttribute("dept_name"))
					.put("location", ctx.request().getFormAttribute("location"));

			mongo.insert("sinhvien", user, lookup -> {
				// inform that the document was created
				ctx.response().setStatusCode(201);
				ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				ctx.response().end(new JsonObject().put("msg", "success").encode());
			});
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}
