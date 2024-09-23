package com.wadhams.hd.cleanup.app

import java.util.regex.Pattern

import groovy.io.FileType
import groovy.transform.ToString

class MongoCleanupApp {
	Pattern projectFilePattern = ~/\.project$/
	Pattern metadataDirFilePattern = ~/\.metadata$/
	Pattern serversDirFilePattern = ~/\Servers$/
	
	static main(args) {
		println 'MongoCleanupApp started...'
		println ''

		String dirPath2012 = "D:\\Kemper\\20120925_kahjax76854_backup\\mongo"
		String dirPath2016 = "D:\\Kemper\\20160802_kahjax76710_backup\\mongo"
		MongoCleanupApp app = new MongoCleanupApp()
		//app.findProjectIntersection(dirPath2012, dirPath2016)
		//app.findMetadataDirectories(dirPath2012, dirPath2016)
		//app.findServerDirectories(dirPath2012, dirPath2016)
		app.findAllProjectsSorted(dirPath2012, dirPath2016)
		//app.findAllEARProjects(dirPath2012, dirPath2016)
		
		println ''
		println 'MongoCleanupApp ended.'
	}
	
	def findAllEARProjects(String dirPath2012, String dirPath2016) {
		List<String> slistAll = []
		
		List<EclipseProject> list01 = findProjects(dirPath2012)
		list01.each {ep ->
			if (ep.name.endsWith('EAR')) {
				slistAll << "${ep.f.absolutePath - '.project'} ${ep.name}"
			}
		}
		
		List<EclipseProject> list02 = findProjects(dirPath2016)
		list02.each {ep ->
			if (ep.name.endsWith('EAR')) {
				slistAll << "${ep.f.absolutePath - '.project'} ${ep.name}"
			}
		}
		
		slistAll.sort()
		
		slistAll.each {line ->
			println line
		}
	}
	
	def findAllProjectsSorted(String dirPath2012, String dirPath2016) {
		List<String> slistAll = []
		List<String> slist01
		List<String> slist02
		
		List<EclipseProject> list01 = findProjects(dirPath2012)
		slist01 = list01.collect {ep ->
			"${ep.name} ${ep.f.absolutePath - '.project'}"
		}
		
		List<EclipseProject> list02 = findProjects(dirPath2016)
		slist02 = list02.collect {ep ->
			"${ep.name} ${ep.f.absolutePath - '.project'}"
		}
		
		slistAll.addAll(slist01)
		slistAll.addAll(slist02)
		slistAll.sort()
		
		slistAll.each {line ->
			println line
		}
	}
	
	def findServerDirectories(String dirPath2012, String dirPath2016) {
		List<String> list01 = findDirectories(serversDirFilePattern, dirPath2012)
		list01.each {line ->
			println line
		}
		List<String> list02 = findDirectories(serversDirFilePattern, dirPath2016)
		list02.each {line ->
			println line
		}
	}
	
	def findMetadataDirectories(String dirPath2012, String dirPath2016) {
		List<String> list01 = findDirectories(metadataDirFilePattern, dirPath2012)
		list01.each {line ->
			println line
		}
		List<String> list02 = findDirectories(metadataDirFilePattern, dirPath2016)
		list02.each {line ->
			println line
		}
	}
	
	def findProjectIntersection(String dirPath2012, String dirPath2016) {
		List<String> slist01
		List<String> slist02
		
		List<EclipseProject> list01 = findProjects(dirPath2012)
		slist01 = list01.collect {ep ->
			String s1 = "${ep.f.absolutePath - ep.dirPath - '.project'}"
			"$s1 ${ep.name}"
		}
		println "slist01 size: ${slist01.size()}"
		
		List<EclipseProject> list02 = findProjects(dirPath2016)
		slist02 = list02.collect {ep ->
			String s1 = "${ep.f.absolutePath - ep.dirPath - '.project'}"
			"$s1 ${ep.name}"
		}
		println "slist02 size: ${slist02.size()}"
		
		List<String> intersection = slist01.intersect(slist02)
		intersection.each {line ->
			println line
		}
		
	}
	
	List<EclipseProject> findProjects(String dirPath) {
		File baseDir = new File(dirPath)
		println baseDir.absolutePath
		println ''
		
		List<String> projectList = [] 
		baseDir.eachFileRecurse() {f ->
			if (projectFilePattern.matcher(f.name).find()) {
				def proj = new XmlSlurper().parse(f)
				String s2 = "${proj.name}"
				EclipseProject ep = new EclipseProject(dirPath: dirPath, f: f, name: s2)
				projectList << ep
			}
		}
		
		return projectList
	}
	
	List<String> findDirectories(Pattern dirPattern, String dirPath) {
		File baseDir = new File(dirPath)
		println baseDir.absolutePath
		println ''
		
		List<String> metadataDirList = [] 
		baseDir.eachFileRecurse(FileType.DIRECTORIES) {f ->
			if (dirPattern.matcher(f.name).find()) {
				metadataDirList << "${f.absolutePath}"
			}
		}
		
		return metadataDirList
	}
	
/*	
	def execute2(String filename) {
		def tripPlanner = new XmlSlurper().parse(new File(filename))
		
		SimpleDateFormat sdf = new SimpleDateFormat('yyyyMMdd')
		Date startDate = sdf.parse(tripPlanner.startDate.text())
		//println startDate
		
		List<LocationNights> ldlist = buildLocationNightsList(tripPlanner.locationNights)
		
		report(startDate, ldlist)
	}
	
	def report(Date startingDate, List<LocationNights> ldlist) {
		SimpleDateFormat sdf = new SimpleDateFormat('EEE, MMM d, yyyy')
		
		Date reportDate = startingDate
		ldlist.each {ld ->
			boolean firstNight = true
			ld.nights.times {
				println "${sdf.format(reportDate)}\t${ld.location}${(firstNight) ? "...: ${ld.thingsToDo}" : ''}"
				firstNight = false
				reportDate = reportDate.next()
			}
			println ''
		}
	}
	
	List<LocationNights> buildLocationNightsList(locationNights) {
		List<LocationNights> ldlist = []
		
		locationNights.each {ln ->
			String location = ln.location
			String nights = ln.nights
			String thingsToDo = ln.thingsToDo
			ldlist << new LocationNights(location : "$location", nights : Integer.parseInt(nights), thingsToDo : "$thingsToDo")
		}
		
		return ldlist
	}
*/	
}

/*
@ToString
class LocationNights {
	String location
	int nights
	String thingsToDo
}
*/

@ToString
class EclipseProject {
	String dirPath
	File f
	String name
}

