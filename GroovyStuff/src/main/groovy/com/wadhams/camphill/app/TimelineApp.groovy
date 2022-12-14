package com.wadhams.camphill.app

import java.text.NumberFormat
import java.text.SimpleDateFormat

class TimelineApp {
	
	static main(args) {
		println 'TimelineApp started...'
		println ''

		TimelineApp app = new TimelineApp()
		app.execute()
		
		println 'TimelineApp ended.'
	}
	
	def execute() {
		SimpleDateFormat sdf = new SimpleDateFormat('dd MMM yyyy')
		NumberFormat cf = NumberFormat.getCurrencyInstance()
		cf.setMaximumFractionDigits(0)
		
		Calendar purchaseDate = new GregorianCalendar(1992, 8, 24)
		println "PurchaseDate\t\t\t\t: ${sdf.format(purchaseDate.getTime())}"
		BigInteger purchasePrice = new BigInteger('115000')
		println "PurchasePrice\t\t\t\t: ${cf.format(purchasePrice)}"
		println ''
		
		Calendar firstRentalDate = new GregorianCalendar(1995, 6, 1)
		println "Start of rental date\t\t\t: ${sdf.format(firstRentalDate.getTime())}"
		int yearsDiff01 = firstRentalDate.get(Calendar.YEAR) - purchaseDate.get(Calendar.YEAR)
		int monthsDiff01 = firstRentalDate.get(Calendar.MONTH) - purchaseDate.get(Calendar.MONTH)
		int months01 = yearsDiff01*12 + monthsDiff01
		println "Owner occupied(1) for\t\t\t: $months01 months"
		BigInteger estimatedValueAtRental = new BigInteger('140000')
		println "Estimated value at start of rental\t: ${cf.format(estimatedValueAtRental)} ????????????????"
		println "\tEstimated gain as Owner occupied(1)\t: ${cf.format(estimatedValueAtRental-purchasePrice)}"
		println ''
		
		Calendar lastRentalDate = new GregorianCalendar(2018, 11, 1)
		println "End of rental date\t\t\t: ${sdf.format(lastRentalDate.getTime())}"
		int yearsDiff02 = lastRentalDate.get(Calendar.YEAR) - firstRentalDate.get(Calendar.YEAR)
		int monthsDiff02 = lastRentalDate.get(Calendar.MONTH) - firstRentalDate.get(Calendar.MONTH)
		int months02 = yearsDiff02*12 + monthsDiff02
		println "Rented for\t\t\t\t: $months02 months"
		BigInteger estimatedValueAfterRental = new BigInteger('750000')
		println "Estimated value at end of rental\t: ${cf.format(estimatedValueAfterRental)} ????????????????"
		println "\tEstimated gain as Landlords\t\t: ${cf.format(estimatedValueAfterRental-estimatedValueAtRental)}"
		println ''

		Calendar ownerOccupiedDate = new GregorianCalendar(2019, 8, 1)
		println "Owner occupied date\t\t\t: ${sdf.format(ownerOccupiedDate.getTime())}"
		int yearsDiff03 = ownerOccupiedDate.get(Calendar.YEAR) - lastRentalDate.get(Calendar.YEAR)
		int monthsDiff03 = ownerOccupiedDate.get(Calendar.MONTH) - lastRentalDate.get(Calendar.MONTH)
		int months03 = yearsDiff03*12 + monthsDiff03
		println "Property was unoccupied/unrented for\t: $months03 months"

		Calendar saleDate = new GregorianCalendar(2020, 8, 24)
		int yearsDiff04 = saleDate.get(Calendar.YEAR) - ownerOccupiedDate.get(Calendar.YEAR)
		int monthsDiff04 = saleDate.get(Calendar.MONTH) - ownerOccupiedDate.get(Calendar.MONTH)
		int months04 = yearsDiff04*12 + monthsDiff04 + 1
		println "Owner occupied(2) for\t\t\t: $months04 months"
		println ''
		BigInteger estimatedValueSale = new BigInteger('800000')
		println "Estimated value at sale\t\t\t: ${cf.format(estimatedValueSale)} ????????????????"
		println "\tEstimated gain as Owner occupied(2)\t: ${cf.format(estimatedValueSale-estimatedValueAfterRental)}"
		println ''

		
		
		//check total elapsed months
//		int yearsDiff09 = saleDate.get(Calendar.YEAR) - purchaseDate.get(Calendar.YEAR)
//		int monthsDiff09 = saleDate.get(Calendar.MONTH) - purchaseDate.get(Calendar.MONTH)
//		int months09 = yearsDiff09*12 + monthsDiff09 + 1
//		println "Total elapsed months (calculated)\t: $months09 months"
//		println "Total elapsed months (summed up)\t: ${months01+months02+months03+months04} months"
//		println ''
	}
}
