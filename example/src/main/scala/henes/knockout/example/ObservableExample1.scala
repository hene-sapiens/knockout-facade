package henes.knockout.example

import henes.knockout.facade.utils.Knockout

import scala.scalajs.js.annotation.JSExportAll
import org.scalajs.dom.window

@JSExportAll
object ObservableExample1 {
  val fullName = Knockout.observable("")

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

  val listOfNames = Knockout.observableArray[String](Nil)

  def insertName(): Unit = {
    if(firstName().isEmpty)
      window.alert("First name not given")
    else if(lastName().isEmpty)
      window.alert("Last name not given")
    else
      listOfNames.push(s"First name: ${firstName().get}. Last name: ${lastName().get}")
  }
}
