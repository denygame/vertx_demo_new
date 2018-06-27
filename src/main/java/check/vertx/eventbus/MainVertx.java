package check.vertx.eventbus;

import io.vertx.core.Vertx;

public class MainVertx {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Consumer());
		vertx.deployVerticle(new Server());
	}
}
