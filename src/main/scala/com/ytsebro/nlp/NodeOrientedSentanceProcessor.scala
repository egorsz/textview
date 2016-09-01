package com.ytsebro.nlp

import com.ytsebro.beans.{Link, Node, Word}

import scala.collection.immutable.Range.Inclusive

/**
 * Created by yegor on 8/31/16.
 */
class NodeOrientedSentanceProcessor(val parser: Parser) extends SentanceProcessor{

  override def processSentance(words: Array[Word]): (Array[Node], Array[Link]) = {

    val allNodes = words2Nodes(words).map(n => Node(n.id + 100*n.sent, n.text, n.pos, n.sent))

    val edges = allNodes.map(t => Link(t.id, t.id + 1, "", "", t.sent))
    val nodes = allNodes

    (nodes, edges)

  }
}
