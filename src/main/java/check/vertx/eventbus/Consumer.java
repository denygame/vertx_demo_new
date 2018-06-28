package check.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class Consumer extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("echo", message -> {
			JsonObject messageBody = (JsonObject) message.body();
			messageBody.put("echo", true);
			message.reply(messageBody);
		});
		
		vertx.eventBus().consumer("ex", message -> {
			JsonObject messageBody = (JsonObject) message.body();
			messageBody.put("ex", true);
			message.reply(messageBody);
		});
		
		vertx.eventBus().consumer("cls", message->{
			JsonObject messageBody = (JsonObject) message.body();
			messageBody.clear();
			message.reply(messageBody);
		});
	}
}
