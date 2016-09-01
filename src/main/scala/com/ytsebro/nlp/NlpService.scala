package com.ytsebro.nlp

import java.io.File

import com.google.gson.GsonBuilder
import com.typesafe.scalalogging.slf4j.LazyLogging
import com.ytsebro.beans.{Node, Link, GraphData, Word}
import com.ytsebro.config.Config
import scala.collection.JavaConversions._
import scala.collection.immutable.Range.Inclusive
import scala.collection.mutable.ArrayBuffer
import spray.json._
import DefaultJsonProtocol._

/**
 * Created by yegor on 22.7.16.
 */
class NlpService extends Serializable with LazyLogging{

  val parser = new Parser()
 
  val defaultProcessor = new EdgeOrientedSentenceProcessor(parser)
  val processors = Map("node" -> new NodeOrientedSentanceProcessor(parser), "edge" -> defaultProcessor)
  val processor = processors.getOrElse(Config.getProp(Config.SENTENCE_PROCESSOR_MODE),  defaultProcessor)

  /**
    * Processes text and returns graph for further visualization
    * @param text
    * @return
    */
  def processText(text: String): GraphData ={


    val sentsAndTexts = parser.parseText(text)

    val sents = sentsAndTexts.map(_._1)
    val texts = sentsAndTexts.map(_._2)
    val graphData =  sents.map(processor.processSentance(_))

    val nodes = graphData.flatMap(_._1)
    val edges = graphData.flatMap(_._2)

    val gson = new GsonBuilder().setPrettyPrinting().create();
    GraphData(nodes.toArray, edges.toArray, texts.toArray)
  }






}
