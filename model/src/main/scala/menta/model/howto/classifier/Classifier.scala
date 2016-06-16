package menta.model.howto.classifier

import collection.immutable.HashSet

/**
 * @author talanovm
 * Date: 18.03.11
 * Time: 10:13
 */

class Classifier(constSuperClassifiers: HashSet[Classifier], constSubClassifiers: HashSet[Classifier], constName: String) {
  def this() = this(HashSet[Classifier](), HashSet[Classifier](), "")
  private var theSuperClassifiers = constSuperClassifiers
  private var theSubClassifiers = constSubClassifiers
  private var theName = constName
  def superClassifiers: HashSet[Classifier] = theSuperClassifiers
  def superClassifiers_(aSuperClassifiers: HashSet[Classifier]) = theSuperClassifiers = aSuperClassifiers
  def subClassifiers: HashSet[Classifier] = theSubClassifiers
  def subClassifiers_(aSubClassifiers: HashSet[Classifier]) = theSubClassifiers = aSubClassifiers
  def name: String = theName
  def name_(aName: String) = theName = aName
  def name_=(aName: String) = theName = aName
}