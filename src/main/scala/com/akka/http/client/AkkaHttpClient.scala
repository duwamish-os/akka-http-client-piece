package com.akka.http.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object AkkaHttpClient {

  private val apiKey = ""
  private val address = "Seattle"
  private val apiEndpoint = s"https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$apiKey"

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val httpClient = FutureBasedHttpClient()

  private val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(new ForkJoinPool(1))

  def main(args: Array[String]): Unit = {

    httpClient.get(apiEndpoint).onComplete({
      case Success(a) => println(a)
      case Failure(e) => e.printStackTrace()
    })(executionContext)

  }

}
