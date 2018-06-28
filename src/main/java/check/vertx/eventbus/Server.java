package check.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class Server extends AbstractVerticle {
	JsonObject messageBody = null;

	@Override
	public void start() throws Exception {
		this.messageBody = new JsonObject().put("web", "first");

		vertx.eventBus().send("echo", this.messageBody, reply -> {
			JsonObject echoMess = (JsonObject) reply.result().body();
			this.messageBody = echoMess;
			System.out.println(this.messageBody);

			vertx.eventBus().send("ex", this.messageBody, reply1 -> {
				this.messageBody = (JsonObject) reply1.result().body();
				System.out.println(this.messageBody);
			});
		});

		vertx.eventBus().send("cls", this.messageBody, reply -> {
			this.messageBody = (JsonObject) reply.result().body();
			System.out.println(this.messageBody);
		});

		vertx.setTimer(10000, handler -> {
			System.out.println(this.messageBody);
		});
	}
}