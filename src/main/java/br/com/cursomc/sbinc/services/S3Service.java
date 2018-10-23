package br.com.cursomc.sbinc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {
	
	private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
	
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			String filename = multipartFile.getOriginalFilename();
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();
			return uploadFile(filename, is, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: "+ e.getMessage());
		}
	}

	public URI uploadFile(String filename, InputStream is, String contentType) {
		try {	
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3Client.putObject(bucketName, filename, is, meta);
			LOG.info("Upload finalizado");	
			return s3Client.getUrl(bucketName, filename).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI"); 
		}
	}
}
