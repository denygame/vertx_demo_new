package io.vertx.demo.rxjava.web.client.simple;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

public class Client extends AbstractVerticle {

  public static void main(String[] args) {
	  Vertx.vertx().deployVerticle(Client.class.getName());
  }

  @Override
  public void start() throws Exception {
    WebClient client = WebClient.create(vertx);
    Single<HttpResponse<String>> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.string())
      .rxSend();

    // Fire the request
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));

    // Again
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));

    // And again
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));
  }
}
