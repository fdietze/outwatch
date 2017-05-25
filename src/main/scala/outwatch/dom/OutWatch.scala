package outwatch.dom

import org.scalajs.dom._
import outwatch.dom.helpers.DomUtils

object OutWatch {
  def render(querySelector: String, vNode: VNode): Unit =
    DomUtils.render(document.querySelector(querySelector), vNode)
}

object Attributes extends Attributes
object Tags extends Tags
object Handlers extends Handlers
