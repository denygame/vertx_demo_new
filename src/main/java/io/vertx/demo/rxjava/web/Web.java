package io.vertx.demo.rxjava.web;

import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;

public class Web extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(Web.class.getName());
	}
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);

	    router.route().handler(routingContext -> {
	      routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
	    });

	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}
