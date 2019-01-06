package com.soumyadeep.springboot.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

	private static final String EXTERNAL_FILE_PATH = "C:\\Users\\sousaha\\Desktop\\testFileDownload\\";

	@RequestMapping("/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		// HttpServletResponse in general tells us that we are going to return a file
		// instead of displaying it
		File file = new File(EXTERNAL_FILE_PATH + fileName);
		if (file.exists()) {
			// check if file exists
			// get the mimetype, known mime type jpeg, pdf
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				// unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
				// octet-stream give options for saveAs
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 * 
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			// Here we have mentioned it to show as attachment
			// response.setHeader("Content-Disposition", String.format("attachment;
			// filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());
			// setContentLength notifies the use the length to be downloaded and how much is
			// downloaded

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			// InputStream reads the content of the file

			FileCopyUtils.copy(inputStream, response.getOutputStream());
			// copy bytes to the response outputStream from the inputStream
		}
	}
}
