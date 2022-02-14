package first
import zhttp.service.Server
import zio.{ExitCode, URIO}

object Service extends zio.App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    Server.start(8090, HttpServer.routes).exitCode
  }.provideCustomLayer(Cache.live ++ Database.live >>> Api.live).orDie

}
