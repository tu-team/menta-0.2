package menta.dao.impl


import org.hypergraphdb.HyperGraph
import menta.dao.EntityTransaction

/**
 * Author: Aidar Makhmutov
 * Date: 23.02.2011
 */

//TODO: Add scala doc comments
class EntityTransactionImpl extends EntityTransaction {
  private var graph: HyperGraph = null

  def this(graph: HyperGraph) = {
    this ()
    this.graph = graph
  }

  /**
   * Rollback the current transaction and reset cache.
   */
  def rollback = {
    graph.getTransactionManager().abort();
  }

  /**
   * Commit the changes.
   */
  def commit = {
    try {
      graph.getTransactionManager().commit();
    }
    catch {
      case e: Exception => graph.getTransactionManager().abort();
    }
  }

  /**
   * Begins new transaction.
   */
  def begin = {
    graph.getTransactionManager().beginTransaction();
  }
}