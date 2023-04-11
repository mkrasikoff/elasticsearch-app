package com.mkrasikoff.elasticsearchapp.controller;

import com.mkrasikoff.elasticsearchapp.service.CsvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class CsvDataController {

    @Autowired
    private CsvDataService csvDataService;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a CSV file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            File tempFile = File.createTempFile("temp-", ".csv");
            file.transferTo(tempFile);
            csvDataService.processCsvFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while processing the CSV file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("CSV file processed successfully.", HttpStatus.OK);
    }
}
