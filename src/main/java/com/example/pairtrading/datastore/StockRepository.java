package com.example.pairtrading.datastore;

import com.example.pairtrading.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StockRepository extends MongoRepository<Stock, String> {
}
