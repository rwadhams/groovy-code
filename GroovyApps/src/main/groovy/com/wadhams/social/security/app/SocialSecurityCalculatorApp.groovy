package com.wadhams.social.security.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.Period
import groovy.transform.ToString

class SocialSecurityCalculatorApp {
	int firstAmt = 1878
	int secondAmt = 2757
	int thirdAmt = 3447
	
	static main(args) {
		println 'SocialSecurityCalculatorApp started...'
		println ''

		SocialSecurityCalculatorApp app = new SocialSecurityCalculatorApp()
		app.execute()
		
		println 'SocialSecurityCalculatorApp ended.'
	}
	
	def execute() {
		NumberFormat nf = NumberFormat.getCurrencyInstance()
		nf.setMaximumFractionDigits(0)
		
		LocalDate birthDate = LocalDate.of(1962,6, 18)

		YearMonth firstYM = YearMonth.of(2024,6)
		boolean firstTier = false
		int firstTotal = 0
		int firstCounter = 0
		
		YearMonth secondYM = YearMonth.of(2029,6)
		boolean secondTier = false
		int secondTotal = 0
		int secondCounter = 0
		boolean secondGreaterThanFirst = false
		
		YearMonth thirdYM = YearMonth.of(2032,6)
		boolean thirdTier = false
		int thirdTotal = 0
		int thirdCounter = 0
		boolean thirdGreaterThanSecond = false
		
		YearMonth age90 = YearMonth.of(2053,6)
		int age = 0
		
		//starting YM
		YearMonth ym = YearMonth.now()
		int monthCounterFromNow = 0
		
		while (ym.isBefore(age90)) {
			monthCounterFromNow++
			
			if (!thirdTier && ym >= thirdYM) {
				thirdTier = true
			}
			else if (!secondTier && ym >= secondYM) {
				secondTier = true
			}
			else if (!firstTier && ym >= firstYM) {
				firstTier = true
			}
			
			if (firstTier) {
				firstTotal += firstAmt
				firstCounter++
			}
			if (secondTier) {
				secondTotal += secondAmt
				secondCounter++
			}
			if (thirdTier) {
				thirdTotal += thirdAmt
				thirdCounter++
			}
			
			LocalDate nowDate = ym.atEndOfMonth()
			age = Period.between(birthDate, nowDate).getYears()
			
			if (firstTotal == firstAmt) {
				println "First tier has started: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
				println "\tYearMonth: $ym:  My age: $age"
				println "\tFirst tier total: ${nf.format(firstTotal).padRight(10, ' ')}"
				println ''
			}
			
			if (secondTotal == secondAmt) {
				println "Second tier has started: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
				println "\tYearMonth: $ym:  My age: $age"
				println "\tFirst tier total....: ${nf.format(firstTotal).padRight(10, ' ')}($firstCounter payments of ${nf.format(firstAmt)})"
				println "\tSecond tier total...: ${nf.format(secondTotal).padRight(10, ' ')}"
				println ''
			}
			
			if (thirdTotal == thirdAmt) {
				println "Third tier has started: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
				println "\tYearMonth: $ym:  My age: $age"
				println "\tFirst tier total....: ${nf.format(firstTotal).padRight(10, ' ')}($firstCounter payments of ${nf.format(firstAmt)})"
				println "\tSecond tier total...: ${nf.format(secondTotal).padRight(10, ' ')}($secondCounter payments of ${nf.format(secondAmt)})"
				println "\tThird tier total....: ${nf.format(thirdTotal).padRight(10, ' ')}"
				println ''
			}
			
			if (secondTotal > firstTotal && !secondGreaterThanFirst) {
				secondGreaterThanFirst = true
				println "Second tier total has passed First tier total: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
				println "\tYearMonth: $ym:  My age: $age"
				println "\tFirst tier total....: ${nf.format(firstTotal).padRight(10, ' ')}($firstCounter payments of ${nf.format(firstAmt)})"
				println "\tSecond tier total...: ${nf.format(secondTotal).padRight(10, ' ')}($secondCounter payments of ${nf.format(secondAmt)})"
				println "\tThird tier total....: ${nf.format(thirdTotal).padRight(10, ' ')}($thirdCounter payments of ${nf.format(thirdAmt)})"
				println ''
			}

			if (thirdTotal > secondTotal && !thirdGreaterThanSecond) {
				thirdGreaterThanSecond = true
				println "Third tier total has passed both First and Second tier totals: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
				println "\tYearMonth: $ym:  My age: $age"
				println "\tFirst tier total....: ${nf.format(firstTotal).padRight(10, ' ')}($firstCounter payments of ${nf.format(firstAmt)})"
				println "\tSecond tier total...: ${nf.format(secondTotal).padRight(10, ' ')}($secondCounter payments of ${nf.format(secondAmt)})"
				println "\tThird tier total....: ${nf.format(thirdTotal).padRight(10, ' ')}($thirdCounter payments of ${nf.format(thirdAmt)})"
				println ''
			}
			
			ym = ym.plusMonths(1L)
		}
		
		println "If social security still exists and I lived to 90: (${monthCounterToYearsAndMonths(monthCounterFromNow)} from now)"
		println "\tYearMonth: $ym:  My age: $age"
		println "\tFirst tier total....: ${nf.format(firstTotal).padRight(10, ' ')}($firstCounter payments of ${nf.format(firstAmt)})"
		println "\tSecond tier total...: ${nf.format(secondTotal).padRight(10, ' ')}($secondCounter payments of ${nf.format(secondAmt)})"
		println "\tThird tier total....: ${nf.format(thirdTotal).padRight(10, ' ')}($thirdCounter payments of ${nf.format(thirdAmt)})"
		println ''
	}
	
	String monthCounterToYearsAndMonths(int monthCounter) {
		int years = monthCounter / 12
		int months = monthCounter - years * 12
		return "$years years and $months months"
	}
}
