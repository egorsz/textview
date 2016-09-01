package com.ytsebro.nlp

import com.ytsebro.beans.Word
import org.junit.Test
import org.junit.Assert._

/**
 * Created by yegor on 9/1/16.
 */
class NodeOrientedSentenceProcessorTest {

  val parser = new Parser()
  val processor = new NodeOrientedSentanceProcessor(parser)

  //words for 1 sentence
  def createWords(): Array[Word] ={
    Array(
      Word("This", "DT", 0),
      Word("class", "NN", 0),
      Word("performs", "VBZ", 0),
      Word("sentence", "NN", 0),
      Word("extraction", "NN", 0)
    )
  }

  @Test
  def testProcessSentence(): Unit ={
    val graph =  processor.processSentance(createWords())
    assertEquals(5, graph._1.size)
    assertEquals(5, graph._2.size)
  }


}
