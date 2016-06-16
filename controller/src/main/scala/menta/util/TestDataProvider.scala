package menta.util

import menta.knowledgebaseserver.KnowledgeBaseServerImpl

import java.net.URI
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper._
import menta.solutiongenerator.util.IndividualEncoder
import menta.model.helpers.individual.AddConstantIndividualHelper
import menta.model.howto.helper.{AddEmptyHowToIndividualHelper, AddParameterHelper, AddParameterIndividualHelper}
import menta.translator.wrappers.{TrApplyTemplates, TrVariable}
import menta.translator.{TranslationStrategy, AddLanguage, AddTranslationHowTo, TranslationStrategyRule}
import org.slf4j.{Logger, LoggerFactory}
import menta.MentaController
import org.menta.model.conversation._interface.ConstantBlock
import menta.model.howto._

/**
 * @author toschev alexander, talanov max
 * Date: 30.10.11
 * Time: 11:34
 */

/*
  provide data for solution generator
 */
object TestDataProviderGlobal {

   val log: Logger = LoggerFactory.getLogger(this.getClass)
  // val controller = new MentaController
  val kbServer = new KnowledgeBaseServerImpl

  /*
      generate solution to translate into
      <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msdata="urn:schemas-microsoft-com:xml-msdata"
      xmlns:codegen="urn:schemas-microsoft-com:xml-msprop"
      xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"
      targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">
        <xs:include schemaLocation="Employee.type.xsd" />
        <xs:include schemaLocation="Store.type.xsd" />
        <xs:include schemaLocation="LTill.type.xsd" />
        <xs:include schemaLocation="Basket.type.xsd" />
      </schema>
  */

  /*
    generate solution for this
  <xs:schema
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:msdata="urn:schemas-microsoft-com:xml-msdata"
      xmlns:codegen="urn:schemas-microsoft-com:xml-msprop"
      xmlns:rdc="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"
      targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">

    <xs:include schemaLocation="Employee.type.xsd" />
    <xs:include schemaLocation="Store.type.xsd" />
    <xs:include schemaLocation="LTill.type.xsd" />
    <xs:include schemaLocation="Basket.type.xsd" />
    <xs:element name="Employee" msdata:Prefix="rdc" msdata:IsDataSet="true" msdata:CaseSensitive="false" msdata:EnforceConstraints="false">
        <!--
        ******************************************************************
            Employee DATASET
        ****************************************************************** -->
        <xs:complexType>
            <xs:choice maxOccurs="unbounded">
                <xs:element name="Employee" type="rdc:EmployeeType" />
                <xs:element name="Language" type="rdc:LanguageType" />
                <xs:element name="DiscountGroup" type="rdc:DiscountGroupType" />
                <xs:element name="LeftRightOption" type="rdc:LeftRightType" />
                <xs:element name="LTill" type="rdc:LTillType" />
                <xs:element name="Store" type="rdc:StoreBaseType" />
                <xs:element name="PermissionGroup" type="rdc:PermissionGroupType" />
                <xs:element name="PermissionGroupsAvailable" type="rdc:PermissionGroupType" rdc:modid="20000" rdc:msgid="9" msdata:Prefix="temp" />
                <xs:element name="PermissionGroupStoreTree" type="rdc:PermissionGroupTreeType" rdc:modid="20000" rdc:msgid="9" msdata:Prefix="temp" />
                <!--
                **********************************************************
                    KEY SEARCH
                ********************************************************** -->
                <xs:element name="EmployeeKeySearch" msdata:Prefix="temp">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element minOccurs="1" name="EmployeeID" type="rdc:EmployeeIDType" />
                        </xs:sequence>
                    </xs:complexType>
                </xs:element>
                <xs:element name="EmployeeKeySearchWithStoreID" msdata:Prefix="temp">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element minOccurs="1" name="EmployeeID" type="rdc:EmployeeIDType" />
                            <xs:element minOccurs="1" name="StoreID" type="rdc:StoreIdType" />
                        </xs:sequence>
                    </xs:complexType>
                </xs:element>

             </xs:choice>
             </xs:complexType>
             </xs:schema>

   */

  /*
  HowTo representation:
  AddSchema(targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"){
    AddEmployeeDATASET(???){
      AddEmployeeKeySearch()
    }
  }
  */

  private var theParameterHelper = new AddParameterHelper();

  @deprecated
  def createAddSchemaSolution(): HowTo = {

    val addEmployeeKeySearch = new AddClass
    addEmployeeKeySearch.name = "AddEmployeeKeySearch"
    addEmployeeKeySearch.isRoot = true
    addEmployeeKeySearch.uri = new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeKeySearch")
    kbServer.save(addEmployeeKeySearch)

    val addEmployeeDATASET = new AddClass
    addEmployeeDATASET.uri = new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET")
    addEmployeeDATASET.name = "AddEmployeeDATASET";
    addEmployeeDATASET.isRoot = true
    addEmployeeDATASET.parameters = List(theParameterHelper("addEmployeeKeySearch", addEmployeeKeySearch, 1))
    kbServer.save(addEmployeeDATASET)

    val addSchema = new AddClass
    addSchema.uri = new URI(menta.model.Constant.modelNamespaceString + "AddSchema")
    addSchema.name = "AddSchema";
    addSchema.isRoot = true
    addSchema.parameters = List(theParameterHelper("targetNamespace", new menta.model.howto.StringLiteral(), 1), theParameterHelper("addEmployeeDATASET", addEmployeeDATASET, 1))
    kbServer.save(addSchema)

    addSchema

  }

  /*
   HowTo representation:
   AddSchema(targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"){
    AddEmployeeDATASET{
      AddTillType()
      AddStoreType()
    }
   }
  */

