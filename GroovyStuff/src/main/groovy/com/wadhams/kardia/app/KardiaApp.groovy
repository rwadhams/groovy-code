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
	DateTimeFormatter df = DateTimeFormatter.ofPattern('dd/MM/yyyy')
	
	List<KardiaReading> krList
	List<ListRange> listRangeList
	List<KardiaMedication> kmList
	List<KardiaSymptom> ksList

	static main(args) {
		println 'KardiaApp started...'
		println ''

		KardiaApp app = new KardiaApp()
		app.execute()
		
		println ''
		println 'KardiaApp ended.'
	}
	
	def execute() {
		String krFilename = 'data/Kardia_reading.xml'
		println "Processing: $krFilename"
		println ''
		
		def kr = new XmlSlurper().parse(new File(krFilename))
		
		krList = buildKardiaReadingList(kr.reading)
//		println krList
//		println ''
		
		listRangeList = buildListRange()
//		println listRangeList
//		println ''
		
		String kmFilename = 'data/Kardia_medication.xml'
		println "Processing: $kmFilename"
		println ''
		
		def km = new XmlSlurper().parse(new File(kmFilename))
		
		kmList = buildKardiaMedicationList(km.medication)
//		println kmList
//		println ''
		
		String ksFilename = 'data/Kardia_symptom.xml'
		println "Processing: $ksFilename"
		println ''
		
		def ks = new XmlSlurper().parse(new File(ksFilename))
		
		ksList = buildKardiaSymptomList(ks.symptom)
//		println ksList
//		println ''
		
		File f = new File("out/kardia-report.txt")
		f.withPrintWriter {pw ->
			reportReadings(pw)
			reportTotals(pw)
			reportMedications(pw)
			reportSymptoms(pw)
		}

	}
	
	def reportReadings(PrintWriter pw) {
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern('dd/MM/yyyy')
		
		listRangeList.each {rl ->
			List<KardiaReading> subList = krList.subList(rl.startIndex, rl.endIndex+1)
			//println subList
			def kMin = subList.min {it.rate}
			def avg = subList.average {it.rate}
			def kMax = subList.max {it.rate}
			String resultName = (subList[0].result == 'NSR') ? 'Normal Sinus Rhythm............: ' : 'Possible Atrial Fibrillation...: '
			String startDate = subList[0].dateTime.format(dtf1)
			String endDate = subList[-1].dateTime.format(dtf1)
			long days = ChronoUnit.DAYS.between(subList[0].dateTime.toLocalDate(), subList[-1].dateTime.toLocalDate()) + 1
			String average = (avg as int).toString().padRight(3, ' ')
			
			pw.println "$resultName${startDate} - ${endDate} (${days} days)\tAverage: ${average}\tMin/Max: ${kMin.rate}/${kMax.rate}"
		}
		pw.println ''
	}
	
	def reportTotals(PrintWriter pw) {
		long totalDays = ChronoUnit.DAYS.between(krList[0].dateTime.toLocalDate(), krList[-1].dateTime.toLocalDate()) + 1
		
		long pafDays = 0
		listRangeList.each {rl ->
			List<KardiaReading> subList = krList.subList(rl.startIndex, rl.endIndex+1)
			if (subList[0].result == 'PAF') {
				pafDays += ChronoUnit.DAYS.between(subList[0].dateTime.toLocalDate(), subList[-1].dateTime.toLocalDate()) + 1
			}
		}
		pw.println "Total reporting period: $totalDays days. Possible Atrial Fibrillation: $pafDays days."
		pw.println ''
	}
	
	def reportMedications(PrintWriter pw) {
		int maxMedicationLength = 0
		kmList.each {m ->
			if (m.name.size() > maxMedicationLength) {
				maxMedicationLength = m.name.size()
			}
		}
		
		kmList.each {m ->
			long totalDays = ChronoUnit.DAYS.between(m.start, m.end) + 1
			pw.println "Medication: ${m.name.padRight(maxMedicationLength, ' ')} Start: ${m.start} End: ${m.end} ($totalDays days)"
		}
		pw.println ''
	}
	
	def reportSymptoms(PrintWriter pw) {
		int maxSymptomLength = 0
		ksList.each {m ->
			if (m.name.size() > maxSymptomLength) {
				maxSymptomLength = m.name.size()
			}
		}
		
		ksList.each {m ->
			long totalDays = ChronoUnit.DAYS.between(m.start, m.end) + 1
			pw.println "Symptom: ${m.name.padRight(maxSymptomLength, ' ')} Start: ${m.start} End: ${m.end} ($totalDays days)"
		}
		pw.println ''
	}
	
	def buildListRange() {
		List<ListRange> listRangeList = []
		
		String result = krList[0].result
		int startIndex = 0
		krList.eachWithIndex {k, i ->
//			println k
//			println i
			if (result != k.result) {
				listRangeList << new ListRange(startIndex : startIndex, endIndex : i-1)
				startIndex = i
				result = k.result
			}
		}
		listRangeList << new ListRange(startIndex : startIndex, endIndex : krList.size()-1)
		
		return listRangeList
	}
	
	List<KardiaReading> buildKardiaReadingList(readings) {
		List<KardiaReading> krList = []
		
		readings.each {r ->
			LocalDateTime dateTime = LocalDateTime.parse(r.@datetime.text(), dtf)
			String rate = r.@rate
			String result = r.@result
			krList << new KardiaReading(dateTime : dateTime, rate : Integer.parseInt(rate), result : result)
		}
		
		return krList
	}
	
	List<KardiaMedication> buildKardiaMedicationList(medications) {
		List<KardiaMedication> kmList = []
		
		medications.each {m ->
			String name = m.@name
			LocalDate start = LocalDate.parse(m.@start.text(), df)
			LocalDate end = LocalDate.now()
			String endDate = m.@end.text()
			if (endDate) {
				end = LocalDate.parse(endDate, df)
			}
			
			kmList << new KardiaMedication(name : name, start : start, end : end)
		}
		
		return kmList
	}
	
	List<KardiaSymptom> buildKardiaSymptomList(symptoms) {
		List<KardiaSymptom> ksList = []
		
		symptoms.each {s ->
			String name = s.@name
			LocalDate start = LocalDate.parse(s.@start.text(), df)
			LocalDate end = LocalDate.now()
			String endDate = s.@end.text()
			if (endDate) {
				end = LocalDate.parse(endDate, df)
			}
			
			ksList << new KardiaSymptom(name : name, start : start, end : end)
		}
		
		return ksList
	}
	
}

@ToString
class KardiaReading {
	LocalDateTime dateTime
	int rate
	String result
}

@ToString
class ListRange {
	int startIndex
	int endIndex
}

@ToString
class KardiaMedication {
	String name
	LocalDate start
	LocalDate end
}

@ToString
class KardiaSymptom {
	String name
	LocalDate start
	LocalDate end
}
