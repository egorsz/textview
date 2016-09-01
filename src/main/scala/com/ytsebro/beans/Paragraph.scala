package com.ytsebro.beans

/**
 * Created by yegor on 8/9/16.
 */
class Paragraph (var name: String,var text: String,var tags: String,var children: Array[Paragraph]){
  override def toString = s"Paragraph($name, $text, $tags, $children)"
}
