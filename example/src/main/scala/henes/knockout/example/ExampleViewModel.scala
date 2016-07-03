package henes.knockout.example

import henes.knockout.facade.utils.{Knockout}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
object ExampleViewModel extends js.JSApp {
  val visibleExample = Knockout.observable("")
  val observableExample1 = ObservableExample1
  val componentExample1 = ComponentExample1

  def main(): Unit = {
    Knockout.applyBindings(this)
  }
}