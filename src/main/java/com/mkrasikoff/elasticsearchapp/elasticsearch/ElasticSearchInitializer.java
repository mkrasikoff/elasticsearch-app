package com.mkrasikoff.elasticsearchapp.elasticsearch;

import jakarta.annotation.PostConstruct;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ElasticSearchInitializer {

    @Autowired
    private RestHighLevelClient client;

    @PostConstruct
    public void setup() throws IOException {
        createPipeline();
        createBookIndex();
    }

    public void createPipeline() throws IOException {
        String pipelineName = "lowercase_pipeline";
        Path pipelinePath = Paths.get("src/main/resources/lowercase_pipeline.json");
        String pipelineJson = Files.readString(pipelinePath);

        Request request = new Request("PUT", "/_ingest/pipeline/" + pipelineName);
        request.setJsonEntity(pipelineJson);

        Response response = client.getLowLevelClient().performRequest(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("Pipeline created successfully");
            getPipeline(pipelineName);
        } else {
            System.out.println("Pipeline creation failed");
        }
    }

    public void getPipeline(String pipelineName) throws IOException {
        Request request = new Request("GET", "/_ingest/pipeline/" + pipelineName);
        Response response = client.getLowLevelClient().performRequest(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("Pipeline retrieved successfully");
            System.out.println("Pipeline details: " + EntityUtils.toString(response.getEntity()));
        } else {
            System.out.println("Pipeline retrieval failed");
        }
    }

    public void createBookIndex() throws IOException {
        String indexName = "book-index";
        Path mappingPath = Paths.get("src/main/resources/book_index_mapping.json");
        String mappingJson = Files.readString(mappingPath);

        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.mapping(mappingJson, XContentType.JSON);

        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

        if (response.isAcknowledged()) {
            System.out.println("Index created successfully");
        } else {
            System.out.println("Index creation failed");
        }
    }

}
