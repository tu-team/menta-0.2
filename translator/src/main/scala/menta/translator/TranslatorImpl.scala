package menta.translator

import constant.Constant
import menta.translator.helper.{TranslationWrapperFactory, ConstantTranslationWrapperFactory}
import java.net.URI
import menta.model.container.KnowledgePatch
import org.slf4j.{Logger, LoggerFactory}
import menta.model.Knowledge
import scala.deprecated
import wrappers._
import menta.model.howto._
import menta.model.howto.helper.AddNameHelper

// import menta.knowledgebaseserver.KnowledgeBaseServerImpl

/**
 * @author chepkunov adel, talanov max
 * Date: 16.11.11
 * Time: 3:59
 */

class TranslatorImpl extends Translator {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * Creates patch
   */
  def createPatch(solution: Solution, translationStrategy: TranslationStrategy, constantBlock: AddIndividual): KnowledgePatch = {

    //val translationStrategy: TranslationStrategy = loadTranslationStrategyByURI(translationStrategyURI)
    // put constants in the Context
    val context = transferConstants(constantBlock)

    //copy solition


    // run through Context constants and add AddName and AddTarget and AddField HowTo-s over solution
    // run through Constants if TranslationTemplates found used it over Solution
    val updatedSolution = applyConstantTranslation(constantBlock, solution, context)
    // run through Solution nodes find proper TranslationStrategy Rule in KB
    // execute each Translation HowTo of Rule block
    val patch: KnowledgePatch = applyTranslation(updatedSolution, translationStrategy, context)
    patch
  }

  /**
   * Runs through the ActionIndividual as constant block and it's parameters, crates constantTranslationTemplate, applies template to solution
   * @param constantBlock the constants block to be processed.
   * @param solution translation will be used over it.
   * @context the variable Context.
   * @returns the updated Solution.
   */
  def applyConstantTranslation(constantBlock: ActionIndividual, solution: Solution, context: Context): Solution = {
    //instatiate trfield
    var trField = new TrConstantField()

    def checkSolutionElement(elem:HowTo):Boolean=
    {
      trField.apply(null,elem,context);
      if (elem.isInstanceOf[ActionIndividual] &&  elem.asInstanceOf[ActionIndividual].parameters!=null)
      {
           elem.asInstanceOf[ActionIndividual].parameters.foreach(b=>
           {
              checkSolutionElement(b);
           })
      }
      true;
    }

    solution.howTos.foreach(h=>
    {
      (checkSolutionElement(h));
    })
    LOG.info("ModifiedSolution: "+solution.toString)

    solution
   /* var updatedSolutionSolutionHowToList: List[HowTo] = List[HowTo]()
      val constantTranslationStrategyFactory = new ConstantTranslationWrapperFactory()
      val actionClass: ActionClass = constantBlock.actionClass
      if (actionClass.uri.toString != menta.model.Constant.modelNamespaceString + menta.model.Constant.constantBlock) {
        LOG.error("Inbound AddIndividual is not {} actual value is {}", menta.model.Constant.constantBlock, actionClass.uri.toString)
        throw new IllegalArgumentException("Inbound AddIndividual is not " + menta.model.Constant.constantBlock + " actual value is " + actionClass.uri.toString)
      }
      val constantsActionClasses: List[HowTo] = constantBlock.parameters
      val constantIndividuals: List[HowTo] = constantsActionClasses.filter(constant => {
        constant.isInstanceOf[AddIndividual]
      })
      if (constantIndividuals.size > 0) {
        LOG.debug("constantIndividuals {}", constantIndividuals)
        for (val constant <- constantsActionClasses) {
          val constantAddIndividual = constant.asInstanceOf[AddIndividual]
          LOG.debug("constantAddIndividual {}", constantAddIndividual)
          try {
            val value: AddIndividual = constantAddIndividual
            val address: URI = constantAddIndividual.actionClass.uri
            LOG.debug("address: {} value: {}", address, value)
            val constantTranslationTemplate: TrConstantWrapper = constantTranslationStrategyFactory(constantAddIndividual)
            for (val howTo: HowTo <- solution.howTos) {
              updatedSolutionSolutionHowToList = updatedSolutionSolutionHowToList :::
                List(constantTranslationTemplate(constantAddIndividual, howTo, context))
            }
          } catch {
            case e: IndexOutOfBoundsException => {
              LOG.error("constant does not contain 2 parameters: {}", e.getMessage)
            }
            case e: ClassCastException => {
              LOG.error("invalid constant parameter class: {}", e.getMessage)
            }
          }
        }
        if (updatedSolutionSolutionHowToList.size > 0) {
          new Solution(updatedSolutionSolutionHowToList)
        } else {
          solution
        }
      } else {
        solution
      }
    } else {
      solution
    }
    new Solution(updatedSolutionSolutionHowToList) */
  }

