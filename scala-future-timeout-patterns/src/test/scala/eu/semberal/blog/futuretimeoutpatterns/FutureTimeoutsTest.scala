package eu.semberal.blog.futuretimeoutpatterns

import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.{Executors, TimeoutException}

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future, Promise}

class FutureTimeoutsTest
  extends TestKit(ActorSystem())
  with FunSuiteLike
  with Matchers
  with ScalaFutures
  with BeforeAndAfterAll {

  override protected def afterAll(): Unit = system.shutdown()

  def longRunningOperation(): Int = { Thread.sleep(1000); 1 }

  test("A Future should be awaitable using Await.result()") {
    val f = Future { longRunningOperation() }
    Await.result(f, 1100.milliseconds) should be(1)
  }

  test("A Future should continue running even after several Await.result() timeouts") {
    val f = Future { longRunningOperation() }
    intercept[TimeoutException] { Await.result(f, 300.milliseconds) }
    intercept[TimeoutException] { Await.result(f, 500.milliseconds) }
    whenReady(f, timeout(300.milliseconds)) { x => x should be(1) }
  }

  val e = new TimeoutException("Future timeout")

  test("firstCompletedOf() should timeout when the first completed (blocking) Future throws an exception") {
    val f1 = Future { longRunningOperation() }
    val timeoutFuture = Future { Thread.sleep(500); throw e }
    val f = Future.firstCompletedOf(f1 :: timeoutFuture :: Nil)
    whenReady(f.failed, timeout(600.milliseconds)) { ex => ex shouldBe an[TimeoutException] }
  }


  test("firstCompletedOf() should timeout when the first completed (non-blocking) Future throws an exception") {
    val f1 = Future { longRunningOperation() }
    val timeoutFuture = akka.pattern.after(500.milliseconds, using = system.scheduler) { Future.failed(e) }
    val f = Future.firstCompletedOf(f1 :: timeoutFuture :: Nil)
    whenReady(f.failed, timeout(600.milliseconds)) { ex => ex shouldBe an[TimeoutException] }
  }

  test("SchedulerExecutorService job should be able to complete a promise") {
    val scheduler = Executors.newScheduledThreadPool(1)
    val p = Promise[Int]()
    p tryCompleteWith Future { longRunningOperation() }
    val action = new Runnable {
      override def run(): Unit = p tryFailure e
    }
    scheduler.schedule(action, 500, MILLISECONDS)
    whenReady(p.future.failed, timeout(600.milliseconds)) { ex => ex shouldBe an[TimeoutException] }
  }

  test("Actor ask is constrained by an implicit timeout") {
    import akka.pattern.ask
    val actor = system.actorOf(Props(new Actor { override def receive: Receive = PartialFunction.empty } ))
    implicit val _timeout = Timeout(500.milliseconds)
    val f = actor ? "PING"
    whenReady(f.failed, timeout(600.milliseconds)) { ex => ex shouldBe an[TimeoutException] }
  }

  test("Future should check the termination flag and terminate itself") {
    @volatile var terminated = false
    val f = Future { while(!terminated) println("Running"); 1 }
    /* In 1 second, a newly created future will set the terminated flag to true */
    akka.pattern.after(1.second, using = system.scheduler) { Future { terminated = true } }
    whenReady(f, timeout(1100.milliseconds)) { x => x should be(1) }
  }
}
