package menta.model.howto

import java.net.URI
import org.slf4j.LoggerFactory
import menta.model.helpers.constantblock.{AddFieldHelper, AddFieldChecker}
import menta.model.helpers.individual.AddConstantIndividualHelper

/**
 * @author talanov max
 * @date 2011-05-10
 * Time: 10:52 PM
 */

class Context(aContents: Map[URI, Map[URI, List[HowTo]]]) {

  /**
   * first URI the scope URI
   * second URI the variable URI
   * HowTo the value
   */
  type ContextType = Map[URI, Map[URI, List[HowTo]]]

  var theContents: ContextType = aContents

  def this() = this (Map())

  def contents = theContents

  def contents_(anContents: ContextType): Context = {
    theContents = anContents;
    this
  }


  val LOG = LoggerFactory.getLogger(this.getClass)


  def contents_=(anContents: ContextType): Context = {
    theContents = anContents;
    this
  }

  def removeScope(aScopeURI: URI) = {
    var scope = this.theContents.get(aScopeURI);
    if (!scope.isEmpty)
    {
      theContents=theContents.filterNot(p=>p._1==aScopeURI);
    }
  }

  def addConstantBlock(aScopeURI: URI, block: ActionIndividual): Context = {
    var trConstantScope = new URI(menta.model.Constant.trConstantScope);

    //suppose block is a list of target individuals
    block.parameters.foreach(singleBlock => {
      //extract action class and place constant under this actionbs class
      singleBlock match {
        case AddFieldChecker(addField) => {
          var actionClass = (new AddFieldHelper(addField)).actionClassURI();
          addEntry(trConstantScope, actionClass, List(addField), true);
        }
        case _ => {
          var actionClass = AddConstantIndividualHelper.getIndividual(singleBlock.asInstanceOf[AddIndividual], "").actionClass.uri;
          addEntry(trConstantScope, actionClass, List(singleBlock), true);
        }
      }
    });

    this
  }

  def addEntryInvariant(aScopeURI: URI, aVariableURI: URI, aValue: List[HowTo]): Context = {
    // check the inbound parameters:
    if (aScopeURI == null) {
      throw new NullPointerException("aScopeURI parameter is null")
    }
    if (aVariableURI == null) {
      throw new NullPointerException("aVariableURI parameter is null")
    }
    if (aValue == null) {
      throw new NullPointerException("aValue parameter is null")
    }
    val scopeMap: Option[Map[URI, List[HowTo]]] = this.theContents.get(aScopeURI)
    aValue.foreach(singleValue => {
      this.theContents += Pair(aScopeURI, scopeMap match {
        case Some(variableMap) => {
          var res = variableMap + Pair(aVariableURI, List(singleValue))
          alreadyAdded = List[HowTo]();
          if (singleValue.isInstanceOf[ActionIndividual]) {
            var childAutoMaps = _modifyValueNA(singleValue.asInstanceOf[ActionIndividual].parameters, aVariableURI.toString, 0)
            res = mergeMap(List(res, childAutoMaps))
          }
          res
        }
        case None => {
          var res = Map(Pair(aVariableURI, List(singleValue)))
          alreadyAdded = List[HowTo]();
          if (singleValue.isInstanceOf[ActionIndividual]) {
            var childAutoMaps = _modifyValueNA(singleValue.asInstanceOf[ActionIndividual].parameters, aVariableURI.toString, 0)
            res = mergeMap(List(res, childAutoMaps))
          }
          res
        }
      })
    });


    this
  }

  def addEntry(aScopeURI: URI, aVariableURI: URI, aValue: List[HowTo]): Context = {
    return addEntry(aScopeURI, aVariableURI, aValue, false);
  }

  /**
   * Adds entry in the context.
   * @scopeURI URI the scope URI
   * @variableURI URI the variable URI
   * @value List[HowTo] the value
   */
  def addEntry(aScopeURI: URI, aVariableURI: URI, aValue: List[HowTo], notScanChild: Boolean): Context = {
    // check the inbound parameters:
    if (aScopeURI == null) {
      throw new NullPointerException("aScopeURI parameter is null")
    }
    if (aVariableURI == null) {
      throw new NullPointerException("aVariableURI parameter is null")
    }
    if (aValue == null) {
      throw new NullPointerException("aValue parameter is null")
    }
    val scopeMap: Option[Map[URI, List[HowTo]]] = this.theContents.get(aScopeURI)
    this.theContents += Pair(aScopeURI, scopeMap match {
      case Some(variableMap) => {
        var res = variableMap + Pair(aVariableURI, aValue)
        alreadyAdded = List[HowTo]();
        if (!notScanChild) {
          var childAutoMaps = _modifyValue(aValue, aVariableURI.toString, 0)
          res = mergeMap(List(res, childAutoMaps))
        }
        res
      }
      case None => {
        var res = Map(Pair(aVariableURI, aValue))
        alreadyAdded = List[HowTo]();
        if (!notScanChild) {
          var childAutoMaps = _modifyValue(aValue, aVariableURI.toString, 0)
          res = mergeMap(List(res, childAutoMaps))
        }
        res
      }
    })
    this
  }

