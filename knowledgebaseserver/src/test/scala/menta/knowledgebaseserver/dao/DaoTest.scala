package menta.knowledgebaseserver.dao

import org.junit.{Test, Assert}
import org.slf4j.LoggerFactory
import actors.Actor
import menta.model.{KnowledgeClass, Knowledge, KnowledgeIndividual}
import menta.knowledgebaseserver.dao.impl.EntityManagerFactoryImpl
import menta.dao.EntityManager
import java.net.URI
import org.hypergraphdb.peer.log.Log
import org.hypergraphdb.{HGPersistentHandle, handle}

/**
 * Test DAO classes.
 *
 * @author ayratn
 */

// TODO add logging the output parameters.
class DaoTest {

  val ADD_OPERATOR_URI = "menta/v0.2#AddOperator.dc9830a1-6975-4aa8-9661-bea6e171384d"
  val REMOVE_OPERATOR_URI = "menta/v0.2#RemoveOperator.dc9830a1-6975-4aa8-9661-bea6e171384d"
  val SOME_OPERATOR_URI = "menta/v0.2#SomeOperator.dc9830a1-6975-4aa8-9661-bea6e171384d"

  val log = LoggerFactory.getLogger(this.getClass)
  val UID = "dc9830a1-6975-4aa8-9661-bea6e171384d"

  @Test
  def testAddAndFindClass() {
    val entityManager: EntityManager = null
    try {
      val entityManager = EntityManagerFactoryImpl.createEntityManager
      val knowledgeClass = new KnowledgeIndividual
      knowledgeClass.uri = new URI(ADD_OPERATOR_URI)

      // Add entity into DB
      val uid = entityManager.persist(knowledgeClass, null)

      // Find entity from DB by URI
      val entity: Option[Knowledge] = entityManager.find(uid.toString)
      entity match {
        case None => assert(false)
        case Some(k: Knowledge) => {
          Assert.assertNotNull(k)
          Assert.assertEquals(k.uri.toString, ADD_OPERATOR_URI)
        }
      }
    }
    finally {
      if (entityManager != null) entityManager.close
    }
  }

  /**
   * Test adding without calling close() method at the end.
   */
  @Test
  def testAddWithoutClose() {
    val entityManager = EntityManagerFactoryImpl.createEntityManager
    val knowledgeClass = new KnowledgeIndividual
    knowledgeClass.uri = new URI(ADD_OPERATOR_URI)

    // Add entity into DB
    val res = entityManager.persist(knowledgeClass, null)
    log.debug("saved {}", res.toString)

    // Find entity from DB by URI
    val entity: Option[Knowledge] = entityManager.find(res.toString)
    entity match {
      case None => assert(false)
      case Some(k: Knowledge) => {
        Assert.assertNotNull(k)
        Assert.assertEquals(k.uri.toString, ADD_OPERATOR_URI)
      }
    }
    //DON'T CLOSE ENTITYMANAGER INSTANCE HERE, FOR TESTING PURPOSES ONLY
  }

  @Test
  def testRemoveClass() {
    val entityManager: EntityManager = null
    try {
      // First add class
      val entityManager = EntityManagerFactoryImpl.createEntityManager
      val knowledgeClass = new KnowledgeClass
      knowledgeClass.uri = new URI(REMOVE_OPERATOR_URI)
      val uid = entityManager.persist(knowledgeClass, null)
      var entity: Option[Knowledge] = entityManager.find(uid.toString)
      entity match {
        case None => assert(false)
        case Some(k: Knowledge) => {
          Assert.assertNotNull(k)
          Assert.assertEquals(k.uri.toString, REMOVE_OPERATOR_URI)
        }
      }

      // Add some other class, to make sure all other classes won't be removed on basic remove operation
      val knowledgeClass2 = new KnowledgeClass
      knowledgeClass2.uri = new URI(SOME_OPERATOR_URI)
      val uid1 = entityManager.persist(knowledgeClass2, null)
      entity = entityManager.find(uid1.toString)
      entity match {
        case None => assert(false)
        case Some(k: Knowledge) => {
          Assert.assertNotNull(k)
          Assert.assertEquals(k.uri.toString, SOME_OPERATOR_URI)
        }
      }

      // Now test removing
      //TODO possibly wrong
      entityManager.remove(uid.toString)//knowledgeClass.uri.toString)
      // Check it was really removed
      entity = entityManager.find(uid.toString)
      entity match {
        case None => assert(true)
        case Some(k: Knowledge) => assert(false)
      }
      // Check other class is still there
      entity = entityManager.find(uid1.toString)
      entity match {
        case None => assert(false)
        case Some(k: Knowledge) => {
          Assert.assertNotNull(k)
          Assert.assertEquals(k.uri.toString, SOME_OPERATOR_URI)
        }
      }
    }
    finally {
      if (entityManager != null) entityManager.close
    }
  }

