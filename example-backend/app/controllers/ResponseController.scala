package controllers

import play.api.mvc.{Action, Controller}

import scala.util.Random

class ResponseController extends Controller {
  def index() = Action {
    Ok(views.html.example())
  }

  case class Data(text: String)
  var data = Vector(
    Data("foo"),
    Data("bar"),
    Data("fez")
  )

  def setValue(index: Int) = Action {
    implicit request =>
      val response = request.body.asFormUrlEncoded match {
        case Some(content) if content.contains("text") =>
          val nextText = content("text").head
          val (prev, end) = data.splitAt(index)
          data = prev.dropRight(1) ++ Vector(Data(nextText)) ++ end
          "Ok"

        case None =>
          "Fail"
      }
      Ok(response)
  }

  def getValues(start: Int, length: Int) = Action {
    Ok(data.slice(start, start+length).mkString(","))
  }

  def length() = Action {
    Ok(data.size.toString)
  }

  def randomInteger(min: Int, max: Int) = Action {
    val distance = max - min + 1
    val result = (math.abs(Random.nextInt()) % distance) + min
    Ok(result.toString)
  }
}