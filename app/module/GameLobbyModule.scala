package modules

import javax.inject.Inject
import akka.actor.Props
import actors.ActorGameRoom
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.{ Configuration, Environment }
import com.google.inject.AbstractModule

class GameLobbyModule(
  environment: Environment,
  config: Configuration
) extends AbstractModule with AkkaGuiceSupport {
  def configure: Unit = {
    bindActor[ActorGameRoom]("first-game-lobby")
  }
}
