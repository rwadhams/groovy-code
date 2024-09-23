package com.wadhams.camphill.app

import java.text.NumberFormat
import com.wadhams.camphill.service.TaxCalculatorService

class CampHillTaxEstimatorApp {
	TaxCalculatorService service = new TaxCalculatorService()
	
	static main(args) {
		println 'CampHillTaxEstimatorApp started...'
		println ''

		CampHillTaxEstimatorApp app = new CampHillTaxEstimatorApp()
		app.execute(750000, 10000, 12, 140000, 5000, 6)
		
		println 'CampHillTaxEstimatorApp ended.'
	}
	
	def execute(int salePrice2018, int salePriceReductionAmount, int salePriceReductionNumber, int salePrice1995, int salePriceIncreaseAmount, int salePriceIncreaseNumber) {
		NumberFormat cf = NumberFormat.getCurrencyInstance()
		cf.setMaximumFractionDigits(0)
		
		BigInteger sp2018 = new BigInteger(salePrice2018)
		salePriceReductionNumber.times {
			BigInteger sp1995 = new BigInteger(salePrice1995)
			println "2018(${cf.format(sp2018)}):"
			salePriceIncreaseNumber.times {
				BigInteger taxableIncome = sp2018.subtract(sp1995) / 2	//50%
				println "\t1995(${cf.format(sp1995)}), Taxable Income(${cf.format(taxableIncome)}), Tax Owed(${cf.format(service.calculate(taxableIncome, '2019-20'))})"
				sp1995 = sp1995.add(salePriceIncreaseAmount)
			}
			println ''
			sp2018 = sp2018.subtract(salePriceReductionAmount)
		}
	}
}
