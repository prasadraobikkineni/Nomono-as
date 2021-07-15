package com.trail.file.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController()
@ConditionalOnExpression("${file.uploading.service:true}")
@RequestMapping("/api/uploading/")
public class FileUploadingService {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);
    @Value("${file.directory}")
    String directory;

    @RequestMapping(value = "/files",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                ResponseEntity.badRequest().body("File name is invalid");
            }

            Path targetLocation = Paths.get(directory)
                    .toAbsolutePath().normalize().resolve(fileName);
            if(targetLocation.toFile().exists()){
                return ResponseEntity.badRequest().body("File already exists.");
            }
            Files.copy(file.getInputStream(), targetLocation);

        } catch (IOException ex) {
            ResponseEntity.badRequest().body(ex.getMessage());
        }

        logger.info("service is done successfully");

        return ResponseEntity.ok("File stored successfully");
    }

}