  /**
   * Run through Solution if TranslationTemplates found used it over Solution.
   * @param solution Constants Translation will be used over solution.
   * @param translationStrategy the strategy to be via constants.
   * @return Solution updated
   */
  def applyTranslation(solution: Solution, translationStrategy: TranslationStrategy, context: Context): KnowledgePatch = {
    var solutionHowToList = solution.howTos

    //suppose every solution has a root (ECJ requieremnts) skip root
    solutionHowToList=solutionHowToList.head.asInstanceOf[ActionIndividual].parameters;


    val translationStrategyFactory = new TranslationWrapperFactory()
    val translationResults: List[Pair[StringLiteral, HowTo]] = for (val solutionHowTo: HowTo <- solutionHowToList) yield {
      val fileName: StringLiteral = getFileName(solutionHowTo, translationStrategy)
      //val template: AddTranslationHowTo = translationStrategy.getTemplate(solutionHowTo)
      //val wrapper: TrWrapper = translationStrategyFactory(template)
      val applier = new TrApplyTemplates(new NilHowTo, new LETrue)
      val trRes = applier.apply(new AddIndividual(), solutionHowTo, context, translationStrategy)
      fileName -> trRes
    }
    val translationStringLiterals: List[Pair[StringLiteral, HowTo]] = translationResults.filter(filename_howToPair => {
      filename_howToPair._2.isInstanceOf[StringLiteral]
    })
    if (translationStringLiterals.size < translationResults.size) {
      LOG.warn("translation results contains {} non StringLiterals", translationResults.size - translationStringLiterals.size)
    }
    val translationStringLiteralsConverted: List[Pair[StringLiteral, StringLiteral]] = translationStringLiterals.map(filename_howToPair => {
      filename_howToPair._1 -> filename_howToPair._2.asInstanceOf[StringLiteral]
    })
    new KnowledgePatch(translationStringLiteralsConverted)
  }

  private def getFileName(solutionHowTo: HowTo, translationStrategy: TranslationStrategy) = {
    //find in solution HowTo special how to for file Name Store, should be at top
    var fileName: StringLiteral = translationStrategy.getTemplateFileName(solutionHowTo)
    var casted = solutionHowTo.asInstanceOf[ActionIndividual];
    casted.parameters.foreach(b=>{
      if (b.isInstanceOf[ActionIndividual] )
      {
        var ac= b.asInstanceOf[ActionIndividual];
        if (ac.actionClass!=null && ac.actionClass.superClass!=null && ac.actionClass.superClass.name=="AddFileName")
        {
           //TODO Use Helpers

            fileName= ac.parameters.head.asInstanceOf[ActionIndividual].parameters.head.asInstanceOf[StringLiteral]
        }
      }
    })

    if (fileName == null || fileName.valueString == null || fileName.valueString == "") {
      fileName = new StringLiteral(Constant.translatorDefaultLog)
    }
    fileName
  }


  /**
   * Runs through constants in constant block and for each constant.actionClass, searches for the translation strategy if it is found applies the transformation.
   * @param context to search constants and process.
   * @param solution to translate.
   * @returns updated solution
   */
  @deprecated
  def applyConstant2Solution(context: Context, solution: Solution): Solution = {
    var updatedSolutionHowToList = List[HowTo]()
    val tf = new TrConstantWrappersFactory()
    val constantsMapOption: Option[Map[URI, List[HowTo]]] = context.scope(Constant.translatorNamespace)
    constantsMapOption match {
      case Some(constantsMap: Map[URI, List[HowTo]]) => {
        for (val constantURI: URI <- constantsMap.keySet) {
          LOG.debug("constantURI {}", constantURI)
          val wrapper: TrConstantWrapper = tf(constantURI)
          if (constantsMap(constantURI).size > 0) {
            for (val constant <- constantsMap(constantURI)) {
              for (val solutionHowTo: HowTo <- solution.howTos) {
                val ht = wrapper(constant.asInstanceOf[AddIndividual], solutionHowTo, context)
                updatedSolutionHowToList = updatedSolutionHowToList ::: List(ht)
              }
            }
          }
        }
        new Solution(updatedSolutionHowToList)
      }
      case None => {
        solution
      }
    }
  }

