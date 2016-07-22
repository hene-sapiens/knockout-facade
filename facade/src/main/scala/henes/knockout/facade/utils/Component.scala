package henes.knockout.facade.utils

import henes.knockout.facade.ko
import org.scalajs.dom._
import org.scalajs.dom.raw.MutationObserver

import scala.scalajs.js

// V: View model class
// P: Component parameters trait
case class Component[V, P <: js.Object](name: String, createViewModelFn: (P, ko.components.ComponentInfo) => V, template: String) {
  private var instancesMap : Map[Node, V] = Map()
  lazy val instances = Knockout.observableArray[V](Nil)

  private def updateInstancesObservable(): Unit =
    instances(js.Array(instancesMap.values.toVector: _*))

  private def addInstance(node: Node, viewModel: V): Unit = {
    instancesMap += node -> viewModel
    updateInstancesObservable()

    val observer = new MutationObserver({
      (_: js.Array[MutationRecord], o: MutationObserver) =>
        if(node.parentNode == null) {
          removeInstance(node)
          o.disconnect()
        }
    })
    observer.observe(node.parentNode, js.Dynamic.literal(childList = true).asInstanceOf[MutationObserverInit])
  }

  private def removeInstance(node: Node): Unit =
    instancesMap.get(node).foreach {
      _ =>
        instancesMap -= node
        updateInstancesObservable()
    }
}

object Component {
  def register[V, P <: js.Object](component: Component[V, P]): Component[V, P] = {
    ko.components.register(component.name, js.Dynamic.literal(
      viewModel = js.Dynamic.literal(
        createViewModel = {
          (params: P, componentInfo: ko.components.ComponentInfo) =>
            val viewModel = component.createViewModelFn(params, componentInfo)
            component.addInstance(componentInfo.element, viewModel)
            viewModel
        }
      ), template = component.template
    ).asInstanceOf[ko.components.ComponentParameters])
    component
  }
}