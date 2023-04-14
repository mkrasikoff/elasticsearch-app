# How to work with application:

1. Start ElasticSearchApplication
2. Run elasticsearch container through the next command:
   **docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.15.0**
3. Use postman to create requests. For example:
    POST - http://localhost:8080/upload-json
   {
   "id": "44",
   "title": "Idiot",
   "author": "Dostoevsky",
   "price": "2600.0",
   "avg_reviews": "5.0",
   "pages": "900",
   "stars": "5"
   }

To check the data in elasticsearch, use the get requests:
All: http://localhost:9200/json_data/_search?pretty
One id: http://localhost:9200/json_data/_search?q=id:44&pretty