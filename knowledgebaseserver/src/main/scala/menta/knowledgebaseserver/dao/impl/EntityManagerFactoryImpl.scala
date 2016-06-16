package menta.knowledgebaseserver.dao.impl

import org.slf4j.LoggerFactory
import menta.dao.config.Configuration
import menta.dao.config.impl.ConfigurationImpl
import menta.dao.{EntityManager, EntityManagerFactory}
import org.hypergraphdb.{HGConfiguration, HGEnvironment, HyperGraph}
import org.hypergraphdb.storage.BDBConfig
import org.hypergraphdb.handle.SequentialUUIDHandleFactory
import menta.exception.PropertyNotFoundException
import menta.knowledgebaseserver.exception.UnableToGetHyperGraphInstanceException
import menta.knowledgebaseserver.constant.Constant
import menta.model.util.serialization.ProtocolRegistry
import menta.knowledgebaseserver.helpers.StorageState
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.query.AtomTypeCondition
import scala.collection.JavaConversions._

/**
 * Class used to interact with the entity manager factory.
 * Responsible for creating {@link EntityManagerImpl} using default or customized properties,
 * connecting to the HyperGraphDB.
 *
 * @author Aidar Makhmutov
 */

object EntityManagerFactoryImpl extends EntityManagerFactory {
  val log = LoggerFactory.getLogger(this.getClass)

  private var open: Boolean = false
  //  private var entityManagerPool = null
  private var configuration: Configuration = null
  private var pool = List[EntityManager]()

  /**
   * Creates  { @link EntityManager } using default entity manager factory.
   */
  def createEntityManager: EntityManager = {
    val configuration = new ConfigurationImpl
    configuration.loadFromClasspath()
    createEntityManager(configuration)
  }

  /**
   * Creates  { @link EntityManager } using specific  { @link Configuration }
   */
  def createEntityManager(configuration: Configuration): EntityManager = {
    this.configuration = configuration
    val dataSourcePath = configuration.getDataSourcePath
    val cacheSizeString: String = configuration.getProperties.getOrElse(
      Constant.cacheSizePropertyKey,
      throw new PropertyNotFoundException("Property " + Constant.cacheSizePropertyKey + " not found.")).toString
    val cacheSize = cacheSizeString.toInt

    val hyperGraph = getHyperGraphInstance(dataSourcePath)

    val config: HGConfiguration = new HGConfiguration()
    val bdbConfig: BDBConfig = config.getStoreImplementation.getConfiguration.asInstanceOf[BDBConfig]
    // Change the storage cache from the 20MB default to 500MB
    bdbConfig.getEnvironmentConfig.setCacheSize(cacheSize);

    //The the HandleFactory is now frozen, this is good to load it from properties file and create on the fly.
    val handleFactory: SequentialUUIDHandleFactory = new SequentialUUIDHandleFactory(System.currentTimeMillis(), 0)
    config.setHandleFactory(handleFactory)
    hyperGraph.setConfig(config)

    open = true
    val entityManager = new EntityManagerImpl(hyperGraph)

    pool ::= entityManager

    //log.info("HGDB Entity Manager successfully created.")
    return entityManager
  }

  /**
   * Returns current database configuration
   */
  def getCurrentConfiguration = {
    if (configuration == null)
      log.warn("Configuration hadn't been loaded yet.")
    configuration
  }

  /**
   * Checks that factory is open.
   */
  def isOpen = open

  /**
   * Returns the pool of EntityManagers
   */
  def getPool: List[EntityManager] = this.pool

  /**
   *  Closes the factory releasing all resources.
   */
  def close {
    open = false
    // TODO: Remove transactions ==
    // TODO: get all entityManagers from pool and close them
    pool.foreach((em: EntityManager) => {
      em.close
    })
    pool = List[EntityManager]()

    //log.info("HGDB Entity Manager resources were released")
  }

  /**
   * Returns HyperGraph for the specified data source path
   */
  private def getHyperGraphInstance(dataSourcePath: String): HyperGraph = {
    var graph: HyperGraph = null;

    try {
      //graph = new HyperGraph(dataSourcePath);
      graph = HGEnvironment.get(dataSourcePath);
      //setup Handles

      //check if storage initialized
      var handle = hg.getAll[StorageState](graph, new AtomTypeCondition(classOf[StorageState])).toList
      if (handle.length <= 0) {
        ProtocolRegistry.registerClasses(graph)
        graph.add(new StorageState)
      }


    }
    catch {
      case e: Exception => {
        log.error(e.getMessage)
        throw new UnableToGetHyperGraphInstanceException("Unable to get HyperGraph Instace " + e.getMessage)
      }
    }
    return graph
  }
}