package com.starstar.repositories;

import com.starstar.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByBelongtoUsername(String username);
}
