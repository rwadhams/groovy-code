package com.wadhams.trip.planner.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import groovy.transform.ToString

class TripPlannerApp {
	
	static main(args) {
		println 'TripPlannerApp started...'
		println ''

		if (args.size() > 0) {
			//args[0] must be a number (nn = 01, 02, 03...99). Filename pattern will be data/TripPlanner<nn>.xml
			String fn = "data/TripPlanner${args[0]}.xml"
			println "Filename: $fn"
			println ''
			
			TripPlannerApp app = new TripPlannerApp()
			app.execute(fn)
		}
		else {
			println 'Missing parameter(s). Application did not run.'
		}
		
		println ''
		println 'TripPlannerApp ended.'
	}
	
	def execute(String filename) {
		def tripPlanner = new XmlSlurper().parse(new File(filename))
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern('yyyyMMdd')
		LocalDate startDate = LocalDate.parse(tripPlanner.startDate.text(), dtf)
		//println startDate
		
		List<LocationNights> ldlist = buildLocationNightsList(tripPlanner.locationNights)
		
		report(startDate, ldlist)
	}
	
	def report(LocalDate startingDate, List<LocationNights> ldlist) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern('EEE, MMM d, yyyy')
		
		LocalDate reportDate = startingDate
		ldlist.each {ld ->
			//println ld
			boolean firstNight = true
			ld.nights.times {
				println "${dtf.format(reportDate)}\t${ld.location}${(firstNight) ? "...: ${ld.nights}: ${ld.thingsToDo}" : ''}"
				firstNight = false
				reportDate = reportDate.next()
			}
			println ''
		}
	}
	
	List<LocationNights> buildLocationNightsList(locationNights) {
		List<LocationNights> ldlist = []
		
		locationNights.each {ln ->
			String location = ln.location
			String nights = ln.nights
			String thingsToDo = ln.thingsToDo
			ldlist << new LocationNights(location : "$location", nights : Integer.parseInt(nights), thingsToDo : "$thingsToDo")
		}
		
		return ldlist
	}
	
}

@ToString
class LocationNights {
	String location
	int nights
	String thingsToDo
}
