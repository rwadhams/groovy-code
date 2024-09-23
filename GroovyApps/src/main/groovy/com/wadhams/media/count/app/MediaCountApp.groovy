package com.wadhams.media.count.app

import static groovy.io.FileType.FILES

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import groovy.transform.ToString

class MediaCountApp {
//	DateTimeFormatter dtf = DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm')
//	List<KardiaItem> kList
	List<String> filenameDateList
	
	static main(args) {
		println 'MediaCountApp started...'
		println ''

		MediaCountApp app = new MediaCountApp()
		app.execute()
		
		println ''
		println 'MediaCountApp ended.'
	}
	
	def execute() {
		filenameDateList = buildFilenameDateList()
//		println filenameDateList.size()
//		println ''
//		filenameDateList.each {fnd ->
//			println fnd
//		}
//		println ''
		
		reportCountsByYear()

		reportCountsByMonth()
		
		reportCountsByHalfMonth()
		
//		def k = new XmlSlurper().parse(new File(kFilename))
//		kList = buildKardiaItemList(k.reading)
//		println kList
//		println ''
		
//		List<ListRange> listRangeList = buildListRange()
//		println listRangeList
//		println ''
		
//		report(listRangeList)
	}
	
	def reportCountsByHalfMonth() {
		String month = "${filenameDateList[0].substring(0,6)}"
		int firstHalfMonthCount = 0
		int secondHalfMonthCount = 0
		filenameDateList.each {fnd ->
			int day = Integer.parseInt(fnd.substring(6,8))
			if (month == fnd.substring(0,6)) {
				if (day < 15) {
					firstHalfMonthCount++
				}
				else {
					secondHalfMonthCount++
				}
			}
			else {
				println "$month\t$firstHalfMonthCount / $secondHalfMonthCount"
				//reset for next month
				month = fnd.substring(0,6)
				if (day < 15) {
					firstHalfMonthCount = 1
					secondHalfMonthCount = 0
				}
				else {
					firstHalfMonthCount = 0
					secondHalfMonthCount = 1
				}
			}
		}
		println "$month\t$firstHalfMonthCount / $secondHalfMonthCount"
		println ''
	}
	
	def reportCountsByMonth() {
		String month = "${filenameDateList[0].substring(0,6)}"
		int count = 0
		filenameDateList.each {fnd ->
			if (month == fnd.substring(0,6)) {
				count++
			}
			else {
				println "$month\t$count"
				//reset for next month
				month = fnd.substring(0,6)
				count = 1
			}
		}
		println "$month\t$count"
		println ''
	}
	
	def reportCountsByYear() {
		String year = "${filenameDateList[0].substring(0,4)}"
		int count = 0
		filenameDateList.each {fnd ->
			if (year == fnd.substring(0,4)) {
				count++
			}
			else {
				println "$year\t$count"
				//reset for next year
				year = fnd.substring(0,4)
				count = 1
			}
		}
		println "$year\t$count"
		println ''
	}
	
	List<String> buildFilenameDateList() {
		List<String> filenameDateList = []
		
		File baseDir = new File('C:\\Grey_Nomad_Media\\Image_Files')
		baseDir.eachFile(FILES) {f ->
			String filenameDate = f.name[0..7]
			filenameDateList << filenameDate
		}
		
		return filenameDateList
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