  def value(aScope: URI, aVariableURI: URI): Option[List[HowTo]] = {
    //log.debug("context {}", aContext.contents.get(aScope))
    for (scope <- this.contents.get(aScope)) {
      return scope.get(aVariableURI)
    }
    None
  }


  private var alreadyAdded: List[HowTo] = List[HowTo]();

  /*
   add all parts of solution to map
   consider URI format:
   mentaPrefix/#BaseClass.path.path
  */
  def _modifyValue(in: List[HowTo], childPath: String, childLevel: Int): Map[URI, List[HowTo]] = {
    var extendedMap = Map[URI, List[HowTo]]();

    var indexOfChild = 0;
    var newChildIndex = childPath;
    var key: URI = null;
    //across all howto
    in.foreach(hw => {

      //prevent recursion stack overflow
      if (alreadyAdded.contains(hw)) {
        //LOG.info("Warning, possible SOF detected "+hw.toString)
        //return  Map[URI, List[HowTo]]()
      };
      alreadyAdded = hw :: alreadyAdded


      //if (hw.uri == null) hw.uri = generateURI(hw);
      newChildIndex = childPath + "/" + indexOfChild;
      key = new URI(childPath + "/" + indexOfChild);


      if (childLevel > 0) {
        //add item index
        //hw.uri=modifyPath(hw.uri,childPath+childIndex+"/")
        //newChildIndex = childPath + childIndex;
        //key =new URI(newChildIndex) // modifyPath(parent.uri, newChildIndex);
        //put how to to map
        extendedMap += Pair(new URI(childPath + "/" + indexOfChild + "/"), List(hw));

      }
      else {
        //alredy put by caller
        //suppose root, leave url as is and skip
        //extendedMap+=Pair(hw.uri,List(hw));
        //extendedList = extendedList ::: List(hw);
      }

      var namePath = modifyPath(key, "/name/")
      extendedMap += Pair(namePath, List(new StringLiteral(hw.name)));
      if (hw.isInstanceOf[ActionIndividual]) {
        //put action class,name to Context

        if (key == null) key = new URI(childPath)
        if (hw.asInstanceOf[ActionIndividual].actionClass != null) {
          var actionClassPath = modifyPath(key, "/actionClass")
          extendedMap = mergeMap(List(extendedMap, _modifyValue(List(hw.asInstanceOf[ActionIndividual].actionClass), actionClassPath.toString, childLevel + 1)));
          //extendedMap += Pair(actionClassPath, List(new StringLiteral(hw.asInstanceOf[ActionIndividual].actionClass.name)));
        }


        if (hw.asInstanceOf[ActionIndividual].parameters != null) {

          //also append children
          extendedMap += Pair(new URI(newChildIndex + "/prms/"), hw.asInstanceOf[ActionIndividual].parameters);

          var newMap = _modifyValue(hw.asInstanceOf[ActionIndividual].parameters, newChildIndex + "/prms", childLevel + 1);
          //connect in list
          val listmap = List(extendedMap, newMap);
          extendedMap = mergeMap(listmap);

        }
      }
      indexOfChild = indexOfChild + 1;

    });

    extendedMap
  }

