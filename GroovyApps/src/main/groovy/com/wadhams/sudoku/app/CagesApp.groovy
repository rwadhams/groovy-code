package com.wadhams.sudoku.app

class CagesApp {
	
	static main(args) {
		println 'CagesApp started...'
		println ''

		CagesApp app = new CagesApp()
		app.execute()
//		app.execute2()
//		app.execute3()
//		app.execute4()
		
		println ''
		println 'CagesApp ended.'
	}
	
	def execute() {
		List<List<String>> cageList = []
		
		cageList << buildTwoCages()
		
		3.times {index ->
			cageList << buildNextCages(cageList[index])
		}
		
		Map<Integer, List<String>> fullMap = [:]	//<Integer, List<String>>
		
		def updateFullMap = {map ->
			Set keySet = map.keySet()
			keySet.each{k ->
				List<String> mapList = fullMap[k]
				if (mapList) {
					mapList.addAll(map[k])
				}
				else {
					fullMap[k] = map[k]
				}
			}
		}

		cageList.each {cage ->
			println cage
			Map<Integer, List<String>> map = buildMap(cage)
			updateFullMap.call(map)
		}

		//println fullMap
		report(fullMap)
	}
	
	List<String> buildTwoCages() {
		List<String> resultList = []
		for (int i in 1..8) {
			for (int j in i+1..9) {
				String result = "$i$j"
				//println result
				resultList << result
			}
		}
		//println ''
		
		return resultList
	}
	
	List<String> buildNextCages(List<String> cageList) {
		List<String> resultList = []
		for (String s in cageList) {
			String last = s[-1]	//last digit
			//println "s: $s\tlast:$last"
			int start = (last as int) + 1
			if (start <= 9) {
				Range r = start..9
				//println "r:$r"
				for (int i in r) {
					String result = "$s$i"
					resultList << result
				}
			}
		}
		return resultList
	}

	Map<Integer, List<String>> buildMap(List<String> cageList) {
		
		Map<Integer, List<String>> map = [:]	//<Integer, List<String>>
		
		cageList.each {s ->
			//println s
			List digitList = s.toList()
			//println list
			
			int total = 0
			digitList.each {d ->
				//println d
				total += (d as int)
			}
			//println "$s: $total"
			
			List<String> mapList = map[total]
			if (mapList) {
				mapList << s
			}
			else {
				map[total] = [s]
			}
		}
		return map
	}
	
	def report(Map<Integer, List<String>> map) {
		Set keySet = map.keySet()
		keySet.each{k ->
			println "$k:"
			print '  '
			List<String> mapList = map[k]
			int cageLength = mapList[0].size()
			mapList.each {s ->
				if (cageLength != s.size()) {
					println ''
					print '  '
					cageLength = s.size()
				}
				print " $s"
			}
			println ''
		}
	}
	

	
/*		
OLD CODE ------------------------------------------------------ OLD CODE
	List<String> buildThreeCages(List<String> twoCageList) {
		List<String> resultList = []
		for (String s in twoCageList) {
			String last = s[-1]	//last digit
//			println "s: $s\tlast:$last"
			int start = (last as int) + 1
			if (start <= 9) {
				Range r = start..9
//				println "r:$r"
				for (int i in r) {
					String result = "$s$i"
					resultList << result
				}
			}
		}
		return resultList
	}
	
	List<String> buildFourCages(List<String> threeCageList) {
		List<String> resultList = []
		for (String s in threeCageList) {
			String last = s[-1]	//last digit
//			println "s: $s\tlast:$last"
			int start = (last as int) + 1
			if (start <= 9) {
				Range r = start..9
//				println "r:$r"
				for (int i in r) {
					String result = "$s$i"
					resultList << result
				}
			}
		}
		return resultList
	}
	
	def execute2() {
		List<String> twoCageList = buildTwoCages()
		println twoCageList
		println ''
		
		Map<Integer, List<String>> map = [:]	//<Integer, List<String>>
		
		twoCageList.each {s ->
			//println s
			List digitList = s.toList()
			//println list
			
			int total = 0
			digitList.each {d ->
				//println d
				total += (d as int)
			}
			println "$s: $total"
			
			List<String> mapList = map[total]
			if (mapList) {
				mapList << s
			}
			else {
				map[total] = [s]
			}
		}
		println map
	}

		def execute3() {
		List<String> twoCageList = buildTwoCages()
//		println twoCageList
//		println ''
		List<String> threeCageList = buildNextCages(twoCageList)
//		println threeCageList
//		println ''
		
		Map<Integer, List<String>> fullMap = [:]	//<Integer, List<String>>
		
		Map<Integer, List<String>> map2 = buildMap(twoCageList)
		Set keySet2 = map2.keySet()
		keySet2.each{k ->
			List<String> mapList = fullMap[k]
			if (mapList) {
				mapList.addAll(map2[k])
			}
			else {
				fullMap[k] = map2[k]
			}
		}
		
		Map<Integer, List<String>> map3 = buildMap(threeCageList)
		Set keySet3 = map3.keySet()
		keySet3.each{k ->
			List<String> mapList = fullMap[k]
			if (mapList) {
				mapList.addAll(map3[k])
			}
			else {
				fullMap[k] = map3[k]
			}
		}
		
//		println fullMap
		
		Set keySet = fullMap.keySet()
		keySet.each{k ->
			println "$k:"
			print '  '
			List<String> mapList = fullMap[k]
			int cageLength = mapList[0].size()
			mapList.each {s ->
				if (cageLength != s.size()) {
					println ''
					print '  '
					cageLength = s.size()
				}
				print " $s"
			}
			println ''
		}

	}
	
	def execute4() {
		List<String> twoCageList = buildTwoCages()
		Map<Integer, List<String>> map2 = buildMap(twoCageList)
		
		List<String> threeCageList = buildNextCages(twoCageList)
		Map<Integer, List<String>> map3 = buildMap(threeCageList)
		
		Map<Integer, List<String>> fullMap = [:]	//<Integer, List<String>>

		def updateFullMap = {map ->
			Set keySet = map.keySet()
			keySet.each{k ->
				List<String> mapList = fullMap[k]
				if (mapList) {
					mapList.addAll(map[k])
				}
				else {
					fullMap[k] = map[k]
				}
			}
		}
		
		updateFullMap.call(map2)
		updateFullMap.call(map3)
		
		//println fullMap
		report(fullMap)
	}
	

*/	
}
