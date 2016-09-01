package com.ytsebro.nlp

import java.io.InputStream

import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.junit.{Test}
import org.junit.Assert._

/**
 * Created by yegor on 8/9/16.
 */
class ParagraphServiceTest {

  val service: ParagraphService = new ParagraphService()

  def readTestFile(filename: String): String ={
    val is = getTestStream(filename)
    IOUtils.toString(is)
  }

  def getTestStream(filename: String): InputStream ={
    this.getClass.getResourceAsStream(filename)
  }

  @Test
  def testExtract1(): Unit ={
    val p1 = service.extract(readTestFile("/paragraph/text1.txt"), "(?m)^(\\d+[.]?)+", false)
    assertEquals(p1.children.length, 2)
    assertEquals(p1.children(0).name, "2")
    assertEquals(p1.children(0).children(1).name, "2.2")
    println(p1)
  }

  @Test
  def testExtract2(): Unit ={
    val p1 = service.extract(readTestFile("/paragraph/text2.txt"), "(?m)^(\\d+[.]?)+", false)
    assertEquals(p1.children.length, 2)
    assertEquals(p1.children(0).name, "1.")
    assertEquals(p1.children(1).children(1).name, "2.2")
    println(p1)
  }

  @Test
  def testExtract3(): Unit ={
    val p1 = service.extract(readTestFile("/paragraph/text3.txt"), "(?m)^.*?\\[edit\\]", true)
    println(p1)
  }

  @Test
  def testExtract4(): Unit ={
    val p1 = service.extract(readTestFile("/paragraph/text4.txt"), "(?m)^Scala$", true)
    println(p1)
  }

  @Test
  def testJsoup(): Unit ={
    val doc = Jsoup.connect("https://apacheignite.readme.io/docs/getting-started").get();
    val text: String = doc.body.text
    println(text)
  }

}
