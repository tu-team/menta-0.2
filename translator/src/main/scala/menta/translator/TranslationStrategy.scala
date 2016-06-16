package menta.translator

import menta.translator.helper.TranslationStrategyClassHelper
import org.slf4j.{LoggerFactory, Logger}
import java.net.URI
import menta.model.howto._

/**
 * @author Chepkunov Adel
 * Date: 16.11.11
 * Time: 18:57
 */

class AddLanguage(lang: String) extends StringLiteral(lang) {

}


class TranslationStrategyRuleClass(_superClass: ActionClass, _parameters: List[HowTo]) extends ActionClass(_superClass, _parameters) {
  def this() = this (null, null)

  uri = new URI(menta.model.Constant.modelNamespaceString + menta.model.Constant.trRule)
}


class TranslationStrategyRule(aTargetLanguage: AddLanguage, aDomainHowToClassURI: URI, aTranslationTemplate: AddTranslationHowTo) extends AddIndividual {

  def this(aTargetLanguage: AddLanguage, aDomainHowToClass: ActionClass, aTranslationTemplate: AddTranslationHowTo) =
    this (aTargetLanguage, aDomainHowToClass.uri, aTranslationTemplate)

  def this(aTargetLanguage: AddLanguage, aClassName: String, aTranslationTemplate: AddTranslationHowTo) =
    this (aTargetLanguage, new URI(menta.model.Constant.modelNamespaceString + aClassName), aTranslationTemplate)

  actionClass = new TranslationStrategyRuleClass()
  parameters = List(aTargetLanguage, new StringLiteral(aDomainHowToClassURI.toString), aTranslationTemplate)

  //TODO create action class trRule
  def domain() = parameters(1).asInstanceOf[StringLiteral].toString

  val translationTemplate = parameters(2).asInstanceOf[AddTranslationHowTo]
}

object TranslationStrategyRule {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def unapply(individual: AddIndividual): Option[TranslationStrategyRule] = {
    if (individual.actionClass.uri.toString.contains(menta.model.Constant.trRule)) {
      try {
        Some(individual.asInstanceOf[TranslationStrategyRule])
      } catch {
        case e: ClassCastException => {
          LOG.error(e.getMessage)
          None
        }
      }
    } else {
      None
    }
  }
}

object TranslationStrategy {

  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(individual: AddIndividual): TranslationStrategy = {
    try {
      individual.asInstanceOf[TranslationStrategy]
    } catch {
      case e: ClassCastException => {
        LOG.error("Individual is not TranslationStrategy: {}", e.getMessage)
        throw e
      }
    }
  }
}

class TranslationStrategy(val translationStrategyRules: List[HowTo]) extends AddIndividual(new TranslationStrategyClassHelper()(), translationStrategyRules) {

  this.parameters = translationStrategyRules

  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * Returns the filename according to the templateHowTo.
   * @templateHowTo the HowTo to find the filename.
   * @return String filename.
   */
  def getTemplateFileName(templateHowTo: HowTo): StringLiteral = {
    new StringLiteral("")
  }

  def getTemplate(node: HowTo): AddTranslationHowTo = {
    if (!node.isInstanceOf[ActionIndividual]) throw new Exception("Solution is not individual " + node.toString)
    if (node.asInstanceOf[ActionIndividual].actionClass == null) throw new Exception("Individual must have an action class")
    getTemplate(new URI(menta.model.Constant.modelNamespaceString + node.asInstanceOf[ActionIndividual].actionClass.name))
     
  }

  def getTemplate(domainURI: URI): AddTranslationHowTo = {
    //LOG.debug("domainURI {}", domainURI)
    if (domainURI == null) {
      return new AddTranslationHowTo(List())
    }

    def filterTranslationStrategyRule(node: HowTo): Boolean = {
      node match {
        case TranslationStrategyRule(tr: TranslationStrategyRule) => {
          if (tr != null && tr.domain() != null && domainURI != null) {
            //LOG.debug("tr.domain() {}", tr.domain())
            domainURI.toString.toUpperCase == tr.domain().toUpperCase
          } else {
            false
          }
        }
        case _ => false
      }
    }

    //  .asInstanceOf[TranslationStrategyRule].domain()
    val matched: List[HowTo] = parameters.filter(filterTranslationStrategyRule)
    if (matched.size == 1) {
      matched(0).asInstanceOf[TranslationStrategyRule].translationTemplate
    } else if (matched.size > 1) {
      //LOG.error("matched " + matched.size + " rules for domain " + domainURI)
      matched(0).asInstanceOf[TranslationStrategyRule].translationTemplate
    } else if (domainURI.toString != menta.model.Constant.modelNamespaceString) {
      //LOG.info("try to find default temlate for spcified domainURI {}...", domainURI.toString)
      getTemplate(new URI(menta.model.Constant.modelNamespaceString))
      //LOG.error("empty domain URI specified")
    } else {
      LOG.error("no TranslationRules found")
      new AddTranslationHowTo(List())
    }
  }


  def getTemplate(node: HowTo, condition: HowTo): Option[AddTranslationHowTo] = {
    //ToDo: do it
    None
  }


  def getTemplateOption(node: HowTo): Option[AddTranslationHowTo] = {
    getTemplateOption(node.uri)
  }

  def getTemplateOption(domainURI: URI): Option[AddTranslationHowTo] = {
    val res = getTemplate(domainURI)
    if (res.parameters.size > 0) {
      Some(res)
    } else {
      None
    }
  }

}

/*

class TranslationStrategyRuleInstance(val targetLanguage: AddLanguage, val domainHowToIndividual: AddIndividual, val translationResult: Knowledge) {
  //TODO: map to parameters
}

class TranslationStrategyInstance(val translationStrategyRuleInstance: List[HowTo]) {

  //TODO: map to parameters
}

*/
