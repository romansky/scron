import com.uniformlyrandom.scron.Scron
import org.scalatest.FunSpec

import org.joda.time.DateTime

class MainSpec extends FunSpec {

	describe("sanity tests"){
		it("returns an empty list of options when providing less or more then six items in the string"){
			expectResult(List()){ Scron._genOptions("1 1 1 1 1") }
			expectResult(List()){ Scron._genOptions("1 1 1 1 1 1 1") }
		}
	}

	describe("expected options generation"){
		it("allwos only numbers"){
			expectResult(List(List(1),List(1),List(1),List(1),List(1),List(1))){ Scron._genOptions("1 1 1 1 1 1") } }
		it("allows astisks"){ 
			expectResult(Scron.maxPool) { Scron._genOptions("* * * * * *") } }

		it("allows steps"){
			val expected = List(List(0,10,20,30,40,50),List(0,5,10,15,20,25,30,35,40,45,50,55),
				List(0,3,6,9,12,15,18,21),List(1,13,25),List(1,6,11),List(0,2,4,6))
			expectResult(expected) { Scron._genOptions("*/10 */5 */3 */12 */5 */2") }
		}

		it ("allows steps with ranges"){
			val expected = List(List(0,10),List(0,5,10),List(0,3,6,9),List(1),List(1,6),List(0,2,4))
			expectResult(expected) { Scron._genOptions("0-10/10 0-10/5 0-10/3 1-10/12 1-10/5 0-4/2") }
		}

		it("allows comma delimited values"){
			val expected = List( List(1,2),List(3,4),List(5,6),List(7,8),List(9,10),List(1,4) )
			expectResult(expected) { Scron._genOptions("1,2 3,4 5,6 7,8 9,10 1,4") }
		}
	}


	describe("end to end parsing"){

		it("gets all seconds for full hour"){
			val cron = "* * * * * *"
			val startTime = DateTime.now.getMillis / 1000
			val endTime = startTime + ( 60 * 60 )
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 60 * 60 ) { res.length }
		}

		it("gets all seconds for full hour overnight"){
			val cron = "* * * * * *"
			val startTime = DateTime.now.withMonthOfYear(7).withDayOfMonth(13).withHourOfDay(1).withMinuteOfHour(1).getMillis / 1000
			val endTime = startTime + ( 60 * 60 )
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 60 * 60 ) { res.length }
		}

		it("gets all seconds for full hour morning "){
			val cron = "* * * * * *"
			val startTime = DateTime.now.withMonthOfYear(7).withDayOfMonth(12).withHourOfDay(23).withMinuteOfHour(12).getMillis / 1000
			val endTime = startTime + ( 60 * 60 )
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 60 * 60 ) { res.length }
		}

		it("ignores date range outside of allowed month"){
			val cron = "* * * 1 1 *"
			val startTime = DateTime.now.withMonthOfYear(2).withDayOfMonth(1).getMillis / 1000
			val endTime = DateTime.now.withMonthOfYear(3).withDayOfMonth(1).getMillis / 1000
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 0 ) { res.length }
		}

		it("returns one entry per hour"){
			val cron = "0 0 * * * *"
			val startTime = DateTime.now.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val endTime = DateTime.now.withMonthOfYear(1).withDayOfMonth(2).withHourOfDay(1).getMillis / 1000
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 24 ) { res.length }
		}

		it("finds first day of every month for 12 years"){
			val cron = "0 0 0 1 * *"
			val startTime = DateTime.now.withYear(2000).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val endTime = DateTime.now.withYear(2012).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 12 * 12 ) { res.length }
		}

		it("finds first month in 12 years"){
			val cron = "0 0 0 1 1 *"
			val startTime = DateTime.now.withYear(2000).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val endTime = DateTime.now.withYear(2012).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 12 ) { res.length }
		}

		it("finds five first DOW in a specific month"){
			val cron = "0 0 0 * * 0"
			val startTime = DateTime.now.withYear(2012).withMonthOfYear(9).withDayOfMonth(1).withHourOfDay(1).getMillis / 1000
			val endTime = DateTime.now.withYear(2012).withMonthOfYear(9).withDayOfMonth(30).withHourOfDay(1).getMillis / 1000
			val res = Scron.parse(cron, startTime, endTime)
			expectResult( 5 ) { res.length }
		}

		it("can generate cron formatted string for given time"){
			val givenTime = DateTime.now.withYear(2012).withMonthOfYear(10).withDayOfMonth(1).withHourOfDay(12)
								.withMinuteOfHour(11).withSecondOfMinute(10).getMillis / 1000
			val expectedCron = "10 11 12 1 10 *"
			expectResult(expectedCron) { Scron.timeToCron(givenTime) }
		}

	}




}
