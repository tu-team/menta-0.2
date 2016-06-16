package menta.dao

import java.net.URI
import org.hypergraphdb.HGPersistentHandle
import menta.model.{KnowledgeIndividual, Knowledge}
import menta.model.howto.AddClass


/**
 * Base interface for entity manager.
 *
 * EntityManager is used to create and remove persistent entity instances, to find entities by their primary key,
 * and to query over entities using predefined query syntax.
 *
 * @author ayratn
 */
trait EntityManager {

  /**
   * Find entity by its primary key.
   */
  def find(uid: String) : Option[Knowledge]

  /**
   * find entity by primary key as object
   */

  def find(uri: URI) : Option[Knowledge]
  /**
   * Persists the entity.
   */
  def persist(entity: Knowledge,uid:String): HGPersistentHandle

  /**
   * Removes the entity.
   */
  def remove(uri: String): Boolean
   /*
    return persistent handle according to object
   */
  def getPersistentHandler(obj:Knowledge):HGPersistentHandle
  
  /**
   * Returns transaction object that can be used to control current transaction.
   */
  def getTransaction : EntityTransaction

  /**
   * Closes the entity manager releasing resources.
   */
  // TODO add at least true/false result.
  def close: Boolean

  /**
   * Gets the transaction manager and aborts the transaction resetting the cache.
   */
  def rollback: Boolean

  def linkObjects(src:String,target:String,linkName:String)

  def getLinkedObjects(src: String, linkName: String): List[Knowledge]

  def findOfType(typeLink:Knowledge):List[Knowledge]
}