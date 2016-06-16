package hypergrpahDB


import menta.model.solutiongenerator.solutionchecker.Rule
import menta.model.util.sequence.Sequence
import menta.model.util.sequence.SequenceElement
import java.net.URI
import menta.model.howto.{AddClass, HowTo}
import org.hypergraphdb.HGQuery.hg
import scala.collection.JavaConversions._


import org.hypergraphdb.query._
import reflect.BeanProperty
import org.hypergraphdb._
import collection.immutable.HashSet
import org.slf4j.LoggerFactory

/**
 * @author $ { user.name }
 */
object HGDBHelper {

  val log = LoggerFactory.getLogger(this.getClass)


  def put10ScalaClasses(graph: HyperGraph) = {

    var target = new TestBook();
    target.name = "A. Michael";
    graph.add(target);
    for (val i <- 0 until 10) {
      var sc = new TestBook();
      sc.name = "Richard" + i;
      graph.add(sc);
    }
  }

  def get10ScalaClasses(graph: HyperGraph) = {
    var target: TestBook = hg.getOne(graph, new And(
      new AtomTypeCondition(classOf[TestBook]), hg.eq("name", "A. Michael")))

    var allOfType = graph.find(new And(
      new AtomTypeCondition(classOf[TestBook]))) //, hg.eq("test","88") ))

    for (val hd: HGHandle <- allOfType) {
      val sc: TestBook = graph.get(hd);
      log.info(sc.name);
      graph.remove(hd);

    }
  }

