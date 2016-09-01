package com.ytsebro.nlp

import org.junit.Assert._
import org.junit.Test

/**
 * Created by yegor on 8/21/16.
 */
class ParserTest {

  def parser = new Parser()
  def testText = "Class performs sentence extraction and POS tagging. It checks if word corresponds to Edges(Links). "

  @Test
  def testParseText(): Unit ={
    val metadata = parser.parseText(testText)
    assertEquals(2, metadata.size)
    val word =  metadata.last._1(0)
    assertEquals("PRP", word.pos)
    assertEquals(1, word.sent)
  }




}
