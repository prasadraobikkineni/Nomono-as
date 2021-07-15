package com.trail.file.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController()
@ConditionalOnExpression("${file.processing.service:true}")
@RequestMapping("/api/processing/")
public class FileProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);
    @Value("${file.directory}")
    String directory;

    @RequestMapping(value = "/files/{name}",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<String> changeFileName(@PathVariable String name, @RequestParam String newName){
        try {
            if(newName.contains("..")) {
                ResponseEntity.badRequest().body("File new name is invalid");
            }

            Path currentLocation = Paths.get(directory)
                    .toAbsolutePath().normalize().resolve(name);
            if(!currentLocation.toFile().exists()){
                return ResponseEntity.badRequest().body("File does not exist.");
            }
            Path targetLocation = Paths.get(directory)
                    .toAbsolutePath().normalize().resolve(newName);
            if(targetLocation.toFile().exists()){
                return ResponseEntity.badRequest().body("File new name already exists.");
            }
            Files.move(currentLocation, targetLocation);

        } catch (IOException ex) {
            ResponseEntity.badRequest().body(ex.getMessage());
        }
        logger.info("rename file service is done successfully");
        return ResponseEntity.ok("File renamed successfully");
    }

    @RequestMapping(value = "/files/{name}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<String> deleteFile(@PathVariable String name){;
        try {

            Path targetLocation = Paths.get(directory)
                    .toAbsolutePath().normalize().resolve(name);
            if(!targetLocation.toFile().exists()){
                return ResponseEntity.badRequest().body("File does not exist.");
            }
            Files.delete(targetLocation);

        } catch (IOException ex) {
            ResponseEntity.badRequest().body(ex.getMessage());
        }
        logger.info("delete file service is done successfully");
        return ResponseEntity.ok("File deleted successfully");
    }

}
