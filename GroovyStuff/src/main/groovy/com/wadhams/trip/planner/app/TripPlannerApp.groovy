package com.wadhams.trip.planner.app

import java.text.NumberFormat
import java.text.SimpleDateFormat

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
		
		SimpleDateFormat sdf = new SimpleDateFormat('yyyyMMdd')
		Date startDate = sdf.parse(tripPlanner.startDate.text())
		//println startDate
		
		List<LocationNights> ldlist = buildLocationNightsList(tripPlanner.locationNights)
		
		report(startDate, ldlist)
	}
	
	def report(Date startingDate, List<LocationNights> ldlist) {
		SimpleDateFormat sdf = new SimpleDateFormat('EEE, MMM d, yyyy')
		
		Date reportDate = startingDate
		ldlist.each {ld ->
			boolean firstNight = true
			ld.nights.times {
				println "${sdf.format(reportDate)}\t${ld.location}${(firstNight) ? "...: ${ld.thingsToDo}" : ''}"
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
