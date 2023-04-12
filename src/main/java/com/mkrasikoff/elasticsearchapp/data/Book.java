package com.mkrasikoff.elasticsearchapp.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "book-index")
public class Book {

    @Id
    private String id;

    private String title;
    private String author;
    private String price;
    private String avg_reviews;
    private String pages;
    private Integer stars;
}