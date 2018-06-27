package io.vertx.demo.rxjava.web;

import io.vertx.core.file.OpenOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileSystem;
import rx.Observable;


public class ReadStream extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(ReadStream.class.getName());
	}
	@Override
	public void start() throws Exception {
		FileSystem fs = vertx.fileSystem();
		fs.open("data.txt", new OpenOptions(), result -> {
			AsyncFile file = result.result();
			Observable<Buffer> observable = file.toObservable();
			observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
		});
	}
}
