package menta.knowledgebaseserver.dao

import org.junit.{Test, Assert}
import org.slf4j.LoggerFactory
import menta.knowledgebaseserver.dao.impl.EntityManagerFactoryImpl
import menta.dao.config.impl.ConfigurationImpl


/**
 * Author: Aidar Makhmutov
 * Date: 31.03.2011
 */

class BasicDaoTest {

  val log = LoggerFactory.getLogger(this.getClass)

  val ADD_OPERATOR_URI = "menta/v0.2#AddOperator"
  val REMOVE_OPERATOR_URI = "menta/v0.2#RemoveOperator"
  val SOME_OPERATOR_URI = "menta/v0.2#SomeOperator"

  @Test
  def testOpenClose() {
    // Open connections
    for (i <- 0 until 10) {
      EntityManagerFactoryImpl.createEntityManager
    }

    // Close connections
    EntityManagerFactoryImpl.close

    // Pool should be empty
    var i = 0
    EntityManagerFactoryImpl.getPool.foreach(s => i += 1)
    Assert.assertTrue(i == 0)
  }

  @Test
  def testGetCurrentConfiguration() {
    val configuration = new ConfigurationImpl
    val properties = configuration.loadFromClasspath()

    val entityManager = EntityManagerFactoryImpl.createEntityManager(configuration)

    Assert.assertEquals(configuration, EntityManagerFactoryImpl.getCurrentConfiguration)
  }
}