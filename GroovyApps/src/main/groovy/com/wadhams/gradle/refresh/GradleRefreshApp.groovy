package com.wadhams.gradle.refresh

class GradleRefreshApp {
	
	static main(args) {
		println 'GradleRefreshApp started...'
		println ''

		GradleRefreshApp app = new GradleRefreshApp()
		app.execute()
		
		println ''
		println 'GradleRefreshApp ended.'
	}
	
	def execute() {
		File baseDir = new File('c:\\Mongo\\Git_Repositories')
		baseDir.eachFileRecurse {f ->
			if (f.name == '.project') {
				String path = f.absolutePath
				//println path
				def sa = path.split('\\\\')
				//println sa.size()
				if (sa.size() == 6) {
					println "cd /c/Mongo/Git_Repositories/${sa[3]}/${sa[4]}/"
					println 'echo \'--------------------------------------------------------------\''
					println 'pwd'
					println 'echo \'--------------------------------------------------------------\''
					println './gradlew --refresh-dependencies build'
					println 'echo -e \'\\n\''
					println 'echo -e \'\\n\''
					println ''
				}
				
			}
		}
	}
	
}
