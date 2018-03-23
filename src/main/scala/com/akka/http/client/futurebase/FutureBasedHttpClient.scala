package com.akka.http.client.futurebase

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait HttpClient {
  def get(apiEndpoint: String): Future[HttpResponse]
}

class FutureBasedHttpClient()(implicit actorSystem: ActorSystem) extends HttpClient {

  //can just do import actorSystem.dispatcher
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  def get(apiEndpoint: String): Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = apiEndpoint))

}

object FutureBasedHttpClient {
  def apply()(implicit actorSystem: ActorSystem): FutureBasedHttpClient = new FutureBasedHttpClient()
}
