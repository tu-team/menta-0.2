package menta.dao

import config.Configuration

/**
 * Interface used to interact with the entity manager factory.
 * Responsible for creating {@link EntityManager} using predefined properties.
 *
 * @author ayratn
 */
trait EntityManagerFactory {

  /**
   * Creates {@link EntityManager} using default entity manager factory.
   */
  def createEntityManager : EntityManager

  /**
   * Creates {@link EntityManager} using specific {@link Configuration}
   */
  def createEntityManager(configuration: Configuration) : EntityManager

  /**
   * Returns current database configuration
   */
  def getCurrentConfiguration : Configuration

  /**
   * Checks that factory is open.
   */
  def isOpen : Boolean

  /**
   * Closes the factory releasing all resources.
   */
  def close
}