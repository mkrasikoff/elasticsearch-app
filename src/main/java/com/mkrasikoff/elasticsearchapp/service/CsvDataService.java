package com.mkrasikoff.elasticsearchapp.service;

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
public class CsvDataService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void processCsvFile(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            List<Book> csvDataList = new CsvToBeanBuilder<Book>(fileReader)
                    .withType(Book.class)
                    .build()
                    .parse();

            for (Book csvData : csvDataList) {
                XContentBuilder builder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", csvData.getId())
                        .field("title", csvData.getTitle())
                        .field("author", csvData.getAuthor())
                        .field("price", csvData.getPrice())
                        .field("avg_reviews", csvData.getAvg_reviews())
                        .field("pages", csvData.getPages())
                        .field("stars", csvData.getStars())
                        .endObject();

                IndexRequest indexRequest = new IndexRequest("csv_data")
                        .id(UUID.randomUUID().toString())
                        .source(builder);
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            }
        }
    }
}