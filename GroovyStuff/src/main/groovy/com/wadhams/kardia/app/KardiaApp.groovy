package com.wadhams.kardia.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import groovy.transform.ToString

class KardiaApp {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm')
	List<KardiaItem> kList
	
	static main(args) {
		println 'KardiaApp started...'
		println ''

		String kFilename = 'data/Kardia.xml'
		println "Processing: $kFilename"
		println ''
		
		KardiaApp app = new KardiaApp()
		app.execute(kFilename)
		
		println ''
		println 'KardiaApp ended.'
	}
	
	def execute(String kFilename) {
		def k = new XmlSlurper().parse(new File(kFilename))
		kList = buildKardiaItemList(k.reading)
//		println kList
//		println ''
		
		List<ListRange> listRangeList = buildListRange()
//		println listRangeList
//		println ''
		
		report(listRangeList)
	}
	
	def report(List<ListRange> listRangeList) {
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern('dd/MM/yyyy')
		
		listRangeList.each {rl ->
			List<KardiaItem> subList = kList.subList(rl.startIndex, rl.endIndex+1)
			//println subList
			def kMin = subList.min {it.rate}
			def avg = subList.average {it.rate}
			def kMax = subList.max {it.rate}
			String resultName = (subList[0].result == 'NSR') ? 'Normal Sinus Rhythm............: ' : 'Possible Atrial Fibrillation...: '
			String startDate = subList[0].dateTime.format(dtf1)
			String endDate = subList[-1].dateTime.format(dtf1)
			long days = ChronoUnit.DAYS.between(subList[0].dateTime.toLocalDate(), subList[-1].dateTime.toLocalDate()) + 1
			
			println "$resultName${startDate} - ${endDate} (${days} days)\tAverage: ${avg as int}\tMin/Max: ${kMin.rate}/${kMax.rate}"
		}
	}
	
	def buildListRange() {
		List<ListRange> listRangeList = []
		
		String result = kList[0].result
		int startIndex = 0
		kList.eachWithIndex {k, i ->
//			println k
//			println i
			if (result != k.result) {
				listRangeList << new ListRange(startIndex : startIndex, endIndex : i-1)
				startIndex = i
				result = k.result
			}
		}
		listRangeList << new ListRange(startIndex : startIndex, endIndex : kList.size()-1)
		
		return listRangeList
	}
	
	List<KardiaItem> buildKardiaItemList(readings) {
		List<KardiaItem> kList = []
		
		readings.each {reading ->
			LocalDateTime dateTime = LocalDateTime.parse(reading.@datetime.text(), dtf)
			String rate = reading.@rate
			String result = reading.@result
			kList << new KardiaItem(dateTime : dateTime, rate : Integer.parseInt(rate), result : result)
		}
		
		return kList
	}
	
}

@ToString
class KardiaItem {
	LocalDateTime dateTime
	int rate
	String result
}

@ToString
class ListRange {
	int startIndex
	int endIndex
}

