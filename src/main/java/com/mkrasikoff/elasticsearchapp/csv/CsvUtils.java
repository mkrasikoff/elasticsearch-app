package com.mkrasikoff.elasticsearchapp.csv;

import com.mkrasikoff.elasticsearchapp.data.Book;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    public List<Book> readCsvFile(String csvFilePath) throws IOException {
        List<Book> books = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader)) {
            for (CSVRecord record : parser) {
                Book book = new Book(
                        Integer.parseInt(record.get("id")),
                        record.get("title"),
                        record.get("author"),
                        Float.parseFloat(record.get("price")),
                        Float.parseFloat(record.get("avg_reviews")),
                        Integer.parseInt(record.get("pages")),
                        Integer.parseInt(record.get("stars"))
                );
                books.add(book);
            }
        }
        return books;
    }
}