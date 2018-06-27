package io.vertx.demo.rxjava.web.client.zip;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

public class Server extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		HttpServer server = vertx.createHttpServer();
		server.requestStream().toObservable().subscribe(req -> {
			req.response().putHeader("content-type", "application/json")
					.end(new JsonObject().put("time", System.currentTimeMillis()).toString());
		});
		server.listen(8080);
	}
}