  /**
   * Runs through the ActionIndividual as constant block and it's parameters.
   * @param constantBlock the constants block to be processed.
   * @returns the initialized Context.
   */
  def transferConstants(constantBlock: ActionIndividual): Context = {

    var context: Context = new Context()
     //gerate context
    if (constantBlock==null) return context;
    context.addConstantBlock(menta.model.Constant.modelNamespace,constantBlock)


    /*context.scope_=(Constant.translatorNamespace, Map[URI, List[HowTo]]())
      val actionClass: ActionClass = constantBlock.actionClass
      if (actionClass.uri.toString != menta.model.Constant.modelNamespaceString + menta.model.Constant.constantBlock) {
        LOG.error("Inbound AddIndividual is not {} actual value is {}", menta.model.Constant.constantBlock, actionClass.uri.toString)
        throw new IllegalArgumentException("Inbound AddIndividual is not " + menta.model.Constant.constantBlock + " actual value is " + actionClass.uri.toString)
      }
      val constantsActionClasses: List[HowTo] = constantBlock.parameters
      for (val constant: HowTo <- constantsActionClasses) {
        if (constant.isInstanceOf[AddIndividual]) {
          val constantAddIndividual = constant.asInstanceOf[AddIndividual]
          try {
            val value: AddIndividual = constantAddIndividual
            val address: URI = constantAddIndividual.actionClass.uri
            LOG.debug("address: {} value: {}", address, value)
            context.variable_=(Constant.translatorNamespace, address, value)
          } catch {
            case e: IndexOutOfBoundsException => {
              LOG.error("constant does not contain 2 parameters: {}", e.getMessage)
            }
            case e: ClassCastException => {
              LOG.error("invalid constant parameter class: {}", e.getMessage)
            }
          }
        } else {
          LOG.error("Inbound AddIndividual parameters are not AddIndividual")
        }
    }   */
      context

  }

  def createCode(solution: Solution, strategy: TranslationStrategy): String = {
    //TODO: use Solution next generate -(inherited from HowTo)
    //createCode(solution.howTos(0), strategy)
    applyTemplates(solution.howTos(0), strategy: TranslationStrategy)
  }

  /**
   * Implements ApplyTemplates node functions, translates to String.
   * @param node the node to be translated, ex.: node of the Solution.
   * @param paramList the list of the ApplyTemplates node parameters.
   * @param strategy TranslationStrategy to be used.
   * @returns String result of the translation.
   */
  @deprecated
  def applyTemplates(solutionHowTo: HowTo, strategy: TranslationStrategy): String = {
    val applier = new TrApplyTemplates(new NilHowTo, new LETrue)
    val trRes = applier.apply(new AddIndividual(), solutionHowTo, new Context, strategy)
    trRes.toString
    /*
    val template = strategy.getTemplate(node)
    var res = ""
    //for (val template: AddIndividual <- templateOption) {
    for (val parameter <- template.parameters) {
      var str = "";
      LOG.debug("class = " + parameter.getClass.getName + "; name = " + parameter.name)
      if (parameter.name == menta.model.Constant.stringLiteral)
        str = parameter.toString;
      else if (parameter.name == menta.model.Constant.trVariable)
        str = parameter.asInstanceOf[TrVariable].getValue(node, paramList).toString;
      else if (parameter.name == menta.model.Constant.trApplyTemplates) {
        if (node.isInstanceOf[ActionClass] && node.asInstanceOf[ActionClass].parameters != null)
          for (val subNode <- node.asInstanceOf[ActionClass].parameters)
            str += applyTemplates(subNode, paramList, strategy: TranslationStrategy)
      }
      res += str
    }
    //}

    res*/
  }

  @deprecated
  def createPatch(solution: Solution, constantBlock: AddIndividual) = null

  def apply(sourceURI: URI, translationStrategyURI: URI) = null

  def apply(inboundKnowledge: Knowledge, translationStrategyURI: URI) = null

  @deprecated
  def apply(inboundKnowledge: Knowledge) = null

  @deprecated
  def apply(sourceURI: URI): URI = null

  @deprecated
  def createPatch(solution: Solution): KnowledgePatch = {
    null
  }

}
