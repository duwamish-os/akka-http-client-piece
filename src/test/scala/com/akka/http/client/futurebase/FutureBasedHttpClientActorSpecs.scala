package com.akka.http.client.futurebase

import akka.actor.{ActorSystem, Props}
import com.akka.http.client.futurebase.MasterActor.RequestNotification
import com.typesafe.config.ConfigFactory

object FutureBasedHttpClientActorSpecs {


  val actorSystem = ActorSystem.create("nlu-actor-system", ConfigFactory.parseString(
    """
      |akka {
      |  loglevel = "DEBUG"
      |}
    """.stripMargin))

  private val apiKey = "AIzaSyDN0EnuCsHbq_f3YroiMYvuMkglQG7B9gU"
  private val address = "Seattle"
  private val apiEndpoint = s"https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$apiKey"

  private val masterActor = actorSystem.actorOf(Props.create(classOf[MasterActor]))

  def main(args: Array[String]): Unit = {

    masterActor ! RequestNotification(apiEndpoint)

  }
}
