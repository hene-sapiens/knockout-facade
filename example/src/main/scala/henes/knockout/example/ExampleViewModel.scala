package henes.knockout.example

import henes.knockout.facade.ko
import henes.knockout.facade.ko.PureComputedOptions
import henes.knockout.facade.utils.{Extender, Knockout, RateLimitExtender}
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.{Event, window, document}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Random, Try}

@JSExportAll
class ExampleViewModel {
  def failOnUndefined[T](v: T): T =
    if(js.isUndefined(v)) throw new Exception() else v

  // Example on how to use facade
  ko.extenders.updateDynamic("integerField"){
    (target: ko.Observable[String], noParam: Unit) =>
      ko.pureComputed[Int](js.Dynamic.literal(
        read = { () => Try(target().toInt).getOrElse(0) },
        write = { (value: Int) => target(value.toString) }
      ).asInstanceOf[PureComputedOptions[Int]])
  }

  val iafExtender = Knockout.extender[String, js.Array[Int]]("integerArrayField", target =>
    Knockout.pureComputed(
      () => Try(js.Array(target().split(",").map(_.trim.toInt): _*)).getOrElse(js.Array()),
      value => target(value.mkString(" ,"))
    )
  )

  val firstName = ko.observable("foo")
  val lastName = ko.observable("bar")
  val fullName = ko.computed(() => s"${firstName()} ${lastName()}")
  RateLimitExtender.extend(fullName, 400, RateLimitExtender.NotifyWhenChangesStop)
    .subscribe { s: String => window.alert("Full name changed") }

  val twoWayComputedFullName = Knockout.pureComputed(
    () => s"${firstName()} ${lastName()}", {
      value: String =>
        val split = value.split(" ")
        val first = if(split.nonEmpty) split(0) else ""
        val last = if(split.size >= 2) split(1) else ""
        firstName(first)
        lastName(last)
    }
  )

  val value1Field = ko.observable("")
  val value1 = value1Field.extend[Int](js.Dynamic.literal(integerField = null))
  val value2Field = ko.observable("")
  val value2 = value2Field.extend[Int](js.Dynamic.literal(integerField = null))
  val value3Field = ko.observable("")
  val value3 = value3Field.extend[Int](js.Dynamic.literal(integerField = null))
  value1(1)
  value2(2)
  value3(3)

  val valuesField = ko.observable("3,4")
  val values = iafExtender.extend(valuesField)

  val sumOfValues = ko.computed[Int](() => value1() + value2() + value3() + values().sum)

  @JSExportAll
  class SubViewModel {
    val aValue = ko.observable(Random.nextPrintableChar())
    val bValueString = ko.observable("")
    val bValue = bValueString.extend[Int](js.Dynamic.literal(integerField = null))
    bValue(Random.nextInt() % 512)
    val cValue = ko.computed(() => s"a: ${aValue()}, b: ${bValue()}")
  }

  val subViewModels = ko.observableArray(js.Array(new SubViewModel, new SubViewModel))
  val subscription = subViewModels.subscribe({
      viewModels: js.Array[SubViewModel] =>
        window.alert(s"Changed. Latest SubViewModel {${viewModels.lastOption.map(_.cValue()).getOrElse("EMPTY")}}")
    })

  def addNewSub(): Unit = {
    subViewModels.push(new SubViewModel)
  }

  def unsubscribe(): Unit = {
    subscription.dispose()
  }

  // Validation
  sealed trait Validity[T]
  case class Valid[T](value: T) extends Validity[T]
  case class Invalid[T](msg: String) extends Validity[T]

  val validationExtender = Extender[String, Validity[String], String]("validationExtender",
    (target, invalidCharacters) => Knockout.pureComputed {
      () =>
        val value = target()
        value
          .find(c => invalidCharacters.contains(c))
          .map(c => Invalid[String](s"String contains character '$c'"))
          .getOrElse(Valid(value))
    }
  )