  def createConstantBlock(): AddIndividual = {
    //tillTypeXsd
    val addName = new AddClass()
    addName.name = "AddNameConstant"
    addName.uri = new URI(menta.model.Constant.modelNamespaceString + "AddNameConstant");

    val res = List((new AddConstantIndividualHelper()).apply("till.xsd", addName, new URI(menta.model.Constant.modelNamespaceString + "AddStoreType")),
      (new AddConstantIndividualHelper()).apply("store.xsd", addName, new URI(menta.model.Constant.modelNamespaceString + "AddTillType")),
      (new AddConstantIndividualHelper()).apply("employee", addName, new URI(menta.model.Constant.modelNamespaceString + "AddSchema")),
      (new AddConstantIndividualHelper()).apply("employee", addName, new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET"))
    );
    (new AddConstantBlockIndividualWrapper(res, null)).addIndividual()
  }


  /*
   AddFacadeUsing, AddFacadeName, AddFacadeBaseType, AddNamespace, AddXSDField, addXSDSchemaName, AddSQLTableName, AddSQLField, AddDataSetElement Where compound is: AddXSDField,  AddSQLField, AddDataSetElement
  */

  /**
   * PLU complete complex solution constants, generator method.
   */
  def createConstantPLUBlock(): AddIndividual = {
    //PLUConstants
    val res = List[AddIndividual](createAddFacadeName(), createAddFacadeBaseType(), createAddAddNamespace(),
      createAddSQLTableName(), createConstantAddXSDField(), createConstantAddSQLField(), createConstantAddDataSetElement());
    (new AddConstantBlockIndividualWrapper(res, null)).addIndividual()
  }


  def createAddFacadeName(): AddIndividual = createConstant("AddFacadeName", "TillFacade")

  def createAddFacadeBaseType(): AddIndividual = createConstant("AddFacadeBaseType", "FacadeType")

  def createAddAddNamespace(): AddIndividual = createConstant("AddNamespace", "http://invia.fujitsu.com/RetailDATACenter/rdc.xsd")

  def createAddSQLTableName(): AddIndividual = createConstant("AddSQLTableName", "PLUTable")

  private def createConstant(name: String, value: String): AddIndividual = {
    val addName = new AddClass()
    addName.name = menta.translator.constant.Constant.constantAddField
    addName.uri = new URI(menta.model.Constant.modelNamespaceString + addName.name);
    (new AddConstantIndividualHelper()).apply(value, addName, new URI(menta.model.Constant.modelNamespaceString + name))
  }

  def createConstantBlockForEmpDataSet(): AddIndividual = {

    var controller = new MentaController

    var block = createConstantBlockForEmpDataSetClient();



    controller.convertConstantBlock(List(block)).asInstanceOf[AddIndividual]
    //val constantAddFacade = createConstantAddFacade()
    //(new AddConstantBlockIndividualWrapper(List[HowTo](constantAddFacade), null)).addIndividual()
  }

  def createConstantBlockForEmpDataSetClient(): ConstantBlock = {



    var block = new ConstantBlock
    block.setTargetActionClassName("AddEmployeeDataSetFileName")
    block.setTargetValue("employee.type.xsd")
    return block;


  }


  def crateConstantBlock(): AddIndividual = {

    var controller = new MentaController

    var block = new ConstantBlock
    block.setTargetActionClassName("AddCSharpClass")
    block.setTargetValue("MyClass")



    controller.convertConstantBlock(List(block)).asInstanceOf[AddIndividual]
    //val constantAddFacade = createConstantAddFacade()
    //(new AddConstantBlockIndividualWrapper(List[HowTo](constantAddFacade), null)).addIndividual()
  }
  def crateConstantBlockForWholeSolutionClnt():List[ConstantBlock]={
         var facadeBlock = new ConstantBlock
    facadeBlock.setTargetActionClassName("AddFacadeName")
    facadeBlock.setTargetValue("PLUExtended")

    var sqlBlock = new ConstantBlock
    sqlBlock.setTargetActionClassName("AddSQLField")
    var field1Type = new ConstantBlock
    field1Type.setTargetActionClassName("AddSQLFieldType")
    field1Type.setTargetValue("int")

    var field1Name = new ConstantBlock
    field1Name.setTargetActionClassName("AddSQLFieldName")
    field1Name.setTargetValue("ObjectID")

    var field1Holder = new ConstantBlock
    field1Holder.getChildren.add(field1Name)
    field1Holder.getChildren.add(field1Type)

    var field2Holder = new ConstantBlock

    var field2Type = new ConstantBlock
    field2Type.setTargetActionClassName("AddSQLFieldType")
    field2Type.setTargetValue("decimal(0,0)")

    var field2Name = new ConstantBlock
    field2Name.setTargetActionClassName("AddSQLFieldName")
    field2Name.setTargetValue("Price")

    field2Holder.getChildren.add(field2Name)
    field2Holder.getChildren.add(field2Type)

    sqlBlock.getChildren.add(field1Holder)
    sqlBlock.getChildren.add(field2Holder)

    ////////////////////////////////////////
    ////XSD block

    var xsdSchemaBlock = new ConstantBlock()
    xsdSchemaBlock.setTargetActionClassName("AddXSDField")

    ///first field
    var xsdField1 = new ConstantBlock();

    var xsdField1Type = new ConstantBlock;
    xsdField1Type.setTargetActionClassName("AddXSDFieldType");
    xsdField1Type.setTargetValue("xs:string");

    var xsdField1Name = new ConstantBlock;
    xsdField1Name.setTargetActionClassName("AddXSDFieldName");
    xsdField1Name.setTargetValue("Name");

    xsdField1.getChildren.add(xsdField1Type)
    xsdField1.getChildren.add(xsdField1Name)

    xsdSchemaBlock.getChildren.add(xsdField1)
    //second field
    var xsdField2 = new ConstantBlock();

    var xsdField2Type = new ConstantBlock;
    xsdField2Type.setTargetActionClassName("AddXSDFieldType");
    xsdField2Type.setTargetValue("xs:string");

    var xsdField2Name = new ConstantBlock;
    xsdField2Name.setTargetActionClassName("AddXSDFieldName");
    xsdField2Name.setTargetValue("Description");

    xsdField2.getChildren.add(xsdField2Type)
    xsdField2.getChildren.add(xsdField2Name)

    xsdSchemaBlock.getChildren.add(xsdField2)


    //SQL Table
    var sqlTableName = new ConstantBlock
    sqlTableName.setTargetActionClassName("AddSQLTableName");
    sqlTableName.setTargetValue("PLUTable")

    //facadeBaseType
    var facadeBaseType = new ConstantBlock
    facadeBaseType.setTargetActionClassName("AddFacadeBaseType");
    facadeBaseType.setTargetValue("PLUFacade")

    //facade namespace
    var facadeNamespace = new ConstantBlock
    facadeNamespace.setTargetActionClassName("AddNamespace");
    facadeNamespace.setTargetValue("ICCO.BusinessLogic");

    //add dac
    var dacTable = new ConstantBlock
    dacTable.setTargetActionClassName("AddDACCountOperationTableName");
    dacTable.setTargetValue("PLUTable");

    //dacname
    var dacName = new ConstantBlock
    dacName.setTargetActionClassName("AddDACName");
    dacName.setTargetValue("PLU");

    //fileNames
    var dacFileName = new ConstantBlock
    dacFileName.setTargetActionClassName("AddDACFileName");
    dacFileName.setTargetValue("plu.dac.xml");

    //schema file name
    var schemaFileName = new ConstantBlock
    schemaFileName.setTargetActionClassName("addXSDSchemaFileName");
    schemaFileName.setTargetValue("plu.type.xsd");

    //facade file name
    var facadeFileName = new ConstantBlock
    facadeFileName.setTargetActionClassName("facadeFileName");
    facadeFileName.setTargetValue("plufacade.cs");

    //sql file name
    var sqlFileName = new ConstantBlock
    sqlFileName.setTargetActionClassName("AddSQLFileName");
    sqlFileName.setTargetValue("plu.sql");

    var block = List(facadeBlock, sqlBlock, xsdSchemaBlock, sqlTableName,
      facadeBaseType, facadeNamespace, dacTable, dacName, dacFileName,
      schemaFileName, facadeFileName, sqlFileName);
    return block
  }
  def crateConstantBlockForWholeSolution(): AddIndividual = {

    var controller = new MentaController

    var block = crateConstantBlockForWholeSolutionClnt();





    controller.convertConstantBlock(block).asInstanceOf[AddIndividual]
    //val constantAddFacade = createConstantAddFacade()
    //(new AddConstantBlockIndividualWrapper(List[HowTo](constantAddFacade), null)).addIndividual()
  }


  def createConstantAddFacade(): AddIndividual = {
    //PLUTypeXSD
    val name = "AddXSDField"
    val addField = new AddClass()
    addField.name = menta.translator.constant.Constant.constantAddField
    addField.uri = new URI(menta.model.Constant.modelNamespaceString + addField.name)
    val addBlock = createConstantAddFacadeBlock()
    (new AddConstantIndividualHelper()).apply(addBlock, addField, new URI(menta.model.Constant.modelNamespaceString + name))

  }

  def createConstantAddFacadeBlock(): AddIndividual = {
    val addFirstBlock = generateConstantNameTypeBlock("PLUIDType", "string")
    val addSecondBlock = generateConstantNameTypeBlock("ItemIDType", "string")
    val addThirdBlock = generateConstantNameTypeBlock("ScanCodeType", "string")
    val addForthBlock = generateConstantNameTypeBlock("ItemDescriptionType", "string")
    val addFifthBlock = generateConstantNameTypeBlock("PriceType", "decimal")
    (new AddConstantBlockIndividualWrapper(List[HowTo](addFirstBlock, addSecondBlock, addThirdBlock, addForthBlock, addFifthBlock), null)).addIndividual()
  }


  def createConstantForAddFacadeCode(): AddIndividual = {
    val addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    val first = List(
      (new AddConstantIndividualHelper()).apply("ICCOPLUFacade", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddFacadeName")),
      (new AddConstantIndividualHelper()).apply("ICCO.BusinessLogic", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddNamespace")),
      (new AddConstantIndividualHelper()).apply("PLUFacade", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddFacadeBaseType"))

    )
    (new AddConstantBlockIndividualWrapper(first, null)).addIndividual()
  }

  def createConstantForAddDAC(): AddIndividual = {
    val addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    val first = List(
      (new AddConstantIndividualHelper()).apply("PLU", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddDACName")),
      (new AddConstantIndividualHelper()).apply("PLU", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddDACCountOperationTableName"))
    )
    (new AddConstantBlockIndividualWrapper(first, null)).addIndividual()
  }

  def createConstantForAddSQLTable(): AddIndividual = {
    val addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    val first = List(
      (new AddConstantIndividualHelper()).apply("PLU", addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddSQLTableName"))
    )
    (new AddConstantBlockIndividualWrapper(first, null)).addIndividual()
  }

  def createConstantAddXSDField(): AddIndividual = {
    //PLUTypeXSD
    val addField = new AddClass()
    addField.name = "AddXSDField"
    addField.uri = new URI(menta.model.Constant.modelNamespaceString + addField.name);

    val addFirstBlock = generateConstantNameTypeBlock("PLUIDType", "string")
    val addSecondBlock = generateConstantNameTypeBlock("ItemIDType", "string")
    val addThirdBlock = generateConstantNameTypeBlock("ScanCodeType", "string")
    val addForthBlock = generateConstantNameTypeBlock("ItemDescriptionType", "string")
    val addFifthBlock = generateConstantNameTypeBlock("PriceType", "decimal")
    (new AddConstantBlockIndividualWrapper(List[HowTo](addFirstBlock, addSecondBlock, addThirdBlock, addForthBlock, addFifthBlock), null)).addIndividual()
  }

  /*
  PLUIDType nvarchar(MAX),
	ItemIDType nvarchar(MAX),
	ScanCodeType nvarchar(MAX),
	ItemDescriptionType nvarchar(MAX),
	PriceType decimal(3,3)
   */

  def createConstantAddSQLField(): AddIndividual = {
    //PLUTypeSQL
    val addField = new AddClass()
    addField.name = "AddSQLFieldConstant"
    addField.uri = new URI(menta.model.Constant.modelNamespaceString + addField.name);

    val addFirstBlock = generateConstantNameTypeBlock("PLUIDType", "nvarchar(MAX)")
    val addSecondBlock = generateConstantNameTypeBlock("ItemIDType", "nvarchar(MAX)")
    val addThirdBlock = generateConstantNameTypeBlock("ScanCodeType", "nvarchar(MAX)")
    val addForthBlock = generateConstantNameTypeBlock("ItemDescriptionType", "nvarchar(MAX)")
    val addFifthBlock = generateConstantNameTypeBlock("PriceType", "decimal(3,3)")
    (new AddConstantBlockIndividualWrapper(List[HowTo](addFirstBlock, addSecondBlock, addThirdBlock, addForthBlock, addFifthBlock), null)).addIndividual()
  }

  /*
  ColorPK
  SizePK
  FitPK
  SupplierPK
  TicketTypePK
   */

  def createConstantAddDataSetElement(): AddIndividual = {
    //PLUTypeSQL
    val addField = new AddClass()
    addField.name = "AddDataSetElement"
    addField.uri = new URI(menta.model.Constant.modelNamespaceString + addField.name);

    val addFirstBlock = generateConstantNameTypeBlock("ColorPK", "PLUID")
    val addSecondBlock = generateConstantNameTypeBlock("SizePK", "PLUID")
    val addThirdBlock = generateConstantNameTypeBlock("FitPK", "PLUID")
    val addForthBlock = generateConstantNameTypeBlock("SupplierPK", "PLUID")
    val addFifthBlock = generateConstantNameTypeBlock("TicketTypePK", "PLUID")
    (new AddConstantBlockIndividualWrapper(List[HowTo](addFirstBlock, addSecondBlock, addThirdBlock, addForthBlock, addFifthBlock), null)).addIndividual()
  }


  private def generateConstantNameTypeBlock(blockName: String, blockType: String): AddIndividual = {
    val addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    val first = List(
      (new AddConstantIndividualHelper()).apply(blockName, addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddName")),
      (new AddConstantIndividualHelper()).apply(blockType, addBlock, new URI(menta.model.Constant.modelNamespaceString + "AddType"))
    )
    (new AddConstantBlockIndividualWrapper(first, null)).addIndividual()
  }

  private def generateAddClass(nm: String, prms: Map[String, HowTo]): AddClass = {

    //check if class already exist in database and load it
    //var res = kbServer.selectActionClass(nm);
    //if (res != null) return res.asInstanceOf[AddClass];
    val addClass = new AddClass
    addClass.name = nm
    addClass.isRoot = true

    if (prms != null) {
      addClass.parameters = prms.map(b => b._2).toList
    }
    addClass.uri = new URI(menta.model.Constant.modelNamespaceString + nm)


    kbServer.save(addClass)
    addClass
  }

  private def generateAddIndividual(actionClass: AddClass, prms: List[HowTo]): ActionIndividual = {
    var res = IndividualEncoder.createIndividual(actionClass);
    if (prms != null) {
      res.parameters = prms;
    }
    return res;
  }

  private def generateAddIndividualList(in: List[AddClass]): List[ActionIndividual] = {
    return in.map(b => generateAddIndividual(b, null))
  }

  def generateSimplestSolution(): List[HowTo] = {


    val facadeName = generateAddClass("AddFacadeName", null);
    val facadeNameInd = generateAddIndividual(facadeName, null);

    val root = generateAddClass("AddFacade", Map("facadeName" -> facadeName));
    val rootInd = generateAddIndividual(root, List(facadeNameInd));

    List[HowTo](rootInd)
  }

  /**
   * Generates AddCSharpClass(AddName("MyClass"))
   * @returns List[HowTo]
   */
  def generateCSharpSolution(): List[HowTo] = {
    val cAddNameClass = generateAddClass("AddName", null);
    val cAddNameInd = generateAddIndividual(cAddNameClass, List[HowTo](new StringLiteral("MyClass")));
    val cSharpClass = generateAddClass("AddCSharpClass", null);
    val cSharpClassInd: ActionIndividual = generateAddIndividual(cSharpClass, null); //List[HowTo](cAddNameInd));
    List[HowTo](cSharpClassInd)
  }

  /**
   * Generates AddTable(AddName("MyTable"), AddColumn(AddName("FirstCol"), AddType("Type"), Add(""))
   * @returns List[HowTo]
   */
  def generateTSQLSolution(): List[HowTo] = {
    val sqlFiledNameClass = generateAddClass("AddName", null)
    val sqlFieldNameInd = generateAddIndividual(sqlFiledNameClass, List[HowTo](new StringLiteral("MyField")))
    val sqlTableAddNameClass = generateAddClass("AddName", null)
    val sqlTableAddNameInd = generateAddIndividual(sqlTableAddNameClass, List[HowTo](new StringLiteral("MyTable")))
    val sqlTableClass = generateAddClass("AddTableClass", null)
    val sqlTableClassInd: ActionIndividual = generateAddIndividual(sqlTableClass, List[HowTo](sqlTableAddNameInd, sqlFieldNameInd))
    List[HowTo](sqlTableClassInd)
  }

  def generateUIGeneratedSolution(simple: Boolean): List[HowTo] = {

    //ADD XSD Fields
    val addXSDFieldType = generateAddClass("AddXSDFieldType", null)
    val addXSDFieldName = generateAddClass("AddXSDFieldName", null)
    //val addXSDBaseFieldType = generateAddClass("AddXSDBaseFieldType", null)
    val xsdFieldOperations = generateAddIndividualList(List(addXSDFieldType, addXSDFieldName));

    //add XSD Field Operation
    val addFieldName = generateAddClass("AddXSDField", Map("addXSDFieldType" -> addXSDFieldType, "addXSDFieldName" -> addXSDFieldName));
    val addXSDFieldIndividual = generateAddIndividual(addFieldName, xsdFieldOperations);

    //ADD SQL Fields
    val addSQLFieldType = generateAddClass("AddSQLFieldType", null)
    val addSQLFieldName = generateAddClass("AddSQLFieldName", null); // generateAddClass("AddSQLFieldName", null)
    val addSQLNOTNullField = generateAddClass("AddSQLNOTNull", null)

    val sqlFieldOperations = generateAddIndividualList(List(addSQLFieldType, addSQLFieldName, addSQLNOTNullField));

    //Add SQL Field Operation

    val addSQLField = generateAddClass("AddSQLField", Map("addSQLFieldType" -> addSQLFieldType, "addSQLFieldName" -> addSQLFieldName, "addSQLNOTNullField" -> addSQLNOTNullField));
    val addSQLFieldIndividual = generateAddIndividual(addSQLField, sqlFieldOperations);

    //Add XSD Schema
    //XSD fileName
    val addXSDSchemaFileName = generateFileNameHowTo("addXSDSchemaFileName");
    val addXSDSchemaFileNameInd = generateAddIndividual(addXSDSchemaFileName, null);

    val addXSDSchemaName = generateAddClass("addXSDSchemaName", null);
    val addXSDSchemaNameInd = generateAddIndividual(addXSDSchemaName, null);
    val addXSDSchema = generateAddClass("AddXSDSchema", Map("addField" -> addFieldName, "name" -> addXSDSchemaName, "fileName" -> addXSDSchemaFileName));
    val addXSDSchemaInd = generateAddIndividual(addXSDSchema, List(addXSDFieldIndividual, addXSDSchemaNameInd, addXSDSchemaFileNameInd));

    //ADD SQL Table
    val addSQLFileName = generateFileNameHowTo("AddSQLFileName");
    val addSQLFileNameInd = generateAddIndividual(addSQLFileName, null);

    val addSQLTableName = generateAddClass("AddSQLTableName", null);
    val addSQLTableNameInd = generateAddIndividual(addSQLTableName, null);
    val addSQLTable = generateAddClass("AddSQLTable", Map("addColumn" -> addSQLField, "name" -> addSQLTableName, "fileName" -> addSQLFileName));
    val addSQLTableInd = generateAddIndividual(addSQLTable, List(addSQLFieldIndividual, addSQLTableNameInd, addSQLFileNameInd));

    //facade name
    val facadeName = generateAddClass("AddFacadeName", null);
    val facadeNameInd = generateAddIndividual(facadeName, null);

    //facade base type
    val facadeBaseType = generateAddClass("AddFacadeBaseType", null);
    val facadeBaseTypeInd = generateAddIndividual(facadeBaseType, null);

    //facade namespace
    val facadeNamespace = generateAddClass("AddNamespace", null);
    val facadeNamespaceInd = generateAddIndividual(facadeNamespace, null);

    //facade using
    val facadeUsing = generateAddClass("AddFacadeUsing", null);
    val facadeUsingInd = generateAddIndividual(facadeUsing, null);

    //facade fileName
    val facadeFileName = generateFileNameHowTo("facadeFileName"); //("facadeFileName", null);
    val facadeFileNameInd = generateAddIndividual(facadeFileName, null);


    //facadeCode
    val addFacadeCode = generateAddClass("AddFacadeCode", Map("name" -> facadeName, "addFacadeBaseType" -> facadeBaseType, "facadeNamespace" -> facadeNamespace, "facadeUsing" -> facadeUsing, "fileName" -> facadeFileName));
    val addFacadeCodeInd = generateAddIndividual(addFacadeCode, List(facadeBaseTypeInd, facadeNamespaceInd, facadeUsingInd, facadeNameInd, facadeFileNameInd));

    //DAC table
    val addDACOperationTableName = generateAddClass("AddDACCountOperationTableName", null)
    val addDACOperationTableNameInd = generateAddIndividual(addDACOperationTableName, null);

    //read dac count
    val addDACFileName = generateFileNameHowTo("AddDACFileName")
    val addDACFileNameInd = generateAddIndividual(addDACFileName, null);


    val addDACOperation = generateAddClass("AddDACCountOperation", Map("tableName" -> addDACOperationTableName))
    val addDACOperationInd = generateAddIndividual(addDACOperation, List(addDACOperationTableNameInd));


    //dac name
    val addDACName = generateAddClass("AddDACName", null);
    val addDACNameInd = generateAddIndividual(addDACName, null);

    //add DAC
    var addDAC = generateAddClass("AddDAC", Map("addDACCount" -> addDACOperationTableName, "name" -> addDACName, "fileName" -> addDACFileName));
    var addDACInd = generateAddIndividual(addDAC, List(addDACOperationTableNameInd, addDACNameInd, addDACFileNameInd));
    //if (simple) {

    // addDACInd = generateAddIndividual(addDAC, List((new AddEmptyHowToIndividualHelper).apply(), addDACNameInd));

    //}

    //add dataset HowTo plu.ds.xsd   for non-simple solution
    val addDataSetElementKeySelector = generateAddClass("addDataSetElementKeySelector", null);
    val addDataSetElementKeyField = generateAddClass("addDataSetElementKeyField", null);
    val addDataSetElementKey = generateAddClass("addDataSetElementKey", Map("dataSetElementKeySelector" -> addDataSetElementKeySelector, "dataSetElementKeyField" -> addDataSetElementKeyField));
    val addDataSetElementName = generateAddClass("AddDataSetElementName", null);
    val addDataSetElement = generateAddClass("AddDataSetElement", Map("dataSetElementKey" -> addDataSetElementKey, "dataSetElementName" -> addDataSetElementName));
    val addSupplierSchema = generateAddClass("AddSupplierSchema", null);
    val addDataSetPLU = generateAddClass("AddDataSetPLU", Map("addSupplierSchema" -> addSupplierSchema, "addSchemaElement" -> addDataSetElement));

    //add individual for non-simple solution
    val addDataSetElementKeyInd = generateAddIndividual(addDataSetElementKey, generateAddIndividualList(List(addDataSetElementKeySelector, addDataSetElementKeyField)));
    val addDataSetElementNameInd = generateAddIndividual(addDataSetElementName, null);
    val addSupplierSchemaInd = generateAddIndividual(addSupplierSchema, null);
    val addDataSetElementInd = generateAddIndividual(addDataSetElement, List(addDataSetElementKeyInd, addDataSetElementNameInd))
    val addDataSetPLUInd = generateAddIndividual(addDataSetPLU, List(addSupplierSchemaInd, addDataSetElementInd))

    //add FACADE


    val addFacade = generateAddClass("AddFacade", Map("addXSDSchema" -> addXSDSchema, "AddSQLTable" -> addSQLTable, "AddFacadeCode" -> addFacadeCode, "AddDAC" -> addDAC)); //, "AddDataSet" -> addDataSetPLU));
    val addFacadeInd = generateAddIndividual(addFacade, List(addXSDSchemaInd, addFacadeCodeInd, addDACInd, addSQLTableInd));
    // if (simple) {
    //   addFacadeInd.parameters = addFacadeInd.parameters ::: List((new AddEmptyHowToIndividualHelper).apply())
    // }
    // else {
    //   addFacadeInd.parameters = addFacadeInd.parameters ::: List(addDataSetPLUInd)
    // }


    List(addFacadeInd, (new AddBlockIndividualHelper()).apply(List(addFacadeInd), null))
  }

  def generateUIGeneratedSolutionAC(simple: Boolean): AddIndividual = {

    var listBlock = List[AddIndividual]();
    //AddFacade
    //make a cycle around all elements
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution/0/prms/", null, null, null) // the reference to the Solution in the context
    val $a = new AddVariableIndividualWrapper("$a", null, null, null)

    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$a/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddFacade", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
    var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    listBlock = listBlock ::: List(loop.addIndividual());

    //add all fields
    //top level AddXSDSchema,AddFacadeCode,AddDAC,AddSQLTable ,AddDataSetPLU
    val rootPrefix = "$Solution/0/prms/0/prms/";

    def rootElement(index: Int): String = {
      return rootPrefix + index + "/actionClass/0/name/";
    }

    def selectedActionClass(prefix: String, index: Int): String = {
      return prefix + index + "/actionClass/0/name/";
    }

    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddXSDSchema", rootElement(0)),
      createAcceptanceCriteriaForEqual("AddFacadeCode", rootElement(1)),
      createAcceptanceCriteriaForEqual("AddSQLTable", rootElement(3)),
      createAcceptanceCriteriaForEqual("AddDAC", rootElement(2)),
      createAcceptanceCriteriaForEqual("AddDataSetPLU", rootElement(4)));

    //AddXSDSchema
    val xsdSchemaPrefix = rootPrefix + "0/prms/";
    val xsdXSDField = xsdSchemaPrefix + "0/prms/";

    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddXSDField", selectedActionClass(xsdSchemaPrefix, 0)),
      createAcceptanceCriteriaForEqual("AddXSDFieldType", selectedActionClass(xsdXSDField, 0)),
      createAcceptanceCriteriaForEqual("AddXSDFieldName", selectedActionClass(xsdXSDField, 1)),
      createAcceptanceCriteriaForEqual("addXSDSchemaName", selectedActionClass(xsdSchemaPrefix, 1)));

    //AddFacadeCode
    val facadePrefix = rootPrefix + "1/prms/";

    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddFacadeBaseType", selectedActionClass(facadePrefix, 0)),
      createAcceptanceCriteriaForEqual("AddNamespace", selectedActionClass(facadePrefix, 1)),
      createAcceptanceCriteriaForEqual("AddFacadeUsing", selectedActionClass(facadePrefix, 2)),
      createAcceptanceCriteriaForEqual("AddFacadeName", selectedActionClass(facadePrefix, 3)));

    //Add DAC
    val dacPrefix = rootPrefix + "2/prms/";
    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddDACCountOperation", selectedActionClass(dacPrefix, 0))
    );

    //AddSQLTable
    val sqlTable = rootPrefix + "3/prms/";
    val sqlTableField = sqlTable + "0/prms/";

    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddSQLField", selectedActionClass(sqlTable, 0)),
      createAcceptanceCriteriaForEqual("AddSQLFieldType", selectedActionClass(sqlTableField, 0)),
      createAcceptanceCriteriaForEqual("AddSQLFieldName", selectedActionClass(sqlTableField, 1)),
      createAcceptanceCriteriaForEqual("AddSQLNOTNull", selectedActionClass(sqlTableField, 2)));

    //AddDataSetPLU
    /*val dataSetPLU = rootPrefix + "4/prms/";
    val dataSetElement = dataSetPLU + "1/prms/";
    val dataSetElementKey = dataSetElement + "0/prms/";

    listBlock = listBlock ::: List(createAcceptanceCriteriaForEqual("AddSupplierSchema", selectedActionClass(dataSetPLU, 0)),
      createAcceptanceCriteriaForEqual("AddDataSetElement", selectedActionClass(dataSetPLU, 1)),
      createAcceptanceCriteriaForEqual("addDataSetElementKey", selectedActionClass(dataSetElement, 0)),
      createAcceptanceCriteriaForEqual("AddDataSetElementName", selectedActionClass(dataSetElement, 1)),
      createAcceptanceCriteriaForEqual("addDataSetElementKeySelector", selectedActionClass(dataSetElementKey, 0)),
      createAcceptanceCriteriaForEqual("addDataSetElementKeyField", selectedActionClass(dataSetElementKey, 1))
    );  */
    var ac = (new AddBlockIndividualHelper().apply(listBlock, null));
    ac.uri = kbServer.save(ac)

    return ac;
  }


  def createAcceptanceCriteriaForEqual(addClassName: String, path: String) = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper(path, null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, addClassName, null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0
  }

  def generateFileNameHowTo(nm: String): AddClass = {
    var superClass = generateAddClass("AddFileName", null);
    var res = new AddClass();
    res.superClass = superClass.asInstanceOf[ActionClass];
    res.name = nm;
    res.isRoot = true;
    res.uri=new URI(menta.model.Constant.modelNamespaceString + nm)
    return kbServer.selectOrCreateActionClass(res).asInstanceOf[AddClass];

  }

  //use this for simple gen
  //@deprecated
  def createAddSchemaDataSetTypesSolution(): HowTo = {

    val addTillType = new AddClass
    addTillType.name = "AddTillType"
    addTillType.isRoot = true
    addTillType.uri = new URI(menta.model.Constant.modelNamespaceString + "AddTillType")
    kbServer.save(addTillType)

    val addTillInd = IndividualEncoder.createIndividual(addTillType)

    val addStoreType = new AddClass
    addStoreType.name = "AddStoreType"
    addStoreType.isRoot = true
    addStoreType.uri = new URI(menta.model.Constant.modelNamespaceString + "AddStoreType")
    kbServer.save(addStoreType)

    var addStorInd = IndividualEncoder.createIndividual(addStoreType)

    val empKeySearch = generateAddClass("EmployeeKeySearch", null);
    val empKeySearchInd = generateAddIndividual(empKeySearch, null);

    val addEmployeeDATASET = new AddClass
    addEmployeeDATASET.uri = new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET")
    addEmployeeDATASET.name = "AddEmployeeDATASET";
    addEmployeeDATASET.isRoot = true
    addEmployeeDATASET.parameters = List(theParameterHelper("addemp", empKeySearch, 1));
    kbServer.save(addEmployeeDATASET)


    val schemaFileName = generateFileNameHowTo("AddEmployeeDataSetFileName");
    val schemaFileNameInd = IndividualEncoder.createIndividual(schemaFileName);

    var addEmplInd = IndividualEncoder.createIndividual(addEmployeeDATASET)
    addEmplInd.parameters = List(empKeySearchInd)

    val addSchema = new AddClass
    addSchema.uri = new URI(menta.model.Constant.modelNamespaceString + "AddSchema")
    addSchema.name = "AddSchema";
    addSchema.isRoot = true
    addSchema.parameters = List(theParameterHelper("addSchema1", addStoreType, 1), theParameterHelper("addSchema2", addTillType, 1), theParameterHelper("facade", addEmployeeDATASET, 1), theParameterHelper("fileName", schemaFileName, 1))
    kbServer.save(addSchema)

    var addSchemaInd = IndividualEncoder.createIndividual(addSchema)
    addSchemaInd.parameters = List(addStorInd, addTillInd, addEmplInd, schemaFileNameInd);

    val solutionHolder = generateAddClass("AddSchemaHolder", Map("addSchema" -> addSchema));
    val solutionHolderInd = IndividualEncoder.createIndividual(solutionHolder)

    solutionHolderInd.parameters = List(addSchemaInd);

    /*def generate(i: Int) = {
      val schm1 = new AddClass()
      schm1.uri = new URI(menta.model.Constant.modelNamespaceString + "TestType1" + i)
      schm1.name = "TestType1" + i;
      schm1.isRoot = true
      kbServer.save(schm1)
    }

    for (val j <- Range(1, 15)) {
      generate(j)
    }
      */





    (new AddBlockIndividualHelper).apply(List(solutionHolderInd), null)

  }

  /**
   * Generates NARS Self --> Ok statement
   */
  def genSelfOk(): AddIndividual = {
    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Self", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "Ok", null)
    var selfOk = new AddInheritanceIndividualWrapper(l1, r1, null).addIndividual()
    selfOk
  }

  def createAcceptanceCriteria(): ActionIndividual = {

    /*
    [14.11 14: 49: 18][Thread - 4] DEBUG MentaProblem -
    Acceptance criteria rule: Rule
      [http :// menta.org / ontologies / v .0.2 / block -> menta.model.howto.AddIndividual@12d d538
       [List(http :// menta.org / ontologies / v .0.2 / equal -> menta.model.howto.AddIndividual@1 c18a4c
        [List(http :// menta.org / ontologies / v .0.2 / leftOperand -> menta.model.howto.AddIndividual@191f 801
         [List(http :// menta.org / ontologies / v .0.2 / variable -> menta.model.howto.AddIndividual@115d 06 c
          [List(null)]
         )]
         , http :// menta.org / ontologies / v .0.2 / rightOperand -> menta.model.howto.AddIndividual@1 b22920
          [List(AddSchema)]
         )]
         , http :// menta.org / ontologies / v .0.2 / loop -> menta.model.howto.AddIndividual@14 c28db
          [List(http :// menta.org / ontologies / v .0.2 / variable -> menta.model.howto.AddIndividual@14275d 4
           [List(null)]
          , http :// menta.org / ontologies / v .0.2 / variable -> menta.model.howto.AddIndividual@163956
           [List(null)]
          )]
         )]
        ];
    */

    //make a cycle around all elements
    val $SolutionHowTos = new AddVariableIndividualWrapper(menta.reasoneradapter.constant.Constant.NonTerminalURIs.loopVariable + "$Solution/0/prms/", null, null, null) // the reference to the Solution in the context
    val $a = new AddVariableIndividualWrapper(menta.reasoneradapter.constant.Constant.NonTerminalURIs.narsVariable + "$a", null, null, null)

    //Acceptance criteria
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "AddSchema", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeDATASET", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val cr0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), genSelfOk(), null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "AddEmployeeDATASET", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeKeySearch", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    val cr1 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop1.addIndividual(), genSelfOk(), null)


    val body = new AddBlockIndividualWrapper(List[HowTo](cr0, cr1), null)
    var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)


    /*

       val variable: AddVariableIndividualWrapper = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    // the loop variable definition
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution", null, null, generateFakeURI()) // the reference to the Solution in the context


    //Acceptance criteria
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val sok0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), genSelfOk(), null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    val sok1 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop1.addIndividual(), genSelfOk(), null)


    val body = new AddBlockIndividualWrapper(List[HowTo](sok0, sok1), null)
    new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)
     */

    // DONE: cr2 for root criteria

    val crWrapper = new AddBlockIndividualWrapper(List[HowTo](generateRootElementCondition(), loop.addIndividual()), null)
    val cr: AddIndividual = crWrapper.addIndividual()


    //val cr = new AddIndividual(null, List[HowTo](generateRootElementCondition(),loop.addIndividual()), null)
    //cr.className = "testXsdAC"

    kbServer.save(cr)
    val res = kbServer.selectActionInstance(cr.uri)

    res
  }

  //todo this variable should be in context
  def addSolution() = {
    val $a = new AddVariableIndividualWrapper("$a", null, null, null)
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution", null, null, null) // the reference to the Solution in the context


  }

  /**
   * Generates variable for first element, variable to get first element in solution
   */
  def generateRootElementCondition(): AddIndividual = {
    //generate variable for first element
    //variable to get first element in solution

    //Acceptance criteria
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/actionClass/0/name/", null, null, null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddSchema", null)
    val left = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()

    left
    /*//make a cycle around all elements
 val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution/0/prms/0/actionClass/0/name/", null, null,null) // the reference to the Solution in the context
 val $a = new AddVariableIndividualWrapper(menta.reasoneradapter.constant.Constant.NonTerminalURIs.narsVariable+ "$rootElement", null, null, null)

 //Acceptance criteria
 val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "AddSchema", null)
 val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddSchema", null)
 val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

 val cr0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), genSelfOk(), null)

 val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "AddEmployeeDATASET", null)
 val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeKeySearch", null)
 val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

 val cr1 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop1.addIndividual(), genSelfOk(), null)


 val body = new AddBlockIndividualWrapper(List[HowTo](cr0, cr1), null)
 var loop=new   AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    */
  }

