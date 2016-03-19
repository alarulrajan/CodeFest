/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 15, 2006
 * Time: 12:41:45 PM
 */
package com.technoetic.xplanner.webservers;

import java.io.File;

import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.start.Monitor;

/**
 * The Class JettyServer.
 */
public class JettyServer {
	
	/**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
	public static void main(final String[] args) throws Exception {
		JettyServer.start();
	}

	/**
     * Start.
     *
     * @throws Exception
     *             the exception
     */
	public static void start() throws Exception {
		final HttpServer server = new Server();
		final SocketListener listener = new SocketListener();
		listener.setPort(8080);
		server.addListener(listener);

		final WebApplicationContext context = new WebApplicationContext("war");
		context.setContextPath("/xplanner");
		context.setTempDirectory(new File("build"));
		context.setIgnoreWebJetty(true);
		server.addContext(context);

		Monitor.monitor();

		server.start();
	}

	/**
     * Stop.
     */
	public static void stop() {
		// Main.main(null);
	}
}