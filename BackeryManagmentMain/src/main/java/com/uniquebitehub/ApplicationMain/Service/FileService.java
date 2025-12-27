package com.uniquebitehub.ApplicationMain.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {

	private final String BASE_REPO_DIRECTORY_PATH = "../storage";

	private final String FILE_PATH_FORMAT = "{0}/{1}/{2}.{3}";

	@Getter
	@Value("${app.max-file-size}")
	private long fileMaxSize;

//	@PostConstruct
//	public void init() {
//		
//		File file=new File(BASE_REPO_DIRECTORY_PATH);
//		if(!file.exists()) {
//			try {
//				if(file.mkdir())
//					log.info("base repo created successfully");
//				else
//					log.info("unable to create base repo");
//			}catch (Exception e) {
//				e.printStackTrace();
//				log.error("Failed to create base repo due to : [{}]",e.getMessage());
//			}
//		}else {
//			log.info(" base repo already exist");
//		}
//		log.info("mix file size {}",getFileMaxSize());
//	}
//
//	public String saveFile(byte[] data,String directoryName,String fileExt) throws IOException {
//	String filepath=MessageFormat.format(FILE_PATH_FORMAT, BASE_REPO_DIRECTORY_PATH,directoryName,UUID.randomUUID().toString(),fileExt);
//	FileUtils.writeByteArrayToFile(new File(filepath), data);
//	return filepath;
//}
	
//	public byte[] getFile(String fullPath) throws IOException {
//		File file = new File(fullPath);
//
//		if (!file.exists()) {
//			throw new FileNotFoundException("File not found at path: " + fullPath);
//		}
//
//		return FileUtils.readFileToByteArray(file);
//	}
	@PostConstruct
	public void init() {
		File baseDir = new File(BASE_REPO_DIRECTORY_PATH);

		if (!baseDir.exists()) {
			boolean created = baseDir.mkdirs();
			log.info(created ? "Base storage directory created" : "Failed to create base storage directory");
		} else {
			log.info("Base storage directory already exists");
		}
		log.info("Max file size allowed: {}", fileMaxSize);
	}

	public String saveFile(byte[] data, String directoryName, String fileExt) throws IOException {

	        File dir = new File(BASE_REPO_DIRECTORY_PATH + "/" + directoryName);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }
	        String filePath = MessageFormat.format(
	                FILE_PATH_FORMAT,
	                BASE_REPO_DIRECTORY_PATH,
	                directoryName,
	                UUID.randomUUID().toString(),
	                fileExt
	        );
	        FileUtils.writeByteArrayToFile(new File(filePath), data);
	        return filePath;
	}
	 public String saveFile(MultipartFile file, String directoryName) {

	        try {
	            if (file.isEmpty()) {
	                throw new RuntimeException("File is empty");
	            }

	            if (file.getSize() > fileMaxSize) {
	                throw new RuntimeException(
	                        "File size exceeds limit: " + fileMaxSize + " bytes");
	           }

	            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
	            return saveFile(file.getBytes(), directoryName, extension);

	        } catch (IOException e) {
	            log.error("Failed to save file", e);
	            throw new RuntimeException("File upload failed");
	        }
	    }

	    // ================= GET FILE =================
	    public byte[] getFile(String fullPath) throws IOException {
	        File file = new File(fullPath);
	        if (!file.exists()) {
	            throw new FileNotFoundException("File not found at path: " + fullPath);
	        }
	        return FileUtils.readFileToByteArray(file);
	    }

	    // ================= DELETE FILE =================
	    public void deleteFile(String fullPath) {
	        File file = new File(fullPath);
	        if (file.exists()) {
	            file.delete();
	            log.info("File deleted: {}", fullPath);
	        }
	    }
}