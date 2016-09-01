package com.ytsebro.nlp

import com.ytsebro.beans.Word
import edu.stanford.nlp.simple.Document
import scala.collection.JavaConversions._

/**
 * Created by yegor on 8/21/16.
 */
class Parser {

  /*
   *Default arrays of POS'es which we suppose to be nodes or edges
   */
  val nodePos = Array("NN", "PRP", "JJ", "DT", "WP", "CD")
  val edgePos = Array("VB", "MD", "RB", "CC", "IN", "UP", "TO")

  /*val nodePos = Array("NN")
  val edgePos = Array("VB" )*/

  /**
   * Performs sentances extraction and POS tagging.
   * @param text
   * @return Seq[(Array[Word], (Int, String))] array of words and text corresponding to each sentence
   */
  def parseText(text: String): Seq[(Array[Word], (Int, String))] ={


    val doc = new Document(text)
    val sentsAndTrees = doc.sentences().zipWithIndex.map{ case(sent, i) =>{
      val words = sent.words().zip(sent.posTags()).filter(t => isEdgePos(t._2) || isNodePos(t._2)).map{case(t, s) => new Word(t, s, i) }
      //words.foreach(println)
      (words.toArray, (i, sent.text()))
    }}
    sentsAndTrees.toSeq
  }

  /**
   * Checks if word corresponds to Nodes
   * @param pos
   * @return
   */
  def isNodePos(pos: String): Boolean ={
    nodePos.filter(pos.contains(_)).nonEmpty
  }

  /**
   * Checks if word corresponds to Edges(Links)
   * @param pos
   * @return
   */
  def isEdgePos(pos: String): Boolean ={
    edgePos.filter(pos.contains(_)).nonEmpty
  }

}
