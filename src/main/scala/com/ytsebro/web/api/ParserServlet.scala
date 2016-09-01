package com.ytsebro.web.api

/**
 * Created by yegor on 22.7.16.
 * 
 */

import java.io.File

import com.google.gson.GsonBuilder
import com.ytsebro.nlp.NlpService
import org.scalatra._

class ParserServlet extends ScalatraFilter {


  val service = new NlpService()

  get("/api") {
    val text = params("name")
    service.processText(text)
  }

  post("/api") {

    val text = request.body
    val data = service.processText(text)
    val gson = new GsonBuilder().setPrettyPrinting().create();
    gson.toJson(service.processText(text))

  }

  //not implemented
  post("/api/sent") {
    val text = request.body +  "\n"
  }
}