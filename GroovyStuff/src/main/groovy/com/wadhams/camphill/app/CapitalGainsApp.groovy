package com.wadhams.camphill.app

import java.text.NumberFormat
import java.text.SimpleDateFormat

import com.wadhams.camphill.service.TaxCalculatorService

class CapitalGainsApp {
	TaxCalculatorService service = new TaxCalculatorService()
	
	SimpleDateFormat sdf = new SimpleDateFormat('dd MMM yyyy')
	NumberFormat cf = NumberFormat.getCurrencyInstance()
	
	BigInteger purchasePrice
	BigInteger salesPrice
	BigInteger estimatedValueAtRental
	BigInteger estimatedValueAfterRental
	
	int totalOwnershipMonths
	int nonRentalMonths
	int rentalMonths
	
	def CapitalGainsApp() {
		cf.setMaximumFractionDigits(0)
	}
		
	static main(args) {
		println 'CapitalGainsApp started...'
		println ''

		CapitalGainsApp app = new CapitalGainsApp()
		app.timeline()
		app.discountIndexCalculation()
		app.discountOnlyCalculation()
		
		println 'CapitalGainsApp ended.'
	}
	
	def timeline() {
		println 'TIMELINE FOR 9 GRANT STREET, CAMP HILL QLD 4152'
		println '==============================================='
		
		Calendar purchaseDate = new GregorianCalendar(1992, 8, 24)
		purchasePrice = new BigInteger('115000')
		println "Purchase date/price\t\t: ${sdf.format(purchaseDate.getTime())}\t${cf.format(purchasePrice)}"
		Calendar salesDate = new GregorianCalendar(2020, 8, 24)
		salesPrice = new BigInteger('800000')
		println "Sales date/price\t\t: ${sdf.format(salesDate.getTime())}\t${cf.format(salesPrice)}"
		println ''

		println "Owner occupied(1) start date\t: ${sdf.format(purchaseDate.getTime())}"
		
		Calendar ownerOccupiedEndDate = new GregorianCalendar(1995, 6, 1)
		println "Owner occupied(1) end date\t: ${sdf.format(ownerOccupiedEndDate.getTime())}"
		
		estimatedValueAtRental = new BigInteger('140000')	//140000 165000
		println "Est. value at start of rental\t: ${cf.format(estimatedValueAtRental)} ????????????????"

		Calendar unoccupiedStartDate = new GregorianCalendar(2018, 11, 1)
		println "Unoccupied start date\t\t: ${sdf.format(unoccupiedStartDate.getTime())}"
		
		estimatedValueAfterRental = new BigInteger('740000')	//750000 740000 710000 680000 650000
		println "Est. value at end of rental\t: ${cf.format(estimatedValueAfterRental)} ????????????????"
		
		Calendar ownerOccupiedDate = new GregorianCalendar(2019, 8, 1)
		println "Owner occupied(2) start date\t: ${sdf.format(ownerOccupiedDate.getTime())}"

		println "Owner occupied(2) end date\t: ${sdf.format(salesDate.getTime())}"
		println ''
		
		int yearsDiff01 = ownerOccupiedEndDate.get(Calendar.YEAR) - purchaseDate.get(Calendar.YEAR)
		int monthsDiff01 = ownerOccupiedEndDate.get(Calendar.MONTH) - purchaseDate.get(Calendar.MONTH)
		int months01 = yearsDiff01*12 + monthsDiff01
		println "Owner occupied(1) for\t\t: $months01 months"
		int yearsDiff03 = ownerOccupiedDate.get(Calendar.YEAR) - unoccupiedStartDate.get(Calendar.YEAR)
		int monthsDiff03 = ownerOccupiedDate.get(Calendar.MONTH) - unoccupiedStartDate.get(Calendar.MONTH)
		int months03 = yearsDiff03*12 + monthsDiff03
		println "Property was unoccupied for\t: $months03 months"
		int yearsDiff04 = salesDate.get(Calendar.YEAR) - ownerOccupiedDate.get(Calendar.YEAR)
		int monthsDiff04 = salesDate.get(Calendar.MONTH) - ownerOccupiedDate.get(Calendar.MONTH)
		int months04 = yearsDiff04*12 + monthsDiff04 + 1
		println "Owner occupied(2) for\t\t: $months04 months"
		println ''
		
		//total ownership months
		int yearsDiff09 = salesDate.get(Calendar.YEAR) - purchaseDate.get(Calendar.YEAR)
		int monthsDiff09 = salesDate.get(Calendar.MONTH) - purchaseDate.get(Calendar.MONTH)
		totalOwnershipMonths = yearsDiff09*12 + monthsDiff09 + 1
		println "Total ownership months\t\t: $totalOwnershipMonths months"
		//total non-rental months
		nonRentalMonths = months01+months03+months04
		println "Total non-rental months\t\t: ${nonRentalMonths} months"
		rentalMonths = totalOwnershipMonths - nonRentalMonths
		println "Total rental months\t\t: ${rentalMonths} months"
	}
	
