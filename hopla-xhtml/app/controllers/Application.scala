package controllers

import _root_.fr.inria.hoplaxhtml.examples.Example
import play.api.mvc._

/**
 * Play Object which runs the server.<br/>
 * Generate html content of the presentation in order to be displayed by the Play Server.
 */
object Application extends Controller {

  /**
   * Define the content of the index page
   * @return a 200:Ok response with the html content of the presentation to display
   */
  def index = Action {
    Ok(views.html.index(Example.presentation.toHtml))
  }

}
