package menta.dao.config

import java.io.{IOException, FileNotFoundException, File}
import java.util.Properties

/**
 * Interface used to work with entity manager factory configuration.
 * Allows to read configuration from {@link File} or from classpath.
 *
 * @author ayratn
 */
trait Configuration {

  /**
   * This is the default properties file configuration is being read from if another one is not specified
   */
  val DEFAULT_PROPERTIES_FILE : String = "database.properties"

  /**
   * Returns the {@link Map} of properties that defines the configuration.
   */
  def getProperties : Map[String, Object]

  /**
   * Reads the configuration from {@link File}.
   */
  //@throws(classOf[FileNotFoundException, IOException])
  def loadFromFile(file: File): Properties

  /**
   * Reads configuration from default properties file.
   */
  //@throws(classOf[FileNotFoundException, IOException])
  def loadFromClasspath(): Properties

  /**
   * Reads the configuration from relative path to file in classpath.
   */
  //@throws(classOf[FileNotFoundException, IOException])
  def loadFromClasspath(path: String): Properties

  /**
   * Returns path to database. Can be NULL if not defined.
   */
  def getDataSourcePath: String
}