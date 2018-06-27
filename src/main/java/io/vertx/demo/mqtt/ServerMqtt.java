package io.vertx.demo.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.ArrayList;
import java.util.List;
import io.vertx.rxjava.core.Vertx;

public class ServerMqtt extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(ServerMqtt.class.getName());
	}

	@Override
	public void start() throws Exception {

		MqttServer mqttServer = MqttServer.create(vertx);

		mqttServer.endpointHandler(endpoint -> {
			// shows main connect info
			System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = "
					+ endpoint.isCleanSession());
			System.out.println("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

			// accept connection from the remote client
			endpoint.accept(false);

			// handling requests for subscriptions
			endpoint.subscribeHandler(subscribe -> {
				
				List<MqttQoS> grantedQosLevels = new ArrayList<>();
				for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
					System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
					grantedQosLevels.add(s.qualityOfService());
				}
				// ack the subscriptions request
				endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

				// just as example, publish a message on the first topic with requested QoS
				endpoint.publish(subscribe.topicSubscriptions().get(0).topicName(),
						Buffer.buffer("Hello from the Vert.x MQTT server"),
						subscribe.topicSubscriptions().get(0).qualityOfService(), false, false);
			});

			// handling requests for unsubscriptions
			endpoint.unsubscribeHandler(unsubscribe -> {

				for (String t : unsubscribe.topics()) {
					System.out.println("Unsubscription for " + t);
				}
				// ack the subscriptions request
				endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
			});

			// handling ping from client
			endpoint.pingHandler(v -> {
				System.out.println("Ping received from client");
			});

			// handling disconnect message
			endpoint.disconnectHandler(v -> {
				System.out.println("Received disconnect from client");
			});

			// handling closing connection
			endpoint.closeHandler(v -> {
				System.out.println("Connection closed");
			});

		}).listen(1883, "0.0.0.0", ar -> {
			if (ar.succeeded()) {
				System.out.println("MQTT server is listening on port " + mqttServer.actualPort());
			} else {
				System.err.println("Error on starting the server" + ar.cause().getMessage());
			}
		});
	}
}
