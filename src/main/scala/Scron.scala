package com.uniformlyrandom.scron

import org.joda.time.DateTime
import org.joda.time.Days

import scala.collection.mutable.ArrayBuffer

object Scron {

	/*
	Cron Format
	-----------
	*	*	*	*	*	*
	|	|	|	|	|	|day of week(0-6 0:Sunday 6:Saturday)
	|	|	|	|	|month(1-12)
	|	|	|	|day of month(1-31)
	|	|	|hour(0-23)
	|	|minute(0-59)
	|seconds(0-59)

	*	- all the options for that field
	* /2- starting from the first option, every other option
	0	- only use the explicitly provided option
	2,4 - use list of values provided, separated by comma
	*/

	val maxPool = List((0 to 59),(0 to 59),(0 to 23),(1 to 31),(1 to 12),(0 to 6))

	val dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy")

	val dowMap = Map( 7 -> 0, 1 -> 1, 2 -> 2, 3 -> 3, 4 -> 4, 5 -> 5, 6 -> 6 )

	/**
	 * @param cron String - cron string
	 * @param startTimeUnix Long - time in seconds since epoch
	 * @param endTimeUnix Long - time in seconds since epoch
	 * @return Seq[Long] - list of specific times in seconds, occuring between start time and end time 
	* */
	def parse(cron: String, startTimeUnix: Long, endTimeUnix:Long) : Seq[Long] = {
		

		if (endTimeUnix < startTimeUnix) {
			println(s"end time is ahread of start time; end time:",endTimeUnix, " start time:",startTimeUnix)
			return List()
		}

		val options = _genOptions(cron)

		val start = new DateTime(startTimeUnix * 1000)
		val end = new DateTime(endTimeUnix * 1000)
		val epoch = new DateTime(0)
		val daysBetweenStartEnd = Days.daysBetween(start.toDateMidnight, end.toDateMidnight).getDays
		val daysSinceEpochStart = Days.daysBetween(epoch, start.toDateMidnight).getDays
		val daysSinceEpochEnd = Days.daysBetween(epoch, end.toDateMidnight).getDays

		val List(seconds, minutes, hours, days, months, dows) = options
		
		for {	iDay <- 0 to daysBetweenStartEnd;
				zDay = start.plusDays(iDay)
					if days.contains(zDay.getDayOfMonth);
					if months.contains(zDay.getMonthOfYear);
					if dows.contains(dowMap(zDay.getDayOfWeek));
				isStartDay = ( Days.daysBetween(epoch, zDay.toDateMidnight).getDays == daysSinceEpochStart );
				isEndDay = ( Days.daysBetween(epoch, zDay.toDateMidnight).getDays == daysSinceEpochEnd );
				hour <- hours
					if !isStartDay || hour >= start.getHourOfDay;
					if !isEndDay || hour <= end.getHourOfDay;
				isStartHour = ( isStartDay && hour == start.getHourOfDay );
				isEndHour = ( isEndDay && hour == end.getHourOfDay );
				minute <- minutes
					if !isStartHour || minute >= start.getMinuteOfHour ;
					if !isEndHour || minute <= end.getMinuteOfHour ;
				isStartMinute = ( isStartHour && minute == start.getMinuteOfHour );
				isEndMinute = ( isEndHour && minute == end.getMinuteOfHour );
				second <- seconds
					if !isStartMinute || second >= start.getSecondOfMinute;
					if !isEndMinute || second < end.getSecondOfMinute
		} yield ( zDay.withHourOfDay(hour).withMinuteOfHour(minute).withSecondOfMinute(second).getMillis / 1000 )
			
	}

	def timeToCron(unixTime: Long) : String = {
		val inputTime = new DateTime(unixTime * 1000)

		s"${inputTime.secondOfMinute.get} ${inputTime.minuteOfHour.get} " +
			s"${inputTime.hourOfDay.get} ${inputTime.dayOfMonth.get} ${inputTime.monthOfYear.get} *"
	}


	def _genOptions(input:String) : List[List[Int]] = {
		if (input.split(" ").length != 6)
			List()
		else 
			maxPool.foldLeft(List[List[Int]]()) {(res, pool)=> {
				res ::: ( input.split(" ")(res.length) match {
					case rangeWithSteps(range,steps) => List[List[Int]]( getOptionsForRange(range,pool,steps.toInt) )
					case justRange(range) => List[List[Int]]( getOptionsForRange(range,pool) )
					case _ => List[List[Int]](List())
				} )
			}}
	}

	private val day = 24*60*60*1000
	private def daysSinceEpoch(date: java.util.Date) = scala.math.floor(date.getTime / day)
	private def getNextDay(date: java.util.Date) = {
		val cal = java.util.Calendar.getInstance
		cal.setTime(date)
		cal.add(java.util.Calendar.DATE,1)
		cal.getTime()
	}

	private val rangeWithSteps = "^([0-9*,-]*)/([0-9*]*)$".r
	private val justRange = "^([0-9*,-]*)$".r
	private val singleValue = "^([0-9]+)$".r
	private val rangeStartEnd = "^([0-9]+)-([0-9]+)$".r
	private val rangeDelimited = "^([0-9,]+)$".r

	private def getOptionsForRange(rangePattern:String, pool: Range, by:Int = 1): List[Int] =
		(rangePattern match {
			case "*" => pool.by(by)
			case singleValue(value) => pool intersect List(value.toInt)
			case rangeStartEnd(start,end) => (start.toInt to end.toInt).by(by).toList.intersect(pool)
			case rangeDelimited(_) => rangePattern.split(",").toList.map(_.toInt).intersect(pool)
			case _ => List()
		}).toList
	


}
