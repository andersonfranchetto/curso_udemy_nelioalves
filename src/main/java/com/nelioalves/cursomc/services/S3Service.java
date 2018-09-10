package com.nelioalves.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    private void uploadFile(String localFilePath){
        try {
            s3Client.putObject(new PutObjectRequest(bucket, "teste", new File(localFilePath)));
        } catch (AmazonServiceException se) {
            LOG.info("AmazonServiceException, " + se.getMessage());
            LOG.info("Status CODE, " + se.getErrorCode());
        } catch (AmazonClientException ce){
            LOG.info("AmazonClientException, " + ce.getMessage());
        }
    }
}
