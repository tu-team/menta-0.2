package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.Knowledge
import java.net.URI

/**
 * User: toscheva
 * Date: 17.08.11
 * Time: 17:40
 * Class is used in serialization in HGDB
 */

/**
 * @class KnowledgeProtocol helper class for
 */
trait KnowledgeProtocol {

  /**
   * Read properties from steam
   * @param in - stream of sbinary
   * @param obj - object to Update
   */
  def readProperties(in: Input, obj: Knowledge) = {
    //extract url from stream
    obj.uri = readURI(in)
  }

  def writeURI(out: Output, vl: URI) = {
    var emptyURI = new URI("http://menta.org/EmptyURI");
    if (vl == null) write[URI](out, emptyURI)
    else write[URI](out, vl)
  }

  def readURI(in: Input): URI = {
    var vl = read[URI](in);
    if (vl.toString == "http://menta.org/EmptyURI") {
      return null;
    }
    return vl;
  }

  def writeString(out: Output, vl: String) = {
    var emptyURI = "empty_ser_string";
    if (vl == null) write[String](out, emptyURI)
    else write[String](out, vl)
  }

  def readString(in: Input): String = {
    var vl = read[String](in);
    if (vl == "empty_ser_string") {
      return null;
    }
    return vl;
  }


  /**
   * write properties to stream
   * @param out - stream of sbinary
   * @param obj - object to write
   */
  def writeProperties(out: Output, obj: Knowledge) = {
    //write URI to stream
    if (obj.uri != null) writeURI(out, obj.uri)
  }

}