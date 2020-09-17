import java.time.{LocalDateTime, ZoneOffset}

import com.uniformlyrandom.scron.Scron
import org.scalatest.funspec.AnyFunSpec

class MainSpec extends AnyFunSpec {

  describe("sanity tests") {
    it("returns an empty list of options when providing less or more then six items in the string") {
      assertResult(List()) {
        Scron._genOptions("1 1 1 1 1")
      }
      assertResult(List()) {
        Scron._genOptions("1 1 1 1 1 1 1")
      }
    }
  }

  describe("expected options generation") {
    it("allwos only numbers") {
      assertResult(List(List(1), List(1), List(1), List(1), List(1), List(1))) {
        Scron._genOptions("1 1 1 1 1 1")
      }
    }
    it("allows astisks") {
      assertResult(Scron.maxPool) {
        Scron._genOptions("* * * * * *")
      }
    }

    it("allows steps") {
      val expected = List(List(0, 10, 20, 30, 40, 50), List(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55),
        List(0, 3, 6, 9, 12, 15, 18, 21), List(1, 13, 25), List(1, 6, 11), List(0, 2, 4, 6))
      assertResult(expected) {
        Scron._genOptions("*/10 */5 */3 */12 */5 */2")
      }
    }

    it("allows steps with ranges") {
      val expected = List(List(0, 10), List(0, 5, 10), List(0, 3, 6, 9), List(1), List(1, 6), List(0, 2, 4))
      assertResult(expected) {
        Scron._genOptions("0-10/10 0-10/5 0-10/3 1-10/12 1-10/5 0-4/2")
      }
    }

    it("allows comma delimited values") {
      val expected = List(List(1, 2), List(3, 4), List(5, 6), List(7, 8), List(9, 10), List(1, 4))
      assertResult(expected) {
        Scron._genOptions("1,2 3,4 5,6 7,8 9,10 1,4")
      }
    }
  }


  describe("end to end parsing") {

    it("gets all seconds for full hour") {
      val cron = "* * * * * *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC)
      val endTime = startTime + (60 * 60)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(60 * 60) {
        res.length
      }
    }

    it("gets all seconds for full hour overnight") {
      val cron = "* * * * * *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC)
        .withMonth(7)
        .withDayOfMonth(13)
        .withHour(1)
        .withMinute(1)
        .toEpochSecond(ZoneOffset.UTC)

      val endTime = startTime + (60 * 60)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(60 * 60) {
        res.length
      }
    }

    it("gets all seconds for full hour morning ") {
      val cron = "* * * * * *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC)
        .withMonth(7)
        .withDayOfMonth(12)
        .withHour(23)
        .withMinute(12)
        .toEpochSecond(ZoneOffset.UTC)
      val endTime = startTime + (60 * 60)
      val res = Scron.parse(cron, startTime, endTime)

      assertResult(60 * 60) {
        res.length
      }
    }

    it("ignores date range outside of allowed month") {
      val cron = "* * * 1 1 *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).withMonth(2).withDayOfMonth(1).toEpochSecond(ZoneOffset.UTC)
      val endTime = LocalDateTime.now(ZoneOffset.UTC).withMonth(3).withDayOfMonth(1).toEpochSecond(ZoneOffset.UTC)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(0) {
        res.length
      }
    }

    it("returns one entry per hour") {
      val cron = "0 0 * * * *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).withMonth(1).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val endTime = LocalDateTime.now(ZoneOffset.UTC).withMonth(1).withDayOfMonth(2).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(24) {
        res.length
      }
    }

    it("finds first day of every month for 12 years") {
      val cron = "0 0 0 1 * *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2000).withMonth(1).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val endTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2012).withMonth(1).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(12 * 12) {
        res.length
      }
    }

    it("finds first month in 12 years") {
      val cron = "0 0 0 1 1 *"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2000).withMonth(1).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val endTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2012).withMonth(1).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(12) {
        res.length
      }
    }

    it("finds five first DOW in a specific month") {
      val cron = "0 0 0 * * 0"
      val startTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2012).withMonth(9).withDayOfMonth(1).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val endTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2012).withMonth(9).withDayOfMonth(30).withHour(1).toEpochSecond(ZoneOffset.UTC)
      val res = Scron.parse(cron, startTime, endTime)
      assertResult(5) {
        res.length
      }
    }

    it("can generate cron formatted string for given time") {
      val givenTime = LocalDateTime.now(ZoneOffset.UTC).withYear(2012).withMonth(10).withDayOfMonth(1).withHour(12)
        .withMinute(11).withSecond(10).toEpochSecond(ZoneOffset.UTC)
      val expectedCron = "10 11 12 1 10 *"
      assertResult(expectedCron) {
        Scron.timeToCron(givenTime)
      }
    }

  }


}
