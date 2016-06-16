package menta.translator

import org.slf4j.{LoggerFactory, Logger}
import wrappers.{LogicalExpression, TrWrapper}
import java.lang.NoSuchMethodError
import menta.model.howto.{StringLiteral, AddIndividual, HowTo, AddClass}

import menta.model.howto.Context

/**
 * @author talanov max
 * Date: 07.12.11
 * Time: 17:58
 */

class AddTranslationHowToClass() extends AddClass
{
  className = this.getClass.getName
}

class AddTranslationHowTo(list: List[HowTo]) extends AddIndividual(new AddTranslationHowToClass(), list) with TrWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context, strategy: TranslationStrategy, mode: StringLiteral, conditionExpression: LogicalExpression) = {
    throw new NoSuchMethodError("the Method should not be used, this should be abstract class")
  }
}