package com.ytsebro.nlp

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.ytsebro.beans.{Link, Node, Word}

import scala.collection.immutable.Range.Inclusive
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

/**
 * Created by yegor on 8/31/16.
 */
class EdgeOrientedSentenceProcessor(val parser: Parser) extends SentanceProcessor with LazyLogging{



  override def processSentance(words: Array[Word]): (Array[Node], Array[Link]) = {
    val allNodes = words2Nodes(words)

    val rawEdges: Array[Node] = allNodes.filter(t => parser.isEdgePos(t.pos))

    val nodeSeqsIndexes: Array[(Inclusive, Inclusive)] = defineNodesRanges(rawEdges, words)

    val mergedNodes: Array[Node] = mergeNodesEdges(nodeSeqsIndexes, allNodes, rawEdges)

    val merged = mergeNeighbouringEdges(mergedNodes)
    val finalNodes= merged.zipWithIndex.map{case(t, i) => Node(i + t.sent*100, t.text, t.pos, t.sent)}

    logger.debug("------------")
    words.foreach(i => logger.debug(i.toString))
    logger.debug("------------")
    nodeSeqsIndexes.foreach(i => logger.debug(i.toString))
    logger.debug("------------")
    rawEdges.foreach(i => logger.debug(i.toString))
    logger.debug("------------")
    mergedNodes.foreach(i => logger.debug(i.toString))
    logger.debug("------------")
    merged.foreach(i => logger.debug(i.toString))
    logger.debug("------------")
    finalNodes.foreach(i => logger.debug(i.toString))
    logger.debug("------------")

    val edges = finalNodes.filter(t => parser.isEdgePos(t.pos)).map(t => Link(t.id - 1, t.id + 1, t.text, t.pos, t.sent))
    val nodes = finalNodes.filter(t => parser.isNodePos(t.pos))

    (nodes, edges)
  }

  def defineNodesRanges(rawEdges: Array[Node], words: Array[Word]): Array[(Inclusive, Inclusive)] ={
    rawEdges.zipWithIndex.map{case(vb, i) => {
      val pred = if(i > 0) rawEdges(i-1).id else -1
      val next = if(i < rawEdges.size -2) rawEdges(i+1).id else words.size
      val index = vb.id
      val sources = pred +1 to index - 1
      val targets = index + 1 to next - 1
      (sources, targets)
    }}
  }

  def mergeNodesEdges(indexes: Array[(Inclusive, Inclusive)], tuples: Array[Node], rawEdges: Array[Node]): Array[Node] ={
    indexes.zipWithIndex.flatMap{case(indxs, i) => {
      val t = indxs._1
      val t2 = indxs._2
      val a = ArrayBuffer[Node]()
      reduceNodes(t, tuples, a)
      a += rawEdges(i)
      if(i == rawEdges.size - 1){
        reduceNodes(t2, tuples, a)
      }
      a
    }}
  }

  def mergeNeighbouringEdges(result: Array[Node]): Array[Node] ={
    val indArray = result.zipWithIndex.map{case(t, i) => {
      if(parser.isNodePos(t.pos)){
        (i, i)
      } else if(i > 0 && parser.isEdgePos(result(i).pos) && parser.isEdgePos(result(i - 1).pos)){
        (-1, -1)
      } else if(i < result.size - 1 && parser.isNodePos(result(i + 1).pos)){
        (i, i)
      } else if(i == result.size - 1) {
        (i, i)
      }  else {
        var end = i
        val start = i
        var j = i + 1
        while (j < result.size && parser.isEdgePos(result(j).pos)){
          end = end + 1
          j = j + 1
        }
        (start, end)
      }
    }}
    val merged = indArray.filter(_._1 != -1).map(i => {
      val sl = result.toList.subList(i._1, i._2 + 1)
      if(sl.size == 1){
        sl(0)
      } else {
        sl.reduce((t1, t2) => {Node(t1.id, t1.text + ","  +  t2.text, t1.pos, t1.sent)})
      }
    })
    merged
  }


  def reduceNodes(t: Range, tuples: Array[Node], a: ArrayBuffer[Node]): Unit ={
    if(t.size == 1){
      a += tuples(t(0))
    } else if(t.size > 0) {
      var reduced = t.map(tuples(_)).reduce((n1, n2) => Node(n1.id, n1.text + "," + n2.text, n1.pos, n1.sent))
      a += reduced
    }
  }
}
