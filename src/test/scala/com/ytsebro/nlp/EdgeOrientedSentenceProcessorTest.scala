package com.ytsebro.nlp

import com.ytsebro.beans.{Node, Word}
import org.junit.Assert._
import org.junit.{Assert, Test}

import scala.collection.immutable.Range.Inclusive

/**
 * Created by yegor on 8/31/16.
 */
class EdgeOrientedSentenceProcessorTest {
  
  val parser = new Parser()
  val processor = new EdgeOrientedSentenceProcessor(parser)

  /*low-level tests*/

  //words for 1 sentence
  def createWords(): Array[Word] ={
    Array(
      Word("This", "DT", 0),
      Word("class", "NN", 0),
      Word("performs", "VBZ", 0),
      Word("sentence", "NN", 0),
      Word("extraction", "NN", 0),
      Word("and", "CC", 0),
      Word("executes", "VBZ", 0),
      Word("part", "NN", 0),
      Word("of", "IN", 0),
      Word("speech", "NN", 0),
      Word("tagging", "NN", 0)
    )
  }

  @Test
  def shouldDefineNodeWordRangesOk(): Unit ={
    val words = createWords()
    val tuples = processor.words2Nodes(words)
    val rawEdges: Array[Node] = tuples.filter(t => processor.parser.isEdgePos(t.pos))
    val indexes: Array[(Inclusive, Inclusive)] = processor.defineNodesRanges(rawEdges, words)
    println(indexes)

    assertEquals(4, indexes.size)
    assertEquals(0 to 1, indexes(0)._1)
    assertEquals(3 to 4, indexes(0)._2)
  }

  @Test
  def shouldMergeNodesOk(): Unit ={
    val words = createWords()
    val tuples = processor.words2Nodes(words)

    val rawEdges: Array[Node] = tuples.filter(t => processor.parser.isEdgePos(t.pos))

    val indexes: Array[(Inclusive, Inclusive)] = processor.defineNodesRanges(rawEdges, words)
    val mergedNodes: Array[Node] = processor.mergeNodesEdges(indexes, tuples, rawEdges)
    println(mergedNodes)
    assertEquals(8, mergedNodes.size)
    assertEquals("This,class", mergedNodes(0).text)
  }

  @Test
  def shouldMergeEdgesOk(): Unit ={
    val words = createWords()
    val tuples = processor.words2Nodes(words)
    val rawEdges: Array[Node] = tuples.filter(t => processor.parser.isEdgePos(t.pos))

    val indexes: Array[(Inclusive, Inclusive)] = processor.defineNodesRanges(rawEdges, words)
    val mergedNodes: Array[Node] = processor.mergeNodesEdges(indexes, tuples, rawEdges)
    val merged = processor.mergeNeighbouringEdges(mergedNodes)

    println(mergedNodes)
    assertEquals(7, merged.size)
    assertEquals("and,executes", merged(3).text)
  }

}
