package com.igoro.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.igoro.models.FileUpload;

@Service
public class FileUploadService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);
	
	private final ArrayList<FileUpload> uploaded = new ArrayList<FileUpload>() ;
	
	private final ArrayList<FileUpload> uploadProgress = new ArrayList<FileUpload>() ;
	
	@Async
	public CompletableFuture<FileUpload> uploadService( FileUpload upload ) {
		
		final long start = System.currentTimeMillis();	
		
		try {
			if(checkFilename(upload.getFilename())) {	
				return CompletableFuture.failedFuture(new Throwable());
			}
			
			saveFile(upload);
			LOGGER.info("File upload service - id: {} ", upload.getId());
			
			upload.setDuration(System.currentTimeMillis() - start);
			uploaded.add(upload);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return CompletableFuture.completedFuture(upload);		
	}

	private void saveFile(FileUpload upload) throws IOException {
		// TODO Auto-generated method stub
		MultipartFile file = upload.getFile();
		
		String path = upload.getPath();
		
		try {
		
			uploadProgress.add(upload);
			
			InputStream inputStream = file.getInputStream();
			OutputStream outputStream = new FileOutputStream(new File(path));
			int read = 0;
			int uploaded = 0;
			
			byte[] bytes = new byte[1024];
			try {
				while((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
					uploaded += read;
					upload.setUploaded(uploaded);
				}
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				outputStream.close();
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uploadProgress.remove(upload);
	}
	
    public String getFinishedUploadsDuration() {

        StringBuilder uploadDuration = new StringBuilder();        

        for( FileUpload file : uploaded ) {
        	
        	uploadDuration.append("upload_duration{id=\"")
        	.append(file.getId())
        	.append("\"} ")
        	.append(file.getDuration())
        	.append(".0")
        	.append("\n");
        }

        return uploadDuration.toString();

    }

	public List<FileUpload> getUploadsProgress() {
		// TODO Auto-generated method stub
		return uploadProgress;
	}
	
	public Boolean checkFilename(String filename) {
		return uploadProgress.stream().anyMatch(o -> o.getFilename().equals(filename));
	}


}
