package controllers

import play.api.mvc.{Action, Controller}

import scala.util.Random

class ResponseController extends Controller {
  def index() = Action {
    Ok(views.html.example())
  }

  def randomInteger(min: Int, max: Int) = Action {
    val distance = max - min + 1
    val result = (math.abs(Random.nextInt()) % distance) + min
    Ok(result.toString)
  }
}