package menta.reasoneradapter.reasonerinterface.impl

import nars.main.{Memory, Center}
import nars.io.{Symbols, StringParser}
import nars.entity.Task
import java.io._
import menta.reasoneradapter.reasonerinterface.NARSInterface
import menta.reasoneradapter.selfcontrol.SelfControl
import menta.model.solutiongenerator.solutionchecker.Test
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.translator.HowToTranslator
import org.slf4j.{Logger, LoggerFactory}
import menta.reasoneradapter.selfcontrol.impl.SelfControlFactoryImpl
import java.lang.Object


/**
 * @author: talanov max, alexander toschev
 * Date: 17.07.2010
 * Time: 16:06:42
 */
object NARSHelper extends NARSInterface {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  /**Flag for running continuously */
  private var running: Boolean = false

  /**Output experience into a file */
  private val outExp: PrintWriter = null

  private var selfControl: SelfControl = null

  private val translator = new HowToTranslator

  private val lock:Object=new Object

  /*
  Handle request using array of tasks
  @param tasks - input value
   */
  def handle(tasks: List[Task]):FrequencyConfidenceNegationTriple = {

    if (tasks.length<=0)
    {
      return new FrequencyConfidenceNegationTriple(0,0,false)
    }


    var stopper = selfControl.getMaxAllowedCycles()
    var clock = 0l
    val start = System.nanoTime

    Center.reset()
    Memory.init()
    for (val t: Task <- tasks) {
      //LOG.debug(" task {}", t)

      Memory.inputTask(t)
      //LOG.debug("Memory Task buffer: {} ", Memory.newTasksToString)
    }

    if (tasks.length<=1)
    {
        val bdg = tasks.head.getSentence.getTruth

        //extract freq confidence
        return new FrequencyConfidenceNegationTriple(bdg.getFrequency, bdg.getConfidence, false)
    }

    // run the Memory.cycle the proper number of times
    var currentIteration = 0;
    //LOG.debug("stopper {}", stopper)
    def newTasksExtr():List[Task]=  scala.collection.JavaConversions.asScalaIterable(Memory.getNewTasks).toList;
    var oldOutput:List[Task]=null

    //initial step for NARS
    Center.setTime(clock)
    Memory.cycle()


    //process cycle until template found
    while (stopper + 1 > 0  && (oldOutput==null || !selfControl.checkTemplate(oldOutput))) {
      clock+=1
      val newOutput=newTasksExtr

      //proper process clock (if something on out - reset clock)
      if (newOutput.length>0)
      {
        clock=0;
        oldOutput=newOutput;
        //LOG.debug("old ouput {}", oldOutput)
      }
      Center.setTime(clock)
      //LOG.debug("memory cycle {}", stopper)
      Memory.cycle()
      stopper -= 1
    }

    if (stopper<=0)
      return new FrequencyConfidenceNegationTriple(0,0.9,false);

    //LOG.debug(" Last state Memory Task buffer: {}", oldOutput)
    LOG.debug("Processing of the tasks {} Done in {} s", tasks, (System.nanoTime - start) / 1000000000d)

    val bdg = oldOutput.last.getSentence.getTruth

    //extract freq confidence
    val triplet = new FrequencyConfidenceNegationTriple(bdg.getFrequency, bdg.getConfidence, false)
    triplet
  }

  /**
   * @deprecated
   * process request and return result
   * @param request - value to Test
   */
  def handle(request: Test): FrequencyConfidenceNegationTriple = {
    reset
    //translate
    var tasks = List(translator.Translate(request.rule.howTo, null, null))

    //assume that all already converted to reasoner suitable format (prop (apply, howto1,howto2)
    request.solution.howTos.foreach(f => {
      tasks = tasks ::: List(translator.Translate(f, null, null))
    })
    handle(tasks);

  }

  def apply(tasks: List[Task], numCycles: Long): FrequencyConfidenceNegationTriple = {
    reset;

      selfControl= SelfControlFactoryImpl.apply(null,null);
    LOG.debug("Max cycles {0}",  selfControl.getMaxAllowedCycles());

    handle(tasks)
  }

  def setSelfControl(value: SelfControl) {
    selfControl = value
  }

  /**
   * Loads file from specified directory and fileName and process it in NARS and returns the last Task.
   * @param directoryName String the name of the directory.
   * @param fileName String the name of the file to process.
   * @return List[Task] the last task processed.
   */

