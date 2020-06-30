package com.wadhams.date.renamer.app

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

class DateRenamerApp {
	SimpleDateFormat sdf = new SimpleDateFormat('yyyy:MM:dd hh:mm:ss')
	
	static main(args) {
		println 'DateRenamerApp started...'
		println ''

		DateRenamerApp app = new DateRenamerApp()
		app.execute()

		println 'DateRenamerApp ended.'
	}

	def execute() {
//		File mov = new File('data/IMG_4409.MOV')
//		println mov.getAbsolutePath()
//		println ''

		File jpg = new File('data/IMG_4411.JPG')
		println jpg.getAbsolutePath()
		println ''
		executeJpg(jpg)
	}

	def executeJpg(File f) {
		String originalFilename = f.getName()
		println "Original Filename: $originalFilename"
		println ''
		
		ImageMetadata metadata = Imaging.getMetadata(f)
		if (metadata instanceof JpegImageMetadata) {
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

			TiffField dt = jpegMetadata.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME)
			String dtDesc = dt.getStringValue()
			println "DateTime: ${dtDesc}"
			Date d = sdf.parse(dtDesc)
			println d
			String renamedFilename = d.format('yyyyMMdd_hhmmss_') + originalFilename
			println "Renamed  Filename: $renamedFilename"
			println ''
		}
	}
	
}
