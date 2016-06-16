package menta


import java.io.{IOException, FileReader, BufferedReader}
import org.junit.Test
import org.slf4j.LoggerFactory
import nars.language.{Term, Inheritance}
import nars.entity._

/**
 * The benchmarking and testing class for the refactored NARS.
 */
@Test
class AppTest {
  val LOG = LoggerFactory.getLogger(this.getClass)
  val completeTestFileName = "SimpleAddFieldFitnessFunction.exp"
  val directoryName = "./src/main/resources/"
  val test1FileName = "test1.narsese"
  val test2FileName = "test2.narsese"
  val test3FileName = "test3.narsese"
  val test4FileName = "test4.narsese"
  val test5FileName = "test5.narsese"
  val testEquivalenceFileName = "testEquivalence.narsese"

  val testString = "** combine results\\n<duplicate --> true>.\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n(--, <duplicate --> true>).\\n59\\n OUT: <duplicate --> true>. %0.10;0.99% {59 : 7;2;3;9;5;1;4;10}"
  val testStringSimple = "**\n<Self --> ok>.\r\n<Self --> ok>.\r\n1"

  @Test
  def readTestExperience() {
    try {
      new BufferedReader(new FileReader(directoryName + completeTestFileName))
    }
    catch {
      case ex: IOException => {
        System.out.println("i/o error: " + ex.getMessage)
        LOG.error("i/o error: " + ex.getMessage)
      }
    }
  }

  /* @Test
  def test1 = {
    val res = NARSHelper.loadProcessFile(directoryName, test1FileName)
    logAndAssert(res)
  }

  // @Test
  def test2 = {
    val res = NARSHelper.loadProcessFile(directoryName, test2FileName)
    logAndAssert(res)
  }

  // @Test
  def test3 = {
    val res = NARSHelper.loadProcessFile(directoryName, test3FileName)
    logAndAssert(res)
  }

  // @Test
  def test4 = {
    val res = NARSHelper.loadProcessFile(directoryName, test4FileName)
    logAndAssert(res)
  }

  // @Test
  def test5 = {
    val res = NARSHelper.loadProcessFile(directoryName, test5FileName)
    logAndAssert(res)
  }

  // @Test
  def testEquivalence = {
    val res = NARSHelper.loadProcessFile(directoryName, testEquivalenceFileName)
    logAndAssert(res)
  }

  @Test
  def testLoadProcessString = {
    val res = NARSHelper.loadProcessString(testStringSimple)
    logAndAssert(res)
  }

  @Test
  def testLoadProcessTask = {
    val cyclesNumber = 1
    NARSHelper.reset
    // first task
    val punc0: Char = '.'
    val truth0 = new TruthValue(1.0f, 0.9f)
    val stamp0 = new Stamp()
    val content0 = Inheritance.make(new Term("Self"), new Term("ok"))
    val sentence0 = Sentence.make(content0, punc0, truth0, stamp0, null, null)
    val budget0 = new BudgetValue(0.8f, 0.8f, 0.9486832618713379f)
    val task0: Task = new Task(sentence0, budget0)
    // second task
    val punc1: Char = '.'
    val truth1 = new TruthValue(1.0f, 0.9f)
    val stamp1 = new Stamp()
    val content1 = Inheritance.make(new Term("Self"), new Term("ok"))
    val sentence1 = Sentence.make(content1, punc1, truth1, stamp1, null, null)
    val budget1 = new BudgetValue(0.8f, 0.8f, 0.9486832618713379f)
    val task1: Task = new Task(sentence1, budget1)
    val res = NARSHelper.loadTasks(List[Task](task0, task1), cyclesNumber)
    logAndAssert(res)
  }

  private def logAndAssert(res: List[Task]) {
    LOG.info(" {}", res)
    assert(res != null)
  }  */
}