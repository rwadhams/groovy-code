package com.wadhams.erin.tasmania.app

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ErinTasmaniaPlannerApp {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM")
	
	static main(args) {
		println 'ErinTasmaniaPlannerApp started...'
		println ''

		ErinTasmaniaPlannerApp app = new ErinTasmaniaPlannerApp()
		app.execute()

		println ''
		println 'ErinTasmaniaPlannerApp ended.'
	}
	
	def execute() {
		LocalDate travelDate = LocalDate.of(2021, 3, 21)
		
		16.times {i ->
			String text = travelDate.format(formatter)
			println "Day ${i+1}\t($text)"
			
			
			
			
			travelDate = travelDate.plusDays(1L)
		}
	}
	
}
