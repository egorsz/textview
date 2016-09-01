package com.ytsebro.nlp

import java.io.File

import com.ytsebro.beans.{Node, Word}
import org.jsoup.Jsoup
import org.junit.{Assert, Test}
import org.junit.Assert._

import scala.collection.immutable.Range.Inclusive

/**
 * Created by yegor on 25.7.16.
 */
class NlpServiceTest {

  val service  = new NlpService
  def testText = "This class performs sentence extraction and executes part of speech tagging."
  def testText2 = "Cat is animal. My cat was red"

  //Some basic high-level tests
  @Test
  def shouldExtractSentencesOk(): Unit ={

    val graph = service.processText(testText2)

    Assert.assertNotNull(graph)
    assertEquals(2, graph.words.size)
    assertEquals("My cat was red", graph.words(1)._2)
    assertEquals(1, graph.words(1)._1)
  }

  @Test
  def shouldExtractNodesOk(): Unit ={
    val graph = service.processText(testText2)
    val nodes = graph.nodes

    assertEquals("NN",  nodes(0).pos)
    assertEquals("Cat",  nodes(0).text)

    assertEquals("NN",  nodes(1).pos)
    assertEquals("animal",  nodes(1).text)
    assertEquals(0,  nodes(1).sent)
  }

  @Test
  def shouldExtractEdgesOk(): Unit ={
    val graph = service.processText(testText2)
    val edges = graph.edges

    assertEquals("VBZ",  edges(0).pos)
    assertEquals(0,  edges(0).sent)
    assertEquals(0,  edges(0).source)
    assertEquals(2,  edges(0).target)
    assertEquals("is",  edges(0).text)
  }

}
