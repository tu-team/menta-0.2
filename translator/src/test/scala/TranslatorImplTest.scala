import java.net.URI
import menta.model.howto._
import menta.model.howto.helper.AddParameterHelper
import menta.translator._
import org.junit.{Ignore, Test}
import org.slf4j.{LoggerFactory, Logger}
import wrappers.{TrApplyTemplates, TrVariable}

/**
 * @author chepkunov adel, talanov max
 * Date: 16.11.11
 * Time: 3:59
 */


@Test
class TranslatorImplTest {

  val log: Logger = LoggerFactory.getLogger(this.getClass)
  val translator = new TranslatorImpl()
  private val theParameterHelper = new AddParameterHelper();

  def strategy(level:Int = 1): TranslationStrategy = {

    val simplestTemplate =
      new AddTranslationHowTo(List(new StringLiteral("<"),
        new TrVariable("name"),
        new StringLiteral(">"),
        new TrApplyTemplates(null, null),
        new StringLiteral("</"),
        new TrVariable("name"),
        new StringLiteral(">")
      ))

    val simplestRule = new TranslationStrategyRule(new AddLanguage("xml"), "", simplestTemplate)

    //medium strategy: some, but with nested nodes or with text, TODO
    //full strategy: some, but attributes, TODO
    //schema strategy: some, but with special schema cases, TODO

    val schemaTemplate =
      new AddTranslationHowTo(List(new StringLiteral("<xs:schema name=\""),
        new TrVariable("name"),
        new StringLiteral("\" namespace=\""),
        new TrVariable("./targetNamespace"),
        new StringLiteral("\">"),
        new TrApplyTemplates(null, null),
        new StringLiteral("</xs:schema>")
      ))

    // schema rule - used constructor with string
    val schemaRule = new TranslationStrategyRule(new AddLanguage("xml"), "AddSchema", schemaTemplate)


    // data rule - used constructor with URI
    val dataRule = new TranslationStrategyRule(new AddLanguage("xml"), new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET"),
      new AddTranslationHowTo(List(new StringLiteral("<xs:element name=\"Employee\" msdata:Prefix=\"rdc\" msdata:IsDataSet=\"true\" msdata:CaseSensitive=\"false\" msdata:EnforceConstraints=\"false\">"),
        new TrApplyTemplates(null, null),
        new StringLiteral("</xs:element>")
        ))
    )

    // data rule - [must] used constructor with ActionClass
    val keyRule = new TranslationStrategyRule(new AddLanguage("xml"), "AddEmployeeKeySearch", // TODO check work with Class
      new AddTranslationHowTo(List(
        new StringLiteral("<xs:element name=\"EmployeeKeySearch\" msdata:Prefix=\"temp\" />")
        ))
    )

    val targetNamespaceRule = new TranslationStrategyRule(new AddLanguage("xml"), "TargetNamespace",
      new AddTranslationHowTo(List(
        new TrVariable("name")
        ))
    )

    val list:List[TranslationStrategyRule] = if (level==1) List(simplestRule)
    else if (level==2)List(simplestRule, schemaRule)
    else if (level==3)List(schemaRule, dataRule, keyRule)
      else List(simplestRule, schemaRule, dataRule, keyRule)

    log.debug("size of Strategy = "+list.size.toString)

    val res = new TranslationStrategy(list)
    res
  }

  @Test
  def getRuleTest() {

    val el = new AddClass
    el.name = "element"
    el.className = el.name

    val strategy2 = strategy(2)

    val uri = new URI("a", "b", "c")

    var res = strategy2.getTemplateOption(el)
    // TODO fix this
    // assert(res.get.parameters(0).toString == "<")

    val addSchema = new AddClass
    addSchema.uri = new URI(menta.model.Constant.modelNamespaceString + "AddSchema")
    addSchema.name = "AddSchema";
    addSchema.isRoot = true
    //addSchema.parameters = List(theParameterHelper("targetNamespace", new menta.model.howto.StringLiteral(), 1), theParameterHelper("addEmployeeDATASET", addEmployeeDATASET, 1))
    res = strategy2.getTemplateOption(addSchema)

    log.info("exepect '<xs:scema name=\"', get:'" +res.get.parameters(0).toString+"'")
    assert(res.get.parameters(0).toString == "<xs:schema name=\"")
  }

  @Ignore
  @Test
  def SimplestTest() {
    val xml = new AddClass
    xml.name = "element"
    xml.isRoot = true
    xml.className = xml.name
    val res = translator.createCode(new Solution(List(xml)), strategy() )
    log.info(res)
    assert(res == "<element></element>")
  }

  @Ignore
  @Test
  def SimpleTest() {
    val el = new AddClass
    el.name = "element"
    el.className = el.name

    val su = new AddClass
    su.name = "super"
    su.className = su.name
    //su.parameters = List(theParameterHelper("element", el, 1))
    su.parameters = List(el)

    log.info("strat SimpleTest")
    val res = translator.createCode(new Solution(List(su)), strategy() )
    log.info(res)
    assert(res == "<super><element></element></super>")
  }



  @Ignore
  @Test
  def ComplexTest() {
    val addEmployeeKeySearch = new AddClass
    addEmployeeKeySearch.name = "AddEmployeeKeySearch"
    addEmployeeKeySearch.isRoot = true
    addEmployeeKeySearch.uri = new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeKeySearch")


    val addEmployeeDATASET = new AddClass
    addEmployeeDATASET.uri = new URI(menta.model.Constant.modelNamespaceString + "AddEmployeeDATASET")
    addEmployeeDATASET.name = "AddEmployeeDATASET";
    addEmployeeDATASET.isRoot = true
    addEmployeeDATASET.parameters = List(theParameterHelper("addEmployeeKeySearch", addEmployeeKeySearch, 1))

    val addSchema = new AddClass
    addSchema.uri = new URI(menta.model.Constant.modelNamespaceString + "AddSchema")
    addSchema.name = "AddSchema";
    addSchema.isRoot = true
    addSchema.parameters = List(theParameterHelper("targetNamespace", new menta.model.howto.StringLiteral(), 1), theParameterHelper("addEmployeeDATASET", addEmployeeDATASET, 1))
    addSchema.parameters = List(theParameterHelper("targetNamespace", new menta.model.howto.StringLiteral(), 1), addEmployeeDATASET)
    //TODO check present of real solution/

    val res = translator.createCode(new Solution(List(addSchema)), strategy(3) )
    log.info(res)
    assert(res == "<xs:schema name=\"AddSchema\" namespace=\"AddSchema\"><xs:element name=\"Employee\" msdata:Prefix=\"rdc\" msdata:IsDataSet=\"true\" msdata:CaseSensitive=\"false\" msdata:EnforceConstraints=\"false\"><xs:element name=\"EmployeeKeySearch\" msdata:Prefix=\"temp\" /></xs:element></xs:schema>")
    //NB: namespace="AddSchema" - need change this part to target namespace after variable will be done
  }

  val keySearchExpectedString = """<xs:element name="EmployeeKeySearch" msdata:Prefix="temp">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element minOccurs="1" name="EmployeeID" type="rdc:EmployeeIDType" />
                        </xs:sequence>
                    </xs:complexType>
                </xs:element>"""


  val fullExpectedString =
    """  <xs:schema
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
    """

}





