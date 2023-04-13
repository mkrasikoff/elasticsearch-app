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
    private Integer id;

    private String title;
    private String author;
    private Float price;
    private Float avg_reviews;
    private Integer pages;
    private Integer stars;
}