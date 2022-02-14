package first

import zio.clock.Clock
import zio.console.Console
import zio.{&, Task, ZIO, ZLayer}

import scala.collection.concurrent.TrieMap

object Cache {

  trait Service {
    def find(id: Int): Task[Option[Result]]

    def put(result: Result): Task[Unit]
  }

  final class ServiceImpl(clock: Clock.Service, console: Console.Service) extends Cache.Service {

    private val data = new TrieMap[Int, Result]()

    override def find(id: Int): Task[Option[Result]] = for {
      now    <- clock.nanoTime
      _      <- console.putStrLn(s"Now is $now, looking for an id $id")
      result <- ZIO.effect(data.get(id))
      _      <- if (result.isEmpty) {
                  console.putStrLn("Cache doesn't have data for id $id")
                } else {
                  console.putStrLn("Found result in cache!")
                }
    } yield result

    override def put(result: Result): Task[Unit] = for {
      _ <- console.putStrLn(s"Storing $result in cache.")
      _ <- ZIO.effect(data.put(result.id, result))
    } yield ()
  }

  def find(id: Int) = ZIO.accessM[CacheService](_.get.find(id))

  val live: ZLayer[Clock & Console, Throwable, CacheService] = (for {
    clock   <- ZIO.access[Clock](_.get)
    console <- ZIO.access[Console](_.get)
  } yield new ServiceImpl(clock, console)).toLayer

}
