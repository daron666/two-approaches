package first

import zio.console.Console
import zio.{Task, ZIO, ZLayer}

import scala.collection.concurrent.TrieMap

object Database {

  trait Service {
    def find(id: Int): Task[Option[Result]]
    def upsert(result: Result): Task[Unit]
  }

  final class DatabaseImpl(console: Console.Service) extends Database.Service {

    private val data = new TrieMap[Int, Result]()

    override def find(id: Int): Task[Option[Result]] = for {
      _      <- console.putStrLn(s"Looking into db for id $id")
      result <- ZIO.effect(data.get(id))
      _      <- console.putStrLn(s"Found in db $result")
    } yield result

    override def upsert(result: Result): Task[Unit] = for {
      _ <- console.putStrLn(s"Userting into db $result")
      _ <- ZIO.effect(data.put(result.id, result))
    } yield ()
  }

  val live: ZLayer[Console, Throwable, DatabaseService] = (for {
    console <- ZIO.access[Console](_.get)
    db       = new DatabaseImpl(console)
    _       <- db.upsert(Result(1, "Alex", 34))

  } yield db).toLayer[Database.Service]

}
