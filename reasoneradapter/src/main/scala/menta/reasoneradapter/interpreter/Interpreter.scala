package menta.reasoneradapter.interpreter

import menta.model.howto.Context
import java.net.URI
import menta.reasoneradapter.reasonerinterface.impl.NARSHelper
import menta.reasoneradapter.constant.Constant
import nars.entity.Task
import menta.reasoneradapter.translator.HowToTranslator
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import java.security.InvalidParameterException
import menta.model.howto.helper.AddParameterIndividualHelper
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper.{AddInheritanceIndividualWrapper, AddHasAPropertyIndividualHelper}
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators._
import menta.model.howto.{AddIndividual, HowTo}
import org.slf4j.{LoggerFactory, Logger}

/**
 * @author max talanov
 * @date 2011-05-29
 * Time: 11:07 PM
 */

class Interpreter {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  val TRANSLATOR: HowToTranslator = new HowToTranslator()
  val URI = new URI("menta/test")
  var CONTEXT = new Context(Map[URI, Map[URI, List[HowTo]]]())

  @deprecated
  def apply(in: AddIndividual, context: Context, scope: URI): FrequencyConfidenceNegationTriple = {
    /*in match {
      case AddLoop(loop: AddLoopHowToInterpreter) => {
        val res = loop(CONTEXT, URI)
        res
      }
      case AddCondition(condition: AddConditionHowToInterpreter) => {
        val res = condition(CONTEXT, URI)
        res
      }
      case _ => {
        throw new InvalidParameterException("AddLoop HowTo expected instead of: " + in.getClass.toString)
      }
    }
    null*/
    apply(List(in), context, scope)
  }

  def apply(in: List[AddIndividual], context: Context, scope: URI, preprocessVariables: Boolean): FrequencyConfidenceNegationTriple = {
    //currently we have a top level expressions with vars,but before NARS processing load all vars
    def findInnerVariables(trgt: List[AddIndividual]): List[HowTo] = {
      var found: List[AddIndividual] = List[AddIndividual]();
      //scan top level
      var toInsert = List[HowTo]();
      var toRemove = List[AddIndividual]();

      trgt.foreach(b => {

        b match {
          case AddVariable(vrbls) => {
            //todo replace with context value
            new AddVariableWrapper(b).value(context, scope, new URI(b.name)) match {
              case Some(valueFromContext) => {
                //do nothing with loop variables
                if (b.name == null || !b.name.contains(Constant.NonTerminalURIs.loopVariable)) {
                  //translate to nars parameters
                  var toCopy = valueFromContext.head;
                  //toCopy.actionClass=b.actionClass;
                  toInsert = toInsert ::: List(toCopy)
                  toRemove = toRemove ::: List(b);
                }
              }
              case None => {
                //translate to NARS
                if (b.name == null || (!b.name.contains(Constant.NonTerminalURIs.narsVariable) && !b.name.contains(Constant.NonTerminalURIs.loopVariable))) {
                  toRemove = toRemove ::: List(b);
                  //val emptyValue = new StringLiteral("variable_not_found")
                  val emptyValue = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, ("variable_not_found"), null)
                  emptyValue.actionClass = b.actionClass

                  toInsert = toInsert ::: List(emptyValue);
                }

              }
            }

          }

          case _ => true
        }

        //if loop how-to , stop inner modification
        b match
        {
          case AddLoop(lp)=>true
          case _ =>
            {
              if (b.parameters != null && b.parameters.count(p => p.isInstanceOf[AddIndividual]) > 0) b.parameters = findInnerVariables(b.parameters.map(p => p.asInstanceOf[AddIndividual]));
            }
        }


      })
      //remove old
      var resultList = trgt.filterNot(p => toRemove.contains(p)).map(p => p.asInstanceOf[HowTo]);
      resultList = resultList ::: toInsert;

      resultList
    }

    var processed = findInnerVariables(in);


