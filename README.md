scron [![Build Status](https://travis-ci.org/uniformlyrandom/scron.png)](https://travis-ci.org/uniformlyrandom/scron)
=================

Cron format to time parsing implemented in scala

## Usage

```scala
import com.uniformlyrandom.scron.Scron

val startTime = DateTime.now.getMillis
val endTime = startTime + ( 60 * 60 )

val times = Scron.parse("* * * * * *", startTime, endTime)
// times is now filled with epoch time for every second for the next hour
times.length == 60 * 60
```

## Supported Cron Formatting


### General Format

	*	*	*	*	*	*
	|	|	|	|	|	|day of week(0-6)
	|	|	|	|	|month(1-12)
	|	|	|	|day of month(1-31)
	|	|	|hour(0-23)
	|	|minute(0-59)
	|seconds(0-59)

See [WikiPedia](http://en.wikipedia.org/wiki/Cron) for more information about the format

### Supported Formatting Of Specific Fields

 * `*`  all the options for that field
 * `*/2` starting from the first option, every other option
 * `0` only use the explicitly provided option
 * `2,4,9` use list of values provided, separated by comma

## Installation

Using sbt, add to `build.sbt`

	libraryDependencies ++= Seq(
        	"com.uniformlyrandom" %% "scron" % "0.5"
	)



	
