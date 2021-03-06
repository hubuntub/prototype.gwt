package com.ge.prototype.sample.server;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class FileServletUpload extends HttpServlet {

	private long FILE_SIZE_LIMIT = 20 * 1024 * 1024; // 20 MiB
	private final Logger logger = Logger.getLogger("UploadServlet");
	private long fileSizeMax;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(
					fileItemFactory);
			Config conf = ConfigFactory.load();
			int max = conf.getInt("upload.max-mega-byte");
			
			fileSizeMax = max * 1024 * 1024;
			fileUpload.setSizeMax(fileSizeMax);
			
			String path = conf.getString("upload.path");
			File folder = new File(path);

			if (!folder.exists()) {
				try {
					folder.mkdir();

				} catch (SecurityException e) {

				}
			}
			List<FileItem> items = fileUpload.parseRequest(req);

			for (FileItem item : items) {
				if (item.isFormField()) {
					logger.log(Level.INFO, "Received form field:");
					logger.log(Level.INFO, "Name: " + item.getFieldName());
					logger.log(Level.INFO, "Value: " + item.getString());
				} else {
					logger.log(Level.INFO, "Received file:");
					logger.log(Level.INFO, "Name: " + item.getName());
					logger.log(Level.INFO, "Size: " + item.getSize());
				}

				if (!item.isFormField()) {
					if (item.getSize() > fileSizeMax) {
						resp.sendError(
								HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
								"File size exceeds limit");

						return;
					}

					// Typically here you would process the file in some way:

					File uploadedFile = new File(folder, item.getName());
					item.write(uploadedFile);

				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Throwing servlet exception for unhandled exception", e);
			throw new ServletException(e);
		}
	}

}
