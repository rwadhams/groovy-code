package com.wadhams.caravan.maintenance.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import groovy.transform.ToString

class CaravanMaintenanceApp {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern('dd/MM/yyyy')
	List<CaravanMaintenanceItem> cmiList
	Map<String, LocalDate> cmcMap
	
	static main(args) {
		println 'CaravanMaintenanceApp started...'
		println ''

		String cmiFilename = 'data/CaravanMaintenanceItems.xml'
		String cmcFilename = 'data/CaravanMaintenanceCompleted.xml'
		println "Processing: $cmiFilename & $cmcFilename"
		println ''
		
		CaravanMaintenanceApp app = new CaravanMaintenanceApp()
		app.execute(cmiFilename, cmcFilename)
		
		println ''
		println 'CaravanMaintenanceApp ended.'
	}
	
	def execute(String cmiFilename, String cmcFilename) {
		def cmi = new XmlSlurper().parse(new File(cmiFilename))
		cmiList = buildCaravanMaintenanceItemList(cmi.item)
//		println cmiList
//		println ''
		
		def cmc = new XmlSlurper().parse(new File(cmcFilename))
		cmcMap = buildCaravanMaintenanceCompleteMap(cmc.complete)
//		println cmcMap
//		println ''
		
		reportMaintenance()
		
		println ''
		
		reportItemsMissingFromCompleted()
	}
	
	def reportMaintenance() {
		LocalDate today = LocalDate.now()
		
		cmiList.each {cmi ->
			//println cmi
			
			LocalDate completedDate = cmcMap[cmi.name]
			if (!completedDate) {
				reportDue(cmi)
			}
			else {
				LocalDate dueDate = completedDate + cmi.frequency
				if (dueDate.isBefore(today)) {
					reportDue(cmi)
				}
				else {
					println "[${cmi.name}] maintenance is up to date."
				}
			}
			
			println ''
		}
	}
	
	def reportDue(CaravanMaintenanceItem cmi) {
		println "[${cmi.name}] maintenance is due."
		cmi.maint.each {m ->
			println "\t$m"
		}
	}

	def reportItemsMissingFromCompleted() {
		println 'Items missing from CaravanMaintenanceCompleted.xml'
		
		List<String> missing = []
		cmiList.each {cmi ->
			if (!cmcMap.containsKey(cmi.name)) {
				missing << cmi.name
			}
		}
		
		if (missing) {
			missing.each {name ->
				println "\t<complete name=\"$name\" date=\"\" />"
			}
		}
		else {
			println '\tNONE'
		}
	}
	
	List<CaravanMaintenanceItem> buildCaravanMaintenanceItemList(items) {
		List<CaravanMaintenanceItem> cmiList = []
		
		items.each {item ->
			String name = item.name
			List<String> maintList = item.maint.collect {it.text()}
			String frequency = item.frequency
			cmiList << new CaravanMaintenanceItem(name : "$name", maint : maintList, frequency : Integer.parseInt(frequency))
		}
		
		return cmiList
	}
	
	Map<String, LocalDate> buildCaravanMaintenanceCompleteMap(completed) {
		Map<String, LocalDate> cmcMap = [:]
		
		completed.each {complete ->
			String name = complete.@name
			
			LocalDate date
			String completedDate = complete.@date.text()
			if (completedDate) {
				date = LocalDate.parse(completedDate, dtf)
			}
			cmcMap[name] = date
		}
		
		return cmcMap
	}
	
}

@ToString
class CaravanMaintenanceItem {
	String name
	List<String> maint
	int frequency
}

@ToString
class CaravanMaintenanceComplete {
	String name
	LocalDate date
}
