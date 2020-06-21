package com.igoro;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.igoro.models.FileUpload;
import com.igoro.service.FileUploadService;

@RestController
public class FileUploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
	
	private static String upload_dir = "uploads";

	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
    ServletContext context;

	//Upload files
	@PostMapping("/api/v1/upload")
	public void uploadFile(@RequestHeader(value = "X-Upload-File", required = true) String filename,
			@RequestHeader("Content-Length") Long contentLength, @RequestParam("file") MultipartFile  file) {
		
		FileUpload upload;
			
			if( file != null) {
				
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				
				String id = filename + "-" + timestamp.toString();
				
				upload = new FileUpload();
				
				upload.setId(id);
				upload.setFilename(filename);
				upload.setSize(contentLength);
				upload.setFile(file);
				
				String absoluteFilePath = context.getRealPath(upload_dir);				
				String path = absoluteFilePath + File.separator + filename;
				
				upload.setPath(path);
				
				fileUploadService.uploadService(upload);
			}
			
			 LOGGER.info("File uploaded {}", filename);
	}
	
	//Progress
	@GetMapping("/api/v1/upload/progress")
    public List<FileUpload> getListUploadsProgress() {		
		return fileUploadService.getUploadsProgress();				
	}
	
	//Duration
	@GetMapping("/api/v1/upload/duration")
    public String getUploadedDuration() {
        return fileUploadService.getFinishedUploadsDuration();
    }
	
}
