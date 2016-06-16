package menta.model

import java.net.URI
import org.hypergraphdb._
import `type`.HGAbstractCompositeType.Projection
import `type`.{HGAtomType, HGProjection, HGCompositeType}
import java.lang.String
import java.util.Iterator
import util.MCloneable
import util.serialization.ProtocolRegistry
import reflect.BeanInfo

/**
 * Main trait for all the Domain model.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 13:06
 */

trait Knowledge extends HGAtomType with HGHandleHolder with Cloneable with MCloneable {


  type propertyType = Map[URI, KnowledgeClass]

  //no default URI, if null then entity is new
  private var theURI: URI =null// Constant.modelNamespace

  private var theGraph = new HyperGraph

  private var theHandle:HGHandle=null

  private var rootElement:Boolean =false;

  def isRoot=
  {
    if (this.uri!=null && uri.toString.lastIndexOf("#")>0 && uri.toString.lastIndexOf(".")>0 && uri.toString.lastIndexOf(".")>(uri.toString.lastIndexOf("#")+2))
    {
      val searchString= this.uri.toString.substring(uri.toString.lastIndexOf("#")+1,uri.toString.lastIndexOf("."))
      searchString.contains(Constant.rootElementID)
    }
    rootElement;

  }



  def isRoot_=(aVal:Boolean)=rootElement=aVal


  def uri = theURI

  def uri_=(aURI: URI) = theURI = aURI

  def className:String = null

  def className_=(aClassName: String) = {
       theURI = new URI(menta.model.Constant.modelNamespaceString + aClassName)
  }

  def setHyperGraph(graph: HyperGraph): Unit = {
    theGraph = graph
  }

  def subsumes(p1: AnyRef, p2: AnyRef): Boolean = false

  def release(handle: HGPersistentHandle): Unit = {
    theGraph.getStore.removeData(handle)
  }

  def store(reference: AnyRef): HGPersistentHandle = {

    val data = ProtocolRegistry.serialize(reference)
    return theGraph.getStore().store(data);

  }

  /**
   * load all children and place it to internal storage
   */
  def loadChildren(graph:HyperGraph, handle:HGHandle) {}

  def extractHandle(): String = {
    if (uri ==null) return null
    val stringURI = uri.toString
    val delimiterIndex = stringURI.lastIndexOf(".")
    if (delimiterIndex > 0 && delimiterIndex < stringURI.length()){
      return stringURI.substring(delimiterIndex + 1)
    }
    ""

  }

  def checkIfSaved():Boolean={

    if (uri==null) return false;
    val stringURI = uri.toString
    val delimiterIndex = stringURI.lastIndexOf(".")
    val hashIndex =  stringURI.lastIndexOf("#")

    if (delimiterIndex > 0 && delimiterIndex>hashIndex && delimiterIndex < stringURI.length() && hashIndex>0){
      return true
    }
    return false
  }

  /**
   * return all children to make cycle across data
   */
  def getAllChildren(): List[Knowledge] = {
    Nil
  }
  /**
   * save all children
   */
  def saveChildren(graph:HyperGraph, handle:HGHandle) {}

  def make(handle: HGPersistentHandle, p2: LazyRef[Array[HGHandle]], p3: IncidenceSetRef): AnyRef = {
    val data = theGraph.getStore.getData(handle)
    return ProtocolRegistry.deserialize(data, this)
  }

  /*def getProjection(p1: String): HGProjection = {
    //if (p1=="uri")  return this.uri;
    return new Projection()
    return null
  }

  def getDimensionNames: Iterator[String] = {
    var res = new java.util.ArrayList[String]()
    res.add("uri")
    return res.iterator

  } */
  def getAtomHandle: HGHandle =
  {
    return theHandle
  }

  def setAtomHandle(p1: HGHandle)
  {
    theHandle=p1

  }

  override def clone()=super.clone()

  def mCopy(trgt:Knowledge)=
  {

    trgt.isRoot=isRoot
    trgt.className=className

  }
}