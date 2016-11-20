package models

import java.util.Base64
import play.api.mvc.PathBindable
import play.api.libs.json._
import java.security.MessageDigest

case class User(username: String) {
  def toJson:JsValue = Json.obj(
    "username" -> username
  )
}

object User {
    def apply(json: JsValue):User = {
    val username = (json \ "username").as[String]
    val u = User(username)
    u
  }

  implicit def pathBinder(implicit stringBinder: PathBindable[String]) = new PathBindable[User] {
    override def bind(key: String, value: String): Either[String, User] = {
      try {
        Right(User(Json.parse(Base64.getDecoder.decode(value))))
      } catch {
        case e:Exception => Left("pathBinding failed")
      }
    }

    override def unbind(key: String, user: User): String = {
      Base64.getEncoder.encodeToString(Json.stringify(user.toJson).getBytes())
    }
  }
}
