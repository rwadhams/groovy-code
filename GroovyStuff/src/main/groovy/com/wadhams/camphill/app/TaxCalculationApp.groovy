package com.wadhams.camphill.app

import com.wadhams.camphill.service.TaxCalculatorService
import java.text.NumberFormat
import java.text.SimpleDateFormat

class TaxCalculationApp {
	TaxCalculatorService service = new TaxCalculatorService()
	
	static main(args) {
		println 'TaxCalculationApp started...'
		println ''

		TaxCalculationApp app = new TaxCalculationApp()
		app.execute(new BigDecimal('333000'))
		app.execute(new BigDecimal('150000'))
		app.execute(new BigDecimal('50000'))
		app.execute(new BigDecimal('25000'))
		app.execute(new BigDecimal('12000'))
		
		println 'TaxCalculationApp ended.'
	}
	
	def execute(BigDecimal taxableIncome) {
		NumberFormat cf = NumberFormat.getCurrencyInstance()
		cf.setMaximumFractionDigits(0)
		
		println "Taxable Income: ${cf.format(taxableIncome)}"
		println "Tax Owed......: ${cf.format(service.calculate(taxableIncome))}"
		println ''
	}
}
