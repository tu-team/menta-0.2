package menta.utilities.system

/**
 * @author toscheva
 * Date: 26.11.11
 * Time: 16:52
 */

object SystemOperations {
    def isWindows():Boolean=
    {
       var osName=   System.getProperty("os.name");
       if (osName.toLowerCase.contains("windows")) return true;
       return false;
    }

    def getMentaDir():String=
    {
      return System.getProperty("user.dir")
    }
}