package com.mkrasikoff.elasticsearchapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkrasikoff.elasticsearchapp.data.Book;
import com.opencsv.bean.CsvToBeanBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DataService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void processCsvFile(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            List<Book> csvDataList = new CsvToBeanBuilder<Book>(fileReader)
                    .withType(Book.class)
                    .build()
                    .parse();

            for (Book csvDataBook : csvDataList) {
                XContentBuilder builder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", csvDataBook.getId())
                        .field("title", csvDataBook.getTitle())
                        .field("author", csvDataBook.getAuthor())
                        .field("price", csvDataBook.getPrice())
                        .field("avg_reviews", csvDataBook.getAvg_reviews())
                        .field("pages", csvDataBook.getPages())
                        .field("stars", csvDataBook.getStars())
                        .endObject();

                IndexRequest indexRequest = new IndexRequest("csv_data")
                        .id(UUID.randomUUID().toString())
                        .source(builder);
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            }
        }
    }

    public void processJsonFile(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Book jsonDataBook = objectMapper.readValue(jsonData, Book.class);

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", jsonDataBook.getId())
                .field("title", jsonDataBook.getTitle())
                .field("author", jsonDataBook.getAuthor())
                .field("price", jsonDataBook.getPrice())
                .field("avg_reviews", jsonDataBook.getAvg_reviews())
                .field("pages", jsonDataBook.getPages())
                .field("stars", jsonDataBook.getStars())
                .endObject();

        IndexRequest indexRequest = new IndexRequest("json_data")
                .id(UUID.randomUUID().toString())
                .source(builder);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }
}