package menta.util

import menta.translator.{AddLanguage, TranslationStrategyRule, AddTranslationHowTo, TranslationStrategy}
import menta.model.helpers.individual.AddConstantIndividualHelper
import java.net.URI
import menta.model.howto.{AddClass, AddIndividual, StringLiteral}
import menta.translator.wrappers.{TrApplyTemplates, TrVariable}


/**
 * @author talanov max
 * Date: 09.12.11
 * Time: 17:43
 */

object TestDataStrategyProvider {

  def createStrategy(): TranslationStrategy = {
    createCSharpStrategy()
  }

  //Service constructions
  val importPrefix = """
using System;
using System.Data;

using RetailDATACenter.DataAccess;
using RetailDATACenter.BusinessLogic;
using System.Collections;
using System.Transactions;
using RetailDATACenter.Globalization;
"""

  val namespacePrefix = """
namespace
  """

  val openBlock = "{"
  val closeBlock = "}"

  val classCommentBlock = """
    /// <summary>
    /// Facade provides access to the business object model services.
    /// </summary>
  """
  val classPrefix = "    public class"
  val classExtensionPostfix = " : PLUFacade"


  ///schema desc
  val schemaCreation = """
    <xs:schema
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:msdata="urn:schemas-microsoft-com:xml-msdata"
      xmlns:codegen="urn:schemas-microsoft-com:xml-msprop"
      xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"
      targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">
  """
  val schemaEnd = """
    </xs:schema>
  """

  def createCSharpStrategy(): TranslationStrategy = {
    val simplestTemplate =
      new AddTranslationHowTo(List(
        new StringLiteral(importPrefix),
        new StringLiteral(namespacePrefix),
        new TrVariable("$NameSpaceBusinessLogic"),
        new StringLiteral(openBlock),
        new StringLiteral(classCommentBlock),
        new StringLiteral(classPrefix),
        new TrVariable("./par/0/par/0"), // the first AddClass(AddName("Value"))
        new StringLiteral(classExtensionPostfix),
        new StringLiteral(openBlock),
        new StringLiteral(closeBlock),
        new StringLiteral("\n"),
        new StringLiteral(closeBlock)

      ))


    val simplestRule = new TranslationStrategyRule(new AddLanguage("C#"), "AddCSharpClass", simplestTemplate)
    val res = new TranslationStrategy(List(simplestRule))
    res
  }

  private def createCSharpConstantBlock(): AddIndividual = {
    val addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);
    (new AddConstantIndividualHelper()).apply("$NameSpaceBusinessLogic", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddFacadeName"))
  }

  def generateFacadeGenerationACRules()={
    val simplestTemplate =
          new AddTranslationHowTo(List(
            new StringLiteral(schemaCreation),
            new TrApplyTemplates(null, null),

            new StringLiteral(schemaEnd)

          ))


        val storeType = new AddTranslationHowTo(List(
          new StringLiteral(" <xs:include schemaLocation=\"Store.type.xsd\" />")));
        val tillType = new AddTranslationHowTo(List(
          new StringLiteral("<xs:include schemaLocation=\"LTill.type.xsd\" />")));



        val AddEmployeeDATASET = new AddTranslationHowTo(List(
          new StringLiteral(""" <xs:element name="Employee" msdata:Prefix="rdc" msdata:IsDataSet="true" msdata:CaseSensitive="false" msdata:EnforceConstraints="false">
            <!--
            ******************************************************************
                Employee DATASET
            ****************************************************************** -->
            <xs:complexType>
                <xs:choice maxOccurs="unbounded">"""),
          new TrApplyTemplates(null, null),
          new StringLiteral("""
                 </xs:choice>
                 </xs:complexType>
                 """)


        ))
        val empKeySearch = new AddTranslationHowTo(List(
          new StringLiteral("""<!--
                    **********************************************************
                        KEY SEARCH
                    ********************************************************** -->
                    <xs:element name="EmployeeKeySearch" msdata:Prefix="temp">
                        <xs:complexType>
                            <xs:sequence>
                                <xs:element minOccurs="1" name="EmployeeID" type="rdc:EmployeeIDType" />
                            </xs:sequence>
                        </xs:complexType>
                    </xs:element>""")


        ))
    List(new TranslationStrategyRule(new AddLanguage("C#"), "AddSchema", simplestTemplate),
      new TranslationStrategyRule(new AddLanguage("C#"), "AddStoreType", storeType),
      new TranslationStrategyRule(new AddLanguage("C#"), "AddTillType", tillType),
      new TranslationStrategyRule(new AddLanguage("C#"), "AddEmployeeDATASET", AddEmployeeDATASET),
      new TranslationStrategyRule(new AddLanguage("C#"), "EmployeeKeySearch", empKeySearch)

    )

  }

  def createFacadeGenerationACStrategy = {


    //val simplestRule = new TranslationStrategyRule(new AddLanguage("C#"), "AddSchema", simplestTemplate)
    val res = new TranslationStrategy(generateFacadeGenerationACRules())
    res
  }

  val xsdHeader = """<?xml version="1.0" encoding="utf-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msdata="urn:schemas-microsoft-com:xml-msdata" xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd" xmlns:codegen="urn:schemas-microsoft-com:xml-msprop" targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">

"""

  // createXSDStrategy()


  val createTable = """CREATE TABLE """
  val openColumnDefinitions = """ (
  """
  val closeColumnDefinitions = """)
  """
  val delimiterColumnDefinitions = ""","""

  def createTransactSQLStrategy(): TranslationStrategy = {

    val res = new TranslationStrategy(createSQLRules())
    res
  };


  def createXSDStrategy(): TranslationStrategy = {

    val res = new TranslationStrategy(createXSDSchemaRules())
    res
  }

  def createAddDACRules(): List[TranslationStrategyRule] = {
    val dacTemplate = new AddTranslationHowTo(List(
      new StringLiteral("""
<?xml version="1.0" encoding="utf-8"?>
<dac xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">

<"""),

      new TrVariable("$CurrentNode/AddDACName/"),
      new StringLiteral(""">
  <Read"""),
      new TrVariable("$CurrentNode/AddDACName/"),
      new StringLiteral("""Count>
        <operation>
          SELECT COUNT(*) FROM
        """),
      new TrVariable("$CurrentNode/AddDACCountOperationTableName/"),
      new StringLiteral("""
        <operation>
  </"""),
      new TrVariable("$CurrentNode/AddDACName/"),
      new StringLiteral(""">
</dac>""")
    )
    )
    val dacRule = new TranslationStrategyRule(new AddLanguage("DAC"), "AddDAC", dacTemplate)

    return List(dacRule);
  }

  def createAddFacadeCodeRules(): List[TranslationStrategyRule] = {
    val facadeTemplate = new AddTranslationHowTo(List(
      new StringLiteral("""
using System;
using System.Data;

using RetailDATACenter.DataAccess;
using RetailDATACenter.BusinessLogic;
using System.Collections;
using System.Transactions;
using RetailDATACenter.Globalization;

namespace
"""),

      new TrVariable("$CurrentNode/AddNamespace/"),
      new StringLiteral("""
{
  /// <summary>
  /// Menta generated facade
  /// </summary>
  public class
      """),
      new TrVariable("$CurrentNode/AddFacadeName/"),
      new StringLiteral(""":"""),
      new TrVariable("$CurrentNode/AddFacadeBaseType/"),
      new StringLiteral("""
      {

      }
}""")
    )
    )

    val facadeRule = new TranslationStrategyRule(new AddLanguage("CSharp"), "AddFacadeCode", facadeTemplate)

    return List(facadeRule);
  }

  def createSQLRules(): List[TranslationStrategyRule] = {

    val tableTemplate = new AddTranslationHowTo(List(
      new StringLiteral(createTable),
      new TrVariable("$CurrentNode/AddSQLTableName/"), // AddTable/AddName/"TableName"
      new StringLiteral(openColumnDefinitions),
      new TrApplyTemplates(),
      new StringLiteral(closeColumnDefinitions)
    )
    )
    val columnTemplate = new AddTranslationHowTo(List(
      new TrVariable("$CurrentNode/AddSQLFieldName/"), // $Column1Name
      new StringLiteral(" "), // $Column1Type
      new TrVariable("$CurrentNode/AddSQLFieldType/"), // $Column1Extensions
      new StringLiteral(delimiterColumnDefinitions)
    ))

    val columnRule = new TranslationStrategyRule(new AddLanguage("TSQL"), "AddSQLField", columnTemplate)
    val tableRule = new TranslationStrategyRule(new AddLanguage("TSQL"), "AddSQLTable", tableTemplate)

    return List(columnRule, tableRule);
  }

  def createXSDSchemaRules(): List[TranslationStrategyRule] = {
    val mainSchemaTemplate = new AddTranslationHowTo(List(
      new StringLiteral("""<?xml version="1.0" encoding="utf-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msdata="urn:schemas-microsoft-com:xml-msdata" xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd" xmlns:codegen="urn:schemas-microsoft-com:xml-msprop" targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">"""),
      new TrApplyTemplates(),
      new StringLiteral("""
</xs:schema>""")
    )
    )
    val simpleXsdType = new AddTranslationHowTo(List(
      new StringLiteral("""
    <xs:simpleType name=""""),
      new TrVariable("$CurrentNode/AddXSDFieldName/"), // AddXSDFieldName
      new StringLiteral("""" >
        <xs:restriction base=""""),
      new TrVariable("$CurrentNode/AddXSDFieldType/"), // AddXSDFieldType
      new StringLiteral("""">
        </xs:restriction>
    </xs:simpleType>""") // $Column1Extensions

    ))

    val columnRule = new TranslationStrategyRule(new AddLanguage("XSD"), "AddXSDField", simpleXsdType)
    val tableRule = new TranslationStrategyRule(new AddLanguage("XSD"), "AddXSDSchema", mainSchemaTemplate)
    return List(tableRule, columnRule);
  }

  def createStrategyForWholeSolution(): TranslationStrategy = {
    val facadeStrategy = new AddTranslationHowTo(List(
      new TrApplyTemplates()
    )
    )
    var rules = createXSDSchemaRules();
    rules = rules ::: createSQLRules();
    rules = rules ::: createAddFacadeCodeRules();
    rules = rules ::: createAddDACRules();
    rules=rules:::generateFacadeGenerationACRules();

    val facadeRule =    new TranslationStrategyRule(new AddLanguage("Facade"), "AddFacade", facadeStrategy)

    rules = rules ::: List(facadeRule);

    val res = new TranslationStrategy(rules)
    res
  }

  def getGlobalStrategy():TranslationStrategy={
    return createStrategyForWholeSolution();
  }
}
