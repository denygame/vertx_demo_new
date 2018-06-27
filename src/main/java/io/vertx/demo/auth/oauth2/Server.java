package io.vertx.demo.auth.oauth2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.rxjava.core.Vertx;

public class Server extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(Server.class.getName());
	}

	private static final String CLIENT_ID = "e04c825d5705e8dd5ce9";
	private static final String CLIENT_SECRET = "82edbe77ea2a750260dea0e16f3e3fb02846ee4a";

	@Override
	public void start() throws Exception {
		final Router router = Router.router(vertx);
		
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		// Simple auth service which uses a GitHub to authenticate the user
		OAuth2Auth authProvider = GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET);
		
		router.route().handler(UserSessionHandler.create(authProvider));
		// we now protect the resource under the path "/protected"
		router.route("/protected").handler(OAuth2AuthHandler.create(authProvider)
				// we now configure the oauth2 handler, it will setup the callback handler
				// as expected by your oauth2 provider.
				.setupCallback(router.route("/callback"))
				// for this resource we require that users have the authority to retrieve the
				// user emails
				.addAuthority("user:email"));
		// Entry point to the application, this will render a custom template.
		router.get("/").handler(ctx -> {
			// we pass the client id to the template
			ctx.put("client_id", CLIENT_ID);
			ctx.response().end("s");
		});
		// The protected resource
		router.get("/protected").handler(ctx -> {
			AccessToken user = (AccessToken) ctx.user();
			// retrieve the user profile, this is a common feature but not from the official
			// OAuth2 spec
			user.userInfo(res -> {
				if (res.failed()) {
					// request didn't succeed because the token was revoked so we
					// invalidate the token stored in the session and render the
					// index page so that the user can start the OAuth flow again
					ctx.session().destroy();
					ctx.fail(res.cause());
				} else {
					// the request succeeded, so we use the API to fetch the user's emails
					final JsonObject userInfo = res.result();

					// fetch the user emails from the github API

					// the fetch method will retrieve any resource and ensure the right
					// secure headers are passed.
					user.fetch("https://api.github.com/user/emails", res2 -> {
						if (res2.failed()) {
							// request didn't succeed because the token was revoked so we
							// invalidate the token stored in the session and render the
							// index page so that the user can start the OAuth flow again
							ctx.session().destroy();
							ctx.fail(res2.cause());
						} else {
							userInfo.put("private_emails", res2.result().jsonArray());
							// we pass the client info to the template
							//ctx.put("userInfo", userInfo);
							System.out.println(userInfo);
						}
					});
				}
			});
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(3000);
	}
}
