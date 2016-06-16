package samples

import sbinary._
import DefaultProtocol._
import Operations._
import TerminalProtocol._

import org.junit._
import org.hypergraphdb.storage.BDBConfig
import org.slf4j.LoggerFactory
import java.net.URI
import collection.immutable.HashSet
import org.hypergraphdb.{HGConfiguration, HGEnvironment, HyperGraph}
import org.hypergraphdb.handle.SequentialUUIDHandleFactory
import menta.model.howto.{Terminal, HowTo, AddClass}
import org.hypergraphdb.HGQuery.hg
import java.io._


/*
  To run this test please place libdb5.dll and  libdb_java50
  into SYSTEM 32
  They are located in  lib\src\main\resources
*/

/*trait Binary[T]{
  /**
   * Read a T from the DataInputStream, reading no more data than is neccessary.
   */
  def reads(stream : DataInputStream) :T;

  /**
   * Write a T to the DataOutputStream.
   */
  def writes(t : T)(stream : DataOutputStream) : Unit;
}

object Operations{
  /**
   * Use an implicit Binary[T] to read type T from the DataInputStream.
   */
  def read[T](stream : DataInputStream)(implicit bin : Binary[T]) : T = bin.reads(stream);

  /**
   * Use an implicit Binary[T] to write type T to the DataOutputStream.
   */
  def write[T](t : T)(stream : DataOutputStream)(implicit bin : Binary[T]) : Unit =
    bin.writes(t)(stream);
}     */


@Test
class HGDBTest {
  val log = LoggerFactory.getLogger(this.getClass)

  // 197.107453
  //  83.474296
  @Test
  def HGDBCreateSample() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {


      graph = new HyperGraph(databaseLocation);
      //load ontology
      val start: Long = System.nanoTime
      hypergrpahDB.HGDBHelper.createObjects(graph)
      hypergrpahDB.HGDBHelper.queryAll(graph)
      //select ontology
      log.info("createObjects and queryAll is done in {}", System.nanoTime - start)
    }
    catch {
      case t: Throwable => t.printStackTrace();
      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  @Test
  def HGDBCreateSampleOptimized() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // Configuration
    val config: HGConfiguration = new HGConfiguration()
    val bdbConfig: BDBConfig = config.getStoreImplementation.getConfiguration.asInstanceOf[BDBConfig]
    // Change the storage cache from the 20MB default to 500MB
    bdbConfig.getEnvironmentConfig.setCacheSize(500 * 1024 * 1024);

    // ...
    try {
      graph = new HyperGraph(databaseLocation);
      graph.setConfig(config)
      //load ontology
      val start: Long = System.nanoTime
      hypergrpahDB.HGDBHelper.createObjects(graph)
      hypergrpahDB.HGDBHelper.queryAll(graph)
      //select ontology
      log.info("Optimised createObjects and queryAll is done in {}", System.nanoTime - start)
    }
    catch {
      case t: Throwable => t.printStackTrace();
      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  // @Test
  def HGDBSelectAll() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = new HyperGraph(databaseLocation);
      //select ontology
      hypergrpahDB.HGDBHelper.queryAll(graph)

    }
    catch {
      case t: Throwable => t.printStackTrace();

      println("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  // @Test
  def HGDBSelect10() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = HGEnvironment.get(databaseLocation);
      //select ontology
      hypergrpahDB.HGDBHelper.query10(graph)

    }
    catch {
      case t: Throwable => t.printStackTrace();

      println("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  @Test
  def HGDBScalaTestPut() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = HGEnvironment.get(databaseLocation);
      //select ontology
      hypergrpahDB.HGDBHelper.put10(graph);

    }
    catch {
      case t: Throwable => t.printStackTrace();

      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  @Test
  def HGDBScalaTestGet() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = HGEnvironment.get(databaseLocation);
      //select ontology
      hypergrpahDB.HGDBHelper.get10(graph);

    }
    catch {
      case t: Throwable => t.printStackTrace();

      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }

  @Test
  def HGDBPersistenceLinkTest() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = HGEnvironment.get(databaseLocation);
      //select ontology
      // HGDBHelper.get10(graph);
      val config: HGConfiguration = new HGConfiguration()

      // Generate UUID persistent handles sequentially. A UUID is made up of two 64bit longs.
      // The following constructs a handle factory that will use the startup time as one of the two longs
      // and will be counting from 0.
      val handleFactory: SequentialUUIDHandleFactory = new SequentialUUIDHandleFactory(System.currentTimeMillis(), 0)
      config.setHandleFactory(handleFactory)
      graph.setConfig(config)

      val before = System.nanoTime
      val addClass_SuperClass = new AddClass()
      addClass_SuperClass.uri = new URI("menta/v0.2#addClass_SuperClass")

      val addMethodAction = new AddClass(addClass_SuperClass, List[HowTo](), HashSet[HowTo]())
      // Done
      val methodsList = List(addMethodAction)

      val addMethod = new AddClass(addClass_SuperClass, methodsList, null) // this should be one of the addNegation addImplication etc
      addMethod.uri = new URI("menta/v0.2#addMethod_01")
      val handle = graph.add(addMethod)
      log.info("Persistant handle: {}", handle)
      val pHandle = graph.getPersistentHandle(handle)
      log.info("Persistant handle: {}", pHandle.toString)
      val readBackHandle = handleFactory.makeHandle(pHandle.toString)
      log.info("read back handle: {}", readBackHandle)

      var after = System.nanoTime
      log.info("Load ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))

    }
    catch {
      case t: Throwable => t.printStackTrace()

      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }

  }

  @Test
  def testScalaSerDeser()
  {
    var term = new Terminal
    term.name="MyTerminal"
    //write object
    //val  bos = new ByteArrayOutputStream()

   //   write[Terminal](bos)
    val data=toByteArray(term)
    //val oos = new ObjectOutputStream(bos)
    //oos.writeObject(term)

    //val data = bos.toByteArray
    //val ois = new ObjectInputStream(new ByteArrayInputStream(data))
    //val clazz=this.getClass
    val obj=fromByteArray[Terminal](data)
    assert (obj==term)//.asInstanceOf[Knowledge]
  }
  @Test
  def storeAndLoadNewModel() {
    val databaseLocation = "C:/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      graph = new HyperGraph(databaseLocation);
      val trgt = new Terminal
      val custHndl = graph.getHandleFactory.makeHandle
      graph.getTypeSystem.addPredefinedType(custHndl,trgt,trgt.getClass)

      trgt.uri=new URI("menta.org/testHGDB")
      trgt.name="testTerminal"

      //load ontology
      val start: Long = System.nanoTime
      var hdl=graph.add(trgt)

      var obj = graph.get[HowTo](hdl)

      var arr =hg.getAll(graph,hg.all)
      //hypergrpahDB.HGDBHelper.createObjects(graph)
      //hypergrpahDB.HGDBHelper.queryAll(graph)
      //select ontology
      log.info("createObjects and queryAll is done in {}", System.nanoTime - start)
    }
    catch {
      case t: Throwable => t.printStackTrace();
      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }
}


