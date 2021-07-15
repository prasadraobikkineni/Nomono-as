package com.trail.file.services;

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
@RequestMapping("/health")
public class HealthService {
    @RequestMapping(value = "/check",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<String> check(){
        return ResponseEntity.ok("Service is up and running");
    }
}
