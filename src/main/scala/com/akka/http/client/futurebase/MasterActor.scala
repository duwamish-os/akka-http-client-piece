package com.akka.http.client.futurebase

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akka.http.client.futurebase.FutureBasedHttpClientActor.{GetEvent, GetResponse}

import scala.concurrent.Future
import scala.concurrent.duration._

class MasterActor extends Actor with ActorLogging {

  import MasterActor._

  private[this] val actor = context.actorOf(Props.create(classOf[FutureBasedHttpClientActor]))

  private[this] implicit val timeout: Timeout = 5 seconds

  private[this] var state = 0

  //can use separate executionContext
  import context.dispatcher

  override def preStart(): Unit = {
    log.info(s"starting $state")
    state = state + 1
  }

  override def receive: Receive = {
    case RequestNotification(endpoint: String) =>
      log.info(s"received request for $endpoint")

      sender ! (actor ? GetEvent(endpoint)).flatMap {
        case GetResponse(data) => Future.successful(data)
        case _ => Future.successful("error")
      }.map(d => ResponseNotification(d))

    case KillEvent =>
      sender ! (throw new Exception("kill the system"))

    case Heartbeat =>
      log.info("heartbeat")
      sender() ! Green
  }
}

object MasterActor {

  case class RequestNotification(endpoint: String)

  case class ResponseNotification(data: String)

  case object KillEvent

  case object Heartbeat

  case object Green

}
