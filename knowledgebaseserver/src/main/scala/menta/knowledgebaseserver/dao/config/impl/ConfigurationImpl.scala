package menta.dao.config.impl

import java.io.{FileInputStream, IOException, File}
import java.util.Properties
import org.slf4j.LoggerFactory
import collection.immutable.HashMap
import menta.dao.config.Configuration

import menta.utilities.system.SystemOperations

/**
 * @author: Aidar Makhmutov
 * Date: 24.02.2011
 * TODO: Take a look for http://www.rgagnon.com/javadetails/java-0434.html
 * TODO: why it does not use the HGConfiguration?
 */

class ConfigurationImpl extends Configuration {

  val log = LoggerFactory.getLogger(this.getClass)

  //get current path to classes (where database.propeties exist)
  var currentPath = getClass.getProtectionDomain.getCodeSource.getLocation.toString.substring(6);

  val defaultClassPath = DEFAULT_PROPERTIES_FILE
  var classPath = ""
  var properties: Properties = null
  var propertiesMap: Map[String, Object] = null


  def loadFromClasspath(): Properties = {
    return loadFromClasspath(defaultClassPath)
  }

  def loadFromClasspath(path: String): Properties = {
    classPath = path
    //log.info("Loading property file from class path: " + path)
    // val file: File = new File(path)
    // loadFromFile(file)

    properties = new Properties()
    try {
      properties.load(this.getClass.getClassLoader.getResourceAsStream(path))
    }
    catch {
      case e: IOException => {
        log.error("Unable to read property file.")
        throw new IOException("Unable to read property file.\n" + e.toString)
      }
    }
    //log.info("Property file has been loaded successfully.")
    properties

  }

  def loadFromFile(file: File): Properties = {
    properties = new Properties();
    try {
      properties.load(new FileInputStream(file));
    }
    catch {
      case e: IOException => {
        log.error("Unable to read property file.")
        throw new IOException("Unable to read property file.\n" + e.toString)
      }
    }
    //log.info("Property file has been loaded successfully.")
    properties
  }

  def getProperties: Map[String, Object] = {
    if (properties == null)
      log.warn("Properties are null. Configuration hadn't been loaded yet.")
    propertiesMap = new HashMap[String, Object]

    //TODO why not to convert to SCALA set properties.stringPropertyNames
    val javaPropertiesSet = properties.stringPropertyNames
    val propertiesSet = scala.collection.JavaConversions.asScalaSet(javaPropertiesSet)
    propertiesSet.foreach((s: String) =>
      propertiesMap += s -> properties.getProperty(s)
    )
    propertiesMap
  }

  def getDataSourcePath: String = {
    if (properties == null) {
      log.error("Data Source Path is empty. Configuration hadn't been loaded yet.")
      return ""
    }
    else
    {
      var dir=   properties.getProperty("database.root.dir")
      if (SystemOperations.isWindows())
      {
           dir=dir.replace("/","\\");
      }
      else
      {
        dir=dir.replace("\\","/");

      }
      SystemOperations.getMentaDir()+ dir
    }
  }


  def getDefaultClassPath = defaultClassPath
}