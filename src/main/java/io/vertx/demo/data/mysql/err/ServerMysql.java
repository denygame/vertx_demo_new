package io.vertx.demo.data.mysql.err;
//package io.vertx.demo.data.mysql;
//
//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.Vertx;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.asyncsql.MySQLClient;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.sql.SQLConnection;
//
//public class ServerMysql extends AbstractVerticle {
//	
//	public static void main(String[] args) {
//		Vertx.vertx().deployVerticle(new ServerMysql());
//	}
//	
//	@Override
//	public void start() throws Exception {
//		SQLClient client = MySQLClient.createShared(vertx, new JsonObject().put("host", "localhost"));
//		
//		client.getConnection(res -> {
//			if (res.succeeded()) {
//				
//				SQLConnection connection = res.result();
//				
//				System.out.println(connection);
//				
//			} else {
//				// Failed to get connection - deal with it
//			}
//		});
//
//	}
//}
