package com.akka.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer

import scala.concurrent.{ExecutionContextExecutor, Future}

trait HttpClient {
  def get(apiEndpoint: String): Future[HttpResponse]
}

class FutureBasedHttpClient()(implicit actorSystem: ActorSystem, materializer: Materializer) extends HttpClient {

  def get(apiEndpoint: String): Future[HttpResponse] = {

    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = apiEndpoint))

    responseFuture

  }
}

object FutureBasedHttpClient {
  def apply()(implicit actorSystem: ActorSystem, materializer: Materializer): FutureBasedHttpClient =
    new FutureBasedHttpClient()
}
