package first

import zhttp.http._
import zio._

import scala.util.Try

object HttpServer {

  def routes: HttpApp[ApiService, Throwable] = Http.collectZIO[Request] { case Method.GET -> !! / "find" / id =>
    ZIO
      .fromTry(Try(id.toInt))
      .flatMap(Api.findById)
      .flatMap {
        case Some(r) => ZIO.succeed(r)
        case None    => ZIO.fail(new Exception(s"User with id $id not found"))
      }
      .map(r => Response.text(s"${r.name} id ${r.age} years old"))
  }
}
