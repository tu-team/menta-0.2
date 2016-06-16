package menta.reasoner

import org.junit._
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.{VelocityContext, Template}
import java.io.StringWriter
import org.slf4j.LoggerFactory
import util.Random


@Test
class AppTest {

  val LOG = LoggerFactory.getLogger(this.getClass)

  val MODULES_LIST = List(
    "Customer",
    "Order",
    "CommandExecutor",
    "CreateCustomer",
    "CreateOrder",
    "DatabaseCommand",
    "ListCustomerOrders",
    "ListCustomers",
    "CreateCustomerServlet",
    "CreateOrderServlet",
    "ListCustomerOrdersServlet",
    "ListCustomersServlet"
    )

  val OPERATIONS_LIST = List(
    "AddFieldCommand",
    "RemoveFieldCommand")

  @Test
  def testFitnessFunction() {

    // Generating random genome
    var random = new Random();
    val genomeLength = random.nextInt(10);
    val genome = new Array[Int](genomeLength)
    for (val i <- 0.to(genomeLength - 1)) {
      genome(i) = random.nextInt(24)
      LOG.debug(" {}", genome(i))
    }

    // Print out linear genome
    printGenomeStringRepresentation(genome)
  }

  def getGenomeStringRepresentation(genome: Array[Int]): String = {
    val ve = new VelocityEngine();
    ve.init();
    val t = ve.getTemplate("./src/main/resources/GenomeTemplate.vm");
    var i = 1
    var resultString = ""

    for (atom: Int <- genome) {
      var velocityContext = new VelocityContext();
      velocityContext.put("module", getModuleAsString(atom));
      velocityContext.put("iteration", i.toString);
      velocityContext.put("operation", getOperationAsString(atom));
      var writer = new StringWriter();
      t.merge(velocityContext, writer);
      LOG.debug(writer.toString());
      resultString = resultString + writer.toString() + "\n"
      i = i + 1
    }
    return resultString
  }

  def getModuleAsString(moduleIndex: Int): String = {
    var realModuleIndex = moduleIndex
    if (moduleIndex > 11)
      realModuleIndex -= 12;
    MODULES_LIST.apply(realModuleIndex) // get element with current index
  }

  def getOperationAsString(operationIndex: Int): String = {
    var realOperationIndex = 0
    if (operationIndex > 11)
      realOperationIndex = 1
    OPERATIONS_LIST.apply(realOperationIndex)
  }

  // print to nars exp
  def printGenomeStringRepresentation(genome: Array[Int]) {
    val ve = new VelocityEngine();
    ve.init();
    var t = ve.getTemplate("./src/main/resources/SimpleAddFieldFitnessFunction.vm");
    val velocityContext = new VelocityContext();
    velocityContext.put("genotype", getGenomeStringRepresentation(genome));
    val writerString = new StringWriter();
    val writer = new java.io.FileWriter("./src/main/resources/SimpleAddFieldFitnessFunction.exp")
    t.merge(velocityContext, writer);
    LOG.debug(" {}", writerString.flush);
    writer.flush
  }


}



