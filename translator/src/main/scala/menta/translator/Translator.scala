package menta.translator

import menta.model.translator.Translation
import java.net.URI
import menta.model.container.KnowledgePatch
import menta.model.Knowledge
import menta.model.howto.{AddIndividual, Solution}

/**
 * Translates the inbound Knowledge in descendants of Report.
 * Translates file resources specified by sourceURI in HowTo-s.
 * Creates patch from specification Solution
 *
 * @author talanov max
 * Date: 07.03.11
 * Time: 17:35
 */

trait Translator {
  @deprecated
  def apply(inboundKnowledge: Knowledge): Translation

  @deprecated
  def apply(sourceURI: URI): URI

  @deprecated
  def createPatch(solution: Solution): KnowledgePatch

  @deprecated
  def createPatch(solution: Solution, constantBlock: AddIndividual): KnowledgePatch

  /**
   * Creates patch of solution and constantBlock via translationStrategy.
   * @param solution Solution to create the patch over it.
   * @param constantBlock the block to be processed over solution.
   * @param translationStrategyURI the URI of the complex of the TranslationRule-s to be used to produce patch.
   * @return KnowledgePatch created.
   */
  def createPatch(solution: Solution, translationStrategy: TranslationStrategy, constantBlock: AddIndividual): KnowledgePatch

  /**
   * Knowledge to Knowledge translation.
   * @param inboundKnowledge to be translated.
   * @param translationStrategyURI the URI of the set of TranslationRule-s.
   * @return Translation translation result Knowledge
   */
  def apply(inboundKnowledge: Knowledge, translationStrategyURI: URI): Translation

  /**
   * Importing translation.
   * @param sourceURI the URI of the sources to be translated in HowTo-s.
   * @param translationStrategyURI the URI of the strategy to be used.
   * @return URI of the root resource imported.
   */
  def apply(sourceURI: URI, translationStrategyURI: URI): URI
}