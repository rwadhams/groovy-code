package com.wadhams.camphill.service

import java.text.NumberFormat
import java.text.SimpleDateFormat

class TaxCalculatorService {
	BigDecimal calculate(BigDecimal taxableIncome, String taxYear) {
		BigDecimal taxOwed
		if (taxYear == '2019-20') {
			taxOwed = calculate2019(taxableIncome)
		}
		else if (taxYear == '2020-21') {
			taxOwed = calculate2020(taxableIncome)
		}
		else {
			throw new RuntimeException("Invalid taxYear string: $taxYear")
		}
		
		return taxOwed
	}
	
	BigDecimal calculate2019(BigDecimal taxableIncome) {
		BigDecimal level5Thresehold = new BigDecimal('180000')
		BigDecimal level5Base = new BigDecimal('54097')
		BigDecimal level5Rate = new BigDecimal('0.45')
		
		BigDecimal level4Thresehold = new BigDecimal('90000')
		BigDecimal level4Base = new BigDecimal('20797')
		BigDecimal level4Rate = new BigDecimal('0.37')
		
		BigDecimal level3Thresehold = new BigDecimal('37000')
		BigDecimal level3Base = new BigDecimal('3572')
		BigDecimal level3Rate = new BigDecimal('0.325')
		
		BigDecimal level2Thresehold = new BigDecimal('18200')
		BigDecimal level2Rate = new BigDecimal('0.19')
		
		BigDecimal taxOwed
		
		if (taxableIncome > level5Thresehold) {
			taxOwed = level5Base.add(taxableIncome.subtract(level5Thresehold).multiply(level5Rate))
		}
		else if (taxableIncome > level4Thresehold) {
			taxOwed = level4Base.add(taxableIncome.subtract(level4Thresehold).multiply(level4Rate))
		}
		else if (taxableIncome > level3Thresehold) {
			taxOwed = level3Base.add(taxableIncome.subtract(level3Thresehold).multiply(level3Rate))
		}
		else if (taxableIncome > level2Thresehold) {
			taxOwed = taxableIncome.subtract(level2Thresehold).multiply(level2Rate)
		}
		else {
			taxOwed = new BigDecimal(0)
		}

		return taxOwed
	}
	
	BigDecimal calculate2020(BigDecimal taxableIncome) {
		BigDecimal level5Thresehold = new BigDecimal('180000')
		BigDecimal level5Base = new BigDecimal('51667')
		BigDecimal level5Rate = new BigDecimal('0.45')
		
		BigDecimal level4Thresehold = new BigDecimal('120000')
		BigDecimal level4Base = new BigDecimal('29467')
		BigDecimal level4Rate = new BigDecimal('0.37')
		
		BigDecimal level3Thresehold = new BigDecimal('45000')
		BigDecimal level3Base = new BigDecimal('5092')
		BigDecimal level3Rate = new BigDecimal('0.325')
		
		BigDecimal level2Thresehold = new BigDecimal('18200')
		BigDecimal level2Rate = new BigDecimal('0.19')
		
		BigDecimal taxOwed
		
		if (taxableIncome > level5Thresehold) {
			taxOwed = level5Base.add(taxableIncome.subtract(level5Thresehold).multiply(level5Rate))
		}
		else if (taxableIncome > level4Thresehold) {
			taxOwed = level4Base.add(taxableIncome.subtract(level4Thresehold).multiply(level4Rate))
		}
		else if (taxableIncome > level3Thresehold) {
			taxOwed = level3Base.add(taxableIncome.subtract(level3Thresehold).multiply(level3Rate))
		}
		else if (taxableIncome > level2Thresehold) {
			taxOwed = taxableIncome.subtract(level2Thresehold).multiply(level2Rate)
		}
		else {
			taxOwed = new BigDecimal(0)
		}

		return taxOwed
	}
}
