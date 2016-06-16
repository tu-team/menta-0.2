package menta.translator.constant

import java.net.URI

/**
 * @author talanov max
 * Date: 24.11.11
 * Time: 14:42
 */

object Constant {
  val translatorNamespaceString = "http://menta.org/ontologies/v.0.2/translator/"
  val translatorNamespace = new URI(translatorNamespaceString)
  val translationStrategyClassName = "TranslationStrategy"
  val translatorDefaultLog = "TranslatorDefaultLog.log"
  val constantAddName = "AddNameConstant"
  val constantAddField = "AddFieldConstant"
  val constantAddTarget = "AddTargetConstant"
  val currentNode = "$CurrentNode"
}