  def createObjects(graph: HyperGraph) = {
    val addClass_SuperClass = new AddClass()
    addClass_SuperClass.uri = new URI("menta/v0.2#addClass_SuperClass")

    val addMethodAction = new AddClass(addClass_SuperClass, List[HowTo](), HashSet[HowTo]())

    // Done
    val addOperator = new AddClass(addMethodAction, List[HowTo](), HashSet[HowTo]())
    addOperator.uri = new URI("menta/v0.2#AddOperator_01")

    // Done
    val addCondition = new AddClass(addMethodAction, List[HowTo](), HashSet[HowTo]())
    addCondition.uri = new URI("menta/v0.2#AddCondition_01")

    // Done.
    val addProbabilisticLogicExpressionList = List(addOperator)
    val addProbabilisticLogicExpression = new AddClass(addCondition, List[HowTo](), HashSet[HowTo]())
    addProbabilisticLogicExpression.uri = new URI("menta/v0.2#AddProbabilisticLogicExpression_01")

    // Done.
    val addProbabilisticLogicOperator = new AddClass(addOperator, null, null)
    addProbabilisticLogicOperator.uri = new URI("menta/v0.2#AddProbabilisticLogicOperator_01")

    // Done. addProbabilisticLogicOperator group
    val addNegation = new AddClass(addProbabilisticLogicOperator, List[HowTo](), HashSet[HowTo]())
    addNegation.uri = new URI("menta/v0.2#addNegation_01")
    val addImplication = new AddClass(addProbabilisticLogicOperator, List[HowTo](), HashSet[HowTo]())
    addImplication.uri = new URI("menta/v0.2#addImplication_01")
    val addConjunction = new AddClass(addProbabilisticLogicOperator, List[HowTo](), HashSet[HowTo]())
    addConjunction.uri = new URI("menta/v0.2#addConjunction_01")
    val addProbabilisticProperty = new AddClass(addProbabilisticLogicOperator, List[HowTo](), HashSet[HowTo]())
    addProbabilisticProperty.uri = new URI("menta/v0.2#addProbabilisticProperty_01")

    // Done
    val methodsList = List(addMethodAction)

    val addMethod = new AddClass(addClass_SuperClass, methodsList, null) // this should be one of the addNegation addImplication etc
    addMethod.uri = new URI("menta/v0.2#addMethod_01")



    // Done
    val addNamespace = new AddClass(addClass_SuperClass, null, null)
    addNamespace.uri = new URI("menta/v0.2#addNamespace_01")

    // Done
    val addScope = new AddClass(addClass_SuperClass, null, null)
    addScope.uri = new URI("menta/v0.2#AddScope_01")

    // Done
    val addPropertyHowTos = List(addScope)
    val addProperty = new AddClass(addClass_SuperClass, addPropertyHowTos, null)
    addProperty.uri = new URI("menta/v0.2#AddProperty_01")

    // Done
    val addModuleHowTos = List(addProperty, addMethod)
    val addModule = new AddClass(addClass_SuperClass, addModuleHowTos, null)
    addModule.uri = new URI("menta/v0.2#AddModule_01")

    val addProjectHowTos = List(addModule)
    val addProject = new AddClass(addClass_SuperClass, addProjectHowTos, null)
    addProject.uri = new URI("menta/v0.2#AddProject_01")
    //addProject.uriTest="testURI"

    val addApplicationHowTos = List(addProject)
    val addApplication = new AddClass(addClass_SuperClass, addApplicationHowTos, null)
    addApplication.uri = new URI("menta/v0.2#AddApplication_01")

    val sequenceElement1 = new SequenceElement(null, addClass_SuperClass)
    sequenceElement1.uri = new URI("menta/v0.2#SequenceElement_01")
    val sequence: Sequence = new Sequence(sequenceElement1)

    val rule = new Rule(addClass_SuperClass)
    rule.uri = new URI("menta/v0.2#Rule_01")


    //begin to add
    var before = System.nanoTime
    //rule
    val ruleHandle: HGHandle = graph.add(rule);
    //sequence
    val seqHandle: HGHandle = graph.add(sequence);

    //add project
    graph.add(addProject);

    //add module
    graph.add(addModule);

    //add Scope
    graph.add(addScope);

    //addNamespace
    graph.add(addNamespace);

    // addMethod
    graph.add(addMethod);

    var after = System.nanoTime
    log.info("Load ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))



    //    graph.add(addClass_SuperClass);


    //    HGHandle priceHandle = graph.add(9.95);
    //    HGValueLink link = new HGValueLink("book_price", bookHandle, priceHandle);
  }


  def put10(graph: HyperGraph) = {
    for (i <- 0 until 10) {
      var ob = new TestBook();
      if (i == 0) {
        ob.uri = "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module"
      }
      ob.name = "Richard";
      graph.add(ob);
    }
    var before = System.nanoTime
    var res = hg.getAll(graph, hg.and(new AtomTypeCondition(classOf[TestBook])));
    var after = System.nanoTime
    log.info("Select 10 SCALA ln:" + res.toArray().length + " (cache) ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))
  }

  def testGetByURI(graph: HyperGraph) = {
    val condition = new And(
      new AtomTypeCondition(classOf[TestBook]),
      hg.eq("uri", "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module"))

    //var oneType = hg.findOne(graph, hg.eq("uri","http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module"));
    var oneType: HGHandle = hg.findOne(graph, condition)
    val sc0: TestBook = graph.get(oneType)
    sc0.name = "Changed"
    graph.update(sc0)

    var allOfType = graph.find(new And(
      new AtomTypeCondition(classOf[TestBook]))) //, hg.eq("test","88") ))

    var i = 0
    for (val hd: HGHandle <- allOfType) {
        val sc: TestBook = graph.get(hd);
        i = i + 1
        log.info("Get: " + i + " :" + sc.name)
    }
  }

  def get10(graph: HyperGraph) = {

    var before = System.nanoTime
    var res = hg.getAll(graph, hg.and(new AtomTypeCondition(classOf[TestBook])));
    var after = System.nanoTime
    log.info("Select 10 SCALA ln:" + res.toArray().length + "  ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))

    before = System.nanoTime

    var allOfType = graph.find(new And(new AtomTypeCondition(classOf[TestBook])))

    for (val hd: HGHandle <- allOfType) {
      val sc: TestBook = graph.get(hd);
    }
    after = System.nanoTime
    log.info("Select 10 SCALA via handle ln:" + res.toArray().length + "  ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))
  }

  def queryAll(graph: HyperGraph) = {
    var before = System.nanoTime
    hg.getAll(graph, hg.all)
    var after = System.nanoTime
    log.info("Select ALL ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))
  }

  def query10(graph: HyperGraph) = {
    log.info("query10");
    var before = System.nanoTime
    var allOfType = graph.find(new And(
      new AtomTypeCondition(classOf[AddClass]))) //, hg.eq("test","88") ))

    log.info(allOfType.length.toString);

    var i = 0;
    for (val hd: HGHandle <- allOfType) {
      if (i <= 10) {
        i = i + 1;
        val sc: AddClass = graph.get(hd);

        log.info("Get: " + i + " :" + sc.toString)

        graph.remove(hd);
      }
      else {
        scala.util.control.Breaks.break
        //return false
      }
    }
    var after = System.nanoTime
    log.info("Select 10 ms:" + (after - before) / 1000000 + " nanosec:" + (after - before))
  }

  def query10test(graph: HyperGraph) = {
    var before = System.nanoTime
    var qr = (hg.getAll(graph, hg.and(new AtomTypeCondition(classOf[SimpleClass]))))
    if (qr.toArray().length <= 0) {
      var sc = new SimpleClass()
      sc.Test = "88"
      var hdl = graph.add(sc)
      // var c = new Character('z')
      // graph.add(c)

      //var uriHandle = graph.add("TestURI");
      //var link = new HGValueLink("sc_uri", hdl, uriHandle);
      // graph.add(link)

    }
    else {
      //  var re=hg.getAll(graph,hg.all);
      // var res=hg.getAll(graph,hg.and(new AtomTypeCondition(classOf[SimpleClass])))//, hg.eq("test","88") ))
      try {
        val condition: HGQueryCondition = new And(
          new AtomTypeCondition(classOf[SimpleClass]), new AtomPartCondition(List("Test").toArray, "88"));
        var res = graph.find(condition);
      }
      catch {
        case t: Throwable => t.printStackTrace();
      }
      var allOfType = graph.find(new And(
        new AtomTypeCondition(classOf[SimpleClass]))) //, hg.eq("test","88") ))


      for (val hd: HGHandle <- allOfType) {

        val sc: SimpleClass = graph.get(hd);
        log.info("Value:" + sc.Test);

        graph.remove(hd);
      }
    }


    var after = System.nanoTime
    log.info("Select all ms:" + (after - before) / 1000000)

  }

  def main(args: Array[String]) {
    println("Hello World!")
  }


}

@serializable
class SimpleClass() //extends HGAtomType
{


  @BeanProperty
  var Test = ""

}




