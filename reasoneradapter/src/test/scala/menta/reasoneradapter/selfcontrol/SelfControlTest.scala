package menta.reasoneradapter.selfcontrol

//A.T. comment can't compile
// ayratn: what was the problem with the compilation?
//       next time please ask me rather than just commenting everything out
import impl.SelfControlFactoryImpl
import org.junit._
import Assert._
import nars.language.{Term, Inheritance}
import nars.entity._
import nars.main.Memory

/**
 * SelfControl component implementation test
 *
 * @author ayratn
 */
@Test
class SelfControlTest {


  @Test
  def testOK()
  {
    assert(true);
  }
 @Test
  def testSelfOk() {
    Memory.init

    val self : Term = new Term("Self")
    val ok : Term = new Term("Ok")
    val some : Term = new Term("Some")

    var inheritance = Inheritance.make(self, ok)
    var question = new Question(inheritance, ',', new Stamp());
    var testTask : Task = new Task(question, new BudgetValue())

    val selfControl : SelfControl = SelfControlFactoryImpl.apply(null,null)
    var tasks : List[Task] = List[Task]()
    tasks ::= testTask
    assertTrue(selfControl.checkTemplate(tasks))

    inheritance = Inheritance.make(self, some)
    question = new Question(inheritance, ',', new Stamp());
    testTask = new Task(question, new BudgetValue())
    tasks = List[Task](testTask)
    assertFalse(selfControl.checkTemplate(tasks))

    inheritance = Inheritance.make(some, ok)
    question = new Question(inheritance, ',', new Stamp());
    testTask = new Task(question, new BudgetValue())
    tasks = List[Task](testTask)
    assertFalse(selfControl.checkTemplate(tasks))
  }

  @Test
  def testMaxCycleCount() {
    val selfControl : SelfControl = SelfControlFactoryImpl.apply(null,null)
    assertFalse(selfControl.checkStop(1))
    assertTrue(selfControl.checkStop(5001))
    assertTrue(selfControl.checkStop(5003))
    selfControl.template.maxCyclesCount = 2
    assertFalse(selfControl.checkStop(2))
    assertTrue(selfControl.checkStop(3))
  }
}