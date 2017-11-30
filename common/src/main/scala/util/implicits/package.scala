package util

import java.time.{Instant, LocalDate, ZoneId}
import java.util.Date

package object implicits {

  implicit def d2ld(d: Date): LocalDate = Instant.ofEpochMilli(d.getTime).atZone(ZoneId.of("UTC")).toLocalDate

  implicit def ld2d(ld: LocalDate): Date = Date.from(ld.atStartOfDay(ZoneId.of("UTC")).toInstant)

}