    return apply(processed.map(f => f.asInstanceOf[AddIndividual]), context, scope);
  }

  def apply(in: List[AddIndividual], context: Context, scope: URI): FrequencyConfidenceNegationTriple = {
    var res: FrequencyConfidenceNegationTriple = null
    if (in == null || in.size < 1) {
      throw new InvalidParameterException("Empty HowTo List specified")
    }

    val simpleExpressions: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddCondition(condition) => false
        case AddLoop(loop) => false
        case AddVariable(variables) => false
        case AddBlock(blocks) => false
        case AddEqual(equals) => false
        case AddConstantBlock(ignores)=>false
        case _ => true
      }
    })

    val variables: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddVariable(variables) => true
        case _ => false
      }
    })

    if (simpleExpressions != null && simpleExpressions.size > 0) {
      var tasks: List[Task] = List[Task]()
      var toProcess = List[AddIndividual]();
      var varToProcess = variables.toList



      //prepare and add variables
      for (val vr: AddIndividual <- varToProcess) {
        new AddVariableWrapper(vr).value(context, scope, new URI(vr.name)) match {
          case Some(valueFromContext) => {
            //translate to nars parameters
            toProcess = toProcess ::: valueFromContext.asInstanceOf[List[AddIndividual]];
          }
          case None => {
            //translate to NARS
            toProcess = toProcess ::: List(vr)
          }
        }
      }
      toProcess = toProcess ::: simpleExpressions
      for (val ht: AddIndividual <- toProcess) {
        val task: Task = TRANSLATOR.Translate(ht, CONTEXT, URI)
        tasks = tasks ::: List[Task](task)
      }
      res = NARSHelper(tasks, Constant.maxNumCycles)

    }

    val condition: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddCondition(condition) => true
        case _ => false
      }
    })

    val loop: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddLoop(loop) => true
        case _ => false
      }
    })

    val blocks: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddBlock(blocks) => true
        case _ => false
      }
    })

    val equals: List[AddIndividual] = in.filter(ht => {
      ht match {
        case AddEqual(equals) => true
        case _ => false
      }
    })

    processNonSimpleHowTo(loop, condition, blocks, equals, context, scope) match {
      case Some(fcn: FrequencyConfidenceNegationTriple) => {
        if (res != null) {
          combineResults(List[FrequencyConfidenceNegationTriple](fcn, res), context, scope)
        } else {
          fcn
        }
      }
      case None => {
        res
      }
    }

  }

  private def processNonSimpleHowTo(loop: List[AddIndividual], condition: List[AddIndividual], blocks: List[AddIndividual], equals: List[AddIndividual], context: Context, scope: URI): Option[FrequencyConfidenceNegationTriple] = {

    var res: List[FrequencyConfidenceNegationTriple] = List[FrequencyConfidenceNegationTriple]()
    if ((loop != null && loop.length > 0) || (condition != null && condition.length > 0) || (blocks != null && blocks.length > 0) || (equals != null && equals.length > 0)) {
      if (loop != null && loop.length > 0) {
        for (val lht: AddIndividual <- loop) {
          res = res ::: List(new AddLoopHowToInterpreter(lht.uri, lht.parameters).apply(lht, context, scope))
        }
      }
      if (condition != null && condition.length > 0) {
        //process with condition
        for (val lht: AddIndividual <- condition) {
          res = res ::: List(new AddConditionHowToInterpreter(lht.uri, lht.parameters).apply(lht, context, scope))
        }
      }
      if (blocks != null && blocks.length > 0) {
        //blocks
        for (val lht: AddIndividual <- blocks) {
          res = res ::: List(new AddBlockInterpreter(lht.uri, lht.parameters).apply(lht, context, scope))
        }
      }
      if (equals != null && equals.length > 0) {
        //equals
        for (val lht: AddIndividual <- equals) {
          res = res ::: List(new AddEqualInterpreter(lht.uri, lht.parameters).apply(lht, context, scope))
        }
      }

      Some(combineResults(res, context, scope))
    } else {
      None
    }
  }

  /* private def processLoop(in: List[AddIndividual], context: Context, scope: URI): Option[FrequencyConfidenceNegationTriple] = {

var res: List[FrequencyConfidenceNegationTriple] = List[FrequencyConfidenceNegationTriple]()
if (in != null && in.length > 0) {
for (val lht: AddIndividual <- in) {
  res = res ::: List(new AddLoopHowToInterpreter(lht.uri, lht.parameters).apply(lht, context, scope))
}
Some(combineResults(res, context, scope))
} else {
None
}
}      */

  /**
   * calculates combination of the HowTo-s with FrequencyConfidenceNegationTriple-s
   * @param in the List of Pairs HowTo and FrequencyConfidenceNegationTriple to combine
   * @param context the context Map
   * @param sco
   */
  def applyCombination(in: List[Pair[AddIndividual, FrequencyConfidenceNegationTriple]], context: Context, scope: URI): FrequencyConfidenceNegationTriple = {
    var tasks: List[Task] = List[Task]()
    for (val ht: Pair[AddIndividual, FrequencyConfidenceNegationTriple] <- in) {
      val task: Task = TRANSLATOR.Translate(ht, CONTEXT, URI)
      tasks = tasks ::: List[Task](task)
    }
    val resNotLoop: FrequencyConfidenceNegationTriple = NARSHelper(tasks, Constant.maxNumCycles)
    resNotLoop
  }

  def combineResults(resultList: List[FrequencyConfidenceNegationTriple], context: Context, scope: URI): FrequencyConfidenceNegationTriple = {
    //LOG.debug("combineResults {}", resultList)
    //nothing combine if value less then 2
    if (resultList.size < 2) {
      //nothing to combine - return false
      if (resultList.size < 1)
        return new FrequencyConfidenceNegationTriple(0.0, 0.9, false)

      //can't combine one result - just return  it
      return resultList.head
    }

    //we do not need nars - combine with  average
    var sum:Double =0;
    resultList.foreach(b=>
    {
      sum=sum+b.frequency;
    })
    return new FrequencyConfidenceNegationTriple(sum/resultList.length, 0.9, false)

    /*val howToList: List[Pair[AddIndividual, FrequencyConfidenceNegationTriple]] = resultList.map((fcn: FrequencyConfidenceNegationTriple) => {
      generateSelfOK(fcn, context, scope)
    })
    applyCombination(howToList, context, scope)*/
  }

  def generateSelfOK(in: FrequencyConfidenceNegationTriple, context: Context, scope: URI): Pair[AddIndividual, FrequencyConfidenceNegationTriple] = {
    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Self", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "Ok", null)
    val selfOk = new AddInheritanceIndividualWrapper(l1, r1, null).addIndividual()
    //avoid NARS confidence interpretation
    val newIn = new FrequencyConfidenceNegationTriple(in.frequency, 0.9f, in.negation)

    Pair(selfOk, newIn)
  }
}