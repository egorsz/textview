package com.ytsebro.web

import java.io.{File, FileInputStream}
import java.util.Properties

import com.ytsebro.config.Config
import org.eclipse.jetty.server.{ServerConnector, Handler, Server}
import org.eclipse.jetty.server.handler.{DefaultHandler, HandlerList, ResourceHandler}
import org.eclipse.jetty.servlet.{ServletHolder, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext

/**
 * Created by yegor on 15.7.16.
 * Starts embedded Jetty server with web app deployed on it
 */
object StartServer {


  val WEB_APP_PATH = "/src/main/webapp"
  val WEB_XML_PATH = "/src/main/webapp/WEB-INF/web.xml"
  val HTTP_PORT_NAME = "http.port"

  def main(args: Array[String]): Unit = {

    //read properties

    Config.init(args(0))


    val server = new Server();
    val connector = new ServerConnector(server);
    connector.setPort(Integer.parseInt( Config.getProp(HTTP_PORT_NAME)));
    server.addConnector(connector);
    val handlers = new HandlerList();
    val context = new WebAppContext();
    context.setDescriptor( args(1) +   WEB_XML_PATH);
    context.setResourceBase(args(1) + WEB_APP_PATH);
    context.setContextPath("/");
    context.setParentLoaderPriority(true);
    server.setHandler(context);
    server.start();
    server.join();
  }

}
