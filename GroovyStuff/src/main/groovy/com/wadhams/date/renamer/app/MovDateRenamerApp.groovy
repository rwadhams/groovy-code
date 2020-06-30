package com.wadhams.date.renamer.app

import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.Tag
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.mov.QuickTimeDirectory
import com.drew.imaging.ImageMetadataReader

import java.text.SimpleDateFormat

import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.common.ImageMetadata
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.TiffField
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo

class MovDateRenamerApp {
	SimpleDateFormat sdf = new SimpleDateFormat('yyyy:MM:dd hh:mm:ss')
	
	static main(args) {
		println 'MovDateRenamerApp started...'
		println ''

		MovDateRenamerApp app = new MovDateRenamerApp()
		app.execute()

		println 'MovDateRenamerApp ended.'
	}

	def execute() {
		File mov = new File('data/IMG_4409.MOV')
		println mov.getAbsolutePath()
		String originalFilename = mov.getName()
		println "Original Filename: $originalFilename"
		println ''
		
		Metadata metadata = ImageMetadataReader.readMetadata(mov)
		
//		for (Directory dir : metadata.getDirectories()) {
//			println dir.getClass()
//			for (Tag tag : dir.getTags()) {
//				println tag
//			}
//		}
		
		QuickTimeDirectory directory = metadata.getFirstDirectoryOfType(QuickTimeDirectory.class)
		Date date = directory.getDate(QuickTimeDirectory.TAG_CREATION_TIME)
		println date
		println ''
		String renamedFilename = date.format('yyyyMMdd_hhmmss_') + originalFilename
		println "Renamed  Filename: $renamedFilename"
		println ''

	}

}
