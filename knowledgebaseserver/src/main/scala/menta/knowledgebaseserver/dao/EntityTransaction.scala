package menta.dao

/**
 * The interface is used to control transactions within entity managers.
 *
 * @author ayratn
 */
trait EntityTransaction {
  /**
   * Begins new transaction.
   */
  def begin

  /**
   * Commits the changes.
   */
  def commit

  /**
   * Rollbacks the current transaction.
   */
  def rollback
}