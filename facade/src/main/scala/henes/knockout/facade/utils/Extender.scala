package henes.knockout.facade.utils

import henes.knockout.facade.ko

import scala.scalajs.js

case class Extender[I, O, P](name: String, fn: (ko.Observable[I], P) => ko.Observable[O]) {
  ko.extenders.updateDynamic(name) {
    (k: ko.Observable[I], p: P) => fn(k, p)
  }

  def extend(o: ko.Observable[I]): ko.Observable[O] =
    o.extend[O](js.Dynamic.literal(name -> null))

  def extend(o: ko.Observable[I], parameter: P): ko.Observable[O] =
    o.extend[O](js.Dynamic.literal(name -> parameter.asInstanceOf[js.Any]))
}

object RateLimitExtender {
  sealed trait RateLimitMethod

  case object NotifyAtFixedRate extends RateLimitMethod {
    override def toString: String = "notifyAtFixedRate"
  }

  case object NotifyWhenChangesStop extends RateLimitMethod {
    override def toString: String = "notifyWhenChangesStop"
  }

  def extend[I](o: ko.Observable[I], rateLimit: Int): ko.Observable[I]  =
    o.extend[I](js.Dynamic.literal(rateLimit = rateLimit))

  def extend[I](o: ko.Observable[I], timeout: Int, method: RateLimitMethod): ko.Observable[I] =
    o.extend[I](js.Dynamic.literal(rateLimit = js.Dynamic.literal(timeout = timeout, method = method.toString)))
}

object NotifyAlwaysExtender {
  def extend[I](o: ko.Observable[I]): ko.Observable[I] =
    o.extend[I](js.Dynamic.literal(notify = "always"))
}

object DeferredExtender {
  def extend[I](o: ko.Observable[I]): ko.Observable[I] =
    o.extend[I](js.Dynamic.literal(deferred = true))
}