  def _modifyValueNA(in: List[HowTo], childPath: String, childLevel: Int): Map[URI, List[HowTo]] = {
    var extendedMap = Map[URI, List[HowTo]]();

    var indexOfChild = 0;
    var newChildIndex = childPath;
    var key: URI = null;
    //across all howto
    in.foreach(hw => {

      //prevent recursion stack overflow
      if (alreadyAdded.contains(hw)) {
        //LOG.info("Warning, possible SOF detected "+hw.toString)
        //return  Map[URI, List[HowTo]]()
      };
      alreadyAdded = hw :: alreadyAdded


      //if (hw.uri == null) hw.uri = generateURI(hw);
      //newChildIndex = childPath + "/" + indexOfChild;

      if (!hw.isInstanceOf[ActionIndividual]) {
        return Map[URI, List[HowTo]]();
      }
      var casted = hw.asInstanceOf[ActionIndividual];

      var currentChildName = childPath + "/" + casted.actionClass.name;
      if (casted.actionClass.name == null) {
        currentChildName = childPath + "/unknown";
      }

      key = new URI(currentChildName);


      //if (childLevel > 0) {
      //add item index
      //hw.uri=modifyPath(hw.uri,childPath+childIndex+"/")
      //newChildIndex = childPath + childIndex;
      //key =new URI(newChildIndex) // modifyPath(parent.uri, newChildIndex);
      //put how to to map
      extendedMap += Pair(new URI(currentChildName + "/"), List(hw));

      //}
      //else {
      //alredy put by caller
      //suppose root, leave url as is and skip
      //extendedMap+=Pair(hw.uri,List(hw));
      //extendedList = extendedList ::: List(hw);
      // }

      var namePath = modifyPath(key, "/name/")
      extendedMap += Pair(namePath, List(new StringLiteral(hw.name)));
      if (hw.isInstanceOf[ActionIndividual]) {
        //put action class,name to Context

        if (key == null) key = new URI(childPath)
        //if (hw.asInstanceOf[ActionIndividual].actionClass != null) {
        //  var actionClassPath = modifyPath(key, "/actionClass")
        //  extendedMap = mergeMap(List(extendedMap, _modifyValueNA(List(hw.asInstanceOf[ActionIndividual].actionClass), actionClassPath.toString, childLevel + 1)));
        //extendedMap += Pair(actionClassPath, List(new StringLiteral(hw.asInstanceOf[ActionIndividual].actionClass.name)));
        //}


        if (hw.asInstanceOf[ActionIndividual].parameters != null) {

          //also append children
          extendedMap += Pair(new URI(currentChildName + "/prms/"), hw.asInstanceOf[ActionIndividual].parameters);

          var newMap = _modifyValueNA(hw.asInstanceOf[ActionIndividual].parameters, currentChildName + "/prms", childLevel + 1);
          //connect in list
          val listmap = List(extendedMap, newMap);
          extendedMap = mergeMap(listmap);

        }
      }
      indexOfChild = indexOfChild + 1;

    });

    extendedMap
  }


  def mergeMap[A, B](ms: List[Map[A, B]]): Map[A, B] =
    (Map[A, B]() /: (for (m <- ms; kv <- m) yield kv)) {
      (a, kv) =>

        a + kv

    }


  private def modifyPath(src: URI, append: String): URI = {
    val res = new URI(src.getScheme, src.getAuthority, src.getPath + append, src.getQuery, src.getFragment);
    res
  }

  def scope(scopeURI: URI): Option[Map[URI, List[HowTo]]] = {
    theContents.get(scopeURI)
  }

  def scope_=(scopeURI: URI, contents: Map[URI, List[HowTo]]): Option[Map[URI, List[HowTo]]] = {
    theContents += scopeURI -> contents
    scope(scopeURI)
  }

  def scope_(scopeURI: URI, contents: Map[URI, List[HowTo]]): Option[Map[URI, List[HowTo]]] = {
    theContents += scopeURI -> contents
    scope(scopeURI)
  }

  def variable(scopeURI: URI, variableURI: URI): Option[List[HowTo]] = {
    for (val scopeMap: Map[URI, List[HowTo]] <- scope(scopeURI)) {
      return scopeMap.get(variableURI)
    }
    None
  }

  def variable_=(scopeURI: URI, variableURI: URI, value: HowTo): Option[HowTo] = {
    for (val scopeMap: Map[URI, List[HowTo]] <- scope(scopeURI)) {
      var updatedScopeMap: Map[URI, List[HowTo]] = Map[URI, List[HowTo]]()
      updatedScopeMap ++ scopeMap
      updatedScopeMap += variableURI -> List[HowTo](value)
      scope_=(scopeURI, updatedScopeMap)
      Some(value)
    }
    None
  }

  def variable_(scopeURI: URI, variableURI: URI, value: HowTo): Option[HowTo] = {
    for (val scopeMap: Map[URI, List[HowTo]] <- scope(scopeURI)) {
      var updatedScopeMap: Map[URI, List[HowTo]] = Map[URI, List[HowTo]]()
      updatedScopeMap ++ scopeMap
      updatedScopeMap += variableURI -> List[HowTo](value)
      Some(value)
    }
    None
  }
}