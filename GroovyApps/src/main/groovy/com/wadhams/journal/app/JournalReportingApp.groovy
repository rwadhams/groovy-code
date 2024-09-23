package com.wadhams.journal.app

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import groovy.transform.ToString

class JournalReportingApp {
	DateTimeFormatter xmlDTF = DateTimeFormatter.ofPattern('dd/MM/yyyy')
	DateTimeFormatter reportingDTF = DateTimeFormatter.ofPattern('EEE dd/MM/yyyy')

	String jFilename = 'data/Journal.xml'
	
	static main(args) {
		println 'JournalReportingApp started...'
		println ''

		JournalReportingApp app = new JournalReportingApp()
		app.execute()
		
		println ''
		println 'JournalReportingApp ended.'
	}
	
	def execute() {
		println "Processing: $jFilename"
		println ''
		
		File f1 = new File("out/journal-report.txt")
		f1.withPrintWriter {pw ->
			reportText(pw)
		}
		
		File f2 = new File("out/journal-report.html")
		f2.withPrintWriter {pw ->
			reportHTML(pw)
		}
	}
	
	def reportText(PrintWriter pw) {
		def j = new XmlSlurper().parse(new File(jFilename))
		def transactions = j.data
		
		transactions.each {txn ->
			pw.println "Location....: ${txn.location.text()}"
			pw.println "Stayed at...: ${txn.campsite.text()}"
			txn.dt.each {dt ->
				LocalDate ld = LocalDate.parse(dt.@value.text(), xmlDTF)
				pw.println "  ${ld.format(reportingDTF)}"
				dt.attraction.each {a ->
					String name = a.@name
					if (name) {
						pw.println "     $name"
					}
				}
			}
			pw.println ''
		}
	}
	
	def reportHTML(PrintWriter pw) {
		def j = new XmlSlurper().parse(new File(jFilename))
		def transactions = j.data

		pw.println '<html><table>'		
		transactions.each {txn ->
			pw.println "<tr><td><b>${txn.location.text()}</b>&nbsp;&nbsp;&nbsp;Stayed at...: ${txn.campsite.text()}</td></tr>"
			
			txn.dt.each {dt ->
				LocalDate ld = LocalDate.parse(dt.@value.text(), xmlDTF)
				pw.println "<tr><td>${ld.format(reportingDTF)}</td></tr>"
				
				if (dt.attraction[0].@name.text()) {
					pw.print '<tr><td><ul>'
					dt.attraction.each {a ->
						pw.print "<li>${a.@name}</li>"
					}
					pw.println '</ul></td></tr>'
				}
			}
			pw.println '<tr><td>&nbsp;</td></tr>'
		}
		pw.println '</table></html>'
	}

}