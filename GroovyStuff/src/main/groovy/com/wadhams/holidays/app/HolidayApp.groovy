package com.wadhams.holidays.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import groovy.transform.ToString

class HolidayApp {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern('dd/MM/yyyy')
	int year
	
	static main(args) {
		println 'HolidayApp started...'
		println ''

		if (args.size() > 0) {
			//args[0] must be a number (nnnn = 2022, 2023, 2024...). Filename pattern will be data/Holidays<nnnn>.xml
			String fn = "data/Holidays${args[0]}.xml"
			println "Filename: $fn"
			println ''
			
			HolidayApp app = new HolidayApp()
			app.execute(fn)
		}
		else {
			println 'Missing parameter(s). Application did not run.'
		}
		
		println ''
		println 'HolidayApp ended.'
	}
	
	def execute(String filename) {
		def holidays = new XmlSlurper().parse(new File(filename))
		
		year = Integer.parseInt(holidays.year.text())
		
		def states = holidays.state
		
		List<State> stateList = buildStateList(states)
//		println stateList
//		println ''

		reportTermHolidays(stateList)
		
		reportHolidaysByState(stateList)
		
		reportHolidaysByDate(stateList)
	}
	
	def reportHolidaysByState(List<State> stateList) {
		DateTimeFormatter dayddmmyyyy = DateTimeFormatter.ofPattern('EEE dd/MM/yyyy')
		println 'Holidays reported by state'
		println '--------------------------'
		stateList.each {s ->
			println s.name
			s.holidays.each {h ->
				println "\t${h.date.format(dayddmmyyyy)}\t${h.name}"
			}
			println ''
		}
		println ''
	}
	
	def reportHolidaysByDate(List<State> stateList) {
		DateTimeFormatter dayddmmyyyy = DateTimeFormatter.ofPattern('EEE dd/MM/yyyy')
		println 'Holidays reported by date'
		println '-------------------------'

		//build map keyed by LocalDate		
		Map<LocalDate, Map<String, List>> dateMap = [:] as TreeMap
		stateList.each {s ->
			s.holidays.each {h ->
				Map<String, List> holidayMap = dateMap[h.date]
				if (!holidayMap) {
					holidayMap = [:] as TreeMap
					holidayMap[h.name] = [s.name]
					dateMap[h.date] = holidayMap
				}
				else {
					List<String> holidayStateList = holidayMap[h.name]
					if (!holidayStateList) {
						holidayMap[h.name] = [s.name]
					}
					else {
						holidayStateList << s.name
					} 
				}
			}
		}

		dateMap.each {k1,holidayMap ->
			println "${k1.format(dayddmmyyyy)}"
			//find the length of the longest holiday name
			int holidayNameLength = 0
			holidayMap.keySet().each {hn ->
				if (hn.size() > holidayNameLength) {
					holidayNameLength = hn.size()
				}
			}
			
			holidayMap.each {k2,holidayStateList ->
				println "\t${k2.padRight(holidayNameLength, ' ')}   $holidayStateList"
			}
			println ''
		}
		println ''
	}
	
	def reportTermHolidays(List<State> stateList) {
		DateTimeFormatter ddmm = DateTimeFormatter.ofPattern('dd/MM')

		List<String> holidayNameList = ['BeforeSchoolYear','Autumn School Holidays','Winter School Holidays','Spring School Holidays','AfterSchoolYear']
		holidayNameList.size().times {index ->
			println holidayNameList[index]
			Set<LocalDate> termHolidays = []
			stateList.each {s ->
				termHolidays.addAll(s.termHolidays[index])
			}
			List<LocalDate> termHolidaysSorted = termHolidays.sort()
	
			termHolidaysSorted.each {ld ->
				print "${ld.format(ddmm)} "
			}
			println ''
			
			stateList.each {s ->
				String stateName = s.name
				if (s.name.size() == 2) {
					stateName = "${s.name} "	//pad right with space
				}
				
				termHolidays.each {ld ->
					if (s.termHolidays[index].contains(ld)) {
						print " ${stateName}  "
					}
					else {
						print '      '
					}
				}
				println ''
			}
			println ''
		}
		println ''
	}
	
	List<State> buildStateList(states) {
		List<State> sList = []
		
		//extract values from XML
		states.each {s ->
			String stateName = s.name
			
			//terms
			def terms = s.term
			List<Term> tList = buildTermList(terms)
			
			//holidays
			def holidays = s.holiday
			List<Holiday> hList = []
			holidays.each {h ->
				hList << new Holiday(name : h.@name, date : LocalDate.parse(h.@date.text(), dtf))
			}
			
			sList << new State(name : stateName, terms : tList, holidays : hList)
		}
		
		//create termHolidays lists
		sList.each {s ->
			Term t1 = s.terms.find {t -> t.num == 'One'}
			Term t2 = s.terms.find {t -> t.num == 'Two'}
			Term t3 = s.terms.find {t -> t.num == 'Three'}
			Term t4 = s.terms.find {t -> t.num == 'Four'}
			
			s.termHolidays = []
			List<LocalDate> ldList
			LocalDate start
			LocalDate end
			
			//beforeSchoolYear
			ldList = []
			start = LocalDate.of(year, 1, 1)
			end = t1.startDate - 1
			while (start.isBefore(end)) {
				ldList << start
				start = start + 1
			}
			ldList << end
			s.termHolidays << ldList
			
			//autumn
			ldList = []
			start = t1.endDate + 1
			end = t2.startDate - 1
			while (start.isBefore(end)) {
				ldList << start
				start = start + 1
			}
			ldList << end
			s.termHolidays << ldList
			
			//winter
			ldList = []
			start = t2.endDate + 1
			end = t3.startDate - 1
			while (start.isBefore(end)) {
				ldList << start
				start = start + 1
			}
			ldList << end
			s.termHolidays << ldList
			
			//spring
			ldList = []
			start = t3.endDate + 1
			end = t4.startDate - 1
			while (start.isBefore(end)) {
				ldList << start
				start = start + 1
			}
			ldList << end
			s.termHolidays << ldList
			
			//afterSchoolYear
			ldList = []
			start = t4.endDate + 1
			end = LocalDate.of(year, 12, 31)
			while (start.isBefore(end)) {
				ldList << start
				start = start + 1
			}
			ldList << end
			s.termHolidays << ldList
		}
		
		return sList
	}
	
	List<Term> buildTermList(terms) {
		List<Term> tList = []
		
		terms.each {t ->
			String termNum = t.num
			LocalDate startDate = LocalDate.parse(t.startDate.text(), dtf)
			LocalDate endDate = LocalDate.parse(t.endDate.text(), dtf)
			tList << new Term(num : termNum, startDate : startDate, endDate : endDate)
		}
		
		return tList
	}
}

@ToString(includeNames=true)
class State {
	String name
	List<Term> terms
	List<List<LocalDate>> termHolidays
	List<Holiday> holidays
}

@ToString(includeNames=true)
class Term {
	String num
	LocalDate startDate
	LocalDate endDate
}

@ToString
class Holiday {
	String name
	LocalDate date
}
