package menta.dao

import java.net.URI

/**
 * Created by IntelliJ IDEA.
 * User: alexander
 * Date: 16.04.11
 * Time: 12:15
 * class that hold additional data functions
 */

object Utils
{
  /**
   * Convert URI to string
   * @param uri URI class to be converted
   */
  def uriToString(uri:URI):String=
  {
     if (uri!=null)
       return uri.toString();
     return null;
  }

  /**
   * Convert string to URI
   * @param uri URI class to be converted
   */
  def stringToURI(uri:String):URI=
  {
    if (uri!=null)
      return URI.create(uri);
    return null;

  }
}