  @JSExportAll
  class ValidatedFieldViewModel(notAllowed: String) {
    val explanation = s"Characters '$notAllowed' not allowed"
    val text = ko.observable[String]("")
    val validation = validationExtender.extend(text, notAllowed)
    val result = Knockout.pureComputed(() => validation() match {
      case Valid(v) => "Valid!"
      case Invalid(msg) => s"Invalid: $msg"
    })
  }

  val componentHtml: String =
    """
      <pre style="display: inline" data-bind="text: explanation"></pre>
      <input data-bind="value: text"/>
      <pre style="display: inline" data-bind="text: result"></pre>
    """

  // Component 1
  ko.components.register("validated-field1", js.Dynamic.literal(
    viewModel = js.Dynamic.literal(
      createViewModel = {
        (params: ComponentParameter, componentInfo: js.Dynamic) =>
          new ValidatedFieldViewModel(params.notallowed)
      }
    ), template = componentHtml.toString
  ).asInstanceOf[ko.components.ComponentParameters])

  // Component 2
  val component = Knockout.component("validated-field2", (params: ComponentParameter, _) => new ValidatedFieldViewModel(params.notallowed), componentHtml)

  val allValues = Knockout.computed {
    () => "Component values: " + component.instances().map(v => s"'${v.text()}'").mkString(",")
  }

  val allValid = Knockout.computed {
    () =>
      if(component.instances().forall(_.validation().isInstanceOf[Valid[_]]))
        "All valid"
      else
        "Validation error"
  }

  def removeFoo(): Unit = {
    val foo = document.getElementById("foo")
    if(foo != null) foo.parentNode.removeChild(foo)
  }

  // Ajax example
  def GET(path: String)(fn: (Event, XMLHttpRequest) => Unit): Unit = {
    val request = new XMLHttpRequest()
    request.onreadystatechange = { ev: Event => fn(ev, request) }
    request.open("GET", path, async = true)
    request.send()
  }

  val componentHtml2: String =
    """
      <div>
        <input data-bind="textInput: text"/>
        <pre style="display: inline" data-bind="text: result"></pre>
      </div>
    """

  @JSExportAll
  class IntFieldViewModel(val name: String) {
    val text = RateLimitExtender.extend(ko.observable[String](""), 500, RateLimitExtender.NotifyWhenChangesStop)
    val value = ko.computed {
      () => Try(text().toInt).toOption.map(i => Valid(i)).getOrElse(Invalid[Int]("Not integer"))
    }
    val result = Knockout.pureComputed(() => value() match {
      case Valid(v) => "Valid!"
      case Invalid(msg) => s"Invalid: $msg"
    })
  }

  val intFieldComponent = Knockout.component("int-field", (p: IntFieldParameters, _) => new IntFieldViewModel(p.name), componentHtml2)

  val previousRandomMinMax = ko.observable((0, 0))
  val randomMinMax: ko.Observable[(Int, Int)] = Knockout.computed[(Int, Int)] {
    () => (for {
        min <- intFieldComponent.instances().find(_.name == "min")
        max <- intFieldComponent.instances().find(_.name == "max")
      } yield {
      (min.value(), max.value()) match {
        case (Valid(a), Valid(b)) => (a, b)
        case _ => previousRandomMinMax()
      }
    }).getOrElse(previousRandomMinMax())
  }

  randomMinMax.subscribe { v: (Int, Int) =>
    if(previousRandomMinMax() != v) {
      previousRandomMinMax(v)
      GET(s"/randomInteger/${v._1}/${v._2}") {
        (event, request) =>
          if (request.readyState == 4 && request.status == 200) {
            randomIntSourceText(request.responseText)
          }
      }
    }
  }

  val randomIntSourceText = Knockout.observable("")
  val randomInt = Knockout.computed(() => Try(randomIntSourceText().toInt).toOption)
  val randomIntText = Knockout.computed(() => randomInt().map(_.toString).getOrElse("No random number"))
}

@js.native
trait ComponentParameter extends js.Object {
  def notallowed: String = js.native
}

@js.native
trait IntFieldParameters extends js.Object {
  def name: String = js.native
}

object ExampleViewModel extends js.JSApp {
  def main(): Unit = {
    val vm = new ExampleViewModel
    Knockout.applyBindings(vm)
  }
}