  @Deprecated
  def loadProcessFile(directoryName: String, fileName: String): List[Task] = {
    val start = System.nanoTime
    var inExp: BufferedReader = null
    reset
    try {
      inExp = new BufferedReader(new FileReader(directoryName + fileName))
    }
    catch {
      case ex: IOException => {
        LOG.error("i/o error: " + ex.getMessage)
      }
    }
    loadLines(inExp)

    LOG.debug(" Last state Memory Task buffer: {}", Memory.newTasksToString)
    LOG.debug("Processing of the file {} Done in {} s", fileName, (System.nanoTime - start) / 1000000000d)
    List[Task](Memory.getNewTasks.toArray(new Array[Task](0)): _*)
  }

  /**
   * Loads String and process it in NARS and returns the last Task.
   * @param in String to process.
   * @return the last task. 
   */
  @Deprecated
  def loadProcessString(in: String): List[Task] = {
    val start = System.nanoTime
    reset
    val inExp = new BufferedReader(new StringReader(in))
    loadLines(inExp)

    LOG.debug(" Last state Memory Task buffer: {}", Memory.newTasksToString)
    LOG.debug("Processing of the string {} Done in {} s", in, (System.nanoTime - start) / 1000000000d)
    List[Task](Memory.getNewTasks.toArray(new Array[Task](0)): _*)
  }

  /**
   * Loads tasks in tho the Memory and runs specified number of cycles.
   * @param tasks List[Task] the list of the tasks to process.
   * @param numCycles the number of cycles to run.
   * @return List[Task] the result of inference. 
   */
  @Deprecated
  def loadTasks(tasks: List[Task], numCycles: Long): List[Task] = {
    throw new Exception("Method not supported")
    var stopper = numCycles
    var clock = 0l
    val start = System.nanoTime
    reset
    Center.reset()
    for (val t: Task <- tasks) {
      LOG.debug(" task {}", t)
      Memory.inputTask(t)
      LOG.debug("Memory Task buffer: {} ", Memory.newTasksToString)
    }
    // run the Memory.cycle the proper number of times
    LOG.debug("stopper {}", stopper)
    while (stopper + 1 > 0) {
      clock += 1
      Center.setTime(clock)
      Memory.cycle()
      stopper -= 1
    }
    LOG.debug(" Last state Memory Task buffer: {}", Memory.newTasksToString)
    LOG.debug("Processing of the tasks {} Done in {} s", tasks, (System.nanoTime - start) / 1000000000d)
    List[Task](Memory.getNewTasks.toArray(new Array[Task](0)): _*)
  }

  /**
   * Loads single line in NARS memory, or runs the found number of times NARS cycles if number is found in the string.
   * @param inExp BufferedReader to process.
   */
  @Deprecated
  def loadLines(inExp: BufferedReader) {
    var line: String = null
    line = inExp.readLine
    var stopper = 0l
    var clock = 0l
    while (line != null) {
      stopper = loadLine(line)
      LOG.debug("stopper {}", stopper)
      while (stopper > 0) {
        clock += 1
        Center.setTime(clock)
        Memory.cycle()
        stopper -= 1
      }
      line = inExp.readLine
      // LOG.debug(" line: {}", line)
    }
    inExp.close()
  }

  /**
   * Loads line and returns the number of cycles.
   * @param line String to process.
   * @return Long the number of cycles.
   */
  @Deprecated
  def loadLine(inLine: String): Long = {
    var cycle: Long = 0l
    // LOG.debug("line {}", inLine)
    val line = inLine.trim
    if (line != null && line.trim.length > 0) {
      if (line.length > 0) {
        try {
          cycle = line.toLong
        }
        catch {
          case e: NumberFormatException => {
            if (line.charAt(0) == Symbols.RESET_MARK) {
              Center.reset()
              saveLine(line)
            }
            else if (line.charAt(0) == Symbols.COMMENT_MARK) {
              saveLine(line)
            }
            else {
              StringParser.parseExperience(new StringBuffer(line))
            }
          }
        }
      }
    }
    LOG.debug("Memory Task buffer: {} ", Memory.newTasksToString)
    cycle
  }

  /**
   * Write a line into the output experience file
   * @param line The output line
   */
  @Deprecated
  private def saveLine(line: String): Unit = {
    if (outExp != null) {
      outExp.println(line)
    }
  }


  /**
   * Reset the system and initialises with an empty memory and reset clock.
   */
  @Deprecated
  def reset: Unit = {
    Center.start
  }
}