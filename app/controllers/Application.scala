package controllers

import play.api._
import play.api.data.{Form, Forms, Mapping}
import play.api.data.Forms._
import play.api.mvc._
import models._

import scala.util.Try

class Application extends Controller {

  def index = Action {
    Ok(views.html.index(None))
  }

  def processeasy = Action { implicit request =>
    val val1 = request.getQueryString("val1") map {_.toInt}
    val val2 = request.getQueryString("val2") map {_.toInt}

    val result = for { v <- val1
          v2 <- val2}
      yield v + v2

    Ok(views.html.results(result.getOrElse(-1)))

  }

  def process = Action (parse.form(calcForm)) { implicit request =>

    val result = request.body
    if (calcForm.hasErrors) {
      BadRequest(views.html.index(None))
    }
    else{
      Ok(views.html.index(Option(result.val1 + result.val2)))
    }



  }


  val requiredNumber: Mapping[Int] = Forms.nonEmptyText
    .verifying("Must be numeric", i => Try(i.toInt).isSuccess || i.isEmpty)
    .transform[Int](_.toInt, _.toString)

  val calcForm = Form(
    mapping(
      "val1" -> requiredNumber,
      "val2" -> requiredNumber,
      "val3" -> nonEmptyText(5,25)
    )
    (calcData.apply)(calcData.unapply)
  )
  val form = Form(mapping("orderBy" -> requiredNumber)(identity)(Some(_)))


}