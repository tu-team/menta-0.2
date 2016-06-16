package menta.dao.impl

import reflect.BeanInfo
import menta.model.Knowledge
import menta.dao.Utils
import java.net.URI


/**
 * Wrapper to Knowledge class to make HGDB work properly.
 * Author: Aidar Makhmutov
 * Date: 16.03.2011
 */
// TODO add Class diagram and wiki page with description.
@BeanInfo
class KnowledgeWrapper(anURI: String, aKnowledge: Knowledge) {
  def this() = this(null,null)

  var uri: String = null
  var knowledge = this.aKnowledge
  if (knowledge!=null)  {
    knowledge.uri=new URI(anURI)
    uri= anURI
  }

  def getUri = uri
  def setUri (someUri: String) = {
    this.uri = someUri
  }
  def getKnowledge = this.knowledge
  def setKnowledge( someKnowledge: Knowledge) = {
    this.knowledge = someKnowledge 
  }
}