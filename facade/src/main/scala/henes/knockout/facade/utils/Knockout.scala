package henes.knockout.facade.utils

import henes.knockout.facade.ko
import org.scalajs.dom.html

import scala.scalajs.js

object Knockout {
  def applyBindings[T](viewModel: T, root: Option[html.Element] = None): Unit = {
    val o = viewModel.asInstanceOf[js.Object]
    if (root.isDefined) ko.applyBindings(o, root.get)
    else ko.applyBindings(o)
  }

  def observable[T](v: T): ko.Observable[T] =
    ko.observable(v)

  def observableArray[T](v: Seq[T]): ko.ObservableArray[T] =
    ko.observableArray(js.Array(v :_*))

  def computed[T](readFn: () => T, deferEvaluation: Boolean = false, disposeWhen: Boolean = false, disposeWhenNodeIsRemoved: Boolean = false): ko.Computed[T] =
    ko.computed[T](js.Dynamic.literal(
      read = { () => readFn() },
      deferEvaluation = deferEvaluation,
      disposeWhen = disposeWhen,
      disposeWhenNodeIsRemoved = disposeWhenNodeIsRemoved
    ).asInstanceOf[ko.ComputedOptions[T]])

  def pureComputed[T](readFn: () => T): ko.Computed[T] =
    ko.pureComputed[T](js.Dynamic.literal(
      read = { () => readFn() }
    ).asInstanceOf[ko.PureComputedOptions[T]])

  def pureComputed[T](readFn: () => T, writeFn: T => Unit): ko.Computed[T] =
    ko.pureComputed[T](js.Dynamic.literal(
      read = { () => readFn() },
      write = { v: T => writeFn(v) }
    ).asInstanceOf[ko.PureComputedOptions[T]])

  def component[V, P <: js.Object](name: String, createViewModelFn: (P, ko.components.ComponentInfo) => V, template: String): Component[V, P] =
    Component(name, createViewModelFn, template)

  def extender[I, O](name: String, fn: ko.Observable[I] => ko.Observable[O]): Extender[I, O, Unit] =
    Extender[I, O, Unit](name, (o, _) => fn(o))

  def extender[I, O, P](name: String, fn: (ko.Observable[I], P) => ko.Observable[O]): Extender[I, O, P] =
    Extender(name, fn)
}
