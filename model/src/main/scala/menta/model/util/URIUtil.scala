package menta.model.util

import java.net.URI
import menta.model.Constant

/**
 * URI manipulating helper (URI format: http://menta.org/ontologies/v.0.2#ClassName.UID)
 * @author talanovm
 * Date: 27.09.11
 * Time: 14:49
 */

object URIUtil {


  def extractAddClassId(in: URI): String = {
    val stringURI = in.toString
    val lastDelimiter = stringURI.lastIndexOf(Constant.URIClassDelimiter)
    val lastNamespaceDelimiter = stringURI.lastIndexOf(Constant.URINamespaceDelimiter)
    if (lastDelimiter > lastNamespaceDelimiter){
      val slice = stringURI.slice(lastNamespaceDelimiter + 1, lastDelimiter)
      val preLastDelimiter = slice.lastIndexOf(Constant.URIClassDelimiter)
      if (preLastDelimiter > 0) {
        val res = slice.substring(preLastDelimiter+1)
        return res
      } else {
        return slice
      }
    } else if (lastNamespaceDelimiter > 0 && lastNamespaceDelimiter < stringURI.length()) {
      val res = stringURI.substring(lastNamespaceDelimiter + 1)
      return res
    }
    ""
  }

}