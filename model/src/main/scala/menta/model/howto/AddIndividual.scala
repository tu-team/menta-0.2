package menta.model.howto

import java.net.URI

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 17:46
 */

class AddIndividual(aActionClass: AddClass, aParameters: List[HowTo], @deprecated destination: AddIndividual)
  extends ActionIndividual(aActionClass, aParameters) {
  def this() = this (null, null, null)

  /**
   * Public constructor with no deprecated destination parameter.
   * @param actionClass the AddClass of the AddIndividual.
   * @param parameters the List[HowTo] to be used in AddIndividual.
   */
  def this(actionClass: AddClass, parameters: List[HowTo]) = {
    this (actionClass, parameters, new AddIndividual())
    //this.uri = new URI(URI)
  }

  def this(actionClass: AddClass, parameters: List[HowTo], destination: AddIndividual, URI: String) = {
    this (actionClass, parameters, destination)
    this.uri = new URI(URI)
  }

  override def clone(): AnyRef = {
    this.name
    val res = new AddIndividual()
    this.mClone(res)
    return res
  }

  override def toString: String = {
    val name = if (this.name != null) {
      this.name + "."
    } else {
      ""
    }
    var res = "";
    if (this.actionClass == null || this.actionClass.name == null || this.actionClass.uri == null) {
      res = "\tAddIndividual, unknown :" + super.toString + "\n"; //+ Constant.modelNamespaceString + "AddClass -> unknown" + super.toString + " " + name + " " + "[]\n{"
    } else {
      res = "\tAddIndividual," + this.actionClass.name + ", " + this.actionClass.uri + "\n"
    }
    if (this.parameters != null) {
      res += "{\n";
      parameters.foreach(b => {

        if (b != null) {
          res += "\t\t[" + b.toString + "];\n";
        }
        else {
        }
      });
      res += "}\n";
    }
    res
  }
}

object AddIndividual {
  def unapply(howTo: HowTo): Option[AddIndividual] = {
    if (howTo.isInstanceOf[AddIndividual]) {
      Some(howTo.asInstanceOf[AddIndividual])
    } else {
      None
    }
  }
}

