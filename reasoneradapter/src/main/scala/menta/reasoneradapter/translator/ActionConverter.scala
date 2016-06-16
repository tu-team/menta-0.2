package menta.reasoneradapter.translator


import nars.entity.Task
import ActionConverterTypes._

import nars.language.{Term}
import menta.model.howto.HowTo


/**
 * @auhor toscheva
 * Date: 07.03.11
 * Time: 16:49
 * Action Converter interface
 */


// TODO add Class diagram and wiki page with description.
trait ActionConverter {
  /**
   * convert How to to Nars statement
   * @param oper1 operand 1 (null if narsOp1 specified). For example if we have
   * compund how-to we process it as narsOp1 as already defined statement
   * @param oper1 second operand in How-To
   * @param narsOp1 first operand in terms of NARS
   * @parem narsOp2 second operand in terms of NARS
   */
  def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term

  /**
   * Converter type
   *
   */
  def actionType: ActionConverterTypes

}