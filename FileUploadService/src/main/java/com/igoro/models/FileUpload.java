package com.igoro.models;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class FileUpload {
	@JsonProperty
	private String id;
	
	@JsonIgnore
	private String filename;
	
	@JsonProperty
	private long size;
	
	@JsonProperty
	private long uploaded = 0;
	
	@JsonIgnore
	private long duration = 0;
	
	@JsonIgnore
	private MultipartFile file;
	
	@JsonIgnore
	private String path;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getUploaded() {
		return uploaded;
	}
	public void setUploaded(long uploaded) {
		this.uploaded = uploaded;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
