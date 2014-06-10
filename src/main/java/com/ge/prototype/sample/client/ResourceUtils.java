package com.ge.prototype.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public class ResourceUtils {
	static Resources resources = GWT.create(Resources.class);
	public static ImageResource getResource(String type) {
		switch (type) {
		case "image/png":
			return resources.pngFile();
		case "application/pdf":
			return resources.pdfFile();
		case "text/plain":
			return resources.textFile();
		case "image/jpeg":
			return resources.jpegFile();
		case "application/zip":
			return resources.zipFile();
		case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
			return resources.wordFile();
		case "application/msword":
			return resources.wordFile();
		case "application/vnd.ms-excel":
			return resources.excelFile();
		case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
			return resources.excelFile();
		default:
			return resources.textFile();
		}
	}

}
