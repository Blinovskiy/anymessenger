package util

import java.time.{Instant, LocalDate, ZoneId}
import java.util.Date

import akka.event.LogSource
import util.converter.ConvertWrapper

package object implicits {

  implicit def d2ld(d: Date): LocalDate = Instant.ofEpochMilli(d.getTime).atZone(ZoneId.of("UTC")).toLocalDate

  implicit def ld2d(ld: LocalDate): Date = Date.from(ld.atStartOfDay(ZoneId.of("UTC")).toInstant)

  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = o.getClass.getName

    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }

  implicit def v2cw(v: Any): ConvertWrapper[String] = ConvertWrapper(v)

}
