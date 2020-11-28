package com.wadhams.trip.planner.app

import java.text.NumberFormat
import java.text.SimpleDateFormat

import groovy.transform.ToString

class TripPlannerApp {
	
	static main(args) {
		println 'TripPlannerApp started...'
		println ''

		TripPlannerApp app = new TripPlannerApp()
		app.execute()
		
		println ''
		println 'TripPlannerApp ended.'
	}
	
	def execute() {
		//trip_01
//		List<LocationNights> ldlist = planTrip_01()
//		int year = 2020
//		int month = 6	//0-11 month values
//		int day = 12
//		report(ldlist, year, month, day)
		
		//trip_02
		List<LocationNights> ldlist = planTrip_02()
		int year = 2020
		int month = 10	//0-11 month values
		int day = 28
		report(ldlist, year, month, day)
	}
	
	def report(List<LocationNights> ldlist, int year, int month, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat('EEE, MMM d, yyyy')
		
		GregorianCalendar startingCal = new GregorianCalendar(year, month, day)
		Date startingDate = startingCal.time 
//		println sdf.format(startingDate)
//		println ''
		
		Date reportDate = startingCal.time
		ldlist.each {ld ->
			ld.nights.times {
				println "${sdf.format(reportDate)}\t${ld.location}"
				reportDate = reportDate.next()
			}
			
		}
	}
	
	List<LocationNights> planTrip_01() {
		List<LocationNights> ldlist = []
		
		ldlist << new LocationNights(location : 'Deception Bay (Housesitting)', nights : 1)
		ldlist << new LocationNights(location : 'Nanango (RV Park)', nights : 4)
		ldlist << new LocationNights(location : 'Maidenwell (behind Pub)', nights : 4)
		ldlist << new LocationNights(location : 'Chinchilla (Showgrounds)', nights : 1)
		ldlist << new LocationNights(location : 'Miles (Ben and Gillian\'s)', nights : 2)
		ldlist << new LocationNights(location : 'Roma (Gun Club)', nights : 3)
		ldlist << new LocationNights(location : 'Carnavon Gorge (Sandstone Park)', nights : 5)
		ldlist << new LocationNights(location : 'Springsure (Lion\'s free camp)', nights : 1)
		ldlist << new LocationNights(location : 'Sapphire (Blue Gem Tourist Park)', nights : 4)
		ldlist << new LocationNights(location : 'Gemfield area (???)', nights : 3)
		ldlist << new LocationNights(location : 'Blackwater', nights : 2)
		ldlist << new LocationNights(location : 'Bluff (Blackdown Tablelands)', nights : 2)
		ldlist << new LocationNights(location : 'Rockhampton area', nights : 3)
		ldlist << new LocationNights(location : 'Agnes Water', nights : 4)
		ldlist << new LocationNights(location : 'Bargara', nights : 4)
		ldlist << new LocationNights(location : 'Hervey Bay', nights : 5)
		ldlist << new LocationNights(location : 'Beerwah', nights : 1)
		ldlist << new LocationNights(location : 'Car and caravan storage', nights : 1)

		return ldlist		
	}
	
	List<LocationNights> planTrip_02() {
		List<LocationNights> ldlist = []
		
		ldlist << new LocationNights(location : 'Urunga', nights : 2)
		ldlist << new LocationNights(location : 'Moonee Beach', nights : 3)
		ldlist << new LocationNights(location : 'Minnie Waters', nights : 3)
		ldlist << new LocationNights(location : 'Ballina', nights : 2)
		ldlist << new LocationNights(location : 'Murwillumbah', nights : 2)
		ldlist << new LocationNights(location : 'Beenleigh', nights : 1)
		ldlist << new LocationNights(location : 'Car and caravan storage', nights : 1)

		return ldlist
	}
	
}

@ToString
class LocationNights {
	String location
	int nights
}
