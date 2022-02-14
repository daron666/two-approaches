package first

import zio.{&, Task, ZIO, ZLayer}

object Api {

  trait Service {
    def findById(id: Int): Task[Option[Result]]
  }

  final class ApiImpl(cache: Cache.Service, db: Database.Service) extends Api.Service {
    override def findById(id: Int): Task[Option[Result]] = cache
      .find(id)
      .flatMap {
        case Some(r) => ZIO.succeed(Some(r))
        case None    => db.find(id)
      }
      .tapSome { case Some(r) =>
        cache.put(r)
      }

  }

  def findById(id: Int) = ZIO.accessM[ApiService](_.get.findById(id))

  val live: ZLayer[CacheService & DatabaseService, Throwable, ApiService] = (for {
    cache <- ZIO.access[CacheService](_.get)
    db    <- ZIO.access[DatabaseService](_.get)
  } yield new ApiImpl(cache, db)).toLayer

}
