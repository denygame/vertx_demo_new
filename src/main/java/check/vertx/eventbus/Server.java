package check.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class Server extends AbstractVerticle {
	JsonObject messageBody = null;

	@Override
	public void start() throws Exception {
		vertx.createHttpServer().requestHandler(request -> {
			messageBody = new JsonObject().put("web", "first");

			vertx.eventBus().send("echo", messageBody, reply -> {
				JsonObject echoMess = (JsonObject) reply.result().body();
				messageBody = echoMess;
				
				vertx.eventBus().send("ex", messageBody, reply1 -> {
					messageBody = (JsonObject) reply.result().body();
					request.response().end(messageBody.encodePrettily());
				});
			});


		}).listen(3214);
	}
}