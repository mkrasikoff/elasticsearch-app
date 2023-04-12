package com.mkrasikoff.elasticsearchapp.controller;

import com.mkrasikoff.elasticsearchapp.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a CSV file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            File tempFile = File.createTempFile("temp-", ".csv");
            file.transferTo(tempFile);
            dataService.processCsvFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while processing the CSV file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("CSV file processed successfully.", HttpStatus.OK);
    }

    @PostMapping("/upload-json")
    public ResponseEntity<String> uploadJson(@RequestBody String jsonData) {
        try {
            dataService.processJsonFile(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while processing the JSON data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("JSON data processed successfully.", HttpStatus.OK);
    }
}
