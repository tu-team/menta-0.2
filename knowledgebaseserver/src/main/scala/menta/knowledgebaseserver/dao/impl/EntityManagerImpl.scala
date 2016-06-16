package menta.knowledgebaseserver.dao.impl

import org.hypergraphdb.HGQuery.hg


import org.slf4j.LoggerFactory
import menta.dao.EntityManager
import java.net.URI
import scala.None
import menta.dao.impl.{EntityTransactionImpl, KnowledgeWrapper}
import menta.model.Knowledge
import org.hypergraphdb.handle.{UUID, UUIDPersistentHandle}
import menta.model.howto.{ActionClass, AddClass}
import collection.JavaConversions._
import menta.model.util.serialization.ProtocolRegistry
import org.hypergraphdb.{HGValueLink, HGPersistentHandle, HyperGraph}
import org.hypergraphdb.query.{AtomTypeCondition, And}

/**
 * Implementation via the HyperGraphDB
 *
 * @author Aidar Makhmutov
 * @author max talanov
 * Date: 23.02.2011
 */
class EntityManagerImpl(theGraph: HyperGraph) extends EntityManager {

  val log = LoggerFactory.getLogger(this.getClass)
  var theCacheMap: Map[URI, Knowledge] = Map[URI, Knowledge]()

  def cacheMap = this.theCacheMap

  def cacheMap_(aCache: Map[URI, Knowledge]) {
    this.theCacheMap = aCache
  }

  def cacheMap_=(aCache: Map[URI, Knowledge]) {
    this.theCacheMap = aCache
  }

  def getGraph = this.theGraph

  def graph = this.theGraph


  def getLinkedObjects(src: String, linkName: String): List[Knowledge] = {

    var sourceObject = find(src).get
    var handle = graph.getHandle(sourceObject);
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle)))

    //filter
    lnks = lnks.filter(p => p.getValue == linkName)

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var children = hg.getAll[Knowledge](graph, hg.target(graph.getHandle(lnk)))
      if (children.length > 0) return children.toList
    }

    null
  }

  def linkObjects(src: String, target: String, linkName: String) {
    var sourceObject = find(src).get
    var targetObject = find(target).get

    var hdlSrc = graph.getHandle(sourceObject)
    var hdlTarget = graph.getHandle(targetObject);

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(hdlSrc)))

    //filter
    lnks = lnks.filter(p => p.getValue == linkName)

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      return
      //lnks.foreach(u=>
      //{
      //  graph.remove(graph.getHandle(u))
      //});
    }
    //format array
    var lst = List(hdlSrc, hdlTarget)

    lnk = new HGValueLink(linkName, lst: _*)

    graph.add(lnk)


  }

  //private val transaction: EntityTransaction = null

  /**
   * Finds entity by its URI.
   */
  def find(uid: String): Option[Knowledge] = {
    // log.debug("find({})", uri)
    try {
      //try to find

      var hdl = graph.getHandleFactory().makeHandle(uid)
      //cache is not neccessary now
      /*this.theCacheMap.get(uri) match {
        case Some(x: KnowledgeWrapper) => return Some(x.getKnowledge)
        case None => {
          val hdl =graph.getHandleFactory.makeHandle(uri.getPath)

          val result:Knowledge = graph.get[Knowledge](hdl)
          //val knowledgeWrapperTemp: KnowledgeWrapper =
          //  hg.getOne(graph, hg.and(new AtomTypeCondition(classOf[KnowledgeWrapper]), hg.eq("uri", uri.toString)))



          if (result != null) {
            result.loadChildren(graph,graph.getHandle(result))
            this.theCacheMap += uri -> result
            return Some(result)
          } else {
            None
          }
        }
      }*/
      var item = graph.get[Knowledge](hdl)
      if (item != null) item.loadChildren(graph, hdl)

      return Option[Knowledge](item)
    }
    catch {
      case e: Exception => log.error("entity not found: {}", e.getMessage)
    }
    None
  }

  /**
   * Persists the entity.
   */
  def persist(entity: Knowledge, uid: String): HGPersistentHandle = {
    if (graph == null) throw new Exception("HyperGraph instance is null")

    if (uid == null) {
      //we have a new object
      val uid = graph.add(entity)
      val hdl = graph.getPersistentHandle(uid)
      //prepare children


      entity.saveChildren(graph, hdl)
      hdl;
    }
    else {
      //find
      var target = graph.getHandleFactory().makeHandle(uid)
      graph.replace(target, entity)
      entity.saveChildren(graph, target)
      target
    }
  }


  def findOfType(typeLink: Knowledge): List[Knowledge] = {
    if (graph == null) throw new Exception("HyperGraph instance is null")

    var res = hg.getAll[Knowledge](graph, new AtomTypeCondition(ProtocolRegistry.getHandleForClass(graph, typeLink)));

    res.foreach(h => {
      if (h.isInstanceOf[Knowledge])
        h.loadChildren(graph, graph.getHandle(h));
    })

    //ret
    List(res: _*)
  }

  /**
   * Removes the entity.
   */
  def remove(uid: String): Boolean = {
    if (graph == null)
      throw new Exception("HyperGraph instance is null")


    try {
      var target = graph.getHandleFactory().makeHandle(uid)
      graph.remove(target)
      true
    }
    catch {
      case e: Exception => {
        log.error("Unable to remove. {}", e)
        false
      }
    }
    finally {
      true
    }

  }

  def rollback: Boolean = {
    try {
      getTransaction.rollback
      cacheMap = Map[URI, Knowledge]()
      true
    } catch {
      case e: Exception => {
        log.error("Unble to remove {}", e.getMessage)
        false
      }
    }
  }

  /**
   * Returns transaction object that can be used to control current transaction.
   */
  def getTransaction = new EntityTransactionImpl(getGraph)

  /**
   * Closes the entity manager releasing resources.
   */
  def close: Boolean = {
    try {
      graph.close()
      true
    } catch {
      case e: Error => {
        log.error("Unable to close theGraph {}", e.getMessage)
        false
      }
    }
  }

  def clean() {

  }

  /*
    return persistent handle according to object
   */
  def getPersistentHandler(obj: Knowledge): HGPersistentHandle = {
    graph.getPersistentHandle(obj.getAtomHandle)
  }

  def find(uri: URI): Option[Knowledge] = {
    throw new Exception("Method is deprecated")
  }
}