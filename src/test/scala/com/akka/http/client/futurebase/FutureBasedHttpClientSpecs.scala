package com.akka.http.client.futurebase

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.{Failure, Success}

object FutureBasedHttpClientSpecs {

  //get api token from google maps
  private val apiKey = "AIzaSyDN0EnuCsHbq_f3YroiMYvuMkglQG7B9gU"
  private val address = "Seattle"
  private val apiEndpoint = s"https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$apiKey"

  implicit val testActorSystem: ActorSystem = ActorSystem("nlu-actor-system", ConfigFactory.parseString(
    """
      |akka {
      |  loglevel = "DEBUG"
      |}
    """.stripMargin))

  private val httpClient = FutureBasedHttpClient()(testActorSystem)

  private val parallelism = 1
  private val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(new ForkJoinPool(parallelism))

  def main(args: Array[String]): Unit = {

    httpClient.get(apiEndpoint).onComplete({
      case Success(a) => println(a.entity)
      case Failure(e) => e.printStackTrace()
    })(executionContext)

  }

}
