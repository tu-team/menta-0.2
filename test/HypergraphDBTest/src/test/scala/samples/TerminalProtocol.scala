package samples
import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.Terminal

/**
 * Created by IntelliJ IDEA.
 * User: alexander
 * Date: 11.08.11
 * Time: 0:49
 * To change this template use File | Settings | File Templates.
 */

object TerminalProtocol extends DefaultProtocol{
  implicit object TerminalFormat extends Format[Terminal]{
    def reads(in:Input)=new Terminal(readString(in),false)
    def writes(out:Output,value:Terminal)= writeString(out,value.name);
  }

}