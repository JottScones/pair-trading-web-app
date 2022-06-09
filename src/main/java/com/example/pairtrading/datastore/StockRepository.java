package com.example.pairtrading.datastore;

import com.example.pairtrading.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, String> {
}