  // sync class
  class Ref(var count: Int)

  /**
   * Actor for persisting {@link KnowledgeClass} objects.
   *
   * Persists 10 objects with different {@link URI}.
   */
  class AddEntityActor(entityManager: EntityManager, startFrom: Int, stringURI: String, sync: Ref) extends Actor {
    def act() {
      try {
        for (i <- startFrom until startFrom + 10) {
          val knowledgeClass = new KnowledgeClass
          val uri: String = stringURI + i
          //log.info("Adding: " + uri)
          knowledgeClass.uri = new URI(uri)
          entityManager.persist(knowledgeClass, UID)
        }
      } finally {
        sync.synchronized {
          sync.count -= 1
          sync.notify()
        }
      }
    }
  }

  /**
   * Actor for searching for {@link KnowledgeClass} objects.
   */
  class FindEntityActor(entityManager: EntityManager, startFrom: Int, stringURI: String, sync: Ref) extends Actor {
    def act() {
      try {
        for (i <- startFrom until startFrom + 10) {
          val uri: String = stringURI + i
          //log.info("Searching for: " + uri)
          val entity: Option[Knowledge] = entityManager.find(uri)
          entity match {
            case None => assert(false)
            case Some(k: Knowledge) => {
              Assert.assertNotNull(k)
              Assert.assertEquals(k.uri.toString, uri)
            }
          }
        }
      } finally {
        sync.synchronized {
          sync.count -= 1
          sync.notify()
        }
      }
    }
  }

  /**
   * Run 5 *add* actors adding 10 objects in each.
   * Then run 5 *search* actors searching for objects added previously
   *
   * In all methods the same {@link EntityManager} instance is used.
   */
  // @Test
  def testMultiThreadAddFindWithOneEntityManager() {
    val numActors: Int = 5
    log.info("Starting MultiThread AddFind Test. Number of threads = " + numActors)
    var sync = new Ref(numActors)

    var actors: List[Actor] = Nil
    val entityManager = EntityManagerFactoryImpl.createEntityManager
    // create actors
    for (i <- 0 until numActors) {
      val actor = new AddEntityActor(entityManager, i * 10, ADD_OPERATOR_URI + (i + 1).toString, sync)
      actors = actors ::: List(actor)
    }
    // run 'em all!
    for (i <- 0 until numActors) {
      actors(i).start()
    }

    // wait for classes to be added
    sync.synchronized {
      while (sync.count > 0) sync.wait()
    }

    // Make sure classes were stored
    sync = new Ref(numActors)
    actors = Nil
    // create find actors
    for (i <- 0 until numActors) {
      val actor = new FindEntityActor(entityManager, i * 10, ADD_OPERATOR_URI + (i + 1).toString, sync)
      actors = actors ::: List(actor)
    }
    // run 'em all again!
    for (i <- 0 until numActors) {
      actors(i).start()
    }

    // wait until all actors finish their work or failed
    sync.synchronized {
      while (sync.count > 0) sync.wait()
    }
  }

  /**
   * Run 5 *add* actors adding 10 objects in each.
   * Then run 5 *search* actors searching for objects added previously
   *
   * In all methods different {@link EntityManager} instances are used.
   */
  // @Test
  def testMultiThreadAddFindWithDifferentEntityManagers() {
    val numActors: Int = 5
    log.info("Starting MultiThread AddFind #2 Test. Number of threads = " + numActors)
    var sync = new Ref(numActors)

    var actors: List[Actor] = Nil

    // create actors with different entityManager instances
    for (i <- 0 until numActors) {
      val entityManager = EntityManagerFactoryImpl.createEntityManager
      val actor = new AddEntityActor(entityManager, i * 10, ADD_OPERATOR_URI + (i + 1).toString, sync)
      actors = actors ::: List(actor)
    }
    // run 'em all!
    for (i <- 0 until numActors) {
      actors(i).start()
    }

    // wait for classes to be added
    sync.synchronized {
      while (sync.count > 0) sync.wait()
    }

    // Make sure classes were stored
    sync = new Ref(numActors)
    actors = Nil
    // create find actors
    for (i <- 0 until numActors) {
      val entityManager = EntityManagerFactoryImpl.createEntityManager
      val actor = new FindEntityActor(entityManager, i * 10, ADD_OPERATOR_URI + (i + 1).toString, sync)
      actors = actors ::: List(actor)
    }
    // run 'em all again!
    for (i <- 0 until numActors) {
      actors(i).start()
    }

    // wait until all actors finish their work or failed
    sync.synchronized {
      while (sync.count > 0) sync.wait()
    }
    log.info("Done with - MultiThreadAddFind #2 Test.")
  }

  @Test
  def testTransactionCommit() {
    var entityManager: EntityManager = null
    var concurrentEntityManager: EntityManager = null
    try {
      entityManager = EntityManagerFactoryImpl.createEntityManager
      cleanUp(entityManager, UID)
      entityManager.getTransaction.begin

      val knowledgeClass = new KnowledgeIndividual
      knowledgeClass.uri = new URI(ADD_OPERATOR_URI)

      // Add entity into DB
      val uid = entityManager.persist(knowledgeClass, null)

      // Make sure it is visible in the same transaction
      assertExists(entityManager, uid, ADD_OPERATOR_URI)

      // But not in another one
      //concurrentEntityManager = EntityManagerFactoryImpl.createEntityManager
      //FIXME: FAILS HERE!
      // Is it correct to assert it this way taking into account that it most possibly uses the same hgdb instance?
      //assertNotExists(concurrentEntityManager, new URI(ADD_OPERATOR_URI))

      // commit now!
      entityManager.getTransaction.commit
      //concurrentEntityManager.getTransaction.commit

      // Now it should be possible to find it by both entity managers
      assertExists(entityManager, uid, ADD_OPERATOR_URI)
      //assertExists(concurrentEntityManager, uid, ADD_OPERATOR_URI)
    } finally {
      if (entityManager != null) entityManager.close
      //if (concurrentEntityManager != null) concurrentEntityManager.close
    }
  }

  private def assertExists(entityManager: EntityManager, uid: HGPersistentHandle, uri: String) {
    val entity: Option[Knowledge] = entityManager.find(uid.toString)
    entity match {
      case None => assert(false)
      case Some(k: Knowledge) => {
        Assert.assertNotNull(k)
        Assert.assertEquals( k.uri.toString, uri)
      }
    }
  }


  private def assertNotExists(entityManager: EntityManager, uri: String) {
    val entity = entityManager.find(uri)
    log.debug("assertNotExists {}", entity)
    entity match {
      case None => assert(true)
      case Some(x: Knowledge) => assert(false)
    }
  }

  private def cleanUp(entityManager: EntityManager, uri: String) {
    val entity = entityManager.find(uri)
    entity match {
      case Some(k: Knowledge) => {
        val knowledgeClass = new KnowledgeClass
        knowledgeClass.uri = new URI(uri)
        entityManager.remove(knowledgeClass.uri.toString)
      }
      case None => {

      }
    }
    assertNotExists(entityManager, uri)
  }

  @Test
  def testTransactionRollback() {
    var entityManager: EntityManager = null
    try {
      entityManager = EntityManagerFactoryImpl.createEntityManager
      cleanUp(entityManager, ADD_OPERATOR_URI)

      entityManager.getTransaction.begin

      val knowledgeClass = new KnowledgeIndividual
      knowledgeClass.uri = new URI(ADD_OPERATOR_URI)

      // Add entity into DB
      var uid = entityManager.persist(knowledgeClass, null)

      // Make sure it is visible in the same transaction
      assertExists(entityManager, uid, ADD_OPERATOR_URI)

      // rollback now!
      entityManager.rollback

      // The transaction was rollbacked, so it shouldn't be in db
      log.debug(" testTransactionRollback ")
      assertNotExists(entityManager, ADD_OPERATOR_URI)
    } finally {
      if (entityManager != null) entityManager.close
    }
  }

  @Test
  def testTransactionReadCommited() {
    //TODO: add
  }
}