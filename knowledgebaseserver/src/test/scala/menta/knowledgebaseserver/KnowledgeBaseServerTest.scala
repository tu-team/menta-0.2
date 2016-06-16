package menta.knowledgebaseserver

import constant.Constant
import dao.impl.EntityManagerFactoryImpl
import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{JUnitSuiteRunner, JUnit}
import java.net.URI

import org.slf4j.LoggerFactory
import org.hypergraphdb.HGPersistentHandle
import menta.model.howto._
import menta.model.{Knowledge, KnowledgeClass}

/**
 * @author: talanov max, Adel Chepkunov
 * Date: 28.04.11
 * Time: 8:34
 */

@RunWith(classOf[JUnitSuiteRunner])
class KnowledgeBaseServerTest extends Specification with JUnit /*with ScalaCheck*/ {

  val log = LoggerFactory.getLogger(this.getClass)
  val instKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl
  val classURI = new URI("menta/v0.2#AddOperator")
  val test1URI = new URI("menta/v0.2/Terminal1")
  val individualURI = new URI("menta/v0.2#AddOperator.0")
  val rootActionClassURI = new URI("menta/v0.2#AddClass")

  //rootActionClass.uri=rootActionClassURI
 // val addActionClass = instKBServer.addAddActionClass(classURI, null, List[ActionClass]())
  val entityManager = EntityManagerFactoryImpl.createEntityManager

  val str = new StringLiteral("x")
  val V1 = new Terminal(str, test1URI )

  instKBServer.save(V1)

  //val V2 = instKBServer.selectTerminal(test1URI)
  val V2 = instKBServer.selectTerminal(V1.uri)
  assert(V1.value.toString equals V2.value.toString)




  /*
  "KnowlegeBaseServer" should {
    "generate valid URI for AddIndividual" in {
      // List(1, 2, 3).size must_== 3
      val individual: AddIndividual = new AddIndividual()
      individual.uri=individualURI
     // individual.actionClass=addActionClass
      val handler = entityManager.persist(individual)
      log.debug("individual class {}", individual.getClass.getName)
      val uri = instKBServer.generateURI(individual, handler)
      log.debug("AddIndividual uri = {}", uri.toString)
      assert(uri.toString equals "menta/v0.2#AddOperator." + handler.toString)
    }
  }
    "generate valid URI for AddClass" in {
      val handler = entityManager.persist(addActionClass)
      val uri = instKBServer.generateURI(addActionClass, handler)
      log.debug("AddClass uri = {}", uri.toString)
      assert(uri.toString equals "menta/v0.2#AddClass." + handler.toString)
    }

    "generate valid URI for Knowledge" in {
      // menta/v0.2#KnowledgeClass.0bafc85a-bbb5-4e27-a17b-00f13e4691ff
      val res = new KnowledgeClass()
      res.uri=new URI("test")
      val handler = entityManager.persist(res)
      val uri = instKBServer.generateURI(res, handler)
      log.debug("KnowledgeClass uri = {} {}", uri.toString, Constant.defaultURIPrefix + "KnowledgeClass." + handler.toString)
      assert(uri.toString equals Constant.defaultURIPrefix + "KnowledgeClass." + handler.toString)
    }

    "validate URI for standard prefix" in {
      val test = new KnowledgeClass()
      test.setUri("test")
      val handler: HGPersistentHandle = entityManager.persist(test)
      val res = instKBServer.validateURI(test, handler)
      log.debug("KnowledgeClass uri = test validation for standard prefix {}", res)
      assert(!res)
    }

    "validate URI for the object found in KB" in {
      val test = new KnowledgeClass()
      test.setUri("menta/v0.2#test")
      val handler: HGPersistentHandle = entityManager.persist(test)
      val res = instKBServer.validateURI(test, handler)
      log.debug("KnowledgeClass uri = test validation or the object found in KB {}", res)
      assert(res)
    }

    "validate URI for Individual postfix" in {
      val test = new AddIndividual()
      test.setUri("menta/v0.2#testIndividual")
      val handler: HGPersistentHandle = entityManager.persist(test)
      val res = instKBServer.validateURI(test, handler)
      log.debug("Individual uri = test validation for Individual postfix {}", handler.toString)
      assert(!res)
    }

  }     */
}

object KnowledgeBaseServerTestMain {
  def main(args: Array[String]) {
    new KnowledgeBaseServerTest().main(args)
  }
}