	def discountIndexCalculation() {
		println ''
		println ''
		println 'CAPITAL GAINS CALCULATIONS USING 50% DISCOUNT AND TIME INDEXING'
		println '==============================================================='
		println "Time index calculation\t\t: months rented / months owned"
		println "Time index calculation\t\t: $rentalMonths / $totalOwnershipMonths"
		BigDecimal timeIndex = rentalMonths / totalOwnershipMonths
		println "Time index value\t\t: ${timeIndex}"
		println ''
		println "Base Capital Gains calculation\t: salesPrice - purchasePrice"
		println "Base Capital Gains calculation\t: $salesPrice - $purchasePrice"
		BigInteger baseCapitalGains = salesPrice - purchasePrice
		println "Base Capital Gains\t\t: ${cf.format(baseCapitalGains)}"
		println ''
		println "Discounted Capital Gains\t: 50% of Base Capital Gains"
		println "Discounted Capital Gains\t: $baseCapitalGains / 2"
		BigInteger discountedCapitalGains = baseCapitalGains / 2
		println "Discounted Capital Gains\t: ${cf.format(discountedCapitalGains)}"
		println ''
		println "Time Indexed Capital Gains\t: Discounted Capital Gains * Time Index Value"
		println "Time Indexed Capital Gains\t: $discountedCapitalGains * $timeIndex"
		BigInteger timeIndexedCapitalGains = discountedCapitalGains * timeIndex
		println "Time Indexed Capital Gains\t: ${cf.format(timeIndexedCapitalGains)}"
		println ''
		println "Taxable Income (2020)\t\t: ${cf.format(timeIndexedCapitalGains)}"
		println ''
		println "Estimated Tax  (2020)\t\t: ${cf.format(service.calculate(timeIndexedCapitalGains, '2019-20'))}"
	}
	
	def discountOnlyCalculation() {
		println ''
		println ''
		println 'CAPITAL GAINS CALCULATIONS USING 50% DISCOUNT ONLY'
		println '=================================================='
		println "Base Capital Gains calculation\t: estimatedValueAfterRental - estimatedValueAtRental"
		println "Base Capital Gains calculation\t: $estimatedValueAfterRental - $estimatedValueAtRental"
		BigInteger baseCapitalGains = estimatedValueAfterRental - estimatedValueAtRental
		println "Base Capital Gains\t\t: ${cf.format(baseCapitalGains)}"
		println ''
		println "Discounted Capital Gains\t: 50% of Base Capital Gains"
		println "Discounted Capital Gains\t: $baseCapitalGains / 2"
		BigInteger discountedCapitalGains = baseCapitalGains / 2
		println "Discounted Capital Gains\t: ${cf.format(discountedCapitalGains)}"
		println ''
		println "Taxable Income (2020)\t\t: ${cf.format(discountedCapitalGains)}"
		println ''
		println "Estimated Tax  (2020)\t\t: ${cf.format(service.calculate(discountedCapitalGains, '2019-20'))}"
	}
}
