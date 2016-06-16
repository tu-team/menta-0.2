package menta

import java.net.URI

import collection.immutable.HashSet
import menta.model.howto.{StringLiteral, AddIndividual, AddClass}

/**
 * Author: Aidar Makhmutov
 * Date: 16.02.2011
 */


object TestData {
  /*
  // General super class
  val addClass_SuperClass = new AddClass()
  addClass_SuperClass.setUri("menta/v0.2#addClass_SuperClass")
  val addName_SuperClass = new AddClass(addClass_SuperClass, List(), HashSet(), "menta/v0.2#addName")
  val addSubject_SuperClass = new AddClass(addClass_SuperClass, List(addClass_SuperClass), HashSet(addClass_SuperClass), "menta/v0.2#addSubject")
  val addObject_SuperClass = new AddClass(addClass_SuperClass, List(addClass_SuperClass), HashSet(addClass_SuperClass), "menta/v0.2#addObject")
  // addName_SuperClass serves as number variable
  val addProperty_SuperClass = new AddClass(addClass_SuperClass, List(addSubject_SuperClass, addObject_SuperClass, addName_SuperClass), HashSet(addSubject_SuperClass, addObject_SuperClass), "menta/v0.2#addProperty")
  val addImplication_SuperClass = new AddClass(addClass_SuperClass, List(addSubject_SuperClass, addObject_SuperClass), HashSet(addSubject_SuperClass, addObject_SuperClass), "menta/v0.2#addImplication")
  val addConjunction_SuperClass = new AddClass(addClass_SuperClass, List(addSubject_SuperClass, addObject_SuperClass), HashSet(addSubject_SuperClass, addObject_SuperClass), "menta/v0.2#addConjunction")
  val addNegation_SuperClass = new AddClass(addClass_SuperClass, List(addClass_SuperClass), HashSet(addClass_SuperClass), "menta/v0.2#addNegation")
  val addGoal_SuperClass = new AddClass(addClass_SuperClass, List(), HashSet(), "menta/v0.2#addGoal")

  // addName group // TODO: move to public scope
  val addGenotypeName = addName("genotype", 0)
  val addContainsName = addName("contains", 0)
  val addAddFieldName = addName("addField", 0)
  val addCustomerName = addName("Customer", 0)
  val addNumberName = addName("1", 1)
  val addSelfName = addName("Self", 0)
  val addOkName = addName("ok", 0)
  val addModuleVariableName = addName("Module", 2)
  val addNumberVariableName = addName("Number", 2)
  val addActionVariableName = addName("Action", 2)
  val addNotContainsName = addName("notContains", 0)
  val addRemoveFieldName = addName("removeField", 0)
  val addLinkedName = addName("linked", 0)
  val addNotLinkedName = addName("notLinked", 0)

  //  ** first rule
  // <(*, genotype, <(*, Customer, 1) --> addField>) --> contains>.
  // <<(*, genotype, <(*, #module, #number) --> addField>) --> contains> =|> <Self --> ok>>.
  // <<(*, genotype, <(*, #module, #number) --> removeField>) --> contains> =|> (--, <Self --> ok>)>.
  def createFirstRule: List[AddIndividual] = {
    // 1 string
    // <(*, Customer, 1) --> addField>
    val addCustomer_AddField_Property = addProperty(addCustomerName, addNumberName, addAddFieldName, "menta/v0.2/Property#Customer_Number_AddField")
    // <(*, genotype, <(*, Customer, 1) --> addField>) --> contains>.
    val addGenotype_Customer1AddField_Property = addProperty(addGenotypeName, addCustomer_AddField_Property, addContainsName, "menta/v0.2/Property#Genotype_C1A_Contains")

    // 2 and 3 string
    // <(*, #module, #number) --> addField>
    val addVar_AddField_Property = addProperty(addModuleVariableName, addNumberVariableName, addAddFieldName, "menta/v0.2/Property#Var_Var_AddField")
    // <(*, #module, #number) --> removeField>
    val addVar_RemoveField_Property = addProperty(addModuleVariableName, addNumberVariableName, addRemoveFieldName, "menta/v0.2/Property#Var_Var_RemoveField")

    // <<(*, genotype, <(*, #module, #number) --> addField>) --> contains>
    val addGenotype_VarVarAddField_Property = addProperty(addGenotypeName, addVar_AddField_Property, addContainsName, "menta/v0.2/Property#Genotype_VarVarAdd_Contains")
    // <(*, genotype, <(*, #module, #number) --> removeField>) --> notContains>
    val addGenotype_VarVarRemoveField_Property = addProperty(addGenotypeName, addVar_RemoveField_Property, addNotContainsName, "menta/v0.2/Property#Genotype_VarVarRemove_Contains")

    // <Self --> ok>
    val addSelfOk = new AddIndividual(addGoal_SuperClass, List(addSelfName, addOkName), "menta/v0.2/GoalGroup#SelfOk")
    // (--, <Self --> ok>)
    val addNotSelfOk = new AddIndividual(addNegation_SuperClass, List(addSelfOk), "menta/v0.2/Negation#NotSelfOk")

    // 2 string: <<(*, genotype, <(*, #module, #number) --> addField>) --> contains> =|> <Self --> ok>>.
    val addImplication1 = addImplication(addGenotype_VarVarAddField_Property, addSelfOk, "menta/v0.2/Implication#Impl1")
    // 3 string: <<(*, genotype, <(*, #module, #number) --> removeField>) --> contains> =|> (--, <Self --> ok>)>.
    val addImplication2 = addImplication(addGenotype_VarVarRemoveField_Property, addNotSelfOk, "menta/v0.2/Implication#Impl1")

    List(addGenotype_Customer1AddField_Property, addImplication1, addImplication2)
  }

  // all Modules must relate to the Customer
  // <(*, genotype, <(*, Customer, 1) --> addField>) --> contains>.
  // <(*, Customer, Customer) --> linked>.
  // <(&&, <(*, genotype, <(*, #module, #number) --> #action>) --> contains>, (--, <(*, Customer, #module) --> linked>)) =|> (--, <Self --> ok>)>.
  // <(&&, <(*, genotype, <(*, #module, #number) --> #action>) --> contains>, <(*, Customer, #module) --> linked>) =|> <Self --> ok>>.
  def createSecondRule: List[AddIndividual] = {
    // 1 string
    // <(*, Customer, 1) --> addField>
    val addCustomer_AddField_Property = addProperty(addCustomerName, addNumberName, addAddFieldName, "menta/v0.2/Property#Customer_Number_AddField")
    // <(*, genotype, <(*, Customer, 1) --> addField>) --> contains>.
    val addGenotype_Customer1AddField_Property = addProperty(addGenotypeName, addCustomer_AddField_Property, addContainsName, "menta/v0.2/Property#Genotype_C1A_Contains")

    // 2 string
    // <(*, Customer, Customer) --> linked>.
    val addCustomerCustomerLinked_Property = addProperty(addCustomerName, addCustomerName, addLinkedName, "menta/v0.2/Property#CustomerCustomerLinked")

    // 3 string
    //  <(&&, <(*, genotype, <(*, #module, #number) --> #action>) --> contains>, <(*, Customer, #module) --> linked>) =|> <Self --> ok>>.
    // <Self --> ok>
    val addSelfOk = new AddIndividual(addGoal_SuperClass, List(addSelfName, addOkName), "menta/v0.2/GoalGroup#SelfOk")
    // <<(*, genotype, <(*, #module, #number) --> #action>) --> contains>
    val addVar_Var_Property = addProperty(addModuleVariableName, addNumberVariableName, addActionVariableName, "menta/v0.2/Property#Var_Var_Var")
    val addGenotype_VarVarVar_Property = addProperty(addGenotypeName, addVar_Var_Property, addContainsName, "menta/v0.2/Property#Genotype_VarVarVar_Contains")
    // <(*, Customer, #module) --> linked>
    val addCustomer_Var_Linked_Property = addProperty(addCustomerName, addModuleVariableName, addLinkedName, "menta/v0.2/Property#Customer_Var_Linked")
    val addConjunction1 = addConjunction(addGenotype_VarVarVar_Property, addCustomer_Var_Linked_Property, "menta/v0.2/Conjunction#Conjunction1")
    val addImplication1 = addImplication(addConjunction1, addSelfOk, "menta/v0.2/Implication#Impl3")

    // 4 string
    //  <<(*, genotype, <(*, #module, #number) --> #action>) --> contains>
    // (--, <Self --> ok>)
    val addNotSelfOk = new AddIndividual(addNegation_SuperClass, List(addSelfOk), "menta/v0.2/Negation#NotSelfOk")
    // <(*, Customer, #module) --> notLinked>
    val addCustomer_Var_NotLinked_Property = addProperty(addCustomerName, addModuleVariableName, addNotLinkedName, "menta/v0.2/Property#Customer_Var_NotLinked")
    val addConjunction2 = addConjunction(addGenotype_VarVarVar_Property, addCustomer_Var_NotLinked_Property, "menta/v0.2/Conjunction#Conjunction2")
    val c = addImplication(addConjunction2, addNotSelfOk, "menta/v0.2/Implication#Impl4")
    
    List(addGenotype_Customer1AddField_Property, addCustomerCustomerLinked_Property, addImplication1, addImplication1 )
  }

  private def addProperty(subject: AddIndividual, subject_2: AddIndividual, _object: AddIndividual, URI: String): AddIndividual = {
    val property = new AddIndividual(addProperty_SuperClass, List(subject, subject_2, _object))
    property.uri_=(new URI(URI))
    return property
  }

  private def addConjunction(subject: AddIndividual, _object: AddIndividual, URI: String): AddIndividual = {
    val conjunction = new AddIndividual(addConjunction_SuperClass, List(subject, _object))
    conjunction.uri_=(new URI(URI))
    return conjunction
  }

  private def addImplication(subject: AddIndividual, _object: AddIndividual, URI: String): AddIndividual = {
    val implication = new AddIndividual(addImplication_SuperClass, List(subject, _object))
    implication.uri_=(new URI(URI))
    return implication
  }

  private def addName(name: String, literalTypeIndex: Int): AddIndividual = {
    var literalType = ""
    literalTypeIndex match {
      case 0 => literalType = "StringLiteral"
      case 1 => literalType = "NumberLiteral"
      case 2 => literalType = "Variable"
    }
    val literal = new StringLiteral()
    literal.setValue(name)
    new AddIndividual(addName_SuperClass, List(literal), "menta/v0.2/AddNameHelper#" + name)
  } */
}