  /*
   HowTo representation:
   AddSchema(targetNamespace="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd"){
    AddEmployeeDATASET{
      AddTillType()
      AddStoreType()
    }
   }
  */

  def createAcceptanceCriteriaOfThreeKids = {
    val crWrapper = new AddBlockIndividualWrapper(List[HowTo](generateAddSchemaHolderCheck(), generateAddSchemaCheck(), generateEmployeeDataSetCheck(), generateTillTypeCheck(), generateStoreTypeCheck(), generateEmployeeKeySearch(), generateEmployeeFileNameCheck()), null)
    val cr: AddIndividual = crWrapper.addIndividual()


    kbServer.save(cr)
    val res = kbServer.selectActionInstance(cr.uri)
    res
  }

  /**
   * Generates variable for first element, variable to get first element in solution
   */
  def generateAddSchemaHolderCheck(): AddIndividual = {
    //make a cycle around all elements
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution/0/prms/", null, null, null) // the reference to the Solution in the context
    val $a = new AddVariableIndividualWrapper("$a", null, null, null)

    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$a/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddSchemaHolder", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
    var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    loop.addIndividual()
    /*
  val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/actionClass/0/name/", null).addIndividual(), null)
  val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddSchema", null)
  val left = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()

  left  */
  }


  //AddEmployeeDataSetFileName

  def generateEmployeeFileNameCheck(): AddIndividual = {

    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/3/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeDataSetFileName", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0
  }

  def generateAddSchemaCheck(): AddIndividual = {


    //make a cycle around all elements
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/", null, null, null) // the reference to the Solution in the context
    val $a = new AddVariableIndividualWrapper("$a", null, null, null)

    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$a/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddSchema", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
    var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    loop.addIndividual()

  }

  def generateEmployeeDataSetCheck(): AddIndividual = {

    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/2/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeDATASET", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0

    /* //make a cycle around all elements
  val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/", null, null, null) // the reference to the Solution in the context
  val $a = new AddVariableIndividualWrapper("$a", null, null, null)

  val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$a/0/actionClass/0/name/", null).addIndividual(), null)
  val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddEmployeeDATASET", null)
  val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
  val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
  var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

  loop.addIndividual()  */
  }

  def generateStoreTypeCheck(): AddIndividual = {

    //  val $SolutionHowTos = new AddVariableIndividualWrapper( "$Solution/0/prms/0/prms/0/prms/", null, null, null) // the reference to the Solution in the context
    // val $a = new AddVariableIndividualWrapper("$a", null, null, null)
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddStoreType", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0
    /*val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/1/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddStoreType", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0*/
    // val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
    //var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    // loop.addIndividual()
    /*val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/0/actionClass/0/name/", null).addIndividual(), null)
   val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddTillType", null)
   val left = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
   left */
  }


  def generateTillTypeCheck(): AddIndividual = {

    //  val $SolutionHowTos = new AddVariableIndividualWrapper( "$Solution/0/prms/0/prms/0/prms/", null, null, null) // the reference to the Solution in the context
    // val $a = new AddVariableIndividualWrapper("$a", null, null, null)
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/1/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddTillType", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0
    /*val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddTillType", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0*/
    // val body = new AddBlockIndividualWrapper(List[HowTo](cr0), null)
    //var loop = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)

    // loop.addIndividual()
    /*val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "AddTillType", null)
    val left = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    left */
  }

  def generateEmployeeKeySearch(): AddIndividual = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$Solution/0/prms/0/prms/0/prms/2/prms/0/actionClass/0/name/", null).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "EmployeeKeySearch", null)
    val cr0 = (new AddEqualIndividualWrapper(l0, r0, null)).addIndividual()
    cr0
  }


  private def generateSchema(schemaName: String, facadeName: String) = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, schemaName, null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, facadeName, null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null);
    prop0.addIndividual()

  }

  private def clearAllAddClasses() {
    val classes = kbServer.selectAllActionClass()
    classes.foreach(b => {
      kbServer.removeObject(b)
    });
  }


  type SL = menta.model.howto.StringLiteral

  def createStrategy(level: Int = 1): TranslationStrategy = {

    val simplestTemplate =
      new AddTranslationHowTo(List(new SL("<"),
        new TrVariable("name"),
        new SL(">"),
        new TrApplyTemplates(null, null),
        new SL("</"),
        new TrVariable("name"),
        new SL(">")
      ))

    val simplestRule = new TranslationStrategyRule(new AddLanguage("xml"), "", simplestTemplate)

    //medium createStrategy: some, but with nested nodes or with text,
    //full createStrategy: some, but attributes,
    //schema createStrategy: some, but with special schema cases,

    val schemaTemplate =
      new AddTranslationHowTo(List(new SL("<xs:schema name=\""),
        new TrVariable("name"),
        new SL("\" namespace=\""),
        new TrVariable("./targetNamespace"),
        new SL("\">"),
        new TrApplyTemplates(null, null),
        new SL("</xs:schema>")
      ))

    // schema rule - used constructor with string
    val schemaRule = new TranslationStrategyRule(new AddLanguage("xml"), "AddSchema", schemaTemplate)


    // data rule - used constructor with URI
    val dataRule = new TranslationStrategyRule(new AddLanguage("xml"), new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET"),
      new AddTranslationHowTo(List(new SL("<xs:element name=\"Employee\" msdata:Prefix=\"rdc\" msdata:IsDataSet=\"true\" msdata:CaseSensitive=\"false\" msdata:EnforceConstraints=\"false\">"),
        new TrApplyTemplates(null, null),
        new SL("</xs:element>")
      ))
    )

    // data rule - [must] used constructor with ActionClass
    val keyRule = new TranslationStrategyRule(new AddLanguage("xml"), "AddEmployeeKeySearch",
      new AddTranslationHowTo(List(
        new SL("<xs:element name=\"EmployeeKeySearch\" msdata:Prefix=\"temp\" />")
      ))
    )

    val targetNamespaceRule = new TranslationStrategyRule(new AddLanguage("xml"), "TargetNamespace",
      new AddTranslationHowTo(List(
        new TrVariable("name")
      ))
    )

    val list: List[TranslationStrategyRule] = if (level == 1) List(simplestRule)
    else if (level == 2) List(simplestRule, schemaRule)
    else if (level == 3) List(schemaRule, dataRule, keyRule)
    else List(simplestRule, schemaRule, dataRule, keyRule)

    log.debug("size of Strategy = " + list.size.toString)

    val res = new TranslationStrategy(list)
    res
  }
}