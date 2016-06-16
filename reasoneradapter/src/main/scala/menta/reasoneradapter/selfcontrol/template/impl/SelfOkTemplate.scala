package menta.reasoneradapter.selfcontrol.template.impl

import nars.language.Term
import nars.entity.Task
import menta.reasoneradapter.selfcontrol.entity.Objective

/**
 * Self->Ok template.
 * Checking for Self->Ok is a custom case so it is handled separately.
 *
 * @author ayratn
 */
class SelfOkTemplate extends TemplateImpl {



  override def matchTheTemplate(tasks: List[Task]): Boolean = {

    tasks.foreach(
      task => {
        var term = task.getContent().asInstanceOf[Term]

        if (term.isInstanceOf[nars.language.Inheritance]) {
          val subjectName = term.asInstanceOf[nars.language.Inheritance].getSubject().getName;
          val predicateName = term.asInstanceOf[nars.language.Inheritance].getPredicate().getName;

          if ("self".equalsIgnoreCase(subjectName) && "ok".equalsIgnoreCase(predicateName)) {
            return true
          }
        }
      }
    )
    return false;
  }

}