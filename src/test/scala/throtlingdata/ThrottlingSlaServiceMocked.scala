package throtlingdata

import throttlingdata.model.Sla
import throttlingdata.service.SlaService

import scala.concurrent.{ExecutionContext, Future}

class ThrottlingSlaServiceMocked(implicit executionContext: ExecutionContext) extends SlaService {

  def getSlaByToken(token: String): Future[Sla] = {

    println(s"call getSlaByToken for token = $token")

    token match {

      case "one_per_sec" =>
        val name = "username_one_per_sec"
        val count = 1
        println(s"founds Sla for token = $token " +
          s"with name = $name and allowed count per seconds $count")
        Future {
          Sla(name, count)
        }

      case "three_per_sec" =>
        val name = "username_three_per_sec"
        val count = 3
        println(s"founds Sla for token = $token " +
          s"with name = $name and allowed count per seconds $count")
        Future {
          Sla(name, count)
        }

      case "additional_per_sec" =>
        val name = "username_additional_per_sec"
        val count = 30
        println(s"founds Sla for token = $token " +
          s"with name = $name and allowed count per seconds $count")
        Future {
          Sla(name, count)
        }


      case "1" =>
        Future { Sla("username_111", 12) }
      case "11" =>
        Future { Sla("username_111", 10) }
      case "111" =>
        Future { Sla("username_111", 11) }
    }
  }
}
