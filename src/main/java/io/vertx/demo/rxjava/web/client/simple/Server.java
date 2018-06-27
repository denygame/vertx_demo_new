package io.vertx.demo.rxjava.web.client.simple;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;


public class Server extends AbstractVerticle {

  public static void main(String[] args) {
	  Vertx.vertx().deployVerticle(Server.class.getName());
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    server.requestStream().toObservable().subscribe(req -> {
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
    });
    server.listen(8080);
  }
}

