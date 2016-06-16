package menta.model.conversation

import menta.model.subscribercontroller.Customer
import collection.immutable.HashSet
import java.net.URI
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HyperGraph, HGHandle}
import menta.model.Knowledge
import scala.collection.JavaConversions._

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:20
 */

class Conversation(aAuthor: Customer, aSubscribers: HashSet[Customer]) extends ConversationAny(aAuthor) {

  private var theSubscribers:HashSet[Customer]=aSubscribers

  def subscribers=theSubscribers

  def subscribers_(aVSubscribers: HashSet[Customer])=theSubscribers=aVSubscribers

  def this() = this(new Customer(), HashSet[Customer]())




  /**
   *  load all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theConversationActs")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var childs = hg.getAll[ConversationAct](graph, hg.target(graph.getHandle(lnk)))
      childs.foreach(g => {
        theConversationActs=theConversationActs:::List(g)
      })
    }

    null

  }

  /**
   * save all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theConversationActs")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    //format array
    var lst = List(handle)
    //suppose all are stored in HGDB
    theConversationActs.foreach(vr => {
      lst ++= List(graph.getHandle(vr))
    })
    lnk = new HGValueLink("theConversationActs", lst: _*)
    graph.add(lnk)
  }

   override def getAllChildren(): List[Knowledge] = {
    var lst = super.getAllChildren()
    if (lst == Nil) {
      lst =theConversationActs
    }
    else {
      lst = lst ::: theConversationActs
    }

    lst
  }
}