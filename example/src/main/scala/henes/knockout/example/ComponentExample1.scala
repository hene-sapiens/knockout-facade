package henes.knockout.example

import henes.knockout.facade.ko
import henes.knockout.facade.utils.Knockout

import scala.scalajs.js.annotation.{JSExportAll, ScalaJSDefined}
import org.scalajs.dom.window

import scala.scalajs.js

@JSExportAll
object ComponentExample1 {
  val nameList = Knockout.observableArray[String](Nil)

  val insertNameFieldComponent = Knockout.component[InsertNameFieldViewModel, InsertNameFieldParameters]("insertnamefield", {
    (params, info) =>
      new InsertNameFieldViewModel(
        if(js.isUndefined(params.defaultName)) "" else params.defaultName,
        params.nameList
      )
  },
    """
      |<div>
      |   Full name: <input data-bind="textInput: fullName"/> <br/>
      |   First name: <div style="display: inline" data-bind="text: firstName"></div> <br/>
      |   Last name: <div style="display: inline" data-bind="text: lastName"></div> <br/>
      |   <button style="display: inline" data-bind="click: insertName"> Add to list </button>
      |</div>
    """.stripMargin)

  val nameListNodeComponent = Knockout.component[NameListViewModel, NameListParameters]("namelist", {
    (params, info) =>
      new NameListViewModel(params.nameList)
  },
    """
      |<h4> List of Names: </h4>
      |<div data-bind="foreach: nameList">
      |   <div>
      |      <div style="display: inline" data-bind="text: $data"> </div>
      |      <button style="display: inline" data-bind="click: function() { $parent.removeName($index()) }"> Remove name </button>
      |   </div>
      |</div>
    """.stripMargin)
}

@js.native
trait InsertNameFieldParameters extends js.Object {
  def defaultName: String = js.native
  def nameList: ko.ObservableArray[String] = js.native
}

@JSExportAll
class InsertNameFieldViewModel(defaultName: String, nameList: ko.ObservableArray[String]) {
  val fullName = Knockout.observable(defaultName)

  val firstName = Knockout.pureComputed[Option[String]]({
    () => fullName().split(" ").toList match {
      case name :: _ if name.nonEmpty => Some(name)
      case _ => None
    }
  })

  val lastName = Knockout.pureComputed[Option[String]]({
    () => fullName().split(" ").toList match {
      case _ :: name :: _ => Some(name)
      case _ => None
    }
  })

  def insertName(): Unit =
    if(firstName().isEmpty)
      window.alert("First name not given")
    else if(lastName().isEmpty)
      window.alert("Last name not given")
    else {
      nameList.push(s"${firstName().get} ${lastName().get}")
      fullName("")
    }
}

@js.native
trait NameListParameters extends js.Object {
  def nameList: ko.ObservableArray[String] = js.native
}

@JSExportAll
class NameListViewModel(val nameList: ko.ObservableArray[String]) {
  def removeName(ix: Int): Unit = {
    nameList.splice(ix, 1)
  }
}
