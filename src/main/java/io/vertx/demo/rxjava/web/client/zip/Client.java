package io.vertx.demo.rxjava.web.client.zip;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;


public class Client extends AbstractVerticle {

  public static void main(String[] args) {
	  Vertx.vertx().deployVerticle(Client.class.getName());
  }

  @Override
  public void start() throws Exception {

    // Create two requests
    WebClient client = WebClient.create(vertx);
    Single<JsonObject> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(resp -> resp.body());
    
    // Combine the responses with the zip into a single response
    request
      .zipWith(request, (b1, b2) -> new JsonObject().put("req1", b1).put("req2", b2))
      .subscribe(json -> {
        System.out.println("Got combined result " + json);
      }, err -> {
        err.printStackTrace();
      });
  }
}