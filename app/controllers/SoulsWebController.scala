package controllers

import play.api.data.Forms._
import play.api.data.Form
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.MessagesApi
import play.api.i18n.I18nSupport
import play.api.i18n.Messages.Implicits._
import javax.inject._
import play.api.libs.json._
import akka.actor.{ ActorSystem, Actor, Props, ActorRef }
import akka.stream.Materializer
import models._

@Singleton
class SoulsWebController @Inject() (
  @Named("first-game-lobby") actorGameState: ActorRef,
  implicit val system: ActorSystem,
  implicit val materializer: Materializer) extends Controller {

  val loginForm = Form(
    mapping (
      "username" -> nonEmptyText
    )(models.User.apply)(models.User.unapply)
  )

  def index = Action {
    println("index")
    Redirect(routes.SoulsWebController.prepareLogin)
  }

  def prepareLogin = Action {
    println("prepare login")
    Ok(views.html.login(loginForm))
  }

  def login = Action { implicit request =>
    println("login")
    loginForm.bindFromRequest.fold(
      errors => BadRequest(views.html.login(errors)),
      user => Redirect(routes.SoulsWebController.play(user))
      )
  }

  def socket(user: models.User) = WebSocket.acceptWithActor[JsValue, JsValue] {
    request => out =>
    println("socket accepted")
      actors.ActorPlayerClient.props(user.username, out, actorGameState)
  }

  def play(user: models.User) = Action {
    println("play!")
    Ok(views.html.gamePlay(user))
  }
}
