package io.vertx.demo.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WebVerticle extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(WebVerticle.class.getName());
	}
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);
		
		router.get("/").handler(requestHandler->{
			requestHandler.response().end("Hello World");
		});
		
		router.post("/post").handler(this::handlePost);
		
		vertx.createHttpServer().requestHandler(router::accept).listen(3000);
		System.out.println("Server run in port 3000");
	}
	
	private void handlePost(RoutingContext ctx) {
		ctx.response().putHeader("content-type", "text/html").end("post ok");
	}
}
