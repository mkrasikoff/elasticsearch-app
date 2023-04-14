package com.mkrasikoff.elasticsearchapp.elasticsearch;

import com.mkrasikoff.elasticsearchapp.data.Book;
import org.elasticsearch.client.RestHighLevelClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import java.io.IOException;
import java.util.List;


public class ElasticSearchUtils {

    private final RestHighLevelClient client;

    public ElasticSearchUtils(RestHighLevelClient client) {
        this.client = client;
    }

    public void indexBooks(List<Book> books) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        for (Book book : books) {
            IndexRequest indexRequest = new IndexRequest("book-index")
                    .id(book.getId().toString())
                    .source(new ObjectMapper().writeValueAsString(book), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    System.err.println("Error indexing book (id: " + failure.getId() + "): " + failure.getMessage());
                }
            }
        } else {
            System.out.println("All books indexed successfully");
        }
    }
}
