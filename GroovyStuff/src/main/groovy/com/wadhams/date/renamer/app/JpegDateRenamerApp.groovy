package com.wadhams.date.renamer.app

import java.text.SimpleDateFormat

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.Tag
import com.drew.metadata.exif.ExifSubIFDDirectory

class JpegDateRenamerApp {
	SimpleDateFormat sdf = new SimpleDateFormat('yyyy:MM:dd hh:mm:ss')
	
	static main(args) {
		println 'JpegDateRenamerApp started...'
		println ''

		JpegDateRenamerApp app = new JpegDateRenamerApp()
		app.execute()

		println 'JpegDateRenamerApp ended.'
	}

	def execute() {
		File jpg = new File('data/IMG_4411.JPG')
		println jpg.getAbsolutePath()
		String originalFilename = jpg.getName()
		println "Original Filename: $originalFilename"
		println ''
		
		Metadata metadata = ImageMetadataReader.readMetadata(jpg)
		
//		for (Directory dir : metadata.getDirectories()) {
//			println dir.getClass()
//			for (Tag tag : dir.getTags()) {
//				println tag
//			}
//		}
		
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)
		Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault())
		println date
		println ''
		String renamedFilename = date.format('yyyyMMdd_hhmmss_') + originalFilename
		println "Renamed  Filename: $renamedFilename"
		println ''
	}

}
