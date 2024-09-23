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
		
		//txt files
		File f1 = new File("out/${year}-school-holiday-report.txt")
		f1.withPrintWriter {pw ->
			reportTermHolidays(stateList, year, pw)
		}
		
		File f2 = new File("out/${year}-holidays-by-state-report.txt")
		f2.withPrintWriter {pw ->
			reportHolidaysByState(stateList, year, pw)
		}
		
		File f3 = new File("out/${year}-holidays-by-date-report.txt")
		f3.withPrintWriter {pw ->
			reportHolidaysByDate(stateList, year, pw)
		}
		
		//html files
		File f1a = new File("out/${year}-school-holiday-report.html")
		f1a.withPrintWriter {pw ->
			reportTermHolidaysHtml(stateList, year, pw)
		}
		
		File f2a = new File("out/${year}-holidays-by-state-report.html")
		f2a.withPrintWriter {pw ->
			reportHolidaysByStateHtml(stateList, year, pw)
		}
		
		File f3a = new File("out/${year}-holidays-by-date-report.html")
		f3a.withPrintWriter {pw ->
			reportHolidaysByDateHtml(stateList, year, pw)
		}
	}
	
	def reportHolidaysByState(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter dayddmmyyyy = DateTimeFormatter.ofPattern('EEE dd/MM/yyyy')
		pw.println "Holidays reported by state for $year"
		pw.println '-----------------------------------'
		stateList.each {s ->
			pw.println s.name
			s.holidays.each {h ->
				pw.println "\t${h.date.format(dayddmmyyyy)}\t${h.name}"
			}
			pw.println ''
		}
		pw.println ''
	}
	
	def reportHolidaysByDate(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter dayddmmyyyy = DateTimeFormatter.ofPattern('EEE dd/MM/yyyy')
		pw.println "Holidays reported by date for $year"
		pw.println '----------------------------------'

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
			pw.println "${k1.format(dayddmmyyyy)}"
			//find the length of the longest holiday name
			int holidayNameLength = 0
			holidayMap.keySet().each {hn ->
				if (hn.size() > holidayNameLength) {
					holidayNameLength = hn.size()
				}
			}
			
			holidayMap.each {k2,holidayStateList ->
				pw.println "\t${k2.padRight(holidayNameLength, ' ')}   $holidayStateList"
			}
			pw.println ''
		}
		pw.println ''
	}
	
	def reportTermHolidays(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter ddmm = DateTimeFormatter.ofPattern('dd/MM')

		pw.println "School Holidays for $year"
		pw.println '-------------------------'
		pw.println ''
		
		List<String> holidayNameList = ['BeforeSchoolYear','Autumn School Holidays','Winter School Holidays','Spring School Holidays','AfterSchoolYear']
		holidayNameList.size().times {index ->
			pw.println holidayNameList[index]
			Set<LocalDate> termHolidays = []
			stateList.each {s ->
				termHolidays.addAll(s.termHolidays[index])
			}
			List<LocalDate> termHolidaysSorted = termHolidays.sort()
	
			termHolidaysSorted.each {ld ->
				pw.print "${ld.format(ddmm)} "
			}
			pw.println ''
			
			stateList.each {s ->
				String stateName = s.name
				if (s.name.size() == 2) {
					stateName = "${s.name} "	//pad right with space
				}
				
				termHolidays.each {ld ->
					if (s.termHolidays[index].contains(ld)) {
						pw.print " ${stateName}  "
					}
					else {
						pw.print '      '
					}
				}
				pw.println ''
			}
			pw.println ''
		}
		pw.println ''
	}
	
	def reportHolidaysByStateHtml(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern('EEE')
		DateTimeFormatter ddmmyyyyFormatter = DateTimeFormatter.ofPattern('dd/MM/yyyy')
		pw.println '<html>'
		pw.println "<b>Holidays reported by state for $year</b>"
		pw.println '</br>'
		stateList.each {s ->
			pw.println s.name
			pw.println '<table border="1">'
			s.holidays.each {h ->
				pw.println '<tr>'
				pw.println "<td>${h.date.format(dayFormatter)}</td><td>${h.date.format(ddmmyyyyFormatter)}</td><td>${h.name}</td>"
				pw.println '</tr>'
			}
			pw.println '</table>'
		}
		pw.println '</html>'
	}
	
	def reportHolidaysByDateHtml(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern('EEE')
		DateTimeFormatter ddmmyyyyFormatter = DateTimeFormatter.ofPattern('dd/MM/yyyy')
		pw.println '<html>'
		pw.println "<b>Holidays reported by date for $year</b>"
		pw.println '</br>'

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

		pw.println '<table border="1">'
		dateMap.each {k1,holidayMap ->
			holidayMap.each {k2,holidayStateList ->
				pw.println "<tr><td>${k1.format(dayFormatter)}</td><td>${k1.format(ddmmyyyyFormatter)}</td><td>${k2}</td><td>$holidayStateList</td></tr>"
			}
		}
		pw.println '</table>'
		pw.println '</html>'
	}
	
	def reportTermHolidaysHtml(List<State> stateList, int year, PrintWriter pw) {
		DateTimeFormatter ddmm = DateTimeFormatter.ofPattern('dd/MM')

		pw.println '<html>'
		pw.println "<b>School Holidays for $year</b>"
		pw.println '</br>'
		
		List<String> holidayNameList = ['BeforeSchoolYear','Autumn School Holidays','Winter School Holidays','Spring School Holidays','AfterSchoolYear']
		holidayNameList.size().times {index ->
			pw.println holidayNameList[index]
			pw.println '<table border="1">'
			Set<LocalDate> termHolidays = []
			stateList.each {s ->
				termHolidays.addAll(s.termHolidays[index])
			}
			List<LocalDate> termHolidaysSorted = termHolidays.sort()
	
			pw.println '<tr align="center">'
			termHolidaysSorted.each {ld ->
				pw.print "<td>${ld.format(ddmm)}</td>"
			}
			pw.println '</tr>'
			
			stateList.each {s ->
				String stateName = s.name
				
				pw.println '<tr align="center">'
				termHolidays.each {ld ->
					if (s.termHolidays[index].contains(ld)) {
						pw.print "<td>${stateName}</td>"
					}
					else {
						pw.print '<td></td>'
					}
				}
				pw.println '</tr>'
			}
			pw.println '</table>'
		}
		pw.println '</html>'
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
