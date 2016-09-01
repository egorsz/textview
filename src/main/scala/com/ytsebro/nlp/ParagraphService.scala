package com.ytsebro.nlp

import com.ytsebro.beans.Paragraph

import scala.util.matching.Regex

/**
 * Created by yegor on 8/9/16.
 */
class ParagraphService {

  def extract(text: String, regex: String, flat: Boolean): Paragraph ={
    val fragments = text.split(regex)
    val names = regex.r.findAllIn(text)
    val nms = names.map(_.toString).toArray

    val childs = fragments.drop(1).zip(nms).map{case (f, n) => new Paragraph(n, f, f, Array())}
    if(!flat){
      childs.foreach(c => c.children = findChilds(c, childs))
    }


    new Paragraph("root", "", "", childs.filter( c =>  flat || c.name.split("\\.").length < 2))
  }

  def findChilds(p: Paragraph, pars: Array[Paragraph]): Array[Paragraph] ={
    pars.filter(child => child.name.startsWith(p.name) && p.name.replaceAll("[.]$", "").length + 2 == child.name.length)
  }

}
