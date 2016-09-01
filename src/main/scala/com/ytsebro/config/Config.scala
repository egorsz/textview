package com.ytsebro.config

import java.io.{File, FileInputStream}
import java.util.Properties

/**
 * Created by yegor on 9/1/16.
 * Encapsulates properties object with application wide settings
 */
object Config {

  val SENTENCE_PROCESSOR_MODE = "sentance.processor.mode"

  var config: Properties = new Properties()

  def init(path: String): Unit ={
    config.load(new FileInputStream(new File(path)))
  }

  def getProp(name: String): String ={
    config.getProperty(name)
  }


}
