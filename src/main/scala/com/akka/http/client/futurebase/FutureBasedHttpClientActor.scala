package com.akka.http.client.futurebase

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString

class FutureBasedHttpClientActor extends Actor with ActorLogging {

  import FutureBasedHttpClientActor._
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  def receive = {

    case GetEvent(apiEndpoint) =>
      http.singleRequest(HttpRequest(uri = apiEndpoint))
        .flatMap(_.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String)).map(d => GetResponse(d))

    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response, body: " + body.utf8String)
      }

    case resp@HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()

  }

}

object FutureBasedHttpClientActor {

  case class GetEvent(apiEndpoint: String)

  case class GetResponse(response: String)

}
