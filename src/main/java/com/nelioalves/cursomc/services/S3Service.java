package com.nelioalves.cursomc.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nelioalves.cursomc.services.exceptions.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    public URI uploadFile(MultipartFile multipartFile){
        try {
            return uploadFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename().toString(), multipartFile.getContentType());
        } catch (IOException e){
            throw new FileException("Erro de IO, " + e.getMessage());
        }
    }

    public URI uploadFile(InputStream inputStream, String fileName, String contentType){
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            s3Client.putObject(bucket, fileName, inputStream, objectMetadata);
            return s3Client.getUrl(bucket, fileName).toURI();
        } catch (URISyntaxException e){
            throw new RuntimeException("Erro ao converter URL para URI, " + e.getMessage());
        }
    }
}
