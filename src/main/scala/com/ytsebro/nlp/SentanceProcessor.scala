package com.ytsebro.nlp

import com.ytsebro.beans.{Link, Node, Word}

/**
 * Created by yegor on 8/31/16.
 */
trait SentanceProcessor {

  /**
   * Processed sentance and returns arrays of nodes and edges for it
   * @param words
   * @return
   */
  def processSentance(words: Array[Word]): (Array[Node], Array[Link])

  //convert words to nodes
  def words2Nodes(words: Array[Word]): Array[Node] ={
    words.zipWithIndex.map{case(t, i)=> Node(i, t.word, t.pos, t.sent)}
  }


}
