package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models.{calcData, _}

import scala.util.Try

class Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(None, calcForm))
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
      Ok(views.html.index(Option(result.val1 + result.val2), calcForm))
  }

  val requiredNumber: Mapping[Double] = Forms.nonEmptyText
    .verifying("Must be numeric", i => Try(i.toDouble).isSuccess || i.isEmpty)
    .transform[Double](_.toInt, _.toString)

  val calcForm = Form(
    mapping(
      "val1" -> requiredNumber,
      "val2" -> requiredNumber
    )
    (calcData.apply)(calcData.unapply)
  )

}