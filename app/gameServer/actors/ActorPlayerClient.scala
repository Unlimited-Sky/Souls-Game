package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated, Props}
import play.libs.Akka
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.libs.json._

import gameServer.Messages._

import helpers.CardStack

class ActorPlayerClient(val username: String, val out: ActorRef, val gameLobby: ActorRef)
extends Actor with ActorLogging {

  def receive = {
    case TextMessage(msg) =>
      out ! Json.toJson(Json.obj("message" -> msg))

    case other =>
      log.error(s"[ActorPlayerClient] Not handled: $other")
  }

  override def preStart() {
    println("prestart")
    gameLobby ! PlayerConnect(self, username)
  }

  override def postStop() = {
    println("poststop")
    gameLobby ! PlayerDisconnect(self, username)
  }
}

object ActorPlayerClient {
  def props(username: String, out: ActorRef, gameLobby: ActorRef): Props =
    Props(classOf[ActorPlayerClient], username, out, gameLobby)
}
