package throttlingdata.actors

import akka.actor.PoisonPill
import throttlingdata.model.Sla
import throttlingdata.service.SlaService

import scala.concurrent.Future
import scala.util.{Failure, Success}

object SlaRequestActor {
  sealed trait SlaRequestActorRequest
  case class GetSlaDataByToken(token: String) extends SlaRequestActorRequest

  sealed trait SlaRequestActorResponse
  case class SlaDataByToken(token: String) extends SlaRequestActorResponse
}
class SlaRequestActor(slaService: SlaService) extends ImplicitActor {

  import SlaRequestActor._
  import InitializerActor._

  override def receive: Receive = {

    case GetSlaDataByToken(token) =>
      var requester = sender
      val slaFuture: Future[Sla] =
        slaService.getSlaByToken(token)
      logger.info(s"Get sla for token => $token")
      slaFuture.onComplete {
        case Success(sla) =>
          logger.info(s"successfully receive sla data for token = $token")
          requester ! CreateRpsCounter(token, sla)
          self ! PoisonPill
        case Failure(ex) =>
          logger.info(s"error when call sla service for token = $token, message = ${ex.getMessage}")
          requester ! CreateRpsCounterError(token, ex.getMessage)
          self ! PoisonPill
      }
  }
}
