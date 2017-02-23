package streamadapter

import _root_.play.api.libs.iteratee.Enumeratee
import _root_.play.api.libs.iteratee.Enumerator
import _root_.play.api.libs.iteratee.Iteratee
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import streamadapter.play.iterGenToPlayEnumerator

class PlayFromIterGenSpec extends FromIterGenSpec[Enumerator] {

  def adapterName = "iterGenToPlayEnumerator"

  def adaptPublisher = iterGenToPlayEnumerator.adaptPublisher

  def toIterator: Enumerator[Int] => Iterator[Int] = { (enumerator) =>
    val iteratee = Iteratee.fold[Int, Seq[Int]](Seq()) { case (acc, int) =>
      acc :+ int
    }
    val f = enumerator.run(iteratee)
    Await.result(f, Duration.Inf).toIterator
  }

  def takeThreeOpt = Some(_.through(Enumeratee.take(3)))

}