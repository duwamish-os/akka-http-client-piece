package com.akka.http.client.futurebase

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akka.http.client.futurebase.FutureBasedHttpClientActor.{GetEvent, GetResponse}
import com.akka.http.client.futurebase.MasterActor.{RequestNotification, ResponseNotification}

import scala.concurrent.Future
import scala.concurrent.duration._

class MasterActor extends Actor with ActorLogging {

  private[this] val actor = context.actorOf(Props.create(classOf[FutureBasedHttpClientActor]))

  private[this] implicit val timeout: Timeout = 5 seconds

  import context.dispatcher
  //can use separate executionContext

  override def receive: Receive = {
    case RequestNotification(endpoint: String) =>
      log.info(s"received request for $endpoint")
      sender ! (actor ? GetEvent(endpoint)).flatMap {
        case GetResponse(data) => Future.successful(data)
        case _ => Future.successful("error")
      }.map(d => ResponseNotification(d))
  }
}

object MasterActor {

  case class RequestNotification(endpoint: String)

  case class ResponseNotification(data: String)

}
