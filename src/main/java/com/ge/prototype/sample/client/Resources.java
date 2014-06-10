package com.ge.prototype.sample.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	@Source("cancel.png")
	ImageResource cancelButton();
	
	@Source("application-msword.png")
	ImageResource wordFile();
	@Source("application-msexcel.png")
	ImageResource excelFile();
	@Source("application-png.png")
	ImageResource pngFile();
	@Source("application-jpeg.png")
	ImageResource jpegFile();
	@Source("application-pdf.png")
	ImageResource pdfFile();
	@Source("application-txt.png")
	ImageResource textFile();
	@Source("application-zip.png")
	ImageResource zipFile();
}
