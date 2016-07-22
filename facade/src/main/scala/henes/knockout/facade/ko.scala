package henes.knockout.facade

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("ko")
object ko extends js.Object {
  def applyBindings(viewModel: js.Object, root: html.Element = js.native): Unit = js.native

  @js.native
  trait Subscription extends js.Object {
    def dispose(): Unit = js.native
  }

  def observable[T](v: T): Observable[T] = js.native

  @js.native
  trait Observable[T] extends js.Object {
    def apply(): T = js.native
    def apply(value: T): Unit = js.native
    def subscribe(fn: js.Function1[T, _], callbackTarget: js.Object = js.native, event: String = js.native): Subscription = js.native
    def extend[N](value: js.Dynamic): Observable[N] = js.native
    def notifySubscribers(): Unit = js.native
  }

  def observableArray[T](v: js.Array[T]): ObservableArray[T] = js.native

  @js.native
  trait ObservableArray[T] extends Observable[js.Array[T]] {
    def indexOf(value: T): Int = js.native
    def slice(start: Int, end: Int): js.Array[T] = js.native

    def push(value: T): Unit = js.native
    def pop(): T = js.native
    def shift(): T = js.native
    def unshift(value: T): Unit = js.native
    def reverse(): Unit = js.native
    def sort(fn: js.Function2[T, T, Int]): Unit = js.native
    def splice(start: Int, end: Int): js.Array[T] = js.native

    def remove(value: T): js.Array[T] = js.native
    def remove(fn: js.Function1[T, Boolean]): js.Array[T] = js.native
    def removeAll(values: js.Array[T]): js.Array[T] = js.native
    def removeAll(): js.Array[T] = js.native

    def destroy(value: T): Unit = js.native
    def destroy(fn: js.Function1[T, Boolean]): Unit = js.native
    def destroyAll(values: js.Array[T]): Unit = js.native
    def destroyAll(): Unit = js.native
  }

  def computed[T](read: js.Function0[T], targetObject: Any = js.native, options: ComputedOptions[T] = js.native): Computed[T] = js.native
  def computed[T](options: ComputedOptions[T]): Computed[T] = js.native

  @js.native
  trait ComputedOptions[T] extends js.Object {
    def read: js.Function0[T] = js.native
    def write: js.Function1[T, _] = js.native
    def owner: js.Object = js.native
    def pure: Boolean = js.native
    def deferEvaluation: Boolean = js.native
    def disposeWhen: js.Function0[Boolean] = js.native
    def disposeWhenNodeIsRemoved: dom.Node = js.native
  }

  @js.native
  trait Computed[T] extends Observable[T] {
    def dispose(): Unit = js.native
    def getDependenciesCount: Int = js.native
    def getSubscriptionsCount(event: String = js.native): Int = js.native
    def isActive: Boolean = js.native
    def peek: T = js.native
  }

  def pureComputed[T](read: js.Function0[T], targetObject: Any = js.native): Computed[T] = js.native
  def pureComputed[T](options: PureComputedOptions[T]): Computed[T] = js.native

  @js.native
  trait PureComputedOptions[T] extends js.Object {
    def read: js.Function0[T] = js.native
    def write: js.Function1[T, _] = js.native
    def owner: js.Object = js.native
  }

  @js.native
  object computedContext extends js.Object {
    def isInitial: Boolean = js.native
    def getDependenciesCount: Int = js.native
  }

  def isObservable(ob: js.Object): Boolean = js.native
  def isWritableObservable(ob: js.Object): Boolean = js.native
  def isWriteableObservable(ob: js.Object): Boolean = js.native
  def isPureComputed(ob: js.Object): Boolean = js.native
  def isComputed(ob: js.Object): Boolean = js.native

  val extenders: js.Dynamic = js.native
  val bindingHandlers: js.Dynamic = js.native

  @js.native
  trait AllBindings extends js.Object {
    def get(name: String): js.Object = js.native
    def has(name: String): Boolean = js.native
  }

  @js.native
  trait BindingContext extends js.Object {
    val $parent: js.Object = js.native
    val $parents: js.Array[js.Object] = js.native
    val $root: js.Object = js.native
    val $component: js.Object = js.native
    val $data: js.Object = js.native
    val $index: Int = js.native
    val $parentContext: BindingContext = js.native
    val $rawData: js.Object = js.native
    val $componentTemplateNodes: js.Array[dom.Node] = js.native
  }

  @js.native
  trait BindingHandler extends js.Object {
    // init(element, valueAccessor, allBindings, viewModel, bindingContext)
    def init: js.Function5[html.Element, js.Function0[js.Object], AllBindings, js.Object, BindingContext, _] = js.native

    // update(element, valueAccessor, allBindings, viewModel, bindingContext)
    def update: js.Function5[html.Element, js.Function0[js.Object], AllBindings, js.Object, BindingContext, _] = js.native

    // preprocess(value, name, addBindingCallback(name, value))
    var preprocess: js.Function3[js.Any, String, js.Function2[String, js.Any, _], String] = js.native

    // preprocessNode(node)
    var preprocessNode: js.Function1[dom.Node, js.Array[dom.Node]] = js.native
  }

  @js.native
  object virtualElements extends js.Object {
    val allowedBindings: js.Dynamic = js.native
    def emptyNode(containerElem: html.Element): dom.Node = js.native
    def firstChild(containerElem: html.Element): dom.Node = js.native
    def insertAfter(containerElem: html.Element, nodeToInsert: dom.Node, insertAfter: dom.Node): Unit = js.native
    def nextSibling(containerElem: html.Element): dom.Node = js.native
    def prepend(containerElem: html.Element, node: dom.Node): Unit = js.native
    def setDomNodeChildren(containerElem: html.Element, arrayOfNodes: js.Array[dom.Node]): Unit = js.native
  }

  @js.native
  object components extends js.Object {

    @js.native
    trait ComponentParameters extends js.Object {
      def viewModel: js.Object = js.native
      def template: String = js.native
    }

    @js.native
    trait ComponentInfo extends js.Object {
      def element: html.Element = js.native
      def templateNodes: js.Array[html.Element] = js.native
    }

    def register(name: String, params: ComponentParameters): Unit = js.native
    def isRegistered(name: String): Boolean = js.native
    def unregister(name: String): Unit = js.native
    def get(name: String, callback: js.Object): Unit = js.native
    def clearCachedDefinition(name: String): Unit = js.native


    @js.native
    trait ComponentLoader extends js.Object {
      // getConfig(name, callback)
      def getConfig: js.Function2[String, js.Function1[js.Object, _], _] = js.native

      // loadComponent(name, componentConfig, callback)
      def loadComponent: js.Function3[String, js.Object, js.Function1[js.Object, _], _] = js.native

      // loadTemplate(name, templateConfig, callback)
      def loadTemplate: js.Function3[String, js.Object, js.Function1[js.Object, _], _] = js.native

      // loadViewModel(name, templateConfig, callback)
      def loadViewModel: js.Function3[String, js.Object, js.Function1[js.Object, _], _] = js.native
    }

    val loaders: js.Array[ComponentLoader] = js.native
    val defaultLoader: ComponentLoader = js.native
  }

  @js.native
  object utils extends js.Object {
    @js.native
    object domNodeDisposal extends js.Object {
      def addDisposeCallback(element: dom.Node, callback: js.Function0[_]): Unit = js.native
      var cleanExternalData: js.Function1[dom.Node, _] = js.native
    }
  }
}

