package menta.model.util.serialization.protocols

import menta.model.howto.AddClass
import sbinary.{Output, Input, Format, DefaultProtocol}
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 07.12.11
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */

object  AcceptanceCriteriaProtocol extends DefaultProtocol with RuleProtocol
{
  implicit object AcceptanceCriteriaFormat extends Format[AcceptanceCriteria]{
      def reads(in:Input)=
      {
        var res= new AcceptanceCriteria
        readProperties(in,res)
        res
      }
      def writes(out:Output,value:AcceptanceCriteria)
      {
        writeProperties(out,value)
        //writeString(out,value.name);
      